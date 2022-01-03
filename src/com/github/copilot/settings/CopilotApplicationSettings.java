/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.credentialStore.CredentialAttributes
 *  com.intellij.credentialStore.CredentialAttributesKt
 *  com.intellij.ide.passwordSafe.PasswordSafe
 *  com.intellij.lang.Language
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.components.PersistentStateComponent
 *  com.intellij.openapi.components.State
 *  com.intellij.openapi.components.Storage
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.psi.PsiDocumentManager
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.settings.CopilotApplicationState;
import com.github.copilot.settings.CopilotLocalApplicationSettings;
import com.github.copilot.settings.CopilotLocalApplicationState;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name="github-copilot", storages={@Storage(value="github-copilot.xml")})
public class CopilotApplicationSettings
implements PersistentStateComponent<CopilotApplicationState> {
        private static final CredentialAttributes GITHUB_TOKEN_CREDENTIALS = CopilotApplicationSettings.createCredentials("GitHub Copilot API Token");
    private CopilotApplicationState state;

        public static CopilotApplicationState settings() {
        CopilotApplicationState state = ((CopilotApplicationSettings)ApplicationManager.getApplication().getService(CopilotApplicationSettings.class)).getState();
        assert (state != null);
        CopilotApplicationState copilotApplicationState = state;
        if (copilotApplicationState == null) {
            throw new IllegalStateException("copilotApplicationState cannot be null!");
        }
        return copilotApplicationState;
    }

        public static String getGitHubToken() {
        CopilotLocalApplicationState localSettings = CopilotLocalApplicationSettings.settings();
        String token = localSettings.githubToken;
        if (token == null && !localSettings.githubTokenMigration) {
            localSettings.githubTokenMigration = true;
            String oldToken = PasswordSafe.getInstance().getPassword(GITHUB_TOKEN_CREDENTIALS);
            if (oldToken != null) {
                CopilotApplicationSettings.setGitHubToken(oldToken);
                return oldToken;
            }
        }
        return token;
    }

    public static void setGitHubToken(String secret) {
        CopilotLocalApplicationSettings.settings().githubToken = secret;
    }

    public static boolean isCopilotEnabled(Project project, Editor editor) {
        PsiFile file;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return (file = PsiDocumentManager.getInstance((Project)project).getPsiFile(editor.getDocument())) != null && CopilotApplicationSettings.isCopilotEnabled(file);
    }

    public static boolean isCopilotEnabled(PsiFile file) {
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        Language language = file.getLanguage();
        return CopilotApplicationSettings.isCopilotEnabled(language);
    }

    public static boolean isCopilotEnabled(Language language) {
        if (language == null) {
            throw new IllegalStateException("language cannot be null!");
        }
        CopilotApplicationState settings = CopilotApplicationSettings.settings();
        return settings.enableCompletions && settings.isEnabled(language);
    }

        public synchronized CopilotApplicationState getState() {
        return this.state;
    }

    public synchronized void noStateLoaded() {
        this.state = new CopilotApplicationState();
    }

    public synchronized void loadState(CopilotApplicationState state) {
        if (state == null) {
            throw new IllegalStateException("state cannot be null!");
        }
        this.state = state;
    }

        private static CredentialAttributes createCredentials(String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        return new CredentialAttributes(CopilotApplicationSettings.passwordSafeService(name), null, CopilotApplicationSettings.class, false, true);
    }

        private static String passwordSafeService(String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        String string = CredentialAttributesKt.generateServiceName((String)"GitHub Copilot", (String)name);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
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
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/settings/CopilotApplicationSettings";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "language";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "state";
                break;
            }
            case 6: 
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "settings";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/settings/CopilotApplicationSettings";
                break;
            }
            case 8: {
                objectArray = objectArray2;
                objectArray2[1] = "passwordSafeService";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "isCopilotEnabled";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "loadState";
                break;
            }
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "createCredentials";
                break;
            }
            case 7: {
                objectArray = objectArray;
                objectArray[2] = "passwordSafeService";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

