/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.vscodeRpc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public String toString(int startOffset, int endOffset, @NotNull Charset charset) {
        if (charset == null) {
            ByteArray.$$$reportNull$$$0(0);
        }
        assert (startOffset >= 0);
        assert (startOffset <= endOffset);
        assert (endOffset <= this.size);
        return new String(this.data, startOffset, endOffset - startOffset, charset);
    }

    @NotNull
    public String toString(@NotNull Charset charset) {
        if (charset == null) {
            ByteArray.$$$reportNull$$$0(1);
        }
        String string = this.toString(0, this.size, charset);
        if (string == null) {
            ByteArray.$$$reportNull$$$0(2);
        }
        return string;
    }

    @NotNull
    public String toString() {
        String string = this.toString(StandardCharsets.UTF_8);
        if (string == null) {
            ByteArray.$$$reportNull$$$0(3);
        }
        return string;
    }

    public int indexOf(byte[] query) {
        if (query.length == 0 || query.length > this.size) {
            return -1;
        }
        block0: for (int i = 0; i < this.size - query.length + 1; ++i) {
            for (int j = 0; j < query.length; ++j) {
                if (this.data[i + j] != query[j]) continue block0;
            }
            return i;
        }
        return -1;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: 
            case 3: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: 
            case 3: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "charset";
                break;
            }
            case 2: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/vscodeRpc/ByteArray";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/vscodeRpc/ByteArray";
                break;
            }
            case 2: 
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "toString";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "toString";
                break;
            }
            case 2: 
            case 3: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: 
            case 3: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

