/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.notification.Notification
 *  com.intellij.notification.NotificationListener
 *  com.intellij.notification.NotificationListener$UrlOpeningListener
 *  com.intellij.notification.NotificationType
 *  com.intellij.notification.impl.NotificationFullContent
 *  com.intellij.openapi.util.NlsContexts$NotificationContent
 *  com.intellij.openapi.util.NlsContexts$NotificationTitle
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.impl.NotificationFullContent;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;

public final class CopilotNotifications {
    private static final String GROUP_ID = "github.copilot.notifications";

    private CopilotNotifications() {
    }

    public static Notification createFullContentNotification(String title, String content, NotificationType type, boolean expireOnLinkClick) {
        if (title == null) {
            CopilotNotifications.$$$reportNull$$$0(0);
        }
        if (content == null) {
            CopilotNotifications.$$$reportNull$$$0(1);
        }
        if (type == null) {
            CopilotNotifications.$$$reportNull$$$0(2);
        }
        FullContent notification = new FullContent(GROUP_ID, title, content, type);
        notification.setListener((NotificationListener)new NotificationListener.UrlOpeningListener(expireOnLinkClick));
        return notification;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[0] = "title";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[0] = "content";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[0] = "type";
                break;
            }
        }
        objectArray[1] = "com/github/copilot/CopilotNotifications";
        objectArray[2] = "createFullContentNotification";
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }

    private static class FullContent
    extends Notification
    implements NotificationFullContent {
        public FullContent(String groupId, @NlsContexts.NotificationTitle String title, @NlsContexts.NotificationContent String content, NotificationType type) {
            if (groupId == null) {
                FullContent.$$$reportNull$$$0(0);
            }
            if (title == null) {
                FullContent.$$$reportNull$$$0(1);
            }
            if (content == null) {
                FullContent.$$$reportNull$$$0(2);
            }
            if (type == null) {
                FullContent.$$$reportNull$$$0(3);
            }
            super(groupId, title, content, type);
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            Object[] objectArray;
            Object[] objectArray2 = new Object[3];
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[0] = "groupId";
                    break;
                }
                case 1: {
                    objectArray = objectArray2;
                    objectArray2[0] = "title";
                    break;
                }
                case 2: {
                    objectArray = objectArray2;
                    objectArray2[0] = "content";
                    break;
                }
                case 3: {
                    objectArray = objectArray2;
                    objectArray2[0] = "type";
                    break;
                }
            }
            objectArray[1] = "com/github/copilot/CopilotNotifications$FullContent";
            objectArray[2] = "<init>";
            throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
        }
    }
}

