/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.CopilotBundle;
import com.github.copilot.actions.AbstractDisableCopilotCompletionsAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiFile;

public class DisableFileCopilotCompletionsAction
extends AbstractDisableCopilotCompletionsAction {
    public DisableFileCopilotCompletionsAction() {
        super(true);
    }

    @Override
    public void update(AnActionEvent e) {
        PsiFile file;
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        super.update(e);
        if (e.getPresentation().isEnabledAndVisible() && (file = (PsiFile)e.getData(CommonDataKeys.PSI_FILE)) != null) {
            String language = file.getLanguage().getDisplayName();
            e.getPresentation().setText(CopilotBundle.get("action.copilot.disableCopilotLanguage.text", language));
        }
    }

    
}

