/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.lang.agent.vscodeRpc;

import com.github.copilot.lang.agent.rpc.JsonRpcMessageHandler;
import com.github.copilot.lang.agent.rpc.JsonRpcMessageParser;
import com.github.copilot.lang.agent.vscodeRpc.ByteArray;
import com.intellij.openapi.diagnostic.Logger;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public class VSCodeJsonRpcParser
implements JsonRpcMessageParser {
    private static final Logger LOG = Logger.getInstance(VSCodeJsonRpcParser.class);
    private static final byte[] CONTENT_LENGTH = "Content-Length: ".getBytes(StandardCharsets.UTF_8);
    public static final byte[] SEPARATOR = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    private final JsonRpcMessageHandler messageHandler;
    private int pendingContentLength = 0;
    private final ByteArray pendingContent = new ByteArray();

    @TestOnly
        public String getPendingContent() {
        String string = this.pendingContent.toString(StandardCharsets.UTF_8);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    @Override
    public void append(String content) {
        if (content == null) {
            throw new IllegalStateException("content cannot be null!");
        }
        this.pendingContent.add(content.getBytes(StandardCharsets.UTF_8));
        if (this.pendingContent.indexOf(CONTENT_LENGTH) == 0) {
            this.handleContentLengthMessage();
        }
        if (this.pendingContentLength > 0) {
            this.processPendingContent();
        }
    }

    @Override
    public void close() {
    }

    private void handleContentLengthMessage() {
        assert (this.pendingContentLength == 0);
        int lineEnd = this.pendingContent.indexOf(SEPARATOR);
        if (lineEnd > 0) {
            String line = new String(this.pendingContent.getBytes(CONTENT_LENGTH.length, lineEnd), StandardCharsets.UTF_8);
            this.pendingContent.deleteFirst(lineEnd + SEPARATOR.length);
            int length = Integer.parseInt(line, 0, line.length(), 10);
            LOG.debug("Found content-length: " + line);
            this.pendingContentLength += length;
        }
    }

    private void processPendingContent() {
        if (this.pendingContent.size() < this.pendingContentLength) {
            return;
        }
        String message = this.pendingContent.toString(0, this.pendingContentLength, StandardCharsets.UTF_8);
        this.pendingContent.deleteFirst(this.pendingContentLength);
        this.pendingContentLength = 0;
        this.messageHandler.handleJsonMessage(message);
    }

    public VSCodeJsonRpcParser(JsonRpcMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/vscodeRpc/VSCodeJsonRpcParser";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "content";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getPendingContent";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/vscodeRpc/VSCodeJsonRpcParser";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "append";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

