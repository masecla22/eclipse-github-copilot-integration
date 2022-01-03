/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.text.StringUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.lang.agent.commands.GetCompletionsResult;
import com.intellij.openapi.util.text.StringUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class AgentCompletion
implements CopilotCompletion {
    private final GetCompletionsResult.Completion agentData;
    private final List<String> completion;
    private volatile boolean isCached = false;

    AgentCompletion(GetCompletionsResult.Completion agentData) {
        this.agentData = agentData;
        this.completion = StringUtil.split((String)agentData.getDisplayText(), (String)"\n", (boolean)true, (boolean)false);
    }

    @Override
        public CopilotCompletion asCached() {
        AgentCompletion agentCompletion = this.withCached(true);
        if (agentCompletion == null) {
            throw new IllegalStateException("agentCompletion cannot be null!");
        }
        return agentCompletion;
    }

    @Override
        public CopilotCompletion withoutPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalStateException("prefix cannot be null!");
        }
        return CompletionUtil.apiChoiceWithoutPrefix(this, prefix);
    }

    public AgentCompletion(GetCompletionsResult.Completion agentData, List<String> completion, boolean isCached) {
        this.agentData = agentData;
        this.completion = completion;
        this.isCached = isCached;
    }

    public AgentCompletion(GetCompletionsResult.Completion agentData, List<String> completion) {
        this.agentData = agentData;
        this.completion = completion;
    }

    public GetCompletionsResult.Completion getAgentData() {
        return this.agentData;
    }

    @Override
    public List<String> getCompletion() {
        return this.completion;
    }

    @Override
    public AgentCompletion withCompletion(List<String> completion) {
        return this.completion == completion ? this : new AgentCompletion(this.agentData, completion, this.isCached);
    }

    public AgentCompletion withCached(boolean isCached) {
        return this.isCached == isCached ? this : new AgentCompletion(this.agentData, this.completion, isCached);
    }

    
}

