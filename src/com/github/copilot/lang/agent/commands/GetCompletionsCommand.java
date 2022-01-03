/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.Document;
import com.github.copilot.lang.agent.commands.GetCompletionsResult;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.google.gson.annotations.SerializedName;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetCompletionsCommand
implements JsonRpcCommand<GetCompletionsResult> {
    @SerializedName(value="doc")
        private final Document doc;
    @SerializedName(value="options")
        private final Map<Object, Object> options;

    @Override
        public String getCommandName() {
        return "getCompletions";
    }

    @Override
        public Class<GetCompletionsResult> getResponseType() {
        return GetCompletionsResult.class;
    }

    public GetCompletionsCommand(Document doc, Map<Object, Object> options) {
        if (doc == null) {
            throw new IllegalStateException("doc cannot be null!");
        }
        if (doc == null) {
            throw new NullPointerException("doc is marked non-null but is null");
        }
        this.doc = doc;
        this.options = options;
    }

        public Document getDoc() {
        Document document = this.doc;
        if (document == null) {
            throw new IllegalStateException("document cannot be null!");
        }
        return document;
    }

        public Map<Object, Object> getOptions() {
        return this.options;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GetCompletionsCommand)) {
            return false;
        }
        GetCompletionsCommand other = (GetCompletionsCommand)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Document this$doc = this.getDoc();
        Document other$doc = other.getDoc();
        if (this$doc == null ? other$doc != null : !((Object)this$doc).equals(other$doc)) {
            return false;
        }
        Map<Object, Object> this$options = this.getOptions();
        Map<Object, Object> other$options = other.getOptions();
        return !(this$options == null ? other$options != null : !((Object)this$options).equals(other$options));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GetCompletionsCommand;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Document $doc = this.getDoc();
        result = result * 59 + ($doc == null ? 43 : ((Object)$doc).hashCode());
        Map<Object, Object> $options = this.getOptions();
        result = result * 59 + ($options == null ? 43 : ((Object)$options).hashCode());
        return result;
    }

    public String toString() {
        return "GetCompletionsCommand(doc=" + this.getDoc() + ", options=" + this.getOptions() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 1: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "doc";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/GetCompletionsCommand";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/GetCompletionsCommand";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getDoc";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 1: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

