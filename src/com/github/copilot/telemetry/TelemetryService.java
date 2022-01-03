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
    @NotNull
    public static TelemetryService getInstance() {
        TelemetryService telemetryService = (TelemetryService)ApplicationManager.getApplication().getService(TelemetryService.class);
        if (telemetryService == null) {
            TelemetryService.$$$reportNull$$$0(0);
        }
        return telemetryService;
    }

    public void setTrackingId(@Nullable String var1);

    public void trackException(@NotNull Throwable var1, @NotNull Map<String, String> var2);

    public void track(@NotNull String var1, @NotNull TelemetryData var2);

    default public void track(@NotNull String name) {
        if (name == null) {
            TelemetryService.$$$reportNull$$$0(1);
        }
        this.track(name, Collections.emptyMap(), Collections.emptyMap());
    }

    default public void track(@NotNull String name, @NotNull Map<String, String> properties) {
        if (name == null) {
            TelemetryService.$$$reportNull$$$0(2);
        }
        if (properties == null) {
            TelemetryService.$$$reportNull$$$0(3);
        }
        this.track(name, properties, Collections.emptyMap());
    }

    public void track(@NotNull String var1, @NotNull Map<String, String> var2, @NotNull Map<String, Double> var3);

    public void trackSecure(@NotNull String var1, @NotNull TelemetryData var2);

    default public void trackSecure(@NotNull String name) {
        if (name == null) {
            TelemetryService.$$$reportNull$$$0(4);
        }
        this.trackSecure(name, Collections.emptyMap(), Collections.emptyMap());
    }

    public void trackSecure(@NotNull String var1, @NotNull Map<String, String> var2, @NotNull Map<String, Double> var3);

    default public void trackSecure(@NotNull String name, @NotNull Map<String, String> properties) {
        if (name == null) {
            TelemetryService.$$$reportNull$$$0(5);
        }
        if (properties == null) {
            TelemetryService.$$$reportNull$$$0(6);
        }
        this.trackSecure(name, properties, Collections.emptyMap());
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
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
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
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/telemetry/TelemetryService";
                break;
            }
            case 1: 
            case 2: 
            case 4: 
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
            case 3: 
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "properties";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getInstance";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/telemetry/TelemetryService";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "track";
                break;
            }
            case 4: 
            case 5: 
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "trackSecure";
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
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

