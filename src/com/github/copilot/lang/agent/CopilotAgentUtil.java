/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.execution.configurations.PathEnvironmentVariableUtil
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.github.copilot.CopilotPlugin;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.Nullable;

public class CopilotAgentUtil {
    private static final Logger LOG = Logger.getInstance(CopilotAgentUtil.class);

        public static Path getNodeExecutablePath() {
        File path = PathEnvironmentVariableUtil.findExecutableInPathOnAnyOS((String)"node");
        if (path == null) {
            LOG.warn("node executable not found in $PATH");
            return null;
        }
        Path nioPath = path.toPath();
        if (!Files.isExecutable(nioPath)) {
            LOG.warn("node executable has no execute permissions: " + nioPath);
            return null;
        }
        LOG.debug("Found node executable at " + nioPath);
        return nioPath;
    }

        public static Path getAgentDirectoryPath() {
        String envValue;
        if (ApplicationManager.getApplication().isUnitTestMode() && (envValue = System.getenv("GITHUB_COPILOT_AGENTDIR")) != null) {
            Path envPath = Paths.get(envValue, new String[0]);
            if (!Files.exists(envPath, new LinkOption[0])) {
                LOG.error("GITHUB_COPILOT_AGENTDIR path doesn't exist: " + envPath);
                return null;
            }
            return envPath;
        }
        Path basePath = CopilotPlugin.getPluginBasePath();
        Path distPath = basePath.resolve("copilot-agent/dist");
        if (Files.exists(distPath, new LinkOption[0])) {
            return distPath;
        }
        LOG.error("Unable to locate the Copilot agent dist path in base path: " + basePath);
        return null;
    }
}

