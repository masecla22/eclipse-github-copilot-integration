/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ide.BrowserUtil
 *  com.intellij.ide.ClipboardSynchronizer
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.ui.DialogWrapper
 *  com.intellij.openapi.ui.DialogWrapper$DialogWrapperAction
 *  com.intellij.openapi.ui.DialogWrapper$IdeModalityType
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.github.copilot.github.DeviceCodeResponse;
import com.github.copilot.github.DeviceLoginForm;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.ClipboardSynchronizer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeviceLoginDialog
extends DialogWrapper {
    private final DeviceCodeResponse codeResponse;

    @RequiresEdt
    public static boolean showDeviceLogin(Project project, DeviceCodeResponse codeResponse) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (codeResponse == null) {
            throw new IllegalStateException("codeResponse cannot be null!");
        }
        DeviceLoginDialog dialog = new DeviceLoginDialog(project, codeResponse);
        dialog.init();
        return dialog.showAndGet();
    }

    public DeviceLoginDialog(Project project, DeviceCodeResponse codeResponse) {
        if (codeResponse == null) {
            throw new IllegalStateException("codeResponse cannot be null!");
        }
        super(project, false, DialogWrapper.IdeModalityType.PROJECT);
        this.codeResponse = codeResponse;
        this.setTitle(CopilotBundle.get("deviceAuth.dialog.title"));
    }

    protected void doOKAction() {
        StringSelection content = new StringSelection(this.codeResponse.getUserCode());
        ClipboardSynchronizer.getInstance().setContent((Transferable)content, (ClipboardOwner)content);
        BrowserUtil.browse((String)this.codeResponse.getVerificationUri());
        super.doOKAction();
    }

    protected Action [] createActions() {
        DialogWrapper.DialogWrapperAction proceedButton = new DialogWrapper.DialogWrapperAction(CopilotBundle.get("deviceAuth.okButton")){

            protected void doAction(ActionEvent e) {
                DeviceLoginDialog.super.doOKAction();
            }
        };
        this.getOKAction().putValue("Name", CopilotBundle.get("deviceAuth.copyOpenButton"));
        Action[] actionArray = new Action[]{this.getOKAction(), proceedButton, this.getCancelAction()};
        if (actionArray == null) {
            throw new IllegalStateException("actionArray cannot be null!");
        }
        return actionArray;
    }

        protected JComponent createCenterPanel() {
        DeviceLoginForm form = new DeviceLoginForm();
        form.update(this.codeResponse);
        return form.getMainPanel();
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
            case 3: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 3: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 1: 
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "codeResponse";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/github/DeviceLoginDialog";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/github/DeviceLoginDialog";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "createActions";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "showDeviceLogin";
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 3: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 3: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

