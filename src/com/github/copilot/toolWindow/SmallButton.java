/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.ui.JBUI
 *  com.intellij.util.ui.JBUI$Fonts
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.toolWindow;

import com.intellij.util.ui.JBUI;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import org.jetbrains.annotations.NotNull;

class SmallButton
extends JButton {
    public SmallButton(@NotNull String label) {
        if (label == null) {
            SmallButton.$$$reportNull$$$0(0);
        }
        super(label);
        this.setFont((Font)JBUI.Fonts.smallFont());
    }

    @Override
    public Insets getInsets(Insets insets) {
        return this.getInsets();
    }

    @Override
    public Insets getInsets() {
        return JBUI.emptyInsets();
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "label", "com/github/copilot/toolWindow/SmallButton", "<init>"));
    }
}

