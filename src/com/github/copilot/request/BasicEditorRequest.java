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
            BasicEditorRequest.$$$reportNull$$$0(0);
        }
        if (languageSupport == null) {
            BasicEditorRequest.$$$reportNull$$$0(1);
        }
        return BasicEditorRequest.createGhostText(file, languageSupport, "", 0, new LineInfo(1, 0, 0, 0, "", 0));
    }

    @TestOnly
    public static BasicEditorRequest createGhostText(PsiFile file, LanguageSupport languageSupport, String text, int offset, LineInfo lineInfo) {
        if (file == null) {
            BasicEditorRequest.$$$reportNull$$$0(2);
        }
        if (languageSupport == null) {
            BasicEditorRequest.$$$reportNull$$$0(3);
        }
        if (text == null) {
            BasicEditorRequest.$$$reportNull$$$0(4);
        }
        if (lineInfo == null) {
            BasicEditorRequest.$$$reportNull$$$0(5);
        }
        return new BasicEditorRequest(file.getProject(), languageSupport, CompletionType.GhostText, file.getLanguage(), file.getName(), text, offset, lineInfo, true, 4, 0);
    }

    public BasicEditorRequest(Project project, LanguageSupport language, CompletionType completionType, Language fileLanguage, String relativeFilePath, String documentContent, int offset, LineInfo lineInfo, boolean useTabIndents, int tabWidth, Integer documentModificationSequence) {
        if (project == null) {
            BasicEditorRequest.$$$reportNull$$$0(6);
        }
        if (language == null) {
            BasicEditorRequest.$$$reportNull$$$0(7);
        }
        if (completionType == null) {
            BasicEditorRequest.$$$reportNull$$$0(8);
        }
        if (fileLanguage == null) {
            BasicEditorRequest.$$$reportNull$$$0(9);
        }
        if (relativeFilePath == null) {
            BasicEditorRequest.$$$reportNull$$$0(10);
        }
        if (documentContent == null) {
            BasicEditorRequest.$$$reportNull$$$0(11);
        }
        if (lineInfo == null) {
            BasicEditorRequest.$$$reportNull$$$0(12);
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
            BasicEditorRequest.$$$reportNull$$$0(13);
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
            BasicEditorRequest.$$$reportNull$$$0(14);
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
            BasicEditorRequest.$$$reportNull$$$0(15);
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
            BasicEditorRequest.$$$reportNull$$$0(16);
        }
        if (lineInfo == null) {
            BasicEditorRequest.$$$reportNull$$$0(17);
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
            BasicEditorRequest.$$$reportNull$$$0(18);
        }
        if (lineInfo == null) {
            BasicEditorRequest.$$$reportNull$$$0(19);
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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 14: 
            case 15: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 14: 
            case 15: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 1: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "languageSupport";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "text";
                break;
            }
            case 5: 
            case 12: 
            case 17: 
            case 19: {
                objectArray2 = objectArray3;
                objectArray3[0] = "lineInfo";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "language";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionType";
                break;
            }
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "fileLanguage";
                break;
            }
            case 10: 
            case 16: 
            case 18: {
                objectArray2 = objectArray3;
                objectArray3[0] = "relativeFilePath";
                break;
            }
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "documentContent";
                break;
            }
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "o";
                break;
            }
            case 14: 
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/request/BasicEditorRequest";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/request/BasicEditorRequest";
                break;
            }
            case 14: {
                objectArray = objectArray2;
                objectArray2[1] = "getRelativeFilePath";
                break;
            }
            case 15: {
                objectArray = objectArray2;
                objectArray2[1] = "getLineInfo";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "createEmptyGhostText";
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "createGhostText";
                break;
            }
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 16: 
            case 17: 
            case 18: 
            case 19: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 13: {
                objectArray = objectArray;
                objectArray[2] = "equalsRequest";
                break;
            }
            case 14: 
            case 15: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 14: 
            case 15: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

