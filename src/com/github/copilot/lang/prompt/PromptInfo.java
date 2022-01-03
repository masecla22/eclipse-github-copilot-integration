/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.Immutable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.prompt;

import com.github.copilot.request.BlockMode;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class PromptInfo {
    @Nullable
    private final String languageId;
    @NotNull
    private final String prompt;
    @NotNull
    private final String trailingWhitespace;
    private final int promptLength;
    private final BlockMode blockMode;

    public PromptInfo(@Nullable String languageId, @NotNull String prompt, @NotNull String trailingWhitespace, int promptLength, BlockMode blockMode) {
        if (prompt == null) {
            PromptInfo.$$$reportNull$$$0(0);
        }
        if (trailingWhitespace == null) {
            PromptInfo.$$$reportNull$$$0(1);
        }
        if (prompt == null) {
            throw new NullPointerException("prompt is marked non-null but is null");
        }
        if (trailingWhitespace == null) {
            throw new NullPointerException("trailingWhitespace is marked non-null but is null");
        }
        this.languageId = languageId;
        this.prompt = prompt;
        this.trailingWhitespace = trailingWhitespace;
        this.promptLength = promptLength;
        this.blockMode = blockMode;
    }

    @Nullable
    public String getLanguageId() {
        return this.languageId;
    }

    @NotNull
    public String getPrompt() {
        String string = this.prompt;
        if (string == null) {
            PromptInfo.$$$reportNull$$$0(2);
        }
        return string;
    }

    @NotNull
    public String getTrailingWhitespace() {
        String string = this.trailingWhitespace;
        if (string == null) {
            PromptInfo.$$$reportNull$$$0(3);
        }
        return string;
    }

    public int getPromptLength() {
        return this.promptLength;
    }

    public BlockMode getBlockMode() {
        return this.blockMode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PromptInfo)) {
            return false;
        }
        PromptInfo other = (PromptInfo)o;
        if (this.getPromptLength() != other.getPromptLength()) {
            return false;
        }
        String this$languageId = this.getLanguageId();
        String other$languageId = other.getLanguageId();
        if (this$languageId == null ? other$languageId != null : !this$languageId.equals(other$languageId)) {
            return false;
        }
        String this$prompt = this.getPrompt();
        String other$prompt = other.getPrompt();
        if (this$prompt == null ? other$prompt != null : !this$prompt.equals(other$prompt)) {
            return false;
        }
        String this$trailingWhitespace = this.getTrailingWhitespace();
        String other$trailingWhitespace = other.getTrailingWhitespace();
        if (this$trailingWhitespace == null ? other$trailingWhitespace != null : !this$trailingWhitespace.equals(other$trailingWhitespace)) {
            return false;
        }
        BlockMode this$blockMode = this.getBlockMode();
        BlockMode other$blockMode = other.getBlockMode();
        return !(this$blockMode == null ? other$blockMode != null : !((Object)((Object)this$blockMode)).equals((Object)other$blockMode));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPromptLength();
        String $languageId = this.getLanguageId();
        result = result * 59 + ($languageId == null ? 43 : $languageId.hashCode());
        String $prompt = this.getPrompt();
        result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
        String $trailingWhitespace = this.getTrailingWhitespace();
        result = result * 59 + ($trailingWhitespace == null ? 43 : $trailingWhitespace.hashCode());
        BlockMode $blockMode = this.getBlockMode();
        result = result * 59 + ($blockMode == null ? 43 : ((Object)((Object)$blockMode)).hashCode());
        return result;
    }

    public String toString() {
        return "PromptInfo(languageId=" + this.getLanguageId() + ", prompt=" + this.getPrompt() + ", trailingWhitespace=" + this.getTrailingWhitespace() + ", promptLength=" + this.getPromptLength() + ", blockMode=" + this.getBlockMode() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: 
            case 3: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: 
            case 3: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prompt";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "trailingWhitespace";
                break;
            }
            case 2: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/prompt/PromptInfo";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/prompt/PromptInfo";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getPrompt";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getTrailingWhitespace";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 2: 
            case 3: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: 
            case 3: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

