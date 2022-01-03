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
    public boolean isAvailable(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return !editor.isViewer() && !editor.isOneLineMode() && !editor.isDisposed() && (!(editor instanceof EditorEx) || !((EditorEx)editor).isEmbeddedIntoDialogWrapper()) && !(editor instanceof EditorWindow) && CopilotCompletionService.getInstance().isAvailable(editor);
    }

    @Override
    public int countCompletionInlays(Editor editor, TextRange searchRange, boolean inlineInlays, boolean afterLineEndInlays, boolean blockInlays, boolean matchInLeadingWhitespace) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (searchRange == null) {
            throw new IllegalStateException("searchRange cannot be null!");
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
    public boolean hasTypingAsSuggestedData(Editor editor, char next) {
        EditorRequestResultList request;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
    public boolean applyCompletion(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
    public void applyCompletion(Project project, Editor editor, EditorRequest request, CopilotInlayList completion) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
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
    public void disposeInlays(Editor editor, InlayDisposeContext disposeContext) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (disposeContext == null) {
            throw new IllegalStateException("disposeContext cannot be null!");
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
    public void editorModified(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        this.editorModified(editor, editor.getCaretModel().getOffset(), false);
    }

    @Override
    @RequiresEdt
    public void editorModified(Editor editor, int offset, boolean force) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
    public void cancelCompletionRequests(Editor editor) {
        List requests;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
        @RequiresEdt
    public List<CopilotInlayRenderer> collectInlays(Editor editor, int startOffset, int endOffset) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
            throw new IllegalStateException("arrayList cannot be null!");
        }
        return arrayList;
    }

    @Override
    public boolean hasNextInlaySet(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        EditorRequestResultList request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        CopilotCompletionService service = CopilotCompletionService.getInstance();
        return request != null && (request.hasNext() || service.isSupportingOnDemandCycling(editor) && !request.hasOnDemandCompletions());
    }

    @Override
    public boolean hasPreviousInlaySet(Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return (request = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) != null && request.hasPrev();
    }

    @Override
    public void showNextInlaySet(Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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
    public void showPreviousInlaySet(Editor editor) {
        EditorRequestResultList request;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
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

    private void queueCompletionRequest(Editor editor, EditorRequest contentRequest, Integer maxCompletions, boolean enableCaching, boolean cycling, Consumer<CopilotInlayList> onFirstCompletion) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (contentRequest == null) {
            throw new IllegalStateException("contentRequest cannot be null!");
        }
        this.requestAlarm.cancelAllRequests();
        this.requestAlarm.addRequest(() -> {
            if (!contentRequest.isCancelled()) {
                this.requestCopilotCompletions(editor, contentRequest, maxCompletions, enableCaching, cycling, onFirstCompletion);
            }
        }, 75);
    }

    @RequiresBackgroundThread
    private void requestCopilotCompletions(final Editor editor, final EditorRequest request, Integer maxCompletions, boolean enableCaching, boolean cycling, final Consumer<CopilotInlayList> onFirstCompletion) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
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

    private void insertInlays(CopilotInlayList inlays, EditorRequest request, Editor editor, boolean disposeExistingInlays, InlayDisposeContext disposeContext) {
        if (inlays == null) {
            throw new IllegalStateException("inlays cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (disposeContext == null) {
            throw new IllegalStateException("disposeContext cannot be null!");
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

    private void doInsertInlays(CopilotInlayList inlays, EditorRequest request, Editor editor, boolean disposeExistingInlays, InlayDisposeContext context) {
        if (inlays == null) {
            throw new IllegalStateException("inlays cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (context == null) {
            throw new IllegalStateException("context cannot be null!");
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

    private boolean isProcessing(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return (Boolean)KEY_PROCESSING.get((UserDataHolder)editor);
    }

    private void disposeInlays(List<CopilotInlayRenderer> inlays) {
        LOG.debug("Disposing inlays: " + inlays.size());
        for (CopilotInlayRenderer inlay : inlays) {
            Disposer.dispose(inlay.getInlay());
        }
    }

    private void wrapProcessing(Editor editor, Runnable block) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (block == null) {
            throw new IllegalStateException("block cannot be null!");
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

    private boolean isActiveRequest(EditorRequest request, Editor editor) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request.getRequestId() != RequestId.currentRequestId()) {
            return false;
        }
        EditorRequestResultList stored = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor);
        return stored != null && stored.getRequest().equalsRequest(request);
    }

    private boolean addInlays(Editor editor, List<CopilotInlayList> inlaySets) {
        EditorRequestResultList stored;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (inlaySets == null) {
            throw new IllegalStateException("inlaySets cannot be null!");
        }
        if ((stored = (EditorRequestResultList)KEY_LAST_REQUEST.get((UserDataHolder)editor)) != null) {
            for (CopilotInlayList inlays : inlaySets) {
                stored.addInlays(inlays);
            }
        }
        return stored != null;
    }

    private void showRequestsDisabledNotification(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("requestsDisabledNotification.title"), CopilotBundle.get("requestsDisabledNotification.text"), NotificationType.ERROR, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("requestsDisabledNotification.checkUpdates"), () -> new CopilotPluginUpdater.CheckUpdatesTask(project, true).queue()));
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("requestsDisabledNotification.hide"), (Runnable)EmptyRunnable.getInstance()));
        notification.notify(project);
    }

    private void fetchOnDemandCompletions(Editor editor, EditorRequestResultList request) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
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

    private void sendRejectedTelemetry(EditorRequestResultList lastRequest) {
        if (lastRequest == null) {
            return;
        }
        List<CopilotInlayList> allShownCompletion = lastRequest.getAllShownCompletion();
        if (allShownCompletion != null) {
            List<CopilotCompletion> shown = allShownCompletion.stream().filter(Objects::nonNull).map(CopilotInlayList::getCopilotCompletion).collect(Collectors.toList());
            CopilotCompletionService.getInstance().sendRejectedTelemetry(shown);
        }
    }

    
}

