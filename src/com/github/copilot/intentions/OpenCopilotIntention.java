/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.intention.PriorityAction
 *  com.intellij.codeInsight.intention.PriorityAction$Priority
 *  com.intellij.codeInsight.intention.impl.BaseIntentionAction
 *  com.intellij.codeInspection.util.IntentionFamilyName
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.DumbAware
 *  com.intellij.openapi.project.Project
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.IncorrectOperationException
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.intentions;

import com.github.copilot.CopilotBundle;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.toolWindow.CopilotSplitEditorManager;
import com.intellij.codeInsight.intention.PriorityAction;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class OpenCopilotIntention
extends BaseIntentionAction
implements DumbAware,
PriorityAction {
    public OpenCopilotIntention() {
        this.setText(CopilotBundle.get("intention.openCopilot.text"));
    }

        @IntentionFamilyName
    public String getFamilyName() {
        String string = CopilotBundle.get("intention.openCopilot.familyName");
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public boolean isAvailable(Project project, Editor editor, PsiFile file) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (!CopilotEditorUtil.isSelectedEditor(editor)) {
            return false;
        }
        return CopilotEditorManager.getInstance().isAvailable(editor);
    }

    public boolean startInWriteAction() {
        return false;
    }

        public PriorityAction.Priority getPriority() {
        PriorityAction.Priority priority = PriorityAction.Priority.LOW;
        if (priority == null) {
            throw new IllegalStateException("priority cannot be null!");
        }
        return priority;
    }

    public void invoke(Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        CopilotSplitEditorManager.getInstance().openCopilot(editor, true);
        TelemetryService.getInstance().track("command.executed", Map.of("command", "intention.openCopilot"));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: 
            case 3: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 3: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/intentions/OpenCopilotIntention";
                break;
            }
            case 1: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getFamilyName";
                break;
            }
            case 1: 
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/intentions/OpenCopilotIntention";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getPriority";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "isAvailable";
                break;
            }
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "invoke";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 3: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

