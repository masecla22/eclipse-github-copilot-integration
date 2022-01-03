/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName(value="source")
        String source;
    @SerializedName(value="languageId")
        String languageId;
    @SerializedName(value="position")
        Position position;
    @SerializedName(value="insertSpaces")
    boolean useSpaces;
    @SerializedName(value="tabSize")
    int tabSize;
    @SerializedName(value="path")
    String path;
    @SerializedName(value="relativePath")
    String relativePath;

        public String getSource() {
        String string = this.source;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

        public String getLanguageId() {
        String string = this.languageId;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

        public Position getPosition() {
        Position position = this.position;
        if (position == null) {
            throw new IllegalStateException("position cannot be null!");
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

    public void setSource(String source) {
        if (source == null) {
            throw new IllegalStateException("source cannot be null!");
        }
        this.source = source;
    }

    public void setLanguageId(String languageId) {
        if (languageId == null) {
            throw new IllegalStateException("languageId cannot be null!");
        }
        this.languageId = languageId;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalStateException("position cannot be null!");
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

    public Document(String source, String languageId, Position position, boolean useSpaces, int tabSize, String path, String relativePath) {
        if (source == null) {
            throw new IllegalStateException("source cannot be null!");
        }
        if (languageId == null) {
            throw new IllegalStateException("languageId cannot be null!");
        }
        if (position == null) {
            throw new IllegalStateException("position cannot be null!");
        }
        this.source = source;
        this.languageId = languageId;
        this.position = position;
        this.useSpaces = useSpaces;
        this.tabSize = tabSize;
        this.path = path;
        this.relativePath = relativePath;
    }

    
}

