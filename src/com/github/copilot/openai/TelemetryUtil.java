/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

import com.github.copilot.openai.APIChoice;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.util.String2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.Map;

class TelemetryUtil {
    TelemetryUtil() {
    }

        static TelemetryData createChoiceTelemetry(APIChoice choice, TelemetryData base) {
        if (choice == null) {
            throw new IllegalStateException("choice cannot be null!");
        }
        if (base == null) {
            throw new IllegalStateException("base cannot be null!");
        }
        Object2DoubleMap<String> metrics = String2DoubleMap.of("numTokens", choice.getNumTokens(), "compCharLen", choice.getCompletionTextLength(), "numLines", choice.getCompletion().size());
        Double meanLogProb = choice.getMeanLogProb();
        if (meanLogProb != null) {
            metrics.put((Object)"meanLogProb", choice.getMeanLogProb().doubleValue());
        }
        TelemetryData telemetryData = TelemetryData.extend(base, Map.of("headerRequestId", choice.getResponseInfo().getHeaderRequestId(), "completionId", choice.getCompletionId(), "choiceIndex", String.valueOf(choice.getChoiceIndex()), "created", String.valueOf(choice.getCreatedTimestamp()), "serverExperiments", choice.getResponseInfo().getServerExperiments()), metrics);
        if (telemetryData == null) {
            throw new IllegalStateException("telemetryData cannot be null!");
        }
        return telemetryData;
    }

    
}

