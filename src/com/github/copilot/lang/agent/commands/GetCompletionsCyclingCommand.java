/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.Document;
import com.github.copilot.lang.agent.commands.GetCompletionsCommand;
import java.util.Map;

public class GetCompletionsCyclingCommand
extends GetCompletionsCommand {
    public GetCompletionsCyclingCommand(Document doc, Map<Object, Object> options) {
        if (doc == null) {
            throw new IllegalStateException("doc cannot be null!");
        }
        super(doc, options);
    }

    @Override
        public String getCommandName() {
        return "getCompletionsCycling";
    }
}

