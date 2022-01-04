/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.settings;

import com.intellij.lang.Language;

class LanguageChoice {
	private final Language language;
	private boolean selected;

	public Language getLanguage() {
		Language language = this.language;
		if (language == null) {
			throw new IllegalStateException("language cannot be null!");
		}
		return language;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LanguageChoice)) {
			return false;
		}
		LanguageChoice other = (LanguageChoice) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.isSelected() != other.isSelected()) {
			return false;
		}
		Language this$language = this.getLanguage();
		Language other$language = other.getLanguage();
		return !(this$language == null ? other$language != null : !this$language.equals(other$language));
	}

	protected boolean canEqual(Object other) {
		return other instanceof LanguageChoice;
	}

	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		result = result * 59 + (this.isSelected() ? 79 : 97);
		Language $language = this.getLanguage();
		result = result * 59 + ($language == null ? 43 : $language.hashCode());
		return result;
	}

	public String toString() {
		return "LanguageChoice(language=" + this.getLanguage() + ", selected=" + this.isSelected() + ")";
	}

	public LanguageChoice(Language language, boolean selected) {
		if (language == null) {
			throw new IllegalStateException("language cannot be null!");
		}
		if (language == null) {
			throw new NullPointerException("language is marked non-null but is null");
		}
		this.language = language;
		this.selected = selected;
	}

}
