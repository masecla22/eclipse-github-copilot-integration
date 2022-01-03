/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.telemetry;

import com.github.copilot.util.String2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.Map;

public class TelemetryData {
        private final TelemetryData wrapped;
        private final Map<String, String> properties;
        private final Object2DoubleMap<String> metrics;
    private volatile long issuedTimestamp;
    private volatile long displayedTimestamp;

    public static TelemetryData extend(TelemetryData data, Map<String, String> properties, Object2DoubleMap<String> metrics) {
        return new TelemetryData(data, properties, metrics);
    }

    public static TelemetryData createIssued(Map<String, String> properties) {
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        return TelemetryData.createIssued(properties, String2DoubleMap.EMPTY);
    }

    public static TelemetryData createIssued(Map<String, String> properties, Object2DoubleMap<String> metrics) {
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        if (metrics == null) {
            throw new IllegalStateException("metrics cannot be null!");
        }
        TelemetryData data = TelemetryData.create(properties, metrics);
        data.issueNow();
        return data;
    }

    public static TelemetryData create(Map<String, String> properties) {
        return TelemetryData.create(properties, String2DoubleMap.EMPTY);
    }

    public static TelemetryData create(Map<String, String> properties, Object2DoubleMap<String> metrics) {
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        if (metrics == null) {
            throw new IllegalStateException("metrics cannot be null!");
        }
        return new TelemetryData(null, properties, metrics);
    }

    public long getIssuedTimestamp() {
        if (this.issuedTimestamp == 0L && this.wrapped != null) {
            return this.wrapped.getIssuedTimestamp();
        }
        return this.issuedTimestamp;
    }

    public long getDisplayedTimestamp() {
        if (this.displayedTimestamp == 0L && this.wrapped != null) {
            return this.wrapped.getDisplayedTimestamp();
        }
        return this.displayedTimestamp;
    }

    public void issueNow() {
        this.issuedTimestamp = System.currentTimeMillis();
    }

    public void displayNow() {
        this.displayedTimestamp = System.currentTimeMillis();
    }

    void addProperties(Map<String, String> target) {
        if (target == null) {
            throw new IllegalStateException("target cannot be null!");
        }
        if (this.wrapped != null) {
            this.wrapped.addProperties(target);
        }
        target.putAll(this.properties);
    }

    void addMetrics(Object2DoubleMap<String> target) {
        if (target == null) {
            throw new IllegalStateException("target cannot be null!");
        }
        if (this.wrapped != null) {
            this.wrapped.addMetrics(target);
        }
        target.putAll(this.metrics);
    }

        public TelemetryData getWrapped() {
        return this.wrapped;
    }

        public Map<String, String> getProperties() {
        Map<String, String> map = this.properties;
        if (map == null) {
            throw new IllegalStateException("map cannot be null!");
        }
        return map;
    }

        public Object2DoubleMap<String> getMetrics() {
        Object2DoubleMap<String> object2DoubleMap = this.metrics;
        return object2DoubleMap;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TelemetryData)) {
            return false;
        }
        TelemetryData other = (TelemetryData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getIssuedTimestamp() != other.getIssuedTimestamp()) {
            return false;
        }
        if (this.getDisplayedTimestamp() != other.getDisplayedTimestamp()) {
            return false;
        }
        TelemetryData this$wrapped = this.getWrapped();
        TelemetryData other$wrapped = other.getWrapped();
        if (this$wrapped == null ? other$wrapped != null : !((Object)this$wrapped).equals(other$wrapped)) {
            return false;
        }
        Map<String, String> this$properties = this.getProperties();
        Map<String, String> other$properties = other.getProperties();
        if (this$properties == null ? other$properties != null : !((Object)this$properties).equals(other$properties)) {
            return false;
        }
        Object2DoubleMap<String> this$metrics = this.getMetrics();
        Object2DoubleMap<String> other$metrics = other.getMetrics();
        return !(this$metrics == null ? other$metrics != null : !this$metrics.equals(other$metrics));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TelemetryData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $issuedTimestamp = this.getIssuedTimestamp();
        result = result * 59 + (int)($issuedTimestamp >>> 32 ^ $issuedTimestamp);
        long $displayedTimestamp = this.getDisplayedTimestamp();
        result = result * 59 + (int)($displayedTimestamp >>> 32 ^ $displayedTimestamp);
        TelemetryData $wrapped = this.getWrapped();
        result = result * 59 + ($wrapped == null ? 43 : ((Object)$wrapped).hashCode());
        Map<String, String> $properties = this.getProperties();
        result = result * 59 + ($properties == null ? 43 : ((Object)$properties).hashCode());
        Object2DoubleMap<String> $metrics = this.getMetrics();
        result = result * 59 + ($metrics == null ? 43 : $metrics.hashCode());
        return result;
    }

    public String toString() {
        return "TelemetryData(wrapped=" + this.getWrapped() + ", properties=" + this.getProperties() + ", metrics=" + this.getMetrics() + ", issuedTimestamp=" + this.getIssuedTimestamp() + ", displayedTimestamp=" + this.getDisplayedTimestamp() + ")";
    }

    /*
     * WARNING - void declaration
     */
    private TelemetryData(TelemetryData wrapped, Map<String, String> properties, Object2DoubleMap<String> metrics, long issuedTimestamp, long l) {
        void displayedTimestamp;
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        if (metrics == null) {
            throw new IllegalStateException("metrics cannot be null!");
        }
        if (properties == null) {
            throw new NullPointerException("properties is marked non-null but is null");
        }
        if (metrics == null) {
            throw new NullPointerException("metrics is marked non-null but is null");
        }
        this.wrapped = wrapped;
        this.properties = properties;
        this.metrics = metrics;
        this.issuedTimestamp = issuedTimestamp;
        this.displayedTimestamp = displayedTimestamp;
    }

    private TelemetryData(TelemetryData wrapped, Map<String, String> properties, Object2DoubleMap<String> metrics) {
        if (properties == null) {
            throw new IllegalStateException("properties cannot be null!");
        }
        if (metrics == null) {
            throw new IllegalStateException("metrics cannot be null!");
        }
        if (properties == null) {
            throw new NullPointerException("properties is marked non-null but is null");
        }
        if (metrics == null) {
            throw new NullPointerException("metrics is marked non-null but is null");
        }
        this.wrapped = wrapped;
        this.properties = properties;
        this.metrics = metrics;
    }

    private void setIssuedTimestamp(long issuedTimestamp) {
        this.issuedTimestamp = issuedTimestamp;
    }

    private void setDisplayedTimestamp(long displayedTimestamp) {
        this.displayedTimestamp = displayedTimestamp;
    }

    
}

