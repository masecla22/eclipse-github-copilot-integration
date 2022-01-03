/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.containers.ContainerUtil$ImmutableMapBuilder
 */
package com.github.copilot.util;

import com.intellij.util.containers.ContainerUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Maps {
    private Maps() {
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11) {
        ContainerUtil.ImmutableMapBuilder map = new ContainerUtil.ImmutableMapBuilder();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        return map.build();
    }

    @SafeVarargs
    public static <K, V> Map<K, V> merge(Map<K, V> ... maps) {
        if (maps == null) {
            Maps.$$$reportNull$$$0(0);
        }
        if (maps.length == 0) {
            return Collections.emptyMap();
        }
        if (maps.length == 1) {
            return Map.copyOf(maps[0]);
        }
        HashMap<K, V> all = null;
        for (Map<K, V> map : maps) {
            if (map.isEmpty()) continue;
            if (all == null) {
                all = new HashMap<K, V>();
            }
            all.putAll(map);
        }
        return all == null ? Collections.emptyMap() : Collections.unmodifiableMap(all);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "maps", "com/github/copilot/util/Maps", "merge"));
    }
}

