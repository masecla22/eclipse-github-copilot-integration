/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.Disposable
 *  me.masecla.copilot.extra.Logger
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.ex.DocumentEx
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.psi.PsiDocumentManager
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.request.BasicEditorRequest;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LineInfo;
import com.github.copilot.request.RequestId;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import me.masecla.copilot.extra.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

public class AgentEditorRequest implements EditorRequest, Disposable {
	private static final Logger LOG = Logger.getInstance(BasicEditorRequest.class);
	private final Project project;
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

	public static EditorRequest create(Editor editor, int offset, CompletionType completionType) {
		Project project = editor.getProject();
		if (project == null) {
			return null;
		}
		Document document = editor.getDocument();
		PsiFile file = PsiDocumentManager.getInstance((Project) project).getPsiFile(document);
		if (file == null) {
			return null;
		}
		boolean useTabs = editor.getSettings().isUseTabCharacter(project);
		int tabWidth = editor.getSettings().getTabSize(project);
		String relativePath = CopilotEditorUtil.getRelativeFilePath(project, file);
		LineInfo lineInfo = LineInfo.create(document, offset);
		return new AgentEditorRequest(project, completionType, useTabs, tabWidth, RequestId.incrementAndGet(),
				file.getLanguage(), relativePath, document.getText(), offset, lineInfo,
				document instanceof DocumentEx ? Integer.valueOf(((DocumentEx) document).getModificationSequence())
						: null);
	}

	@Override
	public void cancel() {
		this.isCancelled = true;
		Disposer.dispose((Disposable) this);
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
	public Disposable getDisposable() {
		return this;
	}

	@Override
	public Project getProject() {
		return this.project;
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
		if (!(o instanceof AgentEditorRequest)) {
			return false;
		}
		AgentEditorRequest other = (AgentEditorRequest) o;
		if (!other.canEqual(this)) {
			return false;
		}
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
		if (this$documentModificationSequence == null ? other$documentModificationSequence != null
				: !((Object) this$documentModificationSequence).equals(other$documentModificationSequence)) {
			return false;
		}
		Project this$project = this.getProject();
		Project other$project = other.getProject();
		if (this$project == null ? other$project != null : !this$project.equals(other$project)) {
			return false;
		}
		CompletionType this$completionType = this.getCompletionType();
		CompletionType other$completionType = other.getCompletionType();
		if (this$completionType == null ? other$completionType != null
				: !(this$completionType).equals(other$completionType)) {
			return false;
		}
		Language this$fileLanguage = this.getFileLanguage();
		Language other$fileLanguage = other.getFileLanguage();
		if (this$fileLanguage == null ? other$fileLanguage != null : !this$fileLanguage.equals(other$fileLanguage)) {
			return false;
		}
		String this$relativeFilePath = this.getRelativeFilePath();
		String other$relativeFilePath = other.getRelativeFilePath();
		if (this$relativeFilePath == null ? other$relativeFilePath != null
				: !this$relativeFilePath.equals(other$relativeFilePath)) {
			return false;
		}
		String this$documentContent = this.getDocumentContent();
		String other$documentContent = other.getDocumentContent();
		if (this$documentContent == null ? other$documentContent != null
				: !this$documentContent.equals(other$documentContent)) {
			return false;
		}
		LineInfo this$lineInfo = this.getLineInfo();
		LineInfo other$lineInfo = other.getLineInfo();
		return !(this$lineInfo == null ? other$lineInfo != null : !((Object) this$lineInfo).equals(other$lineInfo));
	}

	protected boolean canEqual(Object other) {
		return other instanceof AgentEditorRequest;
	}

	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		result = result * 59 + (this.isUseTabIndents() ? 79 : 97);
		result = result * 59 + this.getTabWidth();
		result = result * 59 + this.getRequestId();
		result = result * 59 + this.getOffset();
		long $requestTimestamp = this.getRequestTimestamp();
		result = result * 59 + (int) ($requestTimestamp >>> 32 ^ $requestTimestamp);
		result = result * 59 + (this.isCancelled() ? 79 : 97);
		Integer $documentModificationSequence = this.getDocumentModificationSequence();
		result = result * 59
				+ ($documentModificationSequence == null ? 43 : ((Object) $documentModificationSequence).hashCode());
		Project $project = this.getProject();
		result = result * 59 + ($project == null ? 43 : $project.hashCode());
		CompletionType $completionType = this.getCompletionType();
		result = result * 59 + ($completionType == null ? 43 : ($completionType).hashCode());
		Language $fileLanguage = this.getFileLanguage();
		result = result * 59 + ($fileLanguage == null ? 43 : $fileLanguage.hashCode());
		String $relativeFilePath = this.getRelativeFilePath();
		result = result * 59 + ($relativeFilePath == null ? 43 : $relativeFilePath.hashCode());
		String $documentContent = this.getDocumentContent();
		result = result * 59 + ($documentContent == null ? 43 : $documentContent.hashCode());
		LineInfo $lineInfo = this.getLineInfo();
		result = result * 59 + ($lineInfo == null ? 43 : ((Object) $lineInfo).hashCode());
		return result;
	}

	public String toString() {
		return "AgentEditorRequest(completionType=" + this.getCompletionType() + ", useTabIndents="
				+ this.isUseTabIndents() + ", tabWidth=" + this.getTabWidth() + ", requestId=" + this.getRequestId()
				+ ", fileLanguage=" + this.getFileLanguage() + ", relativeFilePath=" + this.getRelativeFilePath()
				+ ", documentContent=" + this.getDocumentContent() + ", offset=" + this.getOffset() + ", lineInfo="
				+ this.getLineInfo() + ", requestTimestamp=" + this.getRequestTimestamp()
				+ ", documentModificationSequence=" + this.getDocumentModificationSequence() + ", isCancelled="
				+ this.isCancelled() + ")";
	}

	public AgentEditorRequest(Project project, CompletionType completionType, boolean useTabIndents, int tabWidth,
			int requestId, Language fileLanguage, String relativeFilePath, String documentContent, int offset,
			LineInfo lineInfo, Integer documentModificationSequence) {
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

}
