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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetCompletionsCyclingCommand
extends GetCompletionsCommand {
    public GetCompletionsCyclingCommand(Document doc, Map<Object, Object> options) {
        if (doc == null) {
            GetCompletionsCyclingCommand.$$$reportNull$$$0(0);
        }
        super(doc, options);
    }

    @Override
        public String getCommandName() {
        return "getCompletionsCycling";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "doc", "com/github/copilot/lang/agent/commands/GetCompletionsCyclingCommand", "<init>"));
    }
}

