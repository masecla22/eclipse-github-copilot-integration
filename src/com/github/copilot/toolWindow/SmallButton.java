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

class SmallButton
extends JButton {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7243341939381218642L;

	public SmallButton(String label) {
        if (label == null) {
            throw new IllegalStateException("label cannot be null!");
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
}

