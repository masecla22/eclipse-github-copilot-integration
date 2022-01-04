/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package com.github.copilot.openai;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class StringDoublePair {
	private final String key;
	private final double value;

	public StringDoublePair(String key, double value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public double getValue() {
		return this.value;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof StringDoublePair)) {
			return false;
		}
		StringDoublePair other = (StringDoublePair) o;
		if (Double.compare(this.getValue(), other.getValue()) != 0) {
			return false;
		}
		String this$key = this.getKey();
		String other$key = other.getKey();
		return !(this$key == null ? other$key != null : !this$key.equals(other$key));
	}

	public int hashCode() {
		int result = 1;
		long $value = Double.doubleToLongBits(this.getValue());
		result = result * 59 + (int) ($value >>> 32 ^ $value);
		String $key = this.getKey();
		result = result * 59 + ($key == null ? 43 : $key.hashCode());
		return result;
	}

	public String toString() {
		return "StringDoublePair(key=" + this.getKey() + ", value=" + this.getValue() + ")";
	}

	static final class TypeAdapter extends com.google.gson.TypeAdapter<StringDoublePair> {
		TypeAdapter() {
		}

		public void write(JsonWriter jsonWriter, StringDoublePair pair) throws IOException {
			jsonWriter.beginObject();
			jsonWriter.name(pair.key);
			jsonWriter.value(pair.value);
			jsonWriter.endObject();
		}

		public StringDoublePair read(JsonReader jsonReader) throws IOException {
			jsonReader.beginObject();
			String key = jsonReader.nextName();
			double value = jsonReader.nextDouble();
			jsonReader.endObject();
			return new StringDoublePair(key, value);
		}
	}
}
