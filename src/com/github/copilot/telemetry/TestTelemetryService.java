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
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class TestTelemetryService implements TelemetryService {
	private volatile String trackingId;
	private final List<TelemetryMessage> recordedMessages = new CopyOnWriteArrayList<TelemetryMessage>();
	private final List<TelemetryMessage> recordedSecureMessages = new CopyOnWriteArrayList<TelemetryMessage>();

	public static TestTelemetryService getInstance() {
		TestTelemetryService testTelemetryService = (TestTelemetryService) ApplicationManager.getApplication()
				.getService(TelemetryService.class);
		if (testTelemetryService == null) {
			throw new IllegalStateException("testTelemetryService cannot be null!");
		}
		return testTelemetryService;
	}

	public void reset() {
		this.trackingId = null;
		this.recordedMessages.clear();
		this.recordedSecureMessages.clear();
	}

	@Override
	public void setTrackingId(String id) {
		this.trackingId = id;
	}

	@Override
	public void trackException(Throwable original, Map<String, String> properties) {
		if (original == null) {
			throw new IllegalStateException("original cannot be null!");
		}
		if (properties == null) {
			throw new IllegalStateException("properties cannot be null!");
		}
	}

	@Override
	public void track(String name, TelemetryData data) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		if (data == null) {
			throw new IllegalStateException("data cannot be null!");
		}
		this.track(name, data.getProperties(), data.getMetrics());
	}

	@Override
	public void track(String name, Map<String, String> properties, Map<String, Double> metrics) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		if (properties == null) {
			throw new IllegalStateException("properties cannot be null!");
		}
		if (metrics == null) {
			throw new IllegalStateException("metrics cannot be null!");
		}
		this.recordedMessages.add(new TelemetryMessage(name, Map.copyOf(properties), Map.copyOf(metrics)));
	}

	@Override
	public void trackSecure(String name, TelemetryData data) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		if (data == null) {
			throw new IllegalStateException("data cannot be null!");
		}
		this.trackSecure(name, data.getProperties(), data.getMetrics());
	}

	@Override
	public void trackSecure(String name, Map<String, String> properties, Map<String, Double> metrics) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		if (properties == null) {
			throw new IllegalStateException("properties cannot be null!");
		}
		if (metrics == null) {
			throw new IllegalStateException("metrics cannot be null!");
		}
		this.recordedSecureMessages.add(new TelemetryMessage(name, Map.copyOf(properties), Map.copyOf(metrics)));
	}

	@TestOnly
	public List<TelemetryMessage> findRecordedMessages(String name) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		List<TelemetryMessage> list = this.recordedMessages.stream().filter(m -> name.equals(m.name))
				.collect(Collectors.toList());
		if (list == null) {
			throw new IllegalStateException("list cannot be null!");
		}
		return list;
	}

	@TestOnly
	public List<TelemetryMessage> findRecordedSecureMessages(String name) {
		if (name == null) {
			throw new IllegalStateException("name cannot be null!");
		}
		List<TelemetryMessage> list = this.recordedSecureMessages.stream().filter(m -> name.equals(m.name))
				.collect(Collectors.toList());
		if (list == null) {
			throw new IllegalStateException("list cannot be null!");
		}
		return list;
	}

	public List<TelemetryMessage> getRecordedMessages() {
		return this.recordedMessages;
	}

	public List<TelemetryMessage> getRecordedSecureMessages() {
		return this.recordedSecureMessages;
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
			TelemetryMessage other = (TelemetryMessage) o;
			String this$name = this.getName();
			String other$name = other.getName();
			if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
				return false;
			}
			Map<String, String> this$properties = this.getProperties();
			Map<String, String> other$properties = other.getProperties();
			if (this$properties == null ? other$properties != null
					: !((Object) this$properties).equals(other$properties)) {
				return false;
			}
			Map<String, Double> this$metrics = this.getMetrics();
			Map<String, Double> other$metrics = other.getMetrics();
			return !(this$metrics == null ? other$metrics != null : !((Object) this$metrics).equals(other$metrics));
		}

		public int hashCode() {
			int PRIME = 59;
			int result = 1;
			String $name = this.getName();
			result = result * 59 + ($name == null ? 43 : $name.hashCode());
			Map<String, String> $properties = this.getProperties();
			result = result * 59 + ($properties == null ? 43 : ((Object) $properties).hashCode());
			Map<String, Double> $metrics = this.getMetrics();
			result = result * 59 + ($metrics == null ? 43 : ((Object) $metrics).hashCode());
			return result;
		}

		public String toString() {
			return "TestTelemetryService.TelemetryMessage(name=" + this.getName() + ", properties="
					+ this.getProperties() + ", metrics=" + this.getMetrics() + ")";
		}
	}
}
