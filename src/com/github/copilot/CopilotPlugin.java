/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ide.plugins.IdeaPluginDescriptor
 *  com.intellij.ide.plugins.PluginManagerCore
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationInfo
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.extensions.PluginId
 *  com.intellij.openapi.util.BuildNumber
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot;

import com.github.copilot.CopilotLifecycleService;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.BuildNumber;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CopilotPlugin {
    public static final PluginId COPILOT_ID = PluginId.getId((String)"com.github.copilot");

    private CopilotPlugin() {
    }

        public static String getVersion() {
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin((PluginId)COPILOT_ID);
        String string = plugin == null ? "unknown" : plugin.getVersion();
        if (string == null) {
            CopilotPlugin.$$$reportNull$$$0(0);
        }
        return string;
    }

        public static Path getPluginBasePath() {
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin((PluginId)COPILOT_ID);
        assert (plugin != null);
        Path path = plugin.getPluginPath();
        if (path == null) {
            CopilotPlugin.$$$reportNull$$$0(1);
        }
        return path;
    }

        public static Disposable getLifecycleDisposable() {
        return (Disposable)ApplicationManager.getApplication().getService(CopilotLifecycleService.class);
    }

        public static String editorVersionString() {
        String string;
        try {
            BuildNumber build = ApplicationInfo.getInstance().getBuild();
            string = "JetBrains-" + build.getProductCode() + "/" + build.asStringWithoutProductCode();
        }
        catch (Exception e) {
            return "JetBrains-??/ERROR";
        }
        if (string == null) {
            CopilotPlugin.$$$reportNull$$$0(2);
        }
        return string;
    }

        public static String pluginVersionString() {
        String string;
        try {
            string = "copilot-intellij/" + CopilotPlugin.getVersion();
        }
        catch (Exception e) {
            return "copilot-intellij/ERROR";
        }
        if (string == null) {
            CopilotPlugin.$$$reportNull$$$0(3);
        }
        return string;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[2];
        objectArray2[0] = "com/github/copilot/CopilotPlugin";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getVersion";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getPluginBasePath";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "editorVersionString";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "pluginVersionString";
                break;
            }
        }
        throw new IllegalStateException(String.format("method %s.%s must not return null", objectArray));
    }
}

