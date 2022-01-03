/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.ui.ComboBox
 *  com.intellij.ui.ColorPanel
 *  com.intellij.ui.components.JBCheckBox
 *  com.intellij.ui.components.JBLabel
 *  com.intellij.uiDesigner.core.GridConstraints
 *  com.intellij.uiDesigner.core.GridLayoutManager
 *  com.intellij.uiDesigner.core.Spacer
 *  com.intellij.util.ui.PresentableEnumUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.settings.LanguageTable;
import com.github.copilot.settings.UpdateChannel;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.PresentableEnumUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsForm {
    private JPanel panel;
    private JBCheckBox enableCompletions;
    private JBCheckBox updateCheckbox;
    private ColorPanel inlayTextColorPanel;
    private JBCheckBox inlayTextColorBox;
    private ComboBox<UpdateChannel> updateChannelBox;
    private JComponent languageTablePanel;
    private JBCheckBox enableIdeCompletions;
    private LanguageTable languageTable;

    public SettingsForm() {
        this.$$$setupUI$$$();
        this.inlayTextColorBox.addActionListener(e -> this.inlayTextColorPanel.setEnabled(this.inlayTextColorBox.isSelected()));
        this.languageTable.initItems(CopilotApplicationSettings.settings().getDisabledLanguageIds());
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public boolean isEnableCompletions() {
        return this.enableCompletions.isSelected();
    }

    public void setEnableCompletions(boolean enableCompletions) {
        this.enableCompletions.setSelected(enableCompletions);
    }

    public boolean isEnableIdeCompletions() {
        return this.enableIdeCompletions.isSelected();
    }

    public void setEnableIdeCompletions(boolean enableCompletions) {
        this.enableIdeCompletions.setSelected(enableCompletions);
    }

    public boolean isUpdateCheckEnabled() {
        return this.updateCheckbox.isSelected();
    }

    public void setUpdateCheckEnabled(boolean enabled) {
        this.updateCheckbox.setSelected(enabled);
    }

    @Nullable
    public Color getInlayTextColor() {
        return this.inlayTextColorBox.isSelected() ? this.inlayTextColorPanel.getSelectedColor() : null;
    }

    public void setInlayTextColor(@Nullable Color color) {
        this.inlayTextColorBox.setSelected(color != null);
        this.inlayTextColorPanel.setSelectedColor(color);
    }

    @NotNull
    public UpdateChannel getUpdateChannel() {
        UpdateChannel updateChannel = (UpdateChannel)((Object)this.updateChannelBox.getItem());
        if (updateChannel == null) {
            SettingsForm.$$$reportNull$$$0(0);
        }
        return updateChannel;
    }

    public void setUpdateChannel(@NotNull UpdateChannel channel) {
        if (channel == null) {
            SettingsForm.$$$reportNull$$$0(1);
        }
        this.updateChannelBox.setSelectedItem((Object)channel);
    }

    public void setDisabledLanguages(@NotNull Set<String> languageIds) {
        if (languageIds == null) {
            SettingsForm.$$$reportNull$$$0(2);
        }
        this.languageTable.setDisabledLanguages(languageIds);
    }

    public Set<String> getDisabledLanguages() {
        return this.languageTable.getDisabledLanguages();
    }

    private void createUIComponents() {
        this.updateChannelBox = new ComboBox();
        PresentableEnumUtil.fill(this.updateChannelBox, UpdateChannel.class);
        this.languageTable = new LanguageTable();
        this.languageTablePanel = this.languageTable.getComponent();
    }

    private /* synthetic */ void $$$setupUI$$$() {
        JBCheckBox jBCheckBox;
        JBCheckBox jBCheckBox2;
        ColorPanel colorPanel;
        JBCheckBox jBCheckBox3;
        JBCheckBox jBCheckBox4;
        JPanel jPanel;
        this.createUIComponents();
        this.panel = jPanel = new JPanel();
        jPanel.setLayout((LayoutManager)new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        Spacer spacer = new Spacer();
        jPanel.add((Component)spacer, new GridConstraints(7, 0, 1, 2, 0, 2, 1, 6, null, null, null));
        this.enableCompletions = jBCheckBox4 = new JBCheckBox();
        this.$$$loadButtonText$$$((AbstractButton)jBCheckBox4, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.enableCompletions.label"));
        jPanel.add((Component)jBCheckBox4, new GridConstraints(0, 0, 1, 2, 0, 1, 3, 3, null, null, null));
        this.updateCheckbox = jBCheckBox3 = new JBCheckBox();
        this.$$$loadButtonText$$$((AbstractButton)jBCheckBox3, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.checkForUpdates.label"));
        jPanel.add((Component)jBCheckBox3, new GridConstraints(3, 0, 1, 1, 0, 1, 3, 3, null, null, null));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout((LayoutManager)new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        jPanel.add((Component)jPanel2, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, null, null, null));
        this.inlayTextColorPanel = colorPanel = new ColorPanel();
        jPanel2.add((Component)colorPanel, new GridConstraints(0, 1, 1, 1, 4, 0, 0, 0, null, null, null));
        Spacer spacer2 = new Spacer();
        jPanel2.add((Component)spacer2, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, null, null, null));
        this.inlayTextColorBox = jBCheckBox2 = new JBCheckBox();
        this.$$$loadButtonText$$$((AbstractButton)jBCheckBox2, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.enableInlayTextColor.label"));
        jPanel2.add((Component)jBCheckBox2, new GridConstraints(0, 0, 1, 1, 0, 0, 3, 3, null, null, null));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout((LayoutManager)new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        jPanel.add((Component)jPanel3, new GridConstraints(4, 0, 1, 1, 0, 3, 3, 3, null, null, null));
        JBLabel jBLabel = new JBLabel();
        this.$$$loadLabelText$$$((JLabel)jBLabel, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.channel.label"));
        jPanel3.add((Component)jBLabel, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 3, null, null, null));
        Spacer spacer3 = new Spacer();
        jPanel3.add((Component)spacer3, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, null, null, null));
        ComboBox<UpdateChannel> comboBox = this.updateChannelBox;
        comboBox.setSwingPopup(true);
        jPanel3.add((Component)comboBox, new GridConstraints(0, 1, 1, 1, 0, 0, 3, 3, null, null, null));
        JComponent jComponent = this.languageTablePanel;
        jPanel.add((Component)jComponent, new GridConstraints(6, 0, 1, 1, 1, 1, 3, 7, new Dimension(-1, 150), new Dimension(-1, 300), null));
        JBLabel jBLabel2 = new JBLabel();
        this.$$$loadLabelText$$$((JLabel)jBLabel2, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.language.disabledLabel"));
        jPanel.add((Component)jBLabel2, new GridConstraints(5, 0, 1, 1, 8, 0, 3, 3, null, null, null));
        this.enableIdeCompletions = jBCheckBox = new JBCheckBox();
        this.$$$loadButtonText$$$((AbstractButton)jBCheckBox, ResourceBundle.getBundle("copilot/copilot").getString("applicationConfigurable.ideCompletions.text"));
        jPanel.add((Component)jBCheckBox, new GridConstraints(1, 0, 1, 1, 8, 0, 3, 3, null, null, null));
        jBLabel.setLabelFor((Component)comboBox);
    }

    public /* synthetic */ JComponent $$$getRootComponent$$$() {
        return this.panel;
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

    private /* synthetic */ void $$$loadButtonText$$$(AbstractButton abstractButton, String string) {
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
        abstractButton.setText(stringBuffer.toString());
        if (bl) {
            abstractButton.setMnemonic(c);
            abstractButton.setDisplayedMnemonicIndex(n);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/settings/SettingsForm";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "channel";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "languageIds";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getUpdateChannel";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/settings/SettingsForm";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "setUpdateChannel";
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "setDisabledLanguages";
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
            case 2: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

