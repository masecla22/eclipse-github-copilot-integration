/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.CompletionResponseInfo;
import com.github.copilot.openai.TelemetryUtil;
import com.github.copilot.telemetry.TelemetryData;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DefaultAPIChoice
implements APIChoice {
        private final CompletionResponseInfo responseInfo;
        private final List<String> completion;
    private final int numTokens;
    private final int choiceIndex;
    private final int requestID;
        private final String completionId;
    private final int createdTimestamp;
        private final Double meanLogProb;
        private final TelemetryData baseTelemetryData;
    private final boolean isCached;
    private TelemetryData telemetryData;

    @Override
        public APIChoice asCached() {
        DefaultAPIChoice defaultAPIChoice = this.withCached(true);
        if (defaultAPIChoice == null) {
            DefaultAPIChoice.$$$reportNull$$$0(0);
        }
        return defaultAPIChoice;
    }

    @Override
        public synchronized TelemetryData getTelemetryData() {
        if (this.telemetryData == null) {
            this.telemetryData = TelemetryUtil.createChoiceTelemetry(this, this.baseTelemetryData);
        }
        TelemetryData telemetryData = this.telemetryData;
        if (telemetryData == null) {
            DefaultAPIChoice.$$$reportNull$$$0(1);
        }
        return telemetryData;
    }

    @Override
        public DefaultAPIChoice withoutPrefix(String prefix) {
        if (prefix == null) {
            DefaultAPIChoice.$$$reportNull$$$0(2);
        }
        return (DefaultAPIChoice)CompletionUtil.apiChoiceWithoutPrefix(this, prefix);
    }

    @Override
        public CompletionResponseInfo getResponseInfo() {
        CompletionResponseInfo completionResponseInfo = this.responseInfo;
        if (completionResponseInfo == null) {
            DefaultAPIChoice.$$$reportNull$$$0(3);
        }
        return completionResponseInfo;
    }

    @Override
        public List<String> getCompletion() {
        List<String> list = this.completion;
        if (list == null) {
            DefaultAPIChoice.$$$reportNull$$$0(4);
        }
        return list;
    }

    @Override
    public int getNumTokens() {
        return this.numTokens;
    }

    @Override
    public int getChoiceIndex() {
        return this.choiceIndex;
    }

    @Override
    public int getRequestID() {
        return this.requestID;
    }

    @Override
        public String getCompletionId() {
        String string = this.completionId;
        if (string == null) {
            DefaultAPIChoice.$$$reportNull$$$0(5);
        }
        return string;
    }

    @Override
    public int getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    @Override
        public Double getMeanLogProb() {
        return this.meanLogProb;
    }

        public TelemetryData getBaseTelemetryData() {
        TelemetryData telemetryData = this.baseTelemetryData;
        if (telemetryData == null) {
            DefaultAPIChoice.$$$reportNull$$$0(6);
        }
        return telemetryData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefaultAPIChoice)) {
            return false;
        }
        DefaultAPIChoice other = (DefaultAPIChoice)o;
        if (this.getNumTokens() != other.getNumTokens()) {
            return false;
        }
        if (this.getChoiceIndex() != other.getChoiceIndex()) {
            return false;
        }
        if (this.getRequestID() != other.getRequestID()) {
            return false;
        }
        if (this.getCreatedTimestamp() != other.getCreatedTimestamp()) {
            return false;
        }
        if (this.isCached() != other.isCached()) {
            return false;
        }
        Double this$meanLogProb = this.getMeanLogProb();
        Double other$meanLogProb = other.getMeanLogProb();
        if (this$meanLogProb == null ? other$meanLogProb != null : !((Object)this$meanLogProb).equals(other$meanLogProb)) {
            return false;
        }
        CompletionResponseInfo this$responseInfo = this.getResponseInfo();
        CompletionResponseInfo other$responseInfo = other.getResponseInfo();
        if (this$responseInfo == null ? other$responseInfo != null : !((Object)this$responseInfo).equals(other$responseInfo)) {
            return false;
        }
        List<String> this$completion = this.getCompletion();
        List<String> other$completion = other.getCompletion();
        if (this$completion == null ? other$completion != null : !((Object)this$completion).equals(other$completion)) {
            return false;
        }
        String this$completionId = this.getCompletionId();
        String other$completionId = other.getCompletionId();
        if (this$completionId == null ? other$completionId != null : !this$completionId.equals(other$completionId)) {
            return false;
        }
        TelemetryData this$baseTelemetryData = this.getBaseTelemetryData();
        TelemetryData other$baseTelemetryData = other.getBaseTelemetryData();
        if (this$baseTelemetryData == null ? other$baseTelemetryData != null : !((Object)this$baseTelemetryData).equals(other$baseTelemetryData)) {
            return false;
        }
        TelemetryData this$telemetryData = this.getTelemetryData();
        TelemetryData other$telemetryData = other.getTelemetryData();
        return !(this$telemetryData == null ? other$telemetryData != null : !((Object)this$telemetryData).equals(other$telemetryData));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNumTokens();
        result = result * 59 + this.getChoiceIndex();
        result = result * 59 + this.getRequestID();
        result = result * 59 + this.getCreatedTimestamp();
        result = result * 59 + (this.isCached() ? 79 : 97);
        Double $meanLogProb = this.getMeanLogProb();
        result = result * 59 + ($meanLogProb == null ? 43 : ((Object)$meanLogProb).hashCode());
        CompletionResponseInfo $responseInfo = this.getResponseInfo();
        result = result * 59 + ($responseInfo == null ? 43 : ((Object)$responseInfo).hashCode());
        List<String> $completion = this.getCompletion();
        result = result * 59 + ($completion == null ? 43 : ((Object)$completion).hashCode());
        String $completionId = this.getCompletionId();
        result = result * 59 + ($completionId == null ? 43 : $completionId.hashCode());
        TelemetryData $baseTelemetryData = this.getBaseTelemetryData();
        result = result * 59 + ($baseTelemetryData == null ? 43 : ((Object)$baseTelemetryData).hashCode());
        TelemetryData $telemetryData = this.getTelemetryData();
        result = result * 59 + ($telemetryData == null ? 43 : ((Object)$telemetryData).hashCode());
        return result;
    }

    public String toString() {
        return "DefaultAPIChoice(responseInfo=" + this.getResponseInfo() + ", completion=" + this.getCompletion() + ", numTokens=" + this.getNumTokens() + ", choiceIndex=" + this.getChoiceIndex() + ", requestID=" + this.getRequestID() + ", completionId=" + this.getCompletionId() + ", createdTimestamp=" + this.getCreatedTimestamp() + ", meanLogProb=" + this.getMeanLogProb() + ", baseTelemetryData=" + this.getBaseTelemetryData() + ", isCached=" + this.isCached() + ", telemetryData=" + this.getTelemetryData() + ")";
    }

    public DefaultAPIChoice(CompletionResponseInfo responseInfo, List<String> completion, int numTokens, int choiceIndex, int requestID, String completionId, int createdTimestamp, Double meanLogProb, TelemetryData baseTelemetryData, boolean isCached) {
        if (responseInfo == null) {
            DefaultAPIChoice.$$$reportNull$$$0(7);
        }
        if (completion == null) {
            DefaultAPIChoice.$$$reportNull$$$0(8);
        }
        if (completionId == null) {
            DefaultAPIChoice.$$$reportNull$$$0(9);
        }
        if (baseTelemetryData == null) {
            DefaultAPIChoice.$$$reportNull$$$0(10);
        }
        if (responseInfo == null) {
            throw new NullPointerException("responseInfo is marked non-null but is null");
        }
        if (completion == null) {
            throw new NullPointerException("completion is marked non-null but is null");
        }
        if (completionId == null) {
            throw new NullPointerException("completionId is marked non-null but is null");
        }
        if (baseTelemetryData == null) {
            throw new NullPointerException("baseTelemetryData is marked non-null but is null");
        }
        this.responseInfo = responseInfo;
        this.completion = completion;
        this.numTokens = numTokens;
        this.choiceIndex = choiceIndex;
        this.requestID = requestID;
        this.completionId = completionId;
        this.createdTimestamp = createdTimestamp;
        this.meanLogProb = meanLogProb;
        this.baseTelemetryData = baseTelemetryData;
        this.isCached = isCached;
    }

    public DefaultAPIChoice(CompletionResponseInfo responseInfo, List<String> completion, int numTokens, int choiceIndex, int requestID, String completionId, int createdTimestamp, Double meanLogProb, TelemetryData baseTelemetryData, boolean isCached, TelemetryData telemetryData) {
        if (responseInfo == null) {
            DefaultAPIChoice.$$$reportNull$$$0(11);
        }
        if (completion == null) {
            DefaultAPIChoice.$$$reportNull$$$0(12);
        }
        if (completionId == null) {
            DefaultAPIChoice.$$$reportNull$$$0(13);
        }
        if (baseTelemetryData == null) {
            DefaultAPIChoice.$$$reportNull$$$0(14);
        }
        if (responseInfo == null) {
            throw new NullPointerException("responseInfo is marked non-null but is null");
        }
        if (completion == null) {
            throw new NullPointerException("completion is marked non-null but is null");
        }
        if (completionId == null) {
            throw new NullPointerException("completionId is marked non-null but is null");
        }
        if (baseTelemetryData == null) {
            throw new NullPointerException("baseTelemetryData is marked non-null but is null");
        }
        this.responseInfo = responseInfo;
        this.completion = completion;
        this.numTokens = numTokens;
        this.choiceIndex = choiceIndex;
        this.requestID = requestID;
        this.completionId = completionId;
        this.createdTimestamp = createdTimestamp;
        this.meanLogProb = meanLogProb;
        this.baseTelemetryData = baseTelemetryData;
        this.isCached = isCached;
        this.telemetryData = telemetryData;
    }

    @Override
    public DefaultAPIChoice withCompletion(List<String> completion) {
        if (completion == null) {
            DefaultAPIChoice.$$$reportNull$$$0(15);
        }
        if (completion == null) {
            throw new NullPointerException("completion is marked non-null but is null");
        }
        return this.completion == completion ? this : new DefaultAPIChoice(this.responseInfo, completion, this.numTokens, this.choiceIndex, this.requestID, this.completionId, this.createdTimestamp, this.meanLogProb, this.baseTelemetryData, this.isCached, this.telemetryData);
    }

    @Override
    public boolean isCached() {
        return this.isCached;
    }

    private DefaultAPIChoice withCached(boolean isCached) {
        return this.isCached == isCached ? this : new DefaultAPIChoice(this.responseInfo, this.completion, this.numTokens, this.choiceIndex, this.requestID, this.completionId, this.createdTimestamp, this.meanLogProb, this.baseTelemetryData, isCached, this.telemetryData);
    }

    private void setTelemetryData(TelemetryData telemetryData) {
        this.telemetryData = telemetryData;
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
            case 2: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 2: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/DefaultAPIChoice";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prefix";
                break;
            }
            case 7: 
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "responseInfo";
                break;
            }
            case 8: 
            case 12: 
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completion";
                break;
            }
            case 9: 
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionId";
                break;
            }
            case 10: 
            case 14: {
                objectArray2 = objectArray3;
                objectArray3[0] = "baseTelemetryData";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "asCached";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getTelemetryData";
                break;
            }
            case 2: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/DefaultAPIChoice";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getResponseInfo";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletion";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletionId";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getBaseTelemetryData";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "withoutPrefix";
                break;
            }
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 15: {
                objectArray = objectArray;
                objectArray[2] = "withCompletion";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 2: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

