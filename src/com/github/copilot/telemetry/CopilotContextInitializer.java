/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationInfo
 *  com.intellij.openapi.application.PermanentInstallationID
 *  com.intellij.openapi.util.SystemInfoRt
 *  com.microsoft.applicationinsights.extensibility.ContextInitializer
 *  com.microsoft.applicationinsights.telemetry.TelemetryContext
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.telemetry;

import com.github.copilot.CopilotPlugin;
import com.github.copilot.CopilotSessionId;
import com.github.copilot.telemetry.AzureInsightsTelemetryService;
import com.github.copilot.util.Maps;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.PermanentInstallationID;
import com.intellij.openapi.util.SystemInfoRt;
import com.microsoft.applicationinsights.extensibility.ContextInitializer;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;
import java.util.Map;

class CopilotContextInitializer
implements ContextInitializer {
    static final CopilotContextInitializer INSTANCE = new CopilotContextInitializer();
    private final Map<String, String> staticProperties = Maps.of("common_vscodemachineid", PermanentInstallationID.get(), "common_vscodesessionid", CopilotSessionId.SESSION_ID, "common_platformversion", SystemInfoRt.OS_VERSION, "common_intellijversion", ApplicationInfo.getInstance().getFullVersion(), "common_intellijbuild", ApplicationInfo.getInstance().getBuild().asString(), "common_extname", "copilot-intellij", "common_extversion", CopilotPlugin.getVersion(), "common_uikind", "desktop", "common_os", CopilotContextInitializer.vscodeOSName(), "copilot_build", AzureInsightsTelemetryService.build(), "copilot_buildType", String.valueOf(AzureInsightsTelemetryService.isDeveloperMode()));
    private final Map<String, String> commonProperties = Map.of("editor_plugin_version", CopilotPlugin.pluginVersionString(), "editor_version", CopilotPlugin.editorVersionString(), "client_machineid", PermanentInstallationID.get(), "client_sessionid", CopilotSessionId.SESSION_ID);

    CopilotContextInitializer() {
    }

    public void initialize(TelemetryContext context) {
        context.getProperties().putAll(this.staticProperties);
        context.getProperties().putAll(this.commonProperties);
        context.getSession().setId(CopilotSessionId.SESSION_ID);
        context.getDevice().setId("");
        context.getCloud().setRoleInstance("");
    }

        private static String vscodeOSName() {
        if (SystemInfoRt.isWindows) {
            return "win32";
        }
        if (SystemInfoRt.isLinux) {
            return "linux";
        }
        if (SystemInfoRt.isMac) {
            return "darwin";
        }
        String string = SystemInfoRt.OS_NAME;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    
}

