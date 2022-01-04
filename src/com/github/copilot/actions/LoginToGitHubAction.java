/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.github.GitHubService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import java.util.Objects;

public class LoginToGitHubAction extends AnAction implements DumbAware, CopilotAction {
	public void update(AnActionEvent e) {
		if (e == null) {
			throw new IllegalStateException("e cannot be null!");
		}
		e.getPresentation().setEnabled(e.getProject() != null);
	}

	public void actionPerformed(AnActionEvent e) {
		if (e == null) {
			throw new IllegalStateException("e cannot be null!");
		}
		GitHubService.getInstance().loginInteractive(Objects.requireNonNull(e.getProject()));
	}
}
