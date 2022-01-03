/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.telemetry;

import com.github.copilot.telemetry.TelemetryData;
import com.intellij.openapi.application.ApplicationManager;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TelemetryService {
        public static TelemetryService getInstance() {
        TelemetryService telemetryService = (TelemetryService)ApplicationManager.getApplication().getService(TelemetryService.class);
        if (telemetryService == null) {
            throw new IllegalStateException("telemetryService cannot be null!");
        }
        return telemetryService;
    }

    public void setTrackingId(String var1);

    public void trackException(Throwable var1, Map<String, String> var2);

    public void track(String var1, TelemetryData var2);

    default public void track(String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        this.track(name, Collections.emptyMap(), Collections.emptyMap());
    }

    default public void track(String name, Map<String, String> properties) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        this.track(name, properties, Collections.emptyMap());
    }

    public void track(String var1, Map<String, String> var2, Map<String, Double> var3);

    public void trackSecure(String var1, TelemetryData var2);

    default public void trackSecure(String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        this.trackSecure(name, Collections.emptyMap(), Collections.emptyMap());
    }

    public void trackSecure(String var1, Map<String, String> var2, Map<String, Double> var3);

    default public void trackSecure(String name, Map<String, String> properties) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        this.trackSecure(name, properties, Collections.emptyMap());
    }

    
}

