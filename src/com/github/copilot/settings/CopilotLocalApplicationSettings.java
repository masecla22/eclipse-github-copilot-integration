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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            case 1: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/settings/CopilotLocalApplicationSettings";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "state";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "settings";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/settings/CopilotLocalApplicationSettings";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "loadState";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

