/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.xmlb.annotations.OptionTag
 *  javax.annotation.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.settings.ZonedDateTimeConverter;
import com.intellij.util.xmlb.annotations.OptionTag;
import java.time.ZonedDateTime;
import javax.annotation.Nullable;

public class CopilotLocalApplicationState {
    @OptionTag(value="lastUpdateCheck", converter=ZonedDateTimeConverter.class)
    @Nullable
    public ZonedDateTime lastUpdateCheck = null;
    @OptionTag(value="checkEarlyBirdChannel")
    public boolean checkEarlyBirdChannel = true;
    @OptionTag(value="githubToken")
    @Nullable
    public volatile String githubToken;
    @OptionTag(value="githubTokenMigration")
    public volatile boolean githubTokenMigration;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CopilotLocalApplicationState)) {
            return false;
        }
        CopilotLocalApplicationState other = (CopilotLocalApplicationState)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.checkEarlyBirdChannel != other.checkEarlyBirdChannel) {
            return false;
        }
        if (this.githubTokenMigration != other.githubTokenMigration) {
            return false;
        }
        ZonedDateTime this$lastUpdateCheck = this.lastUpdateCheck;
        ZonedDateTime other$lastUpdateCheck = other.lastUpdateCheck;
        if (this$lastUpdateCheck == null ? other$lastUpdateCheck != null : !((Object)this$lastUpdateCheck).equals(other$lastUpdateCheck)) {
            return false;
        }
        String this$githubToken = this.githubToken;
        String other$githubToken = other.githubToken;
        return !(this$githubToken == null ? other$githubToken != null : !this$githubToken.equals(other$githubToken));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CopilotLocalApplicationState;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.checkEarlyBirdChannel ? 79 : 97);
        result = result * 59 + (this.githubTokenMigration ? 79 : 97);
        ZonedDateTime $lastUpdateCheck = this.lastUpdateCheck;
        result = result * 59 + ($lastUpdateCheck == null ? 43 : ((Object)$lastUpdateCheck).hashCode());
        String $githubToken = this.githubToken;
        result = result * 59 + ($githubToken == null ? 43 : $githubToken.hashCode());
        return result;
    }
}

