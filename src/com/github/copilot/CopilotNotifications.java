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
            throw new IllegalStateException("title cannot be null!");
        }
        if (content == null) {
            throw new IllegalStateException("content cannot be null!");
        }
        if (type == null) {
            throw new IllegalStateException("type cannot be null!");
        }
        FullContent notification = new FullContent(GROUP_ID, title, content, type);
        notification.setListener((NotificationListener)new NotificationListener.UrlOpeningListener(expireOnLinkClick));
        return notification;
    }

    

    private static class FullContent
    extends Notification
    implements NotificationFullContent {
        public FullContent(String groupId, @NlsContexts.NotificationTitle String title, @NlsContexts.NotificationContent String content, NotificationType type) {
            if (groupId == null) {
                throw new IllegalStateException("groupId cannot be null!");
            }
            if (title == null) {
                throw new IllegalStateException("title cannot be null!");
            }
            if (content == null) {
                throw new IllegalStateException("content cannot be null!");
            }
            if (type == null) {
                throw new IllegalStateException("type cannot be null!");
            }
            super(groupId, title, content, type);
        }

        
    }
}

