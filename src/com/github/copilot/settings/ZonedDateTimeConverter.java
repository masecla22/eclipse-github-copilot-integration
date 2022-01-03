/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.util.xmlb.Converter
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.Converter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ZonedDateTimeConverter
extends Converter<ZonedDateTime> {
    private static final Logger LOG = Logger.getInstance(ZonedDateTimeConverter.class);

        public ZonedDateTime fromString(String value) {
        if (value == null) {
            throw new IllegalStateException("value cannot be null!");
        }
        try {
            return ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        }
        catch (DateTimeParseException e) {
            LOG.warn("Failed to parse LocalDateTime from string: " + value);
            return null;
        }
    }

    public String toString(ZonedDateTime value) {
        if (value == null) {
            throw new IllegalStateException("value cannot be null!");
        }
        return value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "value";
        objectArray2[1] = "com/github/copilot/settings/ZonedDateTimeConverter";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "fromString";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "toString";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

