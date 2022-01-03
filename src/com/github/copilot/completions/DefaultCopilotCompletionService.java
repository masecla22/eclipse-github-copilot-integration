/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ReadAction
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.psi.AbstractFileViewProvider
 *  com.intellij.psi.FileViewProvider
 *  com.intellij.psi.PsiDocumentManager
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import com.github.copilot.completions.ApiChoiceToInlaySetProcessor;
import com.github.copilot.completions.CompletionCache;
import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.completions.SimpleCompletionCache;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.lang.CommonLanguageSupport;
import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.lang.prompt.PromptInfo;
import com.github.copilot.lang.prompt.PromptLanguageSupport;
import com.github.copilot.lang.prompt.PromptUtils;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.OpenAI;
import com.github.copilot.openai.OpenAIService;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.request.LineInfo;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.settings.CopilotApplicationState;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.String2DoubleMap;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.AbstractFileViewProvider;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultCopilotCompletionService
implements CopilotCompletionService {
    private static final Logger LOG = Logger.getInstance(DefaultCopilotCompletionService.class);
    protected final CompletionCache cache = new SimpleCompletionCache(32);

    @Override
    public boolean isAvailable(Editor editor) {
        Project project;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if ((project = editor.getProject()) == null) {
            return false;
        }
        PsiFile file = PsiDocumentManager.getInstance((Project)project).getPsiFile(editor.getDocument());
        return file != null && LanguageSupport.find(file) != null;
    }

    @Override
        public EditorRequest createRequest(Editor editor, int offset, CompletionType completionType) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (completionType == null) {
            throw new IllegalStateException("completionType cannot be null!");
        }
        return CopilotEditorUtil.createEditorRequest(editor, offset, completionType);
    }

    @Override
    @RequiresBackgroundThread
    public boolean fetchCompletions(EditorRequest request, GitHubCopilotToken proxyToken, Integer maxCompletions, boolean enableCaching, boolean cycling, Flow.Subscriber<List<CopilotInlayList>> subscriber) {
        List<CopilotCompletion> cachedItems;
        boolean isMultilineCompletion;
        String apiKey;
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (subscriber == null) {
            throw new IllegalStateException("subscriber cannot be null!");
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("fetchCompletions: " + request);
        }
        if (!(request instanceof LanguageEditorRequest)) {
            LOG.error("Incompatible editor request: " + request);
            return false;
        }
        LanguageEditorRequest languageRequest = (LanguageEditorRequest)request;
        String string = apiKey = proxyToken != null && proxyToken.isValid() ? proxyToken.getToken() : null;
        if (apiKey == null || apiKey.isEmpty()) {
            LOG.warn("fetchCompletions: GitHub Copilot token secret not defined or invalid: " + proxyToken);
            TelemetryService.getInstance().track("editor.intellij.invalidToken");
            return false;
        }
        if (!CopilotApplicationSettings.settings().isTelemetryTermsAccepted()) {
            LOG.warn("fetchCompletions: telemetry terms were not accepted");
            TelemetryService.getInstance().track("editor.intellij.termsNotAccepted");
            return false;
        }
        LineInfo lineInfo = request.getLineInfo();
        if (CommonLanguageSupport.isInlineSuggestion(lineInfo.getLineSuffix()) == null) {
            return false;
        }
        LanguageSupport language = languageRequest.getLanguage();
        PromptLanguageSupport promptSupport = PromptLanguageSupport.find(language.getCopilotLanguage());
        if (promptSupport == null) {
            LOG.warn("no prompt support found for " + request);
            return false;
        }
        PsiFile temporaryFile = (PsiFile)ReadAction.compute(languageRequest::createFile);
        if (temporaryFile == null) {
            LOG.warn("fetchCompletions: unable to create file for " + request);
            TelemetryService.getInstance().track("editor.intellij.createFileFailed");
            return false;
        }
        if (!temporaryFile.isValid() || temporaryFile.isPhysical()) {
            LOG.warn("Temporary file is invalid or physical: " + temporaryFile);
            TelemetryService.getInstance().track("editor.intellij.invalidFile", Map.of("file", temporaryFile.toString()));
        } else if (!AbstractFileViewProvider.isFreeThreaded((FileViewProvider)temporaryFile.getViewProvider())) {
            LOG.warn("File is not free-threaded: " + temporaryFile);
            TelemetryService.getInstance().track("editor.intellij.notFreeThreaded", Map.of("file", temporaryFile.getViewProvider().toString()));
        }
        PromptInfo prompt = (PromptInfo)ReadAction.compute(() -> {
            if (request.isCancelled()) {
                return null;
            }
            return PromptUtils.getPrompt(promptSupport, temporaryFile, request.getRelativeFilePath(), request.getDocumentContent(), request.getOffset());
        });
        if (prompt == null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("no prompt found for " + request);
            }
            return false;
        }
        if (request.isCancelled()) {
            return false;
        }
        if (promptSupport.getBlockMode().isAlwaysMultilineCompletion()) {
            isMultilineCompletion = true;
        } else if (lineInfo.getLineCount() >= 8000) {
            isMultilineCompletion = false;
            TelemetryService.getInstance().track("ghostText.longFileMultilineSkip", Map.of("languageId", StringUtil.defaultIfEmpty((String)prompt.getLanguageId(), (String)""), "lineCount", String.valueOf(lineInfo.getLineCount()), "currentLine", String.valueOf(lineInfo.getColumnOffset())));
        } else {
            isMultilineCompletion = promptSupport.isSupportingMultilineCompletion(languageRequest.getFileLanguage()) && lineInfo.getLineSuffix().isBlank() ? (Boolean)ReadAction.compute(() -> language.isEmptyBlockStart(request.getProject(), temporaryFile, request.getOffset())) : false;
        }
        TelemetryData telemetryBaseData = DefaultCopilotCompletionService.createChoiceBaseTelemetryData(request, prompt, isMultilineCompletion);
        TelemetryService.getInstance().track("ghostText.issued", telemetryBaseData);
        if (request.isCancelled()) {
            return false;
        }
        CopilotApplicationState settings = CopilotApplicationSettings.settings();
        int completionCount = maxCompletions != null ? maxCompletions : Math.min(promptSupport.getBlockMode().getMaxCompletions(), isMultilineCompletion ? settings.multilineCompletionCount : 1);
        int maxTokens = settings.maxTokens;
        int topP = settings.topP;
        Float temperature = settings.temperature;
        if (temperature == null || (double)temperature.floatValue() < 0.0 || (double)temperature.floatValue() > 1.0) {
            temperature = Float.valueOf(OpenAI.getTemperatureForSamples(completionCount));
        }
        if (enableCaching && (cachedItems = this.cache.get(prompt.getPrompt(), isMultilineCompletion)) != null) {
            try (SubmissionPublisher<List<CopilotInlayList>> publisher = new SubmissionPublisher<List<CopilotInlayList>>();){
                publisher.subscribe(subscriber);
                publisher.submit(CompletionUtil.createEditorCompletions(request, cachedItems));
            }
            this.cache.updateLatest(request.getCurrentDocumentPrefix(), prompt.getPrompt(), isMultilineCompletion);
            return true;
        }
        TelemetryData requestTelemetryData = this.createBaseTelemetryData(languageRequest, prompt, temperature, completionCount, isMultilineCompletion);
        long timestamp = System.currentTimeMillis();
        OpenAIService.getInstance().fetchCompletions(apiKey, languageRequest, prompt.getPrompt(), completionCount, temperature.floatValue(), maxTokens, topP, promptSupport.getBlockMode(), isMultilineCompletion, telemetryBaseData, new ApiChoiceToInlaySetProcessor(request, prompt, requestTelemetryData, subscriber, newItem -> {
            LOG.warn(String.format("[%d] Response received. Duration: %d ms", request.getRequestId(), System.currentTimeMillis() - timestamp));
            this.cache.add(request.getCurrentDocumentPrefix(), prompt.getPrompt(), isMultilineCompletion, (CopilotCompletion)newItem);
        }));
        return true;
    }

    @Override
        public List<CopilotInlayList> fetchCachedCompletions(EditorRequest request) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (CopilotApplicationSettings.settings().internalDisableHttpCache) {
            return null;
        }
        String prefix = request.getCurrentDocumentPrefix();
        List<CopilotCompletion> items = this.cache.getLatest(prefix);
        if (items == null) {
            return null;
        }
        boolean dropLinePrefix = this.cache.isLatestPrefix(prefix);
        List<CopilotInlayList> inlays = items.stream().map(item -> CompletionUtil.createEditorCompletion(request, item, dropLinePrefix)).filter(Objects::nonNull).collect(Collectors.toList());
        return inlays.isEmpty() ? null : inlays;
    }

        private static TelemetryData createChoiceBaseTelemetryData(EditorRequest request, PromptInfo prompt, boolean isMultilineRequest) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (prompt == null) {
            throw new IllegalStateException("prompt cannot be null!");
        }
        TelemetryData telemetryData = TelemetryData.createIssued(Map.of("languageId", StringUtil.defaultIfEmpty((String)prompt.getLanguageId(), (String)""), "beforeCursorWhitespace", String.valueOf(request.getLineInfo().getLinePrefix().isBlank()), "afterCursorWhitespace", String.valueOf(request.getLineInfo().getLineSuffix().isBlank()), "isMultiline", String.valueOf(isMultilineRequest), "blockMode", prompt.getBlockMode().getTelemetryValue()), String2DoubleMap.of("promptCharLen", prompt.getPrompt().length(), "promptEndPos", request.getOffset(), "documentLength", request.getDocumentContent().length()));
        if (telemetryData == null) {
            throw new IllegalStateException("telemetryData cannot be null!");
        }
        return telemetryData;
    }

        private TelemetryData createBaseTelemetryData(LanguageEditorRequest request, PromptInfo prompt, Float temperature, int completionCount, boolean isMultilineCompletion) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (prompt == null) {
            throw new IllegalStateException("prompt cannot be null!");
        }
        String[] stopTokens = isMultilineCompletion ? request.getLanguage().getMultiLineStops() : request.getLanguage().getSingleLineStops();
        TelemetryData telemetryData = TelemetryData.createIssued(Map.of("endpoint", "completions", "engineName", request.getLanguage().getCopilotLanguage().getEngineName(), "uiKind", request.getCompletionType().getTelemetryPropertyValue(), "temperature", String.valueOf(temperature), "n", String.valueOf(completionCount), "stop", "[" + StringUtil.join((String[])stopTokens, (String)", ") + "]"), String2DoubleMap.of("promptCharLen", prompt.getPrompt().length()));
        if (telemetryData == null) {
            throw new IllegalStateException("telemetryData cannot be null!");
        }
        return telemetryData;
    }

    @Override
    public void reset() {
        this.cache.clear();
    }

    @Override
    public boolean isSupportingOnDemandCycling(Editor editor) {
        LanguageSupport languageSupport;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if ((languageSupport = CopilotEditorUtil.findLanguageSupport(editor)) == null) {
            return false;
        }
        PromptLanguageSupport promptSupport = PromptLanguageSupport.find(languageSupport.getCopilotLanguage());
        return promptSupport != null && promptSupport.getBlockMode().isSupportingOnDemandCompletions();
    }

    @Override
    public boolean isCyclingReplacingCompletions() {
        return false;
    }

    @Override
    public void sendShownTelemetry(CopilotCompletion completion) {
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
        }
        APIChoice apiChoice = (APIChoice)completion;
        TelemetryData data = apiChoice.getTelemetryData();
        data.displayNow();
        String name = apiChoice.isCached() ? "ghostText.shownFromCache" : "ghostText.shown";
        TelemetryService.getInstance().track(name, data);
    }

    @Override
    public void sendAcceptedTelemetry(CopilotCompletion completion, CompletionType completionType) {
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
        }
        if (completionType == null) {
            throw new IllegalStateException("completionType cannot be null!");
        }
        APIChoice apiChoice = (APIChoice)completion;
        TelemetryData data = apiChoice.getTelemetryData();
        TelemetryService.getInstance().track(completionType.getTelemetryPrefix() + ".accepted", data);
    }

    @Override
    public void sendRejectedTelemetry(List<CopilotCompletion> completions) {
        if (completions == null) {
            throw new IllegalStateException("completions cannot be null!");
        }
        if (completions.isEmpty()) {
            return;
        }
        for (CopilotCompletion completion : completions) {
            TelemetryData data = ((APIChoice)completion).getTelemetryData();
            TelemetryService.getInstance().track("ghostText.rejected", data);
        }
    }

    
}

