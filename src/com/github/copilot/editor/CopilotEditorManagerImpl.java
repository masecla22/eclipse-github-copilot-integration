/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.injected.editor.EditorWindow
 *  com.intellij.notification.Notification
 *  com.intellij.notification.NotificationAction
 *  com.intellij.notification.NotificationType
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.command.CommandProcessor
 *  com.intellij.openapi.command.WriteCommandAction
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.EditorCustomElementRenderer
 *  com.intellij.openapi.editor.Inlay
 *  com.intellij.openapi.editor.InlayModel
 *  com.intellij.openapi.editor.ScrollType
 *  com.intellij.openapi.editor.ex.EditorEx
 *  com.intellij.openapi.editor.ex.util.EditorUtil
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.openapi.util.EmptyRunnable
 *  com.intellij.openapi.util.Key
 *  com.intellij.openapi.util.KeyWithDefaultValue
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.util.Alarm
 *  com.intellij.util.Alarm$ThreadToUse
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotEditorInlay;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.editor.CopilotDefaultInlayRenderer;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorSupport;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.github.copilot.editor.EditorRequestResultList;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.editor.InlayMessage;
import com.github.copilot.editor.InlayUtils;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.openai.IncompatibleCopilotClientException;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.RequestId;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.update.CopilotPluginUpdater;
import com.github.copilot.util.CopilotStringUtil;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.KeyWithDefaultValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.util.Alarm;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopilotEditorManagerImpl
implements CopilotEditorManager {
    private static final Logger LOG = Logger.getInstance(CopilotEditorManagerImpl.class);
    private static final Key<EditorRequestResultList> KEY_LAST_REQUEST = Key.create((String)"copilot.editorRequest");
    private static final Key<Boolean> KEY_PROCESSING = KeyWithDefaultValue.create((String)"copilot.processing", (Object)false);
    private static final int REQUEST_DELAY_MILLIS = 75;
    private static final Set<String> COMMAND_BLACKLIST = Set.of("Expand Live Template by Tab");
    private final Alarm requestAlarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, (Disposable)this);
    private final AtomicBoolean requestsDisabled = new AtomicBoolean(false);

    @Override
    @RequiresEdt
    public boolean isAvailable(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(0);
        }
        return !editor.isViewer() && !editor.isOneLineMode() && !editor.isDisposed() && (!(editor instanceof EditorEx) || !((EditorEx)editor).isEmbeddedIntoDialogWrapper()) && !(editor instanceof EditorWindow) && CopilotCompletionService.getInstance().isAvailable(editor);
    }

    @Override
    public int countCompletionInlays(@NotNull Editor editor, @NotNull TextRange searchRange, boolean inlineInlays, boolean afterLineEndInlays, boolean blockInlays, boolean matchInLeadingWhitespace) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(1);
        }
        if (searchRange == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(2);
        }
        if (!this.isAvailable(editor)) {
            return 0;
        }
        int startOffset = searchRange.getStartOffset();
        int endOffset = searchRange.getEndOffset();
        InlayModel inlayModel = editor.getInlayModel();
        int totalCount = 0;
        if (inlineInlays) {
            totalCount = (int)((long)totalCount + inlayModel.getInlineElementsInRange(startOffset, endOffset).stream().filter(inlay -> {
                if (!(inlay.getRenderer() instanceof CopilotInlayRenderer)) {
                    return false;
                }
                if (matchInLeadingWhitespace) {
                    return true;
                }
                List<String> lines = ((CopilotInlayRenderer)inlay.getRenderer()).getContentLines();
                if (lines.isEmpty()) {
                    return false;
                }
                int whitespaceEnd = inlay.getOffset() + CopilotStringUtil.leadingWhitespaceLength(lines.get(0));
                return searchRange.getEndOffset() >= whitespaceEnd;
            }).count());
        }
        if (blockInlays) {
            totalCount = (int)((long)totalCount + inlayModel.getBlockElementsInRange(startOffset, endOffset).stream().filter(inlay -> inlay.getRenderer() instanceof CopilotInlayRenderer).count());
        }
        if (afterLineEndInlays) {
            totalCount = (int)((long)totalCount + inlayModel.getAfterLineEndElementsInRange(startOffset, endOffset).stream().filter(inlay -> inlay.getRenderer() instanceof CopilotInlayRenderer).count());
        }
        return totalCount;
    }

    @Override
    public boolean hasTypingAsSuggestedData(@NotNull Editor editor, char next) {
        EditorRequestResultList request;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(3);
        }
        if ((request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) == null) {
            return false;
        }
        List<CopilotInlayList> cached = CopilotCompletionService.getInstance().fetchCachedCompletions(request.getRequest());
        if (cached == null || cached.isEmpty()) {
            return false;
        }
        CopilotInlayList first = cached.get(0);
        if (first.isEmpty()) {
            return false;
        }
        CopilotEditorInlay firstInlay = (CopilotEditorInlay)first.iterator().next();
        if (firstInlay.getLines().isEmpty()) {
            return false;
        }
        return firstInlay.getLines().get(0).startsWith(String.valueOf(next));
    }

    @Override
    @RequiresEdt
    public boolean applyCompletion(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(4);
        }
        if (editor.isDisposed()) {
            LOG.warn("editor already disposed");
            return false;
        }
        Project project = editor.getProject();
        if (project == null || project.isDisposed()) {
            LOG.warn("project disposed or null: " + project);
            return false;
        }
        if (this.isProcessing(editor)) {
            LOG.warn("can't apply inlays while processing");
            return false;
        }
        EditorRequestResultList request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        if (request == null) {
            return false;
        }
        CopilotInlayList currentCompletion = request.getCurrentCompletion();
        if (currentCompletion == null) {
            return false;
        }
        this.disposeInlays(editor, InlayDisposeContext.Applied);
        this.applyCompletion(project, editor, request.getRequest(), currentCompletion);
        return true;
    }

    @Override
    @RequiresEdt
    public void applyCompletion(@NotNull Project project, @NotNull Editor editor, @NotNull EditorRequest request, @NotNull CopilotInlayList completion) {
        if (project == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(5);
        }
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(6);
        }
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(7);
        }
        if (completion == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(8);
        }
        TextRange range = completion.getReplacementRange();
        String text = completion.getReplacementText();
        WriteCommandAction.runWriteCommandAction((Project)project, () -> {
            Document document = editor.getDocument();
            if (range.getEndOffset() <= document.getTextLength()) {
                document.replaceString(range.getStartOffset(), range.getEndOffset(), (CharSequence)text);
                editor.getCaretModel().moveToOffset(range.getStartOffset() + text.length());
                editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
            } else {
                LOG.warn("Attempt to apply completions out of document bounds: " + range + ", document: " + document.getTextLength());
            }
        });
        CopilotCompletionService.getInstance().sendAcceptedTelemetry(completion.getCopilotCompletion(), request.getCompletionType());
    }

    @Override
    @RequiresEdt
    public void disposeInlays(@NotNull Editor editor, @NotNull InlayDisposeContext disposeContext) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(9);
        }
        if (disposeContext == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(10);
        }
        if (!this.isAvailable(editor) || this.isProcessing(editor)) {
            return;
        }
        LOG.debug("disposeInlays");
        EditorRequestResultList request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        if (disposeContext.isSendRejectedTelemetry()) {
            this.sendRejectedTelemetry(request);
        }
        if (disposeContext.isResetLastRequest()) {
            KEY_LAST_REQUEST.set((UserDataHolder)editor, null);
        }
        if (request == null || request.getRequest().getOffset() != editor.getCaretModel().getOffset()) {
            this.cancelCompletionRequests(editor);
        }
        this.wrapProcessing(editor, () -> this.disposeInlays(this.collectInlays(editor, 0, editor.getDocument().getTextLength())));
    }

    @Override
    public void editorModified(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(11);
        }
        this.editorModified(editor, editor.getCaretModel().getOffset(), false);
    }

    @Override
    @RequiresEdt
    public void editorModified(@NotNull Editor editor, int offset, boolean force) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(12);
        }
        LOG.debug("editorModified");
        if (this.requestsDisabled.get()) {
            LOG.debug("completions disabled because this plugin version is incompatible with the GitHub Copilot server");
            return;
        }
        EditorRequestResultList lastRequest = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        if (!force && lastRequest != null && lastRequest.getRequest().getRequestId() == RequestId.currentRequestId() && lastRequest.getRequest().getOffset() == offset) {
            LOG.debug("Ignoring duplicate editorModified request");
            return;
        }
        Project project = editor.getProject();
        if (project == null) {
            return;
        }
        this.cancelCompletionRequests(editor);
        String commandName = CommandProcessor.getInstance().getCurrentCommandName();
        if (commandName != null && COMMAND_BLACKLIST.contains(commandName)) {
            return;
        }
        if (editor.getCaretModel().getCaretCount() > 1) {
            return;
        }
        if (editor.getSelectionModel().hasSelection()) {
            return;
        }
        GitHubCopilotToken copilotToken = GitHubService.getInstance().getCopilotToken();
        if (copilotToken == null) {
            GitHubService.getInstance().showLoginNotification(project, false);
        } else if (copilotToken.isUnauthorized()) {
            LOG.warn("Completions unavailable because user isn't authorized for GitHub Copilot");
            return;
        }
        EditorRequest request = CopilotCompletionService.getInstance().createRequest(editor, offset, CompletionType.GhostText);
        if (request == null) {
            return;
        }
        if (request instanceof Disposable) {
            EditorUtil.disposeWithEditor((Editor)editor, (Disposable)((Disposable)request));
        }
        CopilotEditorUtil.addEditorRequest(editor, request);
        KEY_LAST_REQUEST.set((UserDataHolder)editor, (Object)new EditorRequestResultList(request));
        List<CopilotInlayList> typingAsSuggested = CopilotCompletionService.getInstance().fetchCachedCompletions(request);
        if (typingAsSuggested != null && !typingAsSuggested.isEmpty()) {
            this.insertInlays(typingAsSuggested.get(0), request, editor, true, InlayDisposeContext.TypingAsSuggested);
            if (!this.addInlays(editor, typingAsSuggested)) {
                LOG.debug("failed to add inlays for typing-as-suggested");
            }
            return;
        }
        this.sendRejectedTelemetry(lastRequest);
        this.disposeInlays(editor, InlayDisposeContext.Typing);
        if (CopilotEditorSupport.isEditorCompletionsSupported(editor)) {
            boolean enableCaching = !CopilotApplicationSettings.settings().internalDisableHttpCache;
            this.queueCompletionRequest(editor, request, null, enableCaching, false, first -> this.insertInlays((CopilotInlayList)first, request, editor, false, InlayDisposeContext.Typing));
        }
    }

    @Override
    public void cancelCompletionRequests(@NotNull Editor editor) {
        List requests;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(13);
        }
        if ((requests = (List)CopilotEditorUtil.KEY_REQUESTS.get((UserDataHolder)editor)) == null || requests.isEmpty()) {
            return;
        }
        LOG.debug("cancelCompletionRequests: " + requests.size());
        Iterator requestIterator = requests.iterator();
        while (requestIterator.hasNext()) {
            EditorRequest request = (EditorRequest)requestIterator.next();
            requestIterator.remove();
            request.cancel();
        }
    }

    public void dispose() {
    }

    @Override
    @NotNull
    @RequiresEdt
    public List<CopilotInlayRenderer> collectInlays(@NotNull Editor editor, int startOffset, int endOffset) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(14);
        }
        InlayModel model = editor.getInlayModel();
        ArrayList inlays = new ArrayList();
        inlays.addAll(model.getInlineElementsInRange(startOffset, endOffset));
        inlays.addAll(model.getAfterLineEndElementsInRange(startOffset, endOffset));
        inlays.addAll(model.getBlockElementsInRange(startOffset, endOffset));
        ArrayList<CopilotInlayRenderer> renderers = new ArrayList<CopilotInlayRenderer>();
        for (Inlay inlay : inlays) {
            if (!(inlay.getRenderer() instanceof CopilotInlayRenderer)) continue;
            renderers.add((CopilotInlayRenderer)inlay.getRenderer());
        }
        ArrayList<CopilotInlayRenderer> arrayList = renderers;
        if (arrayList == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(15);
        }
        return arrayList;
    }

    @Override
    public boolean hasNextInlaySet(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(16);
        }
        EditorRequestResultList request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        CopilotCompletionService service = CopilotCompletionService.getInstance();
        return request != null && (request.hasNext() || service.isSupportingOnDemandCycling(editor) && !request.hasOnDemandCompletions());
    }

    @Override
    public boolean hasPreviousInlaySet(@NotNull Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(17);
        }
        return (request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) != null && request.hasPrev();
    }

    @Override
    public void showNextInlaySet(@NotNull Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(18);
        }
        if ((request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) == null) {
            return;
        }
        CopilotInlayList set = request.getNextCompletion();
        if (set != null) {
            this.insertInlays(set, request.getRequest(), editor, true, InlayDisposeContext.Cycling);
            return;
        }
        CopilotCompletionService service = CopilotCompletionService.getInstance();
        if (service.isSupportingOnDemandCycling(editor) && this.isActiveRequest(request.getRequest(), editor)) {
            request.setHasOnDemandCompletions();
            this.fetchOnDemandCompletions(editor, request);
        }
    }

    @Override
    public void showPreviousInlaySet(@NotNull Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(19);
        }
        if ((request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) == null) {
            return;
        }
        CopilotInlayList set = request.getPrevCompletion();
        if (set == null) {
            return;
        }
        this.insertInlays(set, request.getRequest(), editor, true, InlayDisposeContext.Cycling);
    }

    private void queueCompletionRequest(@NotNull Editor editor, @NotNull EditorRequest contentRequest, @Nullable Integer maxCompletions, boolean enableCaching, boolean cycling, @Nullable Consumer<CopilotInlayList> onFirstCompletion) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(20);
        }
        if (contentRequest == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(21);
        }
        this.requestAlarm.cancelAllRequests();
        this.requestAlarm.addRequest(() -> {
            if (!contentRequest.isCancelled()) {
                this.requestCopilotCompletions(editor, contentRequest, maxCompletions, enableCaching, cycling, onFirstCompletion);
            }
        }, 75);
    }

    @RequiresBackgroundThread
    private void requestCopilotCompletions(final @NotNull Editor editor, final @NotNull EditorRequest request, @Nullable Integer maxCompletions, boolean enableCaching, boolean cycling, final @Nullable Consumer<CopilotInlayList> onFirstCompletion) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(22);
        }
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(23);
        }
        if (!CopilotEditorUtil.isFocusedEditor(editor)) {
            LOG.debug("skipping completions for unfocused editor: " + editor);
            return;
        }
        GitHubCopilotToken copilotToken = GitHubService.getInstance().getCopilotToken(true, 1L, TimeUnit.MINUTES);
        final AtomicBoolean resetCompletions = new AtomicBoolean(cycling && CopilotCompletionService.getInstance().isCyclingReplacingCompletions());
        CopilotCompletionService.getInstance().fetchCompletions(request, copilotToken, maxCompletions, enableCaching, cycling, new Flow.Subscriber<List<CopilotInlayList>>(){
            private volatile Flow.Subscription subscription;
            private volatile boolean hasFirstCompletion;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1L);
                Disposer.tryRegister((Disposable)request.getDisposable(), this.subscription::cancel);
            }

            @Override
            public void onNext(List<CopilotInlayList> inlaySets) {
                EditorRequestResultList stored;
                LOG.debug("received inlay");
                if (!CopilotEditorManagerImpl.this.isActiveRequest(request, editor)) {
                    LOG.debug("skipping inlay because request already cancelled");
                    return;
                }
                if (resetCompletions.compareAndSet(true, false) && (stored = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) != null) {
                    stored.resetInlays();
                }
                if (!CopilotEditorManagerImpl.this.addInlays(editor, inlaySets)) {
                    LOG.debug("failed to add inlays");
                    return;
                }
                this.subscription.request(1L);
                if (!this.hasFirstCompletion && onFirstCompletion != null && !inlaySets.isEmpty()) {
                    this.hasFirstCompletion = true;
                    CopilotInlayList firstSet = inlaySets.get(0);
                    assert (firstSet != null && !firstSet.isEmpty());
                    ApplicationManager.getApplication().invokeLater(() -> onFirstCompletion.accept(firstSet));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                boolean first;
                if (LOG.isTraceEnabled()) {
                    LOG.debug("onError", throwable);
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("onError: " + throwable.getMessage());
                }
                if (throwable instanceof IncompatibleCopilotClientException && (first = CopilotEditorManagerImpl.this.requestsDisabled.compareAndSet(false, true))) {
                    ApplicationManager.getApplication().invokeLater(() -> CopilotEditorManagerImpl.this.showRequestsDisabledNotification(request.getProject()));
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void insertInlays(@NotNull CopilotInlayList inlays, @NotNull EditorRequest request, @NotNull Editor editor, boolean disposeExistingInlays, @NotNull InlayDisposeContext disposeContext) {
        if (inlays == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(24);
        }
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(25);
        }
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(26);
        }
        if (disposeContext == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(27);
        }
        if (!this.isActiveRequest(request, editor)) {
            LOG.debug("skipping insertion of inlay because request was cancelled");
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("inserting completion inlay into editor, current request ID: " + RequestId.currentRequestId() + ", request ID: " + request.getRequestId() + ", caret: " + editor.getCaretModel().getOffset() + ", request offset: " + request.getOffset());
        }
        if (!CopilotEditorUtil.isFocusedEditor(editor)) {
            LOG.debug("Not inserting inlays into editor without focus.");
            return;
        }
        this.doInsertInlays(inlays, request, editor, disposeExistingInlays, disposeContext);
    }

    private void doInsertInlays(@NotNull CopilotInlayList inlays, @NotNull EditorRequest request, @NotNull Editor editor, boolean disposeExistingInlays, @NotNull InlayDisposeContext context) {
        if (inlays == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(28);
        }
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(29);
        }
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(30);
        }
        if (context == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(31);
        }
        if (disposeExistingInlays) {
            this.disposeInlays(editor, context);
        }
        InlayModel inlayModel = editor.getInlayModel();
        int index = 0;
        for (CopilotEditorInlay inlay : inlays) {
            if (inlay.isEmptyCompletion()) continue;
            CopilotDefaultInlayRenderer renderer = new CopilotDefaultInlayRenderer(request, inlay.getType(), inlay.getLines());
            Inlay editorInlay = null;
            switch (inlay.getType()) {
                case Inline: {
                    editorInlay = inlayModel.addInlineElement(inlay.getEditorOffset(), InlayUtils.createInlineProperties(index), (EditorCustomElementRenderer)renderer);
                    break;
                }
                case AfterLineEnd: {
                    editorInlay = inlayModel.addAfterLineEndElement(inlay.getEditorOffset(), InlayUtils.createAfterLineEndProperties(index), (EditorCustomElementRenderer)renderer);
                    break;
                }
                case Block: {
                    editorInlay = inlayModel.addBlockElement(inlay.getEditorOffset(), InlayUtils.createBlockProperties(index), (EditorCustomElementRenderer)renderer);
                }
            }
            if (editorInlay != null) {
                renderer.setInlay(editorInlay);
            }
            ++index;
        }
        CopilotCompletionService.getInstance().sendShownTelemetry(inlays.getCopilotCompletion());
        LOG.debug("publishing inlaysUpdated");
        ((InlayMessage)ApplicationManager.getApplication().getMessageBus().syncPublisher(INLAY_TOPIC)).inlaysUpdated(request);
    }

    private boolean isProcessing(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(32);
        }
        return (Boolean)KEY_PROCESSING.get((UserDataHolder)editor);
    }

    private void disposeInlays(List<CopilotInlayRenderer> inlays) {
        LOG.debug("Disposing inlays: " + inlays.size());
        for (CopilotInlayRenderer inlay : inlays) {
            Disposer.dispose(inlay.getInlay());
        }
    }

    private void wrapProcessing(@NotNull Editor editor, @NotNull Runnable block) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(33);
        }
        if (block == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(34);
        }
        assert (!((Boolean)KEY_PROCESSING.get((UserDataHolder)editor)).booleanValue());
        try {
            KEY_PROCESSING.set((UserDataHolder)editor, (Object)true);
            block.run();
        }
        finally {
            KEY_PROCESSING.set((UserDataHolder)editor, null);
        }
    }

    private boolean isActiveRequest(@NotNull EditorRequest request, @NotNull Editor editor) {
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(35);
        }
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(36);
        }
        if (request.getRequestId() != RequestId.currentRequestId()) {
            return false;
        }
        EditorRequestResultList stored = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        return stored != null && stored.getRequest().equalsRequest(request);
    }

    private boolean addInlays(@NotNull Editor editor, @NotNull List<CopilotInlayList> inlaySets) {
        EditorRequestResultList stored;
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(37);
        }
        if (inlaySets == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(38);
        }
        if ((stored = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) != null) {
            for (CopilotInlayList inlays : inlaySets) {
                stored.addInlays(inlays);
            }
        }
        return stored != null;
    }

    private void showRequestsDisabledNotification(@NotNull Project project) {
        if (project == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(39);
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("requestsDisabledNotification.title"), CopilotBundle.get("requestsDisabledNotification.text"), NotificationType.ERROR, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("requestsDisabledNotification.checkUpdates"), () -> new CopilotPluginUpdater.CheckUpdatesTask(project, true).queue()));
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("requestsDisabledNotification.hide"), (Runnable)EmptyRunnable.getInstance()));
        notification.notify(project);
    }

    private void fetchOnDemandCompletions(@NotNull Editor editor, @NotNull EditorRequestResultList request) {
        if (editor == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(40);
        }
        if (request == null) {
            CopilotEditorManagerImpl.$$$reportNull$$$0(41);
        }
        EditorRequest currentRequest = request.getRequest();
        LOG.debug("Fetching on-demand completions for cycling");
        this.queueCompletionRequest(editor, currentRequest, 2, false, true, first -> {
            CopilotInlayList nextSet = request.getNextCompletion();
            if (nextSet != null) {
                LOG.debug("Received first on-demand completion");
                this.insertInlays(nextSet, currentRequest, editor, true, InlayDisposeContext.Cycling);
            }
        });
    }

    private void sendRejectedTelemetry(@Nullable EditorRequestResultList lastRequest) {
        if (lastRequest == null) {
            return;
        }
        List<CopilotInlayList> allShownCompletion = lastRequest.getAllShownCompletion();
        if (allShownCompletion != null) {
            List<CopilotCompletion> shown = allShownCompletion.stream().filter(Objects::nonNull).map(CopilotInlayList::getCopilotCompletion).collect(Collectors.toList());
            CopilotCompletionService.getInstance().sendRejectedTelemetry(shown);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 15: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 15: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "searchRange";
                break;
            }
            case 5: 
            case 39: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 7: 
            case 23: 
            case 25: 
            case 29: 
            case 35: 
            case 41: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completion";
                break;
            }
            case 10: 
            case 27: {
                objectArray2 = objectArray3;
                objectArray3[0] = "disposeContext";
                break;
            }
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CopilotEditorManagerImpl";
                break;
            }
            case 21: {
                objectArray2 = objectArray3;
                objectArray3[0] = "contentRequest";
                break;
            }
            case 24: 
            case 28: {
                objectArray2 = objectArray3;
                objectArray3[0] = "inlays";
                break;
            }
            case 31: {
                objectArray2 = objectArray3;
                objectArray3[0] = "context";
                break;
            }
            case 34: {
                objectArray2 = objectArray3;
                objectArray3[0] = "block";
                break;
            }
            case 38: {
                objectArray2 = objectArray3;
                objectArray3[0] = "inlaySets";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CopilotEditorManagerImpl";
                break;
            }
            case 15: {
                objectArray = objectArray2;
                objectArray2[1] = "collectInlays";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "isAvailable";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "countCompletionInlays";
                break;
            }
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "hasTypingAsSuggestedData";
                break;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                objectArray = objectArray;
                objectArray[2] = "applyCompletion";
                break;
            }
            case 9: 
            case 10: {
                objectArray = objectArray;
                objectArray[2] = "disposeInlays";
                break;
            }
            case 11: 
            case 12: {
                objectArray = objectArray;
                objectArray[2] = "editorModified";
                break;
            }
            case 13: {
                objectArray = objectArray;
                objectArray[2] = "cancelCompletionRequests";
                break;
            }
            case 14: {
                objectArray = objectArray;
                objectArray[2] = "collectInlays";
                break;
            }
            case 15: {
                break;
            }
            case 16: {
                objectArray = objectArray;
                objectArray[2] = "hasNextInlaySet";
                break;
            }
            case 17: {
                objectArray = objectArray;
                objectArray[2] = "hasPreviousInlaySet";
                break;
            }
            case 18: {
                objectArray = objectArray;
                objectArray[2] = "showNextInlaySet";
                break;
            }
            case 19: {
                objectArray = objectArray;
                objectArray[2] = "showPreviousInlaySet";
                break;
            }
            case 20: 
            case 21: {
                objectArray = objectArray;
                objectArray[2] = "queueCompletionRequest";
                break;
            }
            case 22: 
            case 23: {
                objectArray = objectArray;
                objectArray[2] = "requestCopilotCompletions";
                break;
            }
            case 24: 
            case 25: 
            case 26: 
            case 27: {
                objectArray = objectArray;
                objectArray[2] = "insertInlays";
                break;
            }
            case 28: 
            case 29: 
            case 30: 
            case 31: {
                objectArray = objectArray;
                objectArray[2] = "doInsertInlays";
                break;
            }
            case 32: {
                objectArray = objectArray;
                objectArray[2] = "isProcessing";
                break;
            }
            case 33: 
            case 34: {
                objectArray = objectArray;
                objectArray[2] = "wrapProcessing";
                break;
            }
            case 35: 
            case 36: {
                objectArray = objectArray;
                objectArray[2] = "isActiveRequest";
                break;
            }
            case 37: 
            case 38: {
                objectArray = objectArray;
                objectArray[2] = "addInlays";
                break;
            }
            case 39: {
                objectArray = objectArray;
                objectArray[2] = "showRequestsDisabledNotification";
                break;
            }
            case 40: 
            case 41: {
                objectArray = objectArray;
                objectArray[2] = "fetchOnDemandCompletions";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 15: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

