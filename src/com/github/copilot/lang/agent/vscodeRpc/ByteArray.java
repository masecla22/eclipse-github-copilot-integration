/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.vscodeRpc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ByteArray {
	static final byte[] EMPTY_ARRAY = new byte[0];
	private int size = 0;
	private byte[] data = EMPTY_ARRAY;

	public void add(byte[] newData) {
		int oldSize = this.size;
		byte[] oldData = this.data;
		this.size += newData.length;
		this.data = new byte[this.size];
		System.arraycopy(oldData, 0, this.data, 0, oldSize);
		System.arraycopy(newData, 0, this.data, oldSize, newData.length);
	}

	public byte[] getBytes() {
		return this.getBytes(0, this.size);
	}

	public byte[] getBytes(int startOffset, int endOffset) {
		assert (startOffset >= 0);
		assert (startOffset <= endOffset);
		assert (endOffset <= this.size);
		int length = endOffset - startOffset;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		byte[] copy = new byte[length];
		System.arraycopy(this.data, startOffset, copy, 0, length);
		return copy;
	}

	public void deleteFirst(int length) {
		if (length == this.size) {
			this.data = EMPTY_ARRAY;
			this.size = 0;
			return;
		}
		byte[] newData = this.getBytes(length, this.size);
		this.data = newData;
		this.size = newData.length;
	}

	public int size() {
		return this.size;
	}

	public String toString(int startOffset, int endOffset, Charset charset) {
		if (charset == null) {
			throw new IllegalStateException("charset cannot be null!");
		}
		assert (startOffset >= 0);
		assert (startOffset <= endOffset);
		assert (endOffset <= this.size);
		return new String(this.data, startOffset, endOffset - startOffset, charset);
	}

	public String toString(Charset charset) {
		if (charset == null) {
			throw new IllegalStateException("charset cannot be null!");
		}
		String string = this.toString(0, this.size, charset);
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public String toString() {
		String string = this.toString(StandardCharsets.UTF_8);
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public int indexOf(byte[] query) {
		if (query.length == 0 || query.length > this.size) {
			return -1;
		}
		block0: for (int i = 0; i < this.size - query.length + 1; ++i) {
			for (int j = 0; j < query.length; ++j) {
				if (this.data[i + j] != query[j])
					continue block0;
			}
			return i;
		}
		return -1;
	}

}
