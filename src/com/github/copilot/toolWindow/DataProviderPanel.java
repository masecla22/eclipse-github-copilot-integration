/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.actionSystem.DataProvider
 *  com.intellij.openapi.actionSystem.PlatformDataKeys
 *  com.intellij.ui.components.JBPanelWithEmptyText
 *  com.intellij.ui.components.panels.VerticalLayout
 *  org.jetbrains.annotations.NonNls
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.toolWindow;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.components.panels.VerticalLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DataProviderPanel
extends JBPanelWithEmptyText
implements DataProvider {
    private final Disposable parentDisposable;

    public void registerKeyboardAction(ActionListener anAction, String aCommand, KeyStroke aKeyStroke, int aCondition) {
        super.registerKeyboardAction(anAction, aCommand, aKeyStroke, aCondition);
    }

    DataProviderPanel(Disposable parentDisposable) {
        super((LayoutManager)new VerticalLayout(5));
        this.parentDisposable = parentDisposable;
    }

    @Nullable
    public Object getData(@NotNull @NonNls String dataId) {
        if (dataId == null) {
            DataProviderPanel.$$$reportNull$$$0(0);
        }
        if (PlatformDataKeys.UI_DISPOSABLE.is(dataId)) {
            return this.parentDisposable;
        }
        return null;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "dataId", "com/github/copilot/toolWindow/DataProviderPanel", "getData"));
    }
}

