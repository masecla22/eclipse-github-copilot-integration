/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.DefaultLanguageHighlighterColors
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.EditorSettings
 *  com.intellij.openapi.editor.actions.IncrementalFindAction
 *  com.intellij.openapi.editor.colors.EditorColors
 *  com.intellij.openapi.editor.colors.EditorColorsManager
 *  com.intellij.openapi.editor.colors.EditorColorsScheme
 *  com.intellij.openapi.editor.ex.EditorEx
 *  com.intellij.openapi.fileEditor.FileEditor
 *  com.intellij.openapi.fileEditor.TextEditor
 *  com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
 *  com.intellij.openapi.fileTypes.FileType
 *  com.intellij.openapi.fileTypes.LanguageFileType
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.openapi.util.Key
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.openapi.wm.ToolWindow
 *  com.intellij.openapi.wm.ToolWindowManager
 *  com.intellij.psi.FileViewProvider
 *  com.intellij.psi.PsiManager
 *  com.intellij.testFramework.LightVirtualFile
 *  com.intellij.ui.EditorTextField
 *  com.intellij.ui.EditorTextFieldProvider
 *  com.intellij.ui.ListFocusTraversalPolicy
 *  com.intellij.ui.SimpleTextAttributes
 *  com.intellij.ui.components.JBScrollPane
 *  com.intellij.ui.components.panels.HorizontalLayout
 *  com.intellij.ui.content.Content
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.containers.ContainerUtil
 *  com.intellij.util.ui.JBDimension
 *  com.intellij.util.ui.JBUI$Borders
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.toolWindow;

