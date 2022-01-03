/*
 * Decompiled with CFR 0.152.
 */
package com.github.copilot.util;

public interface Cancellable {
    public static final Cancellable DUMB = new Cancellable(){

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void cancel() {
        }
    };

    public boolean isCancelled();

    public void cancel();
}

