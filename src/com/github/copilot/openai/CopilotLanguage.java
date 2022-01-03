/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

import org.jetbrains.annotations.NotNull;

public enum CopilotLanguage {
    Python,
    Java,
    Unknown;

        private final String enginePath;

    private CopilotLanguage() {
        this("/v1/engines/copilot-codex");
    }

    private CopilotLanguage(String enginePath) {
        if (enginePath == null) {
            CopilotLanguage.$$$reportNull$$$0(0);
        }
        this.enginePath = enginePath;
    }

    public String getEngineName() {
        String path = this.getEnginePath();
        int index = path.lastIndexOf(47);
        return index == -1 ? path : path.substring(index + 1);
    }

        public String getEnginePath() {
        String string = this.enginePath;
        if (string == null) {
            CopilotLanguage.$$$reportNull$$$0(1);
        }
        return string;
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
                objectArray3[0] = "enginePath";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/CopilotLanguage";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/CopilotLanguage";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getEnginePath";
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

