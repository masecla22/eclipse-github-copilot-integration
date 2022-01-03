/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.util.containers.ContainerUtil
 *  com.intellij.util.containers.hash.EqualityPolicy
 *  com.intellij.util.containers.hash.LinkedHashMap
 *  com.intellij.util.io.DigestUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CompletionCache;
import com.github.copilot.completions.CopilotCompletion;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.hash.EqualityPolicy;
import com.intellij.util.containers.hash.LinkedHashMap;
import com.intellij.util.io.DigestUtil;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCompletionCache
implements CompletionCache {
    private static final Logger LOG = Logger.getInstance(SimpleCompletionCache.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final LinkedHashMap<CacheKey, List<CopilotCompletion>> cache;
    @Nullable
    private String lastPrefix;
    @Nullable
    private String lastPromptHash;
    private boolean lastIsMultiline;

    public SimpleCompletionCache(final int cacheSize) {
        this.cache = new LinkedHashMap<CacheKey, List<CopilotCompletion>>(cacheSize, 0.6f, EqualityPolicy.CANONICAL, true){

            protected boolean removeEldestEntry(Map.Entry<CacheKey, List<CopilotCompletion>> eldest, CacheKey key, List<CopilotCompletion> value) {
                return this.size() > cacheSize;
            }
        };
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isLatestPrefix(@NotNull String prefix) {
        if (prefix == null) {
            SimpleCompletionCache.$$$reportNull$$$0(0);
        }
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            boolean bl = this.lastPrefix != null && this.lastPrefix.equals(prefix);
            return bl;
        }
        finally {
            readLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @Nullable
    public List<CopilotCompletion> get(@NotNull String prompt, boolean isMultiline) {
        if (prompt == null) {
            SimpleCompletionCache.$$$reportNull$$$0(1);
        }
        LOG.trace("Retrieving cached api items for prompt");
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            List list = (List)this.cache.get((Object)new CacheKey(SimpleCompletionCache.promptHash(prompt), isMultiline));
            return list;
        }
        finally {
            readLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @Nullable
    public List<CopilotCompletion> getLatest(@NotNull String prefix) {
        if (prefix == null) {
            SimpleCompletionCache.$$$reportNull$$$0(2);
        }
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            List<CopilotCompletion> list = this.getLatestLocked(prefix);
            return list;
        }
        finally {
            readLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void add(@NotNull String prefix, @NotNull String prompt, boolean isMultiline, @NotNull CopilotCompletion item) {
        if (prefix == null) {
            SimpleCompletionCache.$$$reportNull$$$0(3);
        }
        if (prompt == null) {
            SimpleCompletionCache.$$$reportNull$$$0(4);
        }
        if (item == null) {
            SimpleCompletionCache.$$$reportNull$$$0(5);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Caching new APIChoice for prompt: " + item);
        }
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.lastPrefix = prefix;
            this.lastPromptHash = SimpleCompletionCache.promptHash(prompt);
            this.lastIsMultiline = isMultiline;
            CacheKey key = new CacheKey(this.lastPromptHash, this.lastIsMultiline);
            List apiChoices = (List)this.cache.computeIfAbsent((Object)key, s -> ContainerUtil.createLockFreeCopyOnWriteList());
            apiChoices.add(item.asCached());
        }
        finally {
            writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void updateLatest(@NotNull String prefix, @NotNull String prompt, boolean isMultiline) {
        if (prefix == null) {
            SimpleCompletionCache.$$$reportNull$$$0(6);
        }
        if (prompt == null) {
            SimpleCompletionCache.$$$reportNull$$$0(7);
        }
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.lastPrefix = prefix;
            this.lastPromptHash = SimpleCompletionCache.promptHash(prompt);
            this.lastIsMultiline = isMultiline;
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.lastPromptHash = null;
            this.lastPrefix = null;
            this.lastIsMultiline = false;
            this.cache.clear();
        }
        finally {
            writeLock.unlock();
        }
    }

    @Nullable
    private List<CopilotCompletion> getLatestLocked(@NotNull String prefix) {
        if (prefix == null) {
            SimpleCompletionCache.$$$reportNull$$$0(8);
        }
        if (this.lastPrefix == null || this.lastPromptHash == null || !prefix.startsWith(this.lastPrefix)) {
            return null;
        }
        List result = (List)this.cache.get((Object)new CacheKey(this.lastPromptHash, this.lastIsMultiline));
        if (result == null) {
            return null;
        }
        String remainingPrefix = prefix.substring(this.lastPrefix.length());
        if (remainingPrefix.isEmpty()) {
            return Collections.unmodifiableList(result);
        }
        List<CopilotCompletion> adjustedChoices = result.stream().map(choice -> choice.withoutPrefix(remainingPrefix)).filter(Objects::nonNull).collect(Collectors.toList());
        return adjustedChoices.isEmpty() ? null : adjustedChoices;
    }

    private static String promptHash(@NotNull String prompt) {
        if (prompt == null) {
            SimpleCompletionCache.$$$reportNull$$$0(9);
        }
        return DigestUtil.sha256Hex((byte[])prompt.getBytes(StandardCharsets.UTF_8));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prefix";
                break;
            }
            case 1: 
            case 4: 
            case 7: 
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prompt";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "item";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/completions/SimpleCompletionCache";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "isLatestPrefix";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "get";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "getLatest";
                break;
            }
            case 3: 
            case 4: 
            case 5: {
                objectArray = objectArray2;
                objectArray2[2] = "add";
                break;
            }
            case 6: 
            case 7: {
                objectArray = objectArray2;
                objectArray2[2] = "updateLatest";
                break;
            }
            case 8: {
                objectArray = objectArray2;
                objectArray2[2] = "getLatestLocked";
                break;
            }
            case 9: {
                objectArray = objectArray2;
                objectArray2[2] = "promptHash";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }

    private static final class CacheKey {
        private final String promptHash;
        private final boolean isMultiline;

        public CacheKey(String promptHash, boolean isMultiline) {
            this.promptHash = promptHash;
            this.isMultiline = isMultiline;
        }

        public String getPromptHash() {
            return this.promptHash;
        }

        public boolean isMultiline() {
            return this.isMultiline;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CacheKey)) {
                return false;
            }
            CacheKey other = (CacheKey)o;
            if (this.isMultiline() != other.isMultiline()) {
                return false;
            }
            String this$promptHash = this.getPromptHash();
            String other$promptHash = other.getPromptHash();
            return !(this$promptHash == null ? other$promptHash != null : !this$promptHash.equals(other$promptHash));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + (this.isMultiline() ? 79 : 97);
            String $promptHash = this.getPromptHash();
            result = result * 59 + ($promptHash == null ? 43 : $promptHash.hashCode());
            return result;
        }

        public String toString() {
            return "SimpleCompletionCache.CacheKey(promptHash=" + this.getPromptHash() + ", isMultiline=" + this.isMultiline() + ")";
        }
    }
}

