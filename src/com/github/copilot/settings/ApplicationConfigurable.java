/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.options.Configurable
 *  com.intellij.openapi.options.ConfigurationException
 *  com.intellij.openapi.updateSettings.impl.UpdateSettings
 *  com.intellij.openapi.util.NlsContexts$ConfigurableName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.CopilotBundle;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.settings.CopilotApplicationState;
import com.github.copilot.settings.SettingsForm;
import com.github.copilot.settings.UpdateChannel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import com.intellij.openapi.util.NlsContexts;
import java.util.Objects;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApplicationConfigurable
implements Configurable {
    private final SettingsForm form = new SettingsForm();

    @NlsContexts.ConfigurableName
    public String getDisplayName() {
        return CopilotBundle.get("applicationConfigurable.displayName");
    }

        public JComponent createComponent() {
        return this.form.getPanel();
    }

    public void reset() {
        CopilotApplicationState applicationState = CopilotApplicationSettings.settings();
        this.form.setInlayTextColor(applicationState.inlayTextColor);
        this.form.setUpdateCheckEnabled(applicationState.checkForUpdate);
        this.form.setUpdateChannel(this.findConfiguredChannel());
        this.form.setEnableIdeCompletions(applicationState.isShowIdeCompletions());
        this.form.setEnableCompletions(applicationState.enableCompletions);
        this.form.setDisabledLanguages(applicationState.getDisabledLanguageIds());
    }

    public boolean isModified() {
        CopilotApplicationState settings = CopilotApplicationSettings.settings();
        if (this.form.isEnableCompletions() != settings.enableCompletions) {
            return true;
        }
        if (!Objects.equals(this.form.getInlayTextColor(), settings.inlayTextColor)) {
            return true;
        }
        if (this.form.isUpdateCheckEnabled() != settings.checkForUpdate) {
            return true;
        }
        if (this.form.isEnableIdeCompletions() != settings.isShowIdeCompletions()) {
            return true;
        }
        if (!this.form.getUpdateChannel().equals((Object)this.findConfiguredChannel())) {
            return true;
        }
        return !this.form.getDisabledLanguages().equals(settings.getDisabledLanguageIds());
    }

    public void apply() throws ConfigurationException {
        CopilotApplicationState settings = CopilotApplicationSettings.settings();
        settings.inlayTextColor = this.form.getInlayTextColor();
        settings.setShowIdeCompletions(this.form.isEnableIdeCompletions());
        settings.checkForUpdate = this.form.isUpdateCheckEnabled();
        settings.enableCompletions = this.form.isEnableCompletions();
        settings.setDisabledLanguageIds(this.form.getDisabledLanguages());
        UpdateChannel oldChannel = this.findConfiguredChannel();
        UpdateChannel newChannel = this.form.getUpdateChannel();
        if (!oldChannel.equals((Object)newChannel)) {
            if (!UpdateChannel.Stable.equals((Object)oldChannel)) {
                UpdateSettings.getInstance().getStoredPluginHosts().remove(oldChannel.getChannelUrl());
            }
            if (!UpdateChannel.Stable.equals((Object)newChannel)) {
                UpdateSettings.getInstance().getStoredPluginHosts().add(newChannel.getChannelUrl());
            }
        }
    }

        private UpdateChannel findConfiguredChannel() {
        UpdateChannel currentChannel = UpdateChannel.Stable;
        for (UpdateChannel channel : UpdateChannel.values()) {
            String url = channel.getChannelUrl();
            if (url == null || !UpdateSettings.getInstance().getStoredPluginHosts().contains(url)) continue;
            currentChannel = channel;
            break;
        }
        UpdateChannel updateChannel = currentChannel;
        if (updateChannel == null) {
            ApplicationConfigurable.$$$reportNull$$$0(0);
        }
        return updateChannel;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("method %s.%s must not return null", "com/github/copilot/settings/ApplicationConfigurable", "findConfiguredChannel"));
    }
}

