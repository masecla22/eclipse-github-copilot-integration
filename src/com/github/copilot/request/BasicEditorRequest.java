/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.PsiFileFactory
 *  com.intellij.util.PathUtilRt
 *  javax.annotation.concurrent.ThreadSafe
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.request;

import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.request.LineInfo;
import com.github.copilot.request.RequestId;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.PathUtilRt;
import javax.annotation.concurrent.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

@ThreadSafe
public final class BasicEditorRequest
implements LanguageEditorRequest,
Disposable {
    private static final Logger LOG = Logger.getInstance(BasicEditorRequest.class);
    private final Project project;
    private final LanguageSupport language;
    private final CompletionType completionType;
    private final boolean useTabIndents;
    private final int tabWidth;
    private final int requestId;
    private final Language fileLanguage;
        private final String relativeFilePath;
    private final String documentContent;
    private final int offset;
        private final LineInfo lineInfo;
    private final long requestTimestamp;
        private final Integer documentModificationSequence;
    private volatile boolean isCancelled;

    @TestOnly
    public static BasicEditorRequest createEmptyGhostText(PsiFile file, LanguageSupport languageSupport) {
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        if (languageSupport == null) {
            throw new IllegalStateException("languageSupport cannot be null!");
        }
        return BasicEditorRequest.createGhostText(file, languageSupport, "", 0, new LineInfo(1, 0, 0, 0, "", 0));
    }

    @TestOnly
    public static BasicEditorRequest createGhostText(PsiFile file, LanguageSupport languageSupport, String text, int offset, LineInfo lineInfo) {
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        if (languageSupport == null) {
            throw new IllegalStateException("languageSupport cannot be null!");
        }
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (lineInfo == null) {
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        return new BasicEditorRequest(file.getProject(), languageSupport, CompletionType.GhostText, file.getLanguage(), file.getName(), text, offset, lineInfo, true, 4, 0);
    }

    public BasicEditorRequest(Project project, LanguageSupport language, CompletionType completionType, Language fileLanguage, String relativeFilePath, String documentContent, int offset, LineInfo lineInfo, boolean useTabIndents, int tabWidth, Integer documentModificationSequence) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (language == null) {
            throw new IllegalStateException("language cannot be null!");
        }
        if (completionType == null) {
            throw new IllegalStateException("completionType cannot be null!");
        }
        if (fileLanguage == null) {
            throw new IllegalStateException("fileLanguage cannot be null!");
        }
        if (relativeFilePath == null) {
            throw new IllegalStateException("relativeFilePath cannot be null!");
        }
        if (documentContent == null) {
            throw new IllegalStateException("documentContent cannot be null!");
        }
        if (lineInfo == null) {
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        this.requestTimestamp = System.currentTimeMillis();
        this.project = project;
        this.completionType = completionType;
        this.fileLanguage = fileLanguage;
        this.relativeFilePath = relativeFilePath;
        this.documentContent = documentContent;
        this.offset = offset;
        this.language = language;
        this.lineInfo = lineInfo;
        this.useTabIndents = useTabIndents;
        this.tabWidth = tabWidth;
        this.documentModificationSequence = documentModificationSequence;
        this.requestId = RequestId.incrementAndGet();
    }

    @Override
        public PsiFile createFile() {
        if (this.isCancelled()) {
            return null;
        }
        String name = PathUtilRt.getFileName((String)this.relativeFilePath);
        return PsiFileFactory.getInstance((Project)this.project).createFileFromText(name, this.fileLanguage, (CharSequence)this.documentContent, false, false, true);
    }

    @Override
    public Disposable getDisposable() {
        return this;
    }

    @Override
    public void cancel() {
        this.isCancelled = true;
        Disposer.dispose((Disposable)this);
    }

    public void dispose() {
        LOG.debug("EditorRequest.dispose");
    }

    @Override
    public boolean equalsRequest(EditorRequest o) {
        if (o == null) {
            throw new IllegalStateException("o cannot be null!");
        }
        return this.requestId == o.getRequestId();
    }

    @Override
    public Project getProject() {
        return this.project;
    }

    @Override
    public LanguageSupport getLanguage() {
        return this.language;
    }

    @Override
    public CompletionType getCompletionType() {
        return this.completionType;
    }

    @Override
    public boolean isUseTabIndents() {
        return this.useTabIndents;
    }

    @Override
    public int getTabWidth() {
        return this.tabWidth;
    }

    @Override
    public int getRequestId() {
        return this.requestId;
    }

    @Override
    public Language getFileLanguage() {
        return this.fileLanguage;
    }

    @Override
        public String getRelativeFilePath() {
        String string = this.relativeFilePath;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    @Override
    public String getDocumentContent() {
        return this.documentContent;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
        public LineInfo getLineInfo() {
        LineInfo lineInfo = this.lineInfo;
        if (lineInfo == null) {
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        return lineInfo;
    }

    @Override
    public long getRequestTimestamp() {
        return this.requestTimestamp;
    }

    @Override
        public Integer getDocumentModificationSequence() {
        return this.documentModificationSequence;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BasicEditorRequest)) {
            return false;
        }
        BasicEditorRequest other = (BasicEditorRequest)o;
        if (this.isUseTabIndents() != other.isUseTabIndents()) {
            return false;
        }
        if (this.getTabWidth() != other.getTabWidth()) {
            return false;
        }
        if (this.getRequestId() != other.getRequestId()) {
            return false;
        }
        if (this.getOffset() != other.getOffset()) {
            return false;
        }
        if (this.getRequestTimestamp() != other.getRequestTimestamp()) {
            return false;
        }
        if (this.isCancelled() != other.isCancelled()) {
            return false;
        }
        Integer this$documentModificationSequence = this.getDocumentModificationSequence();
        Integer other$documentModificationSequence = other.getDocumentModificationSequence();
        if (this$documentModificationSequence == null ? other$documentModificationSequence != null : !((Object)this$documentModificationSequence).equals(other$documentModificationSequence)) {
            return false;
        }
        Project this$project = this.getProject();
        Project other$project = other.getProject();
        if (this$project == null ? other$project != null : !this$project.equals(other$project)) {
            return false;
        }
        LanguageSupport this$language = this.getLanguage();
        LanguageSupport other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        CompletionType this$completionType = this.getCompletionType();
        CompletionType other$completionType = other.getCompletionType();
        if (this$completionType == null ? other$completionType != null : !((Object)((Object)this$completionType)).equals((Object)other$completionType)) {
            return false;
        }
        Language this$fileLanguage = this.getFileLanguage();
        Language other$fileLanguage = other.getFileLanguage();
        if (this$fileLanguage == null ? other$fileLanguage != null : !this$fileLanguage.equals(other$fileLanguage)) {
            return false;
        }
        String this$relativeFilePath = this.getRelativeFilePath();
        String other$relativeFilePath = other.getRelativeFilePath();
        if (this$relativeFilePath == null ? other$relativeFilePath != null : !this$relativeFilePath.equals(other$relativeFilePath)) {
            return false;
        }
        String this$documentContent = this.getDocumentContent();
        String other$documentContent = other.getDocumentContent();
        if (this$documentContent == null ? other$documentContent != null : !this$documentContent.equals(other$documentContent)) {
            return false;
        }
        LineInfo this$lineInfo = this.getLineInfo();
        LineInfo other$lineInfo = other.getLineInfo();
        return !(this$lineInfo == null ? other$lineInfo != null : !((Object)this$lineInfo).equals(other$lineInfo));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isUseTabIndents() ? 79 : 97);
        result = result * 59 + this.getTabWidth();
        result = result * 59 + this.getRequestId();
        result = result * 59 + this.getOffset();
        long $requestTimestamp = this.getRequestTimestamp();
        result = result * 59 + (int)($requestTimestamp >>> 32 ^ $requestTimestamp);
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        Integer $documentModificationSequence = this.getDocumentModificationSequence();
        result = result * 59 + ($documentModificationSequence == null ? 43 : ((Object)$documentModificationSequence).hashCode());
        Project $project = this.getProject();
        result = result * 59 + ($project == null ? 43 : $project.hashCode());
        LanguageSupport $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        CompletionType $completionType = this.getCompletionType();
        result = result * 59 + ($completionType == null ? 43 : ((Object)((Object)$completionType)).hashCode());
        Language $fileLanguage = this.getFileLanguage();
        result = result * 59 + ($fileLanguage == null ? 43 : $fileLanguage.hashCode());
        String $relativeFilePath = this.getRelativeFilePath();
        result = result * 59 + ($relativeFilePath == null ? 43 : $relativeFilePath.hashCode());
        String $documentContent = this.getDocumentContent();
        result = result * 59 + ($documentContent == null ? 43 : $documentContent.hashCode());
        LineInfo $lineInfo = this.getLineInfo();
        result = result * 59 + ($lineInfo == null ? 43 : ((Object)$lineInfo).hashCode());
        return result;
    }

    public String toString() {
        return "BasicEditorRequest(language=" + this.getLanguage() + ", completionType=" + this.getCompletionType() + ", useTabIndents=" + this.isUseTabIndents() + ", tabWidth=" + this.getTabWidth() + ", requestId=" + this.getRequestId() + ", fileLanguage=" + this.getFileLanguage() + ", relativeFilePath=" + this.getRelativeFilePath() + ", documentContent=" + this.getDocumentContent() + ", offset=" + this.getOffset() + ", lineInfo=" + this.getLineInfo() + ", requestTimestamp=" + this.getRequestTimestamp() + ", documentModificationSequence=" + this.getDocumentModificationSequence() + ", isCancelled=" + this.isCancelled() + ")";
    }

    public BasicEditorRequest(Project project, LanguageSupport language, CompletionType completionType, boolean useTabIndents, int tabWidth, int requestId, Language fileLanguage, String relativeFilePath, String documentContent, int offset, LineInfo lineInfo, Integer documentModificationSequence, boolean isCancelled) {
        if (relativeFilePath == null) {
            throw new IllegalStateException("relativeFilePath cannot be null!");
        }
        if (lineInfo == null) {
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        this.requestTimestamp = System.currentTimeMillis();
        if (relativeFilePath == null) {
            throw new NullPointerException("relativeFilePath is marked non-null but is null");
        }
        if (lineInfo == null) {
            throw new NullPointerException("lineInfo is marked non-null but is null");
        }
        this.project = project;
        this.language = language;
        this.completionType = completionType;
        this.useTabIndents = useTabIndents;
        this.tabWidth = tabWidth;
        this.requestId = requestId;
        this.fileLanguage = fileLanguage;
        this.relativeFilePath = relativeFilePath;
        this.documentContent = documentContent;
        this.offset = offset;
        this.lineInfo = lineInfo;
        this.documentModificationSequence = documentModificationSequence;
        this.isCancelled = isCancelled;
    }

    public BasicEditorRequest(Project project, LanguageSupport language, CompletionType completionType, boolean useTabIndents, int tabWidth, int requestId, Language fileLanguage, String relativeFilePath, String documentContent, int offset, LineInfo lineInfo, Integer documentModificationSequence) {
        if (relativeFilePath == null) {
            throw new IllegalStateException("relativeFilePath cannot be null!");
        }
        if (lineInfo == null) {
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        this.requestTimestamp = System.currentTimeMillis();
        if (relativeFilePath == null) {
            throw new NullPointerException("relativeFilePath is marked non-null but is null");
        }
        if (lineInfo == null) {
            throw new NullPointerException("lineInfo is marked non-null but is null");
        }
        this.project = project;
        this.language = language;
        this.completionType = completionType;
        this.useTabIndents = useTabIndents;
        this.tabWidth = tabWidth;
        this.requestId = requestId;
        this.fileLanguage = fileLanguage;
        this.relativeFilePath = relativeFilePath;
        this.documentContent = documentContent;
        this.offset = offset;
        this.lineInfo = lineInfo;
        this.documentModificationSequence = documentModificationSequence;
    }

    public BasicEditorRequest withUseTabIndents(boolean useTabIndents) {
        return this.useTabIndents == useTabIndents ? this : new BasicEditorRequest(this.project, this.language, this.completionType, useTabIndents, this.tabWidth, this.requestId, this.fileLanguage, this.relativeFilePath, this.documentContent, this.offset, this.lineInfo, this.documentModificationSequence, this.isCancelled);
    }

    
}

