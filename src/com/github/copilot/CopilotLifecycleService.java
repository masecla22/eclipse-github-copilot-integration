/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 */
package com.github.copilot;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;

public class CopilotLifecycleService
implements Disposable {
    public void dispose() {
        Logger.getInstance(CopilotLifecycleService.class).warn("Disposing Copilot lifecycle service");
    }
}