import com.github.copilot.CopilotBundle;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotEditorInlay;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.toolWindow.DataProviderPanel;
import com.github.copilot.toolWindow.OpenCopilotToolWindowFactory;
import com.github.copilot.toolWindow.SmallButton;
import com.github.copilot.util.RequestCancelledException;
import com.github.copilot.util.RequestTimeoutException;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.actions.IncrementalFindAction;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.EditorTextFieldProvider;
import com.intellij.ui.ListFocusTraversalPolicy;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.content.Content;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class OpenCopilotHandler {
    private static final Logger LOG = Logger.getInstance(OpenCopilotHandler.class);
    private static final int MAX_EDITOR_HEIGHT = 200;
    private static final Key<Boolean> COPILOT_PREVIEW_EDITOR = Key.create((String)"copilot.openCopilotPreview");
    private final Project project;
    private final ToolWindow toolWindow;
    private final FileType fileType;
    private final Editor editor;
        private final EditorRequest editorRequest;
    private final AtomicInteger solutionIndex;
    private final Set<List<CopilotEditorInlay>> allCompletions;
    private final List<JButton> allButtons;
    private final List<Editor> allEditors;
    private final DataProviderPanel contentPanel;
    private volatile Content toolWindowContent;

        static OpenCopilotHandler create(Project project, FileType fileType, Editor editor, EditorRequest request) {
        ToolWindow toolWindow;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (fileType == null) {
            throw new IllegalStateException("fileType cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if ((toolWindow = ToolWindowManager.getInstance((Project)project).getToolWindow("github.copilotToolWindow")) == null) {
            return null;
        }
        return new OpenCopilotHandler(project, toolWindow, fileType, editor, request);
    }

    static boolean isCopilotSnippetFile(VirtualFile virtualFile) {
        if (virtualFile == null) {
            throw new IllegalStateException("virtualFile cannot be null!");
        }
        return COPILOT_PREVIEW_EDITOR.isIn((UserDataHolder)virtualFile);
    }

    OpenCopilotHandler(Project project, ToolWindow toolWindow, FileType fileType, Editor editor, EditorRequest editorRequest) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (toolWindow == null) {
            throw new IllegalStateException("toolWindow cannot be null!");
        }
        if (fileType == null) {
            throw new IllegalStateException("fileType cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (editorRequest == null) {
            throw new IllegalStateException("editorRequest cannot be null!");
        }
        this.solutionIndex = new AtomicInteger(1);
        this.allCompletions = ContainerUtil.newConcurrentSet();
        this.allButtons = ContainerUtil.createLockFreeCopyOnWriteList();
        this.allEditors = ContainerUtil.createLockFreeCopyOnWriteList();
        this.project = project;
        this.toolWindow = toolWindow;
        this.fileType = fileType;
        this.editor = editor;
        this.editorRequest = editorRequest;
        this.contentPanel = new DataProviderPanel(toolWindow.getDisposable());
        Disposer.tryRegister((Disposable)toolWindow.getDisposable(), editorRequest::cancel);
    }

    void updateToolWindow() {
        this.contentPanel.setFocusCycleRoot(true);
        this.contentPanel.setBorder(JBUI.Borders.empty((int)3));
        this.toolWindowContent = this.toolWindow.getContentManager().getFactory().createContent((JComponent)new JBScrollPane((Component)((Object)this.contentPanel), 20, 31), null, false);
        this.toolWindow.getContentManager().addContent(this.toolWindowContent);
        Disposer.tryRegister((Disposable)this.toolWindow.getDisposable(), this.editorRequest::cancel);
        Disposer.tryRegister((Disposable)this.editorRequest.getDisposable(), this::setEditorChangedEmptyText);
        if (!GitHubService.getInstance().isSignedIn()) {
            this.setContentEmptyText(CopilotBundle.get("openCopilot.notAuthenticatedError"), SimpleTextAttributes.ERROR_ATTRIBUTES);
            return;
        }
        this.setContentEmptyText(CopilotBundle.get("openCopilot.calculatingEmptyText"), null);
        GitHubCopilotToken copilotToken = GitHubService.getInstance().getCopilotToken();
        CopilotCompletionService service = CopilotCompletionService.getInstance();
        boolean sent = service.fetchCompletions(this.editorRequest, copilotToken, 10, new Flow.Subscriber<List<CopilotInlayList>>(){
            private volatile Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1L);
            }

            @Override
            public void onNext(List<CopilotInlayList> inlaySets) {
                LOG.debug("onNext: " + inlaySets.size());
                this.subscription.request(1L);
                for (CopilotInlayList inlayList : inlaySets) {
                    List<CopilotEditorInlay> editorInlays = inlayList.getInlays();
                    if (OpenCopilotHandler.this.allCompletions.contains(editorInlays)) continue;
                    OpenCopilotHandler.this.allCompletions.add(editorInlays);
                    OpenCopilotHandler.this.addCompletion((JPanel)((Object)OpenCopilotHandler.this.contentPanel), inlayList);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace(throwable);
                }
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (throwable instanceof RequestCancelledException) {
                        if (!OpenCopilotHandler.this.editorRequest.isCancelled()) {
                            OpenCopilotHandler.this.setEditorChangedEmptyText();
                        }
                    } else if (throwable instanceof RequestTimeoutException) {
                        OpenCopilotHandler.this.setContentEmptyText(CopilotBundle.get("openCopilot.requestTimeoutError"), SimpleTextAttributes.ERROR_ATTRIBUTES);
                    } else {
                        OpenCopilotHandler.this.setContentEmptyText(CopilotBundle.get("openCopilot.unknownRequestError"), SimpleTextAttributes.ERROR_ATTRIBUTES);
                    }
                });
            }

            @Override
            public void onComplete() {
                LOG.debug("onComplete");
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (OpenCopilotHandler.this.allEditors.isEmpty() && !OpenCopilotHandler.this.editorRequest.isCancelled()) {
                        OpenCopilotHandler.this.setContentEmptyText(CopilotBundle.get("openCopilot.noCompletions"), SimpleTextAttributes.ERROR_ATTRIBUTES);
                    } else {
                        OpenCopilotHandler.this.toolWindow.getContentManager().requestFocus(OpenCopilotHandler.this.toolWindowContent, false);
                    }
                });
            }
        });
        if (!sent) {
            this.setContentEmptyText(CopilotBundle.get("openCopilot.noCompletions"), SimpleTextAttributes.ERROR_ATTRIBUTES);
        }
    }

    @RequiresBackgroundThread
    private void addCompletion(final JPanel contentPanel, CopilotInlayList completion) {
        if (contentPanel == null) {
            throw new IllegalStateException("contentPanel cannot be null!");
        }
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
        }
        StringBuilder content = new StringBuilder();
        for (CopilotEditorInlay inlay : completion) {
            for (String line : inlay.getLines()) {
                content.append(line).append("\n");
            }
        }
        int currentSolutionIndex = this.solutionIndex.getAndIncrement();
        String solutionLabel = CopilotBundle.get("openCopilot.editor.solution.label", currentSolutionIndex);
        ApplicationManager.getApplication().invokeLater(() -> {
            LightVirtualFile snippetFile = new LightVirtualFile(solutionLabel, this.fileType, (CharSequence)content);
            COPILOT_PREVIEW_EDITOR.set((UserDataHolder)snippetFile, (Object)true);
            snippetFile.setWritable(false);
            final EditorTextField editor = this.createSnippetEditor(snippetFile);
            if (editor == null) {
                LOG.error("unable to create preview editor for file. File type: " + this.fileType + ", file: " + snippetFile);
                return;
            }
            final SmallButton acceptButton = new SmallButton(CopilotBundle.get("openCopilot.solution.accept", currentSolutionIndex));
            acceptButton.setToolTipText(CopilotBundle.get("openCopilot.solution.accept.tooltip"));
            acceptButton.addFocusListener(new FocusAdapter(){

                @Override
                public void focusGained(FocusEvent e) {
                    FocusEvent.Cause cause = e.getCause();
                    if (cause != FocusEvent.Cause.TRAVERSAL_BACKWARD && cause != FocusEvent.Cause.TRAVERSAL_FORWARD) {
                        return;
                    }
                    Rectangle buttonRect = acceptButton.getBounds();
                    Point buttonPos = SwingUtilities.convertPoint(acceptButton, buttonRect.getLocation(), contentPanel);
                    buttonRect.setLocation(buttonPos);
                    Rectangle editorRect = editor.getBounds();
                    Point editorPos = SwingUtilities.convertPoint((Component)editor, editorRect.getLocation(), contentPanel);
                    editorRect.setLocation(editorPos);
                    contentPanel.scrollRectToVisible(buttonRect.union(editorRect));
                }
            });
            acceptButton.addActionListener(actionEvent -> this.acceptSolution(this.project, this.editorRequest, completion));
            JPanel header = new JPanel((LayoutManager)new HorizontalLayout(0));
            header.add(acceptButton);
            contentPanel.add(header);
            contentPanel.add((Component)editor);
            if (this.allButtons.isEmpty()) {
                acceptButton.requestFocusInWindow();
            }
            this.allButtons.add(acceptButton);
            Dimension editorPreferredSize = contentPanel.getPreferredSize();
            editor.setMaximumSize((Dimension)new JBDimension(0, 200));
            Editor wrappedEditor = editor.getEditor();
            if (wrappedEditor != null) {
                this.allEditors.add(wrappedEditor);
                wrappedEditor.getScrollingModel().scrollVertically(0);
                int contentHeight = wrappedEditor instanceof EditorEx ? ((EditorEx)wrappedEditor).getContentSize().height : wrappedEditor.getContentComponent().getHeight();
                int height = Math.min(200, contentHeight += wrappedEditor.getLineHeight());
                editor.setPreferredSize(new Dimension(editorPreferredSize.width, height));
            }
            editor.invalidate();
            contentPanel.setFocusTraversalPolicy((FocusTraversalPolicy)new ListFocusTraversalPolicy(this.allButtons));
        });
    }

        private EditorTextField createSnippetEditor(LightVirtualFile snippetFile) {
        Language language;
        if (snippetFile == null) {
            throw new IllegalStateException("snippetFile cannot be null!");
        }
        if ((language = snippetFile.getLanguage()) == null && snippetFile.getFileType() instanceof LanguageFileType) {
            language = ((LanguageFileType)snippetFile.getFileType()).getLanguage();
        }
        if (language == null) {
            return null;
        }
        EditorTextField editor = EditorTextFieldProvider.getInstance().getEditorField(language, this.project, List.of(editorEx -> {
            EditorSettings editorSettings = editorEx.getSettings();
            editorSettings.setLineNumbersShown(false);
            editorSettings.setLineMarkerAreaShown(false);
            editorSettings.setAutoCodeFoldingEnabled(false);
            editorSettings.setUseSoftWraps(true);
            editorSettings.setAnimatedScrolling(false);
            editorSettings.setBlinkCaret(false);
            editorSettings.setRightMarginShown(false);
            editorSettings.setShowIntentionBulb(false);
            editorEx.setViewer(true);
            editorEx.setCaretVisible(false);
            editorEx.setCaretEnabled(false);
            editorEx.putUserData(IncrementalFindAction.SEARCH_DISABLED, (Object)Boolean.TRUE);
            editorEx.setRendererMode(false);
            EditorColorsScheme scheme = EditorColorsManager.getInstance().getSchemeForCurrentUITheme();
            Color c = scheme.getColor(EditorColors.READONLY_BACKGROUND_COLOR);
            editorEx.setBackgroundColor(c != null ? c : scheme.getDefaultBackground());
            editorEx.setColorsScheme(scheme);
            editorEx.setPrefixTextAndAttributes(this.editorRequest.getLineInfo().getLinePrefix(), scheme.getAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT));
        }));
        FileViewProvider snippetViewProvider = PsiManager.getInstance((Project)this.project).findViewProvider((VirtualFile)snippetFile);
        if (snippetViewProvider != null) {
            editor.setDocument(snippetViewProvider.getDocument());
        }
        editor.setViewer(true);
        editor.setFocusable(false);
        return editor;
    }

    private void acceptSolution(Project project, EditorRequest editorRequest, CopilotInlayList completion) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (editorRequest == null) {
            throw new IllegalStateException("editorRequest cannot be null!");
        }
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
        }
        if (this.toolWindowContent != null) {
            this.toolWindow.getContentManager().removeContent(this.toolWindowContent, true);
        }
        this.toolWindow.hide();
        FileEditor mainEditor = FileEditorManagerEx.getInstanceEx((Project)project).getSelectedEditor();
        if (!(mainEditor instanceof TextEditor)) {
            return;
        }
        CopilotEditorManager manager = CopilotEditorManager.getInstance();
        Editor textEditor = ((TextEditor)mainEditor).getEditor();
        manager.applyCompletion(project, textEditor, editorRequest, completion);
    }

    private void setEditorChangedEmptyText() {
        if (this.toolWindowContent.isValid()) {
            this.setContentEmptyText(CopilotBundle.get("openCopilot.editorChangedError"), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }

    private void setContentEmptyText(String text, SimpleTextAttributes attrs) {
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        this.contentPanel.removeAll();
        this.contentPanel.getEmptyText().clear();
        if (attrs == null) {
            this.contentPanel.getEmptyText().setText(text);
        } else {
            this.contentPanel.getEmptyText().setText(text, attrs);
        }
        OpenCopilotToolWindowFactory.appendRefreshLine(this.contentPanel.getEmptyText(), this.editor);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 1: 
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "fileType";
                break;
            }
            case 2: 
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "virtualFile";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "toolWindow";
                break;
            }
            case 9: 
            case 14: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editorRequest";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "contentPanel";
                break;
            }
            case 11: 
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completion";
                break;
            }
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "snippetFile";
                break;
            }
            case 16: {
                objectArray2 = objectArray3;
                objectArray3[0] = "text";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/toolWindow/OpenCopilotHandler";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "create";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "isCopilotSnippetFile";
                break;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 10: 
            case 11: {
                objectArray = objectArray2;
                objectArray2[2] = "addCompletion";
                break;
            }
            case 12: {
                objectArray = objectArray2;
                objectArray2[2] = "createSnippetEditor";
                break;
            }
            case 13: 
            case 14: 
            case 15: {
                objectArray = objectArray2;
                objectArray2[2] = "acceptSolution";
                break;
            }
            case 16: {
                objectArray = objectArray2;
                objectArray2[2] = "setContentEmptyText";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

