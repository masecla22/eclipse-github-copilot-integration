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
import org.jetbrains.annotations.NotNull;

class TelemetryUtil {
    TelemetryUtil() {
    }

        static TelemetryData createChoiceTelemetry(APIChoice choice, TelemetryData base) {
        if (choice == null) {
            TelemetryUtil.$$$reportNull$$$0(0);
        }
        if (base == null) {
            TelemetryUtil.$$$reportNull$$$0(1);
        }
        Object2DoubleMap<String> metrics = String2DoubleMap.of("numTokens", choice.getNumTokens(), "compCharLen", choice.getCompletionTextLength(), "numLines", choice.getCompletion().size());
        Double meanLogProb = choice.getMeanLogProb();
        if (meanLogProb != null) {
            metrics.put((Object)"meanLogProb", choice.getMeanLogProb().doubleValue());
        }
        TelemetryData telemetryData = TelemetryData.extend(base, Map.of("headerRequestId", choice.getResponseInfo().getHeaderRequestId(), "completionId", choice.getCompletionId(), "choiceIndex", String.valueOf(choice.getChoiceIndex()), "created", String.valueOf(choice.getCreatedTimestamp()), "serverExperiments", choice.getResponseInfo().getServerExperiments()), metrics);
        if (telemetryData == null) {
            TelemetryUtil.$$$reportNull$$$0(2);
        }
        return telemetryData;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "choice";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "base";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/TelemetryUtil";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/TelemetryUtil";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "createChoiceTelemetry";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "createChoiceTelemetry";
                break;
            }
            case 2: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

