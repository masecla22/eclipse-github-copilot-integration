/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.util.io.DigestUtil
 *  com.microsoft.applicationinsights.TelemetryClient
 *  com.microsoft.applicationinsights.TelemetryConfiguration
 *  com.microsoft.applicationinsights.internal.shutdown.SDKShutdownActivity
 *  com.microsoft.applicationinsights.telemetry.ExceptionTelemetry
 *  com.microsoft.applicationinsights.telemetry.SeverityLevel
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.telemetry;

import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.telemetry.CopilotContextInitializer;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.String2DoubleMap;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.io.DigestUtil;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.internal.shutdown.SDKShutdownActivity;
import com.microsoft.applicationinsights.telemetry.ExceptionTelemetry;
import com.microsoft.applicationinsights.telemetry.SeverityLevel;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AzureInsightsTelemetryService
implements TelemetryService,
Disposable {
    private static final Logger LOG = Logger.getInstance(AzureInsightsTelemetryService.class);
    private static final String KEY = "7d7048df-6dd0-4048-bb23-b716c1461f8f";
    private static final String KEY_SECURE = "3fdd7f28-937a-48c8-9a21-ba337db23bd1";
    private final TelemetryClient client = AzureInsightsTelemetryService.setupClient("7d7048df-6dd0-4048-bb23-b716c1461f8f");
    private final TelemetryClient clientSecure = AzureInsightsTelemetryService.setupClient("3fdd7f28-937a-48c8-9a21-ba337db23bd1");

    public AzureInsightsTelemetryService() {
        GitHubCopilotToken token = GitHubService.getInstance().getCopilotToken();
        this.setTrackingId(token != null ? token.getTrackingId() : null);
    }

    public void dispose() {
        LOG.warn("Disposing AzureInsightsTelemetryService");
        try {
            this.client.flush();
            this.clientSecure.flush();
        }
        catch (Exception e) {
            LOG.debug("Exception flushing telemetry data", (Throwable)e);
        }
        try {
            SDKShutdownActivity.INSTANCE.stopAll();
        }
        catch (Exception e) {
            LOG.error("Exception during shutdown of telemetry service", (Throwable)e);
        }
    }

    @Override
    public void setTrackingId(@Nullable String id) {
        if (id == null) {
            this.client.getContext().getUser().setId(null);
            this.clientSecure.getContext().getUser().setId(null);
        } else {
            String idHash = DigestUtil.sha256Hex((byte[])id.getBytes(StandardCharsets.UTF_8));
            this.client.getContext().getProperties().put("copilot_trackingId", id);
            this.client.getContext().getUser().setId(idHash);
            this.clientSecure.getContext().getProperties().put("copilot_trackingId", id);
            this.clientSecure.getContext().getUser().setId(idHash);
        }
    }

    @Override
    public void trackException(@NotNull Throwable original, @NotNull Map<String, String> properties) {
        if (original == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(0);
        }
        if (properties == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(1);
        }
        LOG.debug("Sending exception report: " + original.getMessage());
        ExceptionTelemetry data = new ExceptionTelemetry(original);
        data.getProperties().putAll(properties);
        data.setSeverityLevel(SeverityLevel.Error);
        this.client.trackException(data);
    }

    @Override
    public void track(@NotNull String name, @NotNull TelemetryData data) {
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(2);
        }
        if (data == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(3);
        }
        this.doTrack(this.client, name, data);
    }

    @Override
    public void track(@NotNull String name, @NotNull Map<String, String> properties, @NotNull Map<String, Double> metrics) {
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(4);
        }
        if (properties == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(5);
        }
        if (metrics == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(6);
        }
        this.doTrack(this.client, name, properties, metrics);
    }

    @Override
    public void trackSecure(@NotNull String name, @NotNull TelemetryData data) {
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(7);
        }
        if (data == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(8);
        }
        this.doTrack(this.clientSecure, name, data);
    }

    @Override
    public void trackSecure(@NotNull String name, @NotNull Map<String, String> properties, @NotNull Map<String, Double> metrics) {
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(9);
        }
        if (properties == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(10);
        }
        if (metrics == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(11);
        }
        this.doTrack(this.clientSecure, name, properties, metrics);
    }

    private void doTrack(@NotNull TelemetryClient client, @NotNull String name, @NotNull TelemetryData data) {
        long displayedTimestamp;
        if (client == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(12);
        }
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(13);
        }
        if (data == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(14);
        }
        long now = System.currentTimeMillis();
        HashMap<String, String> properties = new HashMap<String, String>();
        data.addProperties(properties);
        String2DoubleMap metrics = new String2DoubleMap();
        data.addMetrics((Object2DoubleMap<String>)metrics);
        long issuedTimestamp = data.getIssuedTimestamp();
        if (issuedTimestamp > 0L) {
            metrics.put("timeSinceIssuedMs", (double)now - (double)issuedTimestamp);
        }
        if ((displayedTimestamp = data.getDisplayedTimestamp()) > 0L) {
            metrics.put("timeSinceDisplayedMs", (double)now - (double)displayedTimestamp);
        }
        this.doTrack(client, name, properties, (Map<String, Double>)((Object)metrics));
    }

    private void doTrack(@NotNull TelemetryClient client, @NotNull String name, @NotNull Map<String, String> properties, @NotNull Map<String, Double> metrics) {
        if (client == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(15);
        }
        if (name == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(16);
        }
        if (properties == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(17);
        }
        if (metrics == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(18);
        }
        Object prefixedName = name.startsWith("copilot/") ? name : "copilot/" + name;
        LOG.debug("Sending telemetry event: " + (String)prefixedName);
        client.trackEvent((String)prefixedName, AzureInsightsTelemetryService.sanitizeMap(properties), AzureInsightsTelemetryService.sanitizeMap(metrics));
    }

    private static <V> Map<String, V> sanitizeMap(Map<String, V> data) {
        HashMap<String, V> map = new HashMap<String, V>();
        for (Map.Entry<String, V> entry : data.entrySet()) {
            map.put(entry.getKey().replace('.', '_'), entry.getValue());
        }
        return map;
    }

    private static TelemetryClient setupClient(@NotNull String key) {
        if (key == null) {
            AzureInsightsTelemetryService.$$$reportNull$$$0(19);
        }
        boolean isCI = System.getenv("CI") != null;
        boolean isUnitTestMode = ApplicationManager.getApplication().isUnitTestMode();
        TelemetryConfiguration config = TelemetryConfiguration.createDefault();
        config.getContextInitializers().add(CopilotContextInitializer.INSTANCE);
        config.setTrackingIsDisabled(isUnitTestMode || isCI);
        config.setInstrumentationKey(key);
        config.getChannel().setDeveloperMode(AzureInsightsTelemetryService.isDeveloperMode());
        return new TelemetryClient(config);
    }

    static boolean isDeveloperMode() {
        return ApplicationManager.getApplication().isInternal();
    }

    static String build() {
        if (AzureInsightsTelemetryService.isDeveloperMode()) {
            return "dev";
        }
        return "";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "original";
                break;
            }
            case 1: 
            case 5: 
            case 10: 
            case 17: {
                objectArray2 = objectArray3;
                objectArray3[0] = "properties";
                break;
            }
            case 2: 
            case 4: 
            case 7: 
            case 9: 
            case 13: 
            case 16: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
            case 3: 
            case 8: 
            case 14: {
                objectArray2 = objectArray3;
                objectArray3[0] = "data";
                break;
            }
            case 6: 
            case 11: 
            case 18: {
                objectArray2 = objectArray3;
                objectArray3[0] = "metrics";
                break;
            }
            case 12: 
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "client";
                break;
            }
            case 19: {
                objectArray2 = objectArray3;
                objectArray3[0] = "key";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/telemetry/AzureInsightsTelemetryService";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "trackException";
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "track";
                break;
            }
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: {
                objectArray = objectArray2;
                objectArray2[2] = "trackSecure";
                break;
            }
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: {
                objectArray = objectArray2;
                objectArray2[2] = "doTrack";
                break;
            }
            case 19: {
                objectArray = objectArray2;
                objectArray2[2] = "setupClient";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

