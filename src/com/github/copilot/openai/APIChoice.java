/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.text.StringUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.openai.CompletionResponseInfo;
import com.github.copilot.telemetry.TelemetryData;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface APIChoice
extends CopilotCompletion {
    @NotNull
    public CompletionResponseInfo getResponseInfo();

    default public int getCompletionTextLength() {
        int sum = 0;
        for (String line : this.getCompletion()) {
            sum += line.length();
        }
        return sum;
    }

    @NotNull
    default public String getCompletionText() {
        String string = StringUtil.join(this.getCompletion(), (String)"\n");
        if (string == null) {
            APIChoice.$$$reportNull$$$0(0);
        }
        return string;
    }

    public int getChoiceIndex();

    public int getNumTokens();

    public int getRequestID();

    @NotNull
    public String getCompletionId();

    public int getCreatedTimestamp();

    @Nullable
    public Double getMeanLogProb();

    @NotNull
    public TelemetryData getTelemetryData();

    public boolean isCached();

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/github/copilot/openai/APIChoice", "getCompletionText"));
    }
}

