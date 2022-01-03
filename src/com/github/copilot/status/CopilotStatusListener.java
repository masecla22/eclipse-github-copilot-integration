/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.messages.Topic
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.status;

import com.github.copilot.status.CopilotStatus;
import com.intellij.util.messages.Topic;

@FunctionalInterface
public interface CopilotStatusListener {
    public static final Topic<CopilotStatusListener> TOPIC = Topic.create((String)"copilot.status", CopilotStatusListener.class);

    public void onCopilotStatus(CopilotStatus var1);
}

