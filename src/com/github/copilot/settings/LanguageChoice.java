/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.settings;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

class LanguageChoice {
        private final Language language;
    private boolean selected;

        public Language getLanguage() {
        Language language = this.language;
        if (language == null) {
            throw new IllegalStateException("language cannot be null!");
        }
        return language;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LanguageChoice)) {
            return false;
        }
        LanguageChoice other = (LanguageChoice)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isSelected() != other.isSelected()) {
            return false;
        }
        Language this$language = this.getLanguage();
        Language other$language = other.getLanguage();
        return !(this$language == null ? other$language != null : !this$language.equals(other$language));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LanguageChoice;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSelected() ? 79 : 97);
        Language $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    public String toString() {
        return "LanguageChoice(language=" + this.getLanguage() + ", selected=" + this.isSelected() + ")";
    }

    public LanguageChoice(Language language, boolean selected) {
        if (language == null) {
            throw new IllegalStateException("language cannot be null!");
        }
        if (language == null) {
            throw new NullPointerException("language is marked non-null but is null");
        }
        this.language = language;
        this.selected = selected;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/settings/LanguageChoice";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "language";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getLanguage";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/settings/LanguageChoice";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

