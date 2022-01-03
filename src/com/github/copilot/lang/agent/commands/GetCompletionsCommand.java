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

    
}

