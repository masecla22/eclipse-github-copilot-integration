/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMaps
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMaps$EmptyMap
 *  it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap
 */
package com.github.copilot.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import java.io.IOException;

public final class String2DoubleMap extends Object2DoubleOpenHashMap<String> {
	private static final long serialVersionUID = -4680414524405617139L;
	public static final Object2DoubleMap<String> EMPTY = new Object2DoubleMaps.EmptyMap<String>() {
		private static final long serialVersionUID = 4680414524405687139L;
	};

	public static Object2DoubleMap<String> of() {
		return EMPTY;
	}

	public static Object2DoubleMap<String> of(String key, double value) {
		return Object2DoubleMaps.singleton(key, value);
	}

	public static Object2DoubleMap<String> of(String k1, double v1, String k2, double v2) {
		String2DoubleMap map = new String2DoubleMap();
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static Object2DoubleMap<String> of(String k1, double v1, String k2, double v2, String k3, double v3) {
		String2DoubleMap map = new String2DoubleMap();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	public static Object2DoubleMap<String> of(String k1, double v1, String k2, double v2, String k3, double v3,
			String k4, double v4) {
		String2DoubleMap map = new String2DoubleMap();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	public static Object2DoubleMap<String> of(String k1, double v1, String k2, double v2, String k3, double v3,
			String k4, double v4, String k5, double v5) {
		String2DoubleMap map = new String2DoubleMap();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		return map;
	}

	public static final class TypeAdapter extends com.google.gson.TypeAdapter<String2DoubleMap> {
		public void write(JsonWriter jsonWriter, String2DoubleMap string2DoubleMap) throws IOException {
			jsonWriter.beginObject();
			for (Object2DoubleMap.Entry<String> entry : string2DoubleMap.object2DoubleEntrySet()) {
				jsonWriter.name(entry.getKey());
				jsonWriter.value(entry.getDoubleValue());
			}
			jsonWriter.endObject();
		}

		public String2DoubleMap read(JsonReader jsonReader) throws IOException {
			String2DoubleMap map = new String2DoubleMap();
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String key = jsonReader.nextName();
				double value = jsonReader.nextDouble();
				map.put(key, value);
			}
			jsonReader.endObject();
			return map;
		}
	}
}
