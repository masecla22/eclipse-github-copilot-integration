/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.testFramework.LightVirtualFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class VfsUtil {
    private static final Logger LOG = Logger.getInstance(VfsUtil.class);

    public static void safeDelete(VirtualFile file) {
        if (file == null) {
            VfsUtil.$$$reportNull$$$0(0);
        }
        if (file instanceof LightVirtualFile) {
            ((LightVirtualFile)file).setValid(false);
            return;
        }
        if (!file.isValid()) {
            return;
        }
        if (ApplicationManager.getApplication().isWriteAccessAllowed()) {
            VfsUtil.doSafeDelete(file);
        } else {
            ApplicationManager.getApplication().runWriteAction(() -> VfsUtil.doSafeDelete(file));
        }
    }

    private static void doSafeDelete(VirtualFile file) {
        if (file == null) {
            VfsUtil.$$$reportNull$$$0(1);
        }
        ApplicationManager.getApplication().assertWriteAccessAllowed();
        try {
            file.delete(VfsUtil.class);
        }
        catch (IOException e) {
            LOG.debug("Exception deleting VirtualFile", (Throwable)e);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "file";
        objectArray2[1] = "com/github/copilot/util/VfsUtil";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "safeDelete";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "doSafeDelete";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

