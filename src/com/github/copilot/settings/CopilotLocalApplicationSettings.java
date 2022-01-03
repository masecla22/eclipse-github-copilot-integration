/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.components.PersistentStateComponent
 *  com.intellij.openapi.components.RoamingType
 *  com.intellij.openapi.components.State
 *  com.intellij.openapi.components.Storage
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.settings.CopilotLocalApplicationState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

@State(name="github-copilot-local", storages={@Storage(value="github-copilot.local.xml", roamingType=RoamingType.DISABLED)})
public class CopilotLocalApplicationSettings
implements PersistentStateComponent<CopilotLocalApplicationState> {
    private CopilotLocalApplicationState state;

        public static CopilotLocalApplicationState settings() {
        CopilotLocalApplicationState state = ((CopilotLocalApplicationSettings)ApplicationManager.getApplication().getService(CopilotLocalApplicationSettings.class)).getState();
        assert (state != null);
        CopilotLocalApplicationState copilotLocalApplicationState = state;
        if (copilotLocalApplicationState == null) {
            throw new IllegalStateException("copilotLocalApplicationState cannot be null!");
        }
        return copilotLocalApplicationState;
    }

        public synchronized CopilotLocalApplicationState getState() {
        return this.state;
    }

    public synchronized void noStateLoaded() {
        CopilotLocalApplicationState state = new CopilotLocalApplicationState();
        state.githubTokenMigration = true;
        this.state = state;
    }

    public synchronized void loadState(CopilotLocalApplicationState state) {
        if (state == null) {
            throw new IllegalStateException("state cannot be null!");
        }
        this.state = state;
    }

    
}

