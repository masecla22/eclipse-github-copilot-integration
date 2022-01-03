/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ide.BrowserUtil
 *  com.intellij.ide.ClipboardSynchronizer
 *  com.intellij.ui.components.ActionLink
 *  com.intellij.ui.components.JBLabel
 *  com.intellij.ui.components.fields.ExtendableTextComponent$Extension
 *  com.intellij.ui.components.fields.ExtendableTextField
 *  com.intellij.uiDesigner.core.GridConstraints
 *  com.intellij.uiDesigner.core.GridLayoutManager
 *  com.intellij.uiDesigner.core.Spacer
 *  com.intellij.util.PlatformIcons
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.github.copilot.github.DeviceCodeResponse;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.ClipboardSynchronizer;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.fields.ExtendableTextComponent;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.PlatformIcons;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

class DeviceLoginForm {
    private ExtendableTextField codeField;
    private JPanel mainPanel;
    private JBLabel descriptionLabel;
    private ActionLink websiteAction;

    public DeviceLoginForm() {
        this.$$$setupUI$$$();
        this.websiteAction.addActionListener(actionEvent -> BrowserUtil.open((String)this.websiteAction.getText()));
    }

    private void createUIComponents() {
        this.descriptionLabel = new JBLabel();
        this.descriptionLabel.setAllowAutoWrapping(true);
        this.codeField = new ExtendableTextField();
        ExtendableTextComponent.Extension copy = ExtendableTextComponent.Extension.create((Icon)PlatformIcons.COPY_ICON, (String)CopilotBundle.get("deviceAuth.copyTooltip"), () -> {
            StringSelection content = new StringSelection(this.codeField.getText());
            ClipboardSynchronizer.getInstance().setContent((Transferable)content, (ClipboardOwner)content);
        });
        this.codeField.setExtensions(new ExtendableTextComponent.Extension[]{copy});
    }

    public void update(DeviceCodeResponse codeResponse) {
        if (codeResponse == null) {
            DeviceLoginForm.$$$reportNull$$$0(0);
        }
        String uri = codeResponse.getVerificationUri();
        String code = codeResponse.getUserCode();
        this.descriptionLabel.setText(CopilotBundle.get("deviceAuth.dialog.description", uri, code));
        this.websiteAction.setText(uri);
        this.codeField.setText(code);
    }

    public ExtendableTextField getCodeField() {
        return this.codeField;
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    public JBLabel getDescriptionLabel() {
        return this.descriptionLabel;
    }

    public ActionLink getWebsiteAction() {
        return this.websiteAction;
    }

    private /* synthetic */ void $$$setupUI$$$() {
        ActionLink actionLink;
        JPanel jPanel;
        this.createUIComponents();
        this.mainPanel = jPanel = new JPanel();
        jPanel.setLayout((LayoutManager)new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), 5, 5, false, false));
        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), null, 0, 0, null, null));
        JBLabel jBLabel = this.descriptionLabel;
        jBLabel.setText("-placeholder-");
        jPanel.add((Component)jBLabel, new GridConstraints(0, 0, 1, 2, 0, 1, 3, 3, null, new Dimension(350, -1), null));
        Spacer spacer = new Spacer();
        jPanel.add((Component)spacer, new GridConstraints(4, 1, 1, 1, 0, 2, 5, 6, null, null, null));
        JBLabel jBLabel2 = new JBLabel();
        this.$$$loadLabelText$$$((JLabel)jBLabel2, ResourceBundle.getBundle("copilot/copilot").getString("deviceAuth.code.label"));
        jPanel.add((Component)jBLabel2, new GridConstraints(3, 0, 1, 1, 0, 1, 3, 3, null, new Dimension(161, 18), null));
        JBLabel jBLabel3 = new JBLabel();
        this.$$$loadLabelText$$$((JLabel)jBLabel3, ResourceBundle.getBundle("copilot/copilot").getString("deviceAuth.website.label"));
        jPanel.add((Component)jBLabel3, new GridConstraints(2, 0, 1, 1, 0, 1, 3, 3, null, null, null));
        this.websiteAction = actionLink = new ActionLink();
        actionLink.setText("https://github.com");
        jPanel.add((Component)actionLink, new GridConstraints(2, 1, 1, 1, 0, 1, 3, 3, null, null, null));
        ExtendableTextField extendableTextField = this.codeField;
        extendableTextField.setEditable(false);
        jPanel.add((Component)extendableTextField, new GridConstraints(3, 1, 1, 1, 0, 1, 3, 3, null, null, null));
        Spacer spacer2 = new Spacer();
        jPanel.add((Component)spacer2, new GridConstraints(1, 0, 1, 1, 0, 2, 1, 0, new Dimension(-1, 10), null, new Dimension(-1, 10)));
        jBLabel2.setLabelFor((Component)extendableTextField);
        jBLabel3.setLabelFor((Component)actionLink);
    }

    public /* synthetic */ JComponent $$$getRootComponent$$$() {
        return this.mainPanel;
    }

    private /* synthetic */ void $$$loadLabelText$$$(JLabel jLabel, String string) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = false;
        char c = '\u0000';
        int n = -1;
        for (int i = 0; i < string.length(); ++i) {
            if (string.charAt(i) == '&') {
                if (++i == string.length()) break;
                if (!bl && string.charAt(i) != '&') {
                    bl = true;
                    c = string.charAt(i);
                    n = stringBuffer.length();
                }
            }
            stringBuffer.append(string.charAt(i));
        }
        jLabel.setText(stringBuffer.toString());
        if (bl) {
            jLabel.setDisplayedMnemonic(c);
            jLabel.setDisplayedMnemonicIndex(n);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "codeResponse", "com/github/copilot/github/DeviceLoginForm", "update"));
    }
}

