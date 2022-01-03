/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.telemetry;

import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.intellij.openapi.application.ApplicationManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class TestTelemetryService
implements TelemetryService {
    private volatile String trackingId;
    private final List<TelemetryMessage> recordedMessages = new CopyOnWriteArrayList<TelemetryMessage>();
    private final List<TelemetryMessage> recordedSecureMessages = new CopyOnWriteArrayList<TelemetryMessage>();

    @NotNull
    public static TestTelemetryService getInstance() {
        TestTelemetryService testTelemetryService = (TestTelemetryService)ApplicationManager.getApplication().getService(TelemetryService.class);
        if (testTelemetryService == null) {
            TestTelemetryService.$$$reportNull$$$0(0);
        }
        return testTelemetryService;
    }

    public void reset() {
        this.trackingId = null;
        this.recordedMessages.clear();
        this.recordedSecureMessages.clear();
    }

    @Override
    public void setTrackingId(@Nullable String id) {
        this.trackingId = id;
    }

    @Override
    public void trackException(@NotNull Throwable original, @NotNull Map<String, String> properties) {
        if (original == null) {
            TestTelemetryService.$$$reportNull$$$0(1);
        }
        if (properties == null) {
            TestTelemetryService.$$$reportNull$$$0(2);
        }
    }

    @Override
    public void track(@NotNull String name, @NotNull TelemetryData data) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(3);
        }
        if (data == null) {
            TestTelemetryService.$$$reportNull$$$0(4);
        }
        this.track(name, data.getProperties(), (Map<String, Double>)data.getMetrics());
    }

    @Override
    public void track(@NotNull String name, @NotNull Map<String, String> properties, @NotNull Map<String, Double> metrics) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(5);
        }
        if (properties == null) {
            TestTelemetryService.$$$reportNull$$$0(6);
        }
        if (metrics == null) {
            TestTelemetryService.$$$reportNull$$$0(7);
        }
        this.recordedMessages.add(new TelemetryMessage(name, Map.copyOf(properties), Map.copyOf(metrics)));
    }

    @Override
    public void trackSecure(@NotNull String name, @NotNull TelemetryData data) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(8);
        }
        if (data == null) {
            TestTelemetryService.$$$reportNull$$$0(9);
        }
        this.trackSecure(name, data.getProperties(), (Map<String, Double>)data.getMetrics());
    }

    @Override
    public void trackSecure(@NotNull String name, @NotNull Map<String, String> properties, @NotNull Map<String, Double> metrics) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(10);
        }
        if (properties == null) {
            TestTelemetryService.$$$reportNull$$$0(11);
        }
        if (metrics == null) {
            TestTelemetryService.$$$reportNull$$$0(12);
        }
        this.recordedSecureMessages.add(new TelemetryMessage(name, Map.copyOf(properties), Map.copyOf(metrics)));
    }

    @TestOnly
    @NotNull
    public List<TelemetryMessage> findRecordedMessages(@NotNull String name) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(13);
        }
        List<TelemetryMessage> list = this.recordedMessages.stream().filter(m -> name.equals(m.name)).collect(Collectors.toList());
        if (list == null) {
            TestTelemetryService.$$$reportNull$$$0(14);
        }
        return list;
    }

    @TestOnly
    @NotNull
    public List<TelemetryMessage> findRecordedSecureMessages(@NotNull String name) {
        if (name == null) {
            TestTelemetryService.$$$reportNull$$$0(15);
        }
        List<TelemetryMessage> list = this.recordedSecureMessages.stream().filter(m -> name.equals(m.name)).collect(Collectors.toList());
        if (list == null) {
            TestTelemetryService.$$$reportNull$$$0(16);
        }
        return list;
    }

    public List<TelemetryMessage> getRecordedMessages() {
        return this.recordedMessages;
    }

    public List<TelemetryMessage> getRecordedSecureMessages() {
        return this.recordedSecureMessages;
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
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 15: {
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
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 15: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/telemetry/TestTelemetryService";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "original";
                break;
            }
            case 2: 
            case 6: 
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "properties";
                break;
            }
            case 3: 
            case 5: 
            case 8: 
            case 10: 
            case 13: 
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
            case 4: 
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "data";
                break;
            }
            case 7: 
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "metrics";
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
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 15: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/telemetry/TestTelemetryService";
                break;
            }
            case 14: {
                objectArray = objectArray2;
                objectArray2[1] = "findRecordedMessages";
                break;
            }
            case 16: {
                objectArray = objectArray2;
                objectArray2[1] = "findRecordedSecureMessages";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "trackException";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                objectArray = objectArray;
                objectArray[2] = "track";
                break;
            }
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: {
                objectArray = objectArray;
                objectArray[2] = "trackSecure";
                break;
            }
            case 13: {
                objectArray = objectArray;
                objectArray[2] = "findRecordedMessages";
                break;
            }
            case 15: {
                objectArray = objectArray;
                objectArray[2] = "findRecordedSecureMessages";
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
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 15: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }

    @TestOnly
    public static final class TelemetryMessage {
        private final String name;
        private final Map<String, String> properties;
        private final Map<String, Double> metrics;

        public TelemetryMessage(String name, Map<String, String> properties, Map<String, Double> metrics) {
            this.name = name;
            this.properties = properties;
            this.metrics = metrics;
        }

        public String getName() {
            return this.name;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public Map<String, Double> getMetrics() {
            return this.metrics;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TelemetryMessage)) {
                return false;
            }
            TelemetryMessage other = (TelemetryMessage)o;
            String this$name = this.getName();
            String other$name = other.getName();
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
                return false;
            }
            Map<String, String> this$properties = this.getProperties();
            Map<String, String> other$properties = other.getProperties();
            if (this$properties == null ? other$properties != null : !((Object)this$properties).equals(other$properties)) {
                return false;
            }
            Map<String, Double> this$metrics = this.getMetrics();
            Map<String, Double> other$metrics = other.getMetrics();
            return !(this$metrics == null ? other$metrics != null : !((Object)this$metrics).equals(other$metrics));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $name = this.getName();
            result = result * 59 + ($name == null ? 43 : $name.hashCode());
            Map<String, String> $properties = this.getProperties();
            result = result * 59 + ($properties == null ? 43 : ((Object)$properties).hashCode());
            Map<String, Double> $metrics = this.getMetrics();
            result = result * 59 + ($metrics == null ? 43 : ((Object)$metrics).hashCode());
            return result;
        }

        public String toString() {
            return "TestTelemetryService.TelemetryMessage(name=" + this.getName() + ", properties=" + this.getProperties() + ", metrics=" + this.getMetrics() + ")";
        }
    }
}

