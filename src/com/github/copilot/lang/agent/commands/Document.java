/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.Position;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class Document {
    @SerializedName(value="source")
    @NotNull
    String source;
    @SerializedName(value="languageId")
    @NotNull
    String languageId;
    @SerializedName(value="position")
    @NotNull
    Position position;
    @SerializedName(value="insertSpaces")
    boolean useSpaces;
    @SerializedName(value="tabSize")
    int tabSize;
    @SerializedName(value="path")
    String path;
    @SerializedName(value="relativePath")
    String relativePath;

    @NotNull
    public String getSource() {
        String string = this.source;
        if (string == null) {
            Document.$$$reportNull$$$0(0);
        }
        return string;
    }

    @NotNull
    public String getLanguageId() {
        String string = this.languageId;
        if (string == null) {
            Document.$$$reportNull$$$0(1);
        }
        return string;
    }

    @NotNull
    public Position getPosition() {
        Position position = this.position;
        if (position == null) {
            Document.$$$reportNull$$$0(2);
        }
        return position;
    }

    public boolean isUseSpaces() {
        return this.useSpaces;
    }

    public int getTabSize() {
        return this.tabSize;
    }

    public String getPath() {
        return this.path;
    }

    public String getRelativePath() {
        return this.relativePath;
    }

    public void setSource(@NotNull String source) {
        if (source == null) {
            Document.$$$reportNull$$$0(3);
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        this.source = source;
    }

    public void setLanguageId(@NotNull String languageId) {
        if (languageId == null) {
            Document.$$$reportNull$$$0(4);
        }
        if (languageId == null) {
            throw new NullPointerException("languageId is marked non-null but is null");
        }
        this.languageId = languageId;
    }

    public void setPosition(@NotNull Position position) {
        if (position == null) {
            Document.$$$reportNull$$$0(5);
        }
        if (position == null) {
            throw new NullPointerException("position is marked non-null but is null");
        }
        this.position = position;
    }

    public void setUseSpaces(boolean useSpaces) {
        this.useSpaces = useSpaces;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        Document other = (Document)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isUseSpaces() != other.isUseSpaces()) {
            return false;
        }
        if (this.getTabSize() != other.getTabSize()) {
            return false;
        }
        String this$source = this.getSource();
        String other$source = other.getSource();
        if (this$source == null ? other$source != null : !this$source.equals(other$source)) {
            return false;
        }
        String this$languageId = this.getLanguageId();
        String other$languageId = other.getLanguageId();
        if (this$languageId == null ? other$languageId != null : !this$languageId.equals(other$languageId)) {
            return false;
        }
        Position this$position = this.getPosition();
        Position other$position = other.getPosition();
        if (this$position == null ? other$position != null : !((Object)this$position).equals(other$position)) {
            return false;
        }
        String this$path = this.getPath();
        String other$path = other.getPath();
        if (this$path == null ? other$path != null : !this$path.equals(other$path)) {
            return false;
        }
        String this$relativePath = this.getRelativePath();
        String other$relativePath = other.getRelativePath();
        return !(this$relativePath == null ? other$relativePath != null : !this$relativePath.equals(other$relativePath));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Document;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isUseSpaces() ? 79 : 97);
        result = result * 59 + this.getTabSize();
        String $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        String $languageId = this.getLanguageId();
        result = result * 59 + ($languageId == null ? 43 : $languageId.hashCode());
        Position $position = this.getPosition();
        result = result * 59 + ($position == null ? 43 : ((Object)$position).hashCode());
        String $path = this.getPath();
        result = result * 59 + ($path == null ? 43 : $path.hashCode());
        String $relativePath = this.getRelativePath();
        result = result * 59 + ($relativePath == null ? 43 : $relativePath.hashCode());
        return result;
    }

    public String toString() {
        return "Document(source=" + this.getSource() + ", languageId=" + this.getLanguageId() + ", position=" + this.getPosition() + ", useSpaces=" + this.isUseSpaces() + ", tabSize=" + this.getTabSize() + ", path=" + this.getPath() + ", relativePath=" + this.getRelativePath() + ")";
    }

    public Document(@NotNull String source, @NotNull String languageId, @NotNull Position position, boolean useSpaces, int tabSize, String path, String relativePath) {
        if (source == null) {
            Document.$$$reportNull$$$0(6);
        }
        if (languageId == null) {
            Document.$$$reportNull$$$0(7);
        }
        if (position == null) {
            Document.$$$reportNull$$$0(8);
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        if (languageId == null) {
            throw new NullPointerException("languageId is marked non-null but is null");
        }
        if (position == null) {
            throw new NullPointerException("position is marked non-null but is null");
        }
        this.source = source;
        this.languageId = languageId;
        this.position = position;
        this.useSpaces = useSpaces;
        this.tabSize = tabSize;
        this.path = path;
        this.relativePath = relativePath;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/Document";
                break;
            }
            case 3: 
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "source";
                break;
            }
            case 4: 
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "languageId";
                break;
            }
            case 5: 
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "position";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getSource";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getLanguageId";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getPosition";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/Document";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "setSource";
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "setLanguageId";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "setPosition";
                break;
            }
            case 6: 
            case 7: 
            case 8: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

