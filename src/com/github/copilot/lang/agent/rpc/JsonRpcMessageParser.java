/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.NotThreadSafe
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public interface JsonRpcMessageParser {
	public void append(String var1);

	public void close();
}
