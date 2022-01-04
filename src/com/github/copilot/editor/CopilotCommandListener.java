/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.command.CommandEvent
 *  com.intellij.openapi.command.CommandListener
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.fileEditor.FileEditorManager
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

public class CopilotCommandListener implements CommandListener {
	private static final Logger LOG = Logger.getInstance(CopilotCommandListener.class);
	private final Project project;

	public CopilotCommandListener(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		this.project = project;
	}

	public void commandFinished(CommandEvent event) {
		if (event == null) {
			throw new IllegalStateException("event cannot be null!");
		}
		if (!this.isModificationCommand(event)) {
			LOG.debug("Skipping editor modification for command: " + event.getCommandName());
			return;
		}
		FileEditorManager fileEditorManager = FileEditorManager.getInstance((Project) this.project);
		if (fileEditorManager == null) {
			return;
		}
		Editor editor = fileEditorManager.getSelectedTextEditor();
		if (editor == null || event.getDocument() != null && !event.getDocument().equals(editor.getDocument())) {
			return;
		}
		if (!CopilotApplicationSettings.isCopilotEnabled(this.project, editor)) {
			return;
		}
		CopilotEditorManager editorManager = CopilotEditorManager.getInstance();
		if (editorManager.isAvailable(editor)) {
			editorManager.editorModified(editor, this.isForcedCompletionCommand(event));
		}
	}

	public void undoTransparentActionFinished() {
		FileEditorManager fileEditorManager = FileEditorManager.getInstance((Project) this.project);
		if (fileEditorManager == null) {
			return;
		}
		Editor editor = fileEditorManager.getSelectedTextEditor();
		if (editor == null) {
			return;
		}
		if (!CopilotApplicationSettings.isCopilotEnabled(this.project, editor)) {
			return;
		}
		CopilotEditorManager editorManager = CopilotEditorManager.getInstance();
		if (editorManager.isAvailable(editor) && editorManager.hasCompletionInlays(editor)) {
			editorManager.editorModified(editor, true);
		}
	}

	private boolean isModificationCommand(CommandEvent command) {
		String name;
		if (command == null) {
			throw new IllegalStateException("command cannot be null!");
		}
		if ((name = command.getCommandName()) == null || command.getDocument() == null) {
			return false;
		}
		return name.equals("Typing") || name.equals("Backspace") || name.equals("Choose Lookup Item")
				|| name.equals("Choose Lookup Item Replace") || this.isForcedCompletionCommand(command);
	}

	private boolean isForcedCompletionCommand(CommandEvent command) {
		String name;
		if (command == null) {
			throw new IllegalStateException("command cannot be null!");
		}
		return (name = command.getCommandName()).equals("Undo") || name.startsWith("Undo ");
	}

}
