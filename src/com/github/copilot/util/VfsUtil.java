/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  me.masecla.copilot.extra.Logger
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.testFramework.LightVirtualFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

import com.intellij.openapi.application.ApplicationManager;
import me.masecla.copilot.extra.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import java.io.IOException;

public class VfsUtil {
	private static final Logger LOG = Logger.getInstance(VfsUtil.class);

	public static void safeDelete(VirtualFile file) {
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
		}
		if (file instanceof LightVirtualFile) {
			((LightVirtualFile) file).setValid(false);
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
			throw new IllegalStateException("file cannot be null!");
		}
		ApplicationManager.getApplication().assertWriteAccessAllowed();
		try {
			file.delete(VfsUtil.class);
		} catch (IOException e) {
			LOG.debug("Exception deleting VirtualFile", (Throwable) e);
		}
	}

}
