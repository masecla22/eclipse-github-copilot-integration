/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.masecla.copilot.extra.Logger
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
import me.masecla.copilot.extra.Logger;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.hash.EqualityPolicy;
import com.intellij.util.io.DigestUtil;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class SimpleCompletionCache implements CompletionCache {
	private static final Logger LOG = Logger.getInstance(SimpleCompletionCache.class);
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final LinkedHashMap<CacheKey, List<CopilotCompletion>> cache;
	private String lastPrefix;
	private String lastPromptHash;
	private boolean lastIsMultiline;

	public SimpleCompletionCache(final int cacheSize) {
		this.cache = new LinkedHashMap<CacheKey, List<CopilotCompletion>>(cacheSize, 0.6f, true) {
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(Map.Entry<CacheKey, List<CopilotCompletion>> eldest, CacheKey key,
					List<CopilotCompletion> value) {
				return this.size() > cacheSize;
			}
		};
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public boolean isLatestPrefix(String prefix) {
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		Lock readLock = this.lock.readLock();
		readLock.lock();
		try {
			boolean bl = this.lastPrefix != null && this.lastPrefix.equals(prefix);
			return bl;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<CopilotCompletion> get(String prompt, boolean isMultiline) {
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		LOG.trace("Retrieving cached api items for prompt");
		Lock readLock = this.lock.readLock();
		readLock.lock();
		try {
			List<CopilotCompletion> list = this.cache
					.get(new CacheKey(SimpleCompletionCache.promptHash(prompt), isMultiline));
			return list;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<CopilotCompletion> getLatest(String prefix) {
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		Lock readLock = this.lock.readLock();
		readLock.lock();
		try {
			List<CopilotCompletion> list = this.getLatestLocked(prefix);
			return list;
		} finally {
			readLock.unlock();
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public void add(String prefix, String prompt, boolean isMultiline, CopilotCompletion item) {
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		if (item == null) {
			throw new IllegalStateException("item cannot be null!");
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
			List<CopilotCompletion> apiChoices = this.cache.computeIfAbsent(key, s -> new Vector<>());
			apiChoices.add(item.asCached());
		} finally {
			writeLock.unlock();
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public void updateLatest(String prefix, String prompt, boolean isMultiline) {
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		Lock writeLock = this.lock.writeLock();
		writeLock.lock();
		try {
			this.lastPrefix = prefix;
			this.lastPromptHash = SimpleCompletionCache.promptHash(prompt);
			this.lastIsMultiline = isMultiline;
		} finally {
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
		} finally {
			writeLock.unlock();
		}
	}

	private List<CopilotCompletion> getLatestLocked(String prefix) {
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		if (this.lastPrefix == null || this.lastPromptHash == null || !prefix.startsWith(this.lastPrefix)) {
			return null;
		}
		List<CopilotCompletion> result = this.cache.get(new CacheKey(this.lastPromptHash, this.lastIsMultiline));
		if (result == null) {
			return null;
		}
		String remainingPrefix = prefix.substring(this.lastPrefix.length());
		if (remainingPrefix.isEmpty()) {
			return Collections.unmodifiableList(result);
		}
		List<CopilotCompletion> adjustedChoices = result.stream().map(choice -> choice.withoutPrefix(remainingPrefix))
				.filter(Objects::nonNull).collect(Collectors.toList());
		return adjustedChoices.isEmpty() ? null : adjustedChoices;
	}

	private static String promptHash(String prompt) {
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		return DigestUtil.sha256Hex((byte[]) prompt.getBytes(StandardCharsets.UTF_8));
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
			CacheKey other = (CacheKey) o;
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
			return "SimpleCompletionCache.CacheKey(promptHash=" + this.getPromptHash() + ", isMultiline="
					+ this.isMultiline() + ")";
		}
	}
}
