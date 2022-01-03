/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.ui.PresentableEnum
 */
package com.github.copilot.status;

import com.github.copilot.CopilotBundle;
import com.intellij.util.ui.PresentableEnum;

public enum CopilotStatus implements PresentableEnum
{
    Ready,
    TermsNotAccepted,
    NotSignedIn,
    UnknownError;


    public String getPresentableText() {
        switch (this) {
            case Ready: {
                return CopilotBundle.get("copilotStatus.ready");
            }
            case TermsNotAccepted: {
                return CopilotBundle.get("copilotStatus.termsNotAccepted");
            }
            case NotSignedIn: {
                return CopilotBundle.get("copilotStatus.notSignedIn");
            }
            case UnknownError: {
                return CopilotBundle.get("copilotStatus.unknownError");
            }
        }
        throw new IllegalStateException("Unexpected value:" + this);
    }
}

