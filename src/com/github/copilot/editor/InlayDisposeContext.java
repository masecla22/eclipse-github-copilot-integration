/*
 * Decompiled with CFR 0.152.
 */
package com.github.copilot.editor;

public enum InlayDisposeContext {
    UserAction,
    IdeCompletion,
    CaretChange,
    SettingsChange,
    Cycling,
    TypingAsSuggested,
    Typing,
    Applied;


    public boolean isResetLastRequest() {
        return this == SettingsChange || this == Applied;
    }

    public boolean isSendRejectedTelemetry() {
        return this == UserAction;
    }
}

