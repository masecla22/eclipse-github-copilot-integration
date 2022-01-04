/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.execution.util.ListTableWithButtons
 *  com.intellij.lang.Language
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.ui.AnActionButtonRunnable
 *  com.intellij.util.ui.ColumnInfo
 *  com.intellij.util.ui.ListTableModel
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.CopilotBundle;
import com.github.copilot.settings.LanguageChoice;
import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SortOrder;

class LanguageTable extends ListTableWithButtons<LanguageChoice> {
	void initItems(Set<String> disabledLanguageIds) {
		if (disabledLanguageIds == null) {
			throw new IllegalStateException("disabledLanguageIds cannot be null!");
		}
		List items = Language.getRegisteredLanguages().stream().filter(lang -> !lang.is(Language.ANY))
				.map(lang -> new LanguageChoice((Language) lang, disabledLanguageIds.contains(lang.getID())))
				.sorted(Comparator.comparing(value -> value.getLanguage().getDisplayName(),
						String::compareToIgnoreCase))
				.sorted(Comparator.comparingInt(value -> value.isSelected() ? -1 : 1)).collect(Collectors.toList());
		this.setValues(items);
	}

	void setDisabledLanguages(Set<String> ids) {
		if (ids == null) {
			throw new IllegalStateException("ids cannot be null!");
		}
		List items = this.getElements();
		for (LanguageChoice item : items) {
			item.setSelected(ids.contains(item.getLanguage().getID()));
		}
		this.getTableView().getTableViewModel().setItems(items);
	}

	Set<String> getDisabledLanguages() {
		HashSet<String> ids = new HashSet<String>();
		List items = this.getElements();
		for (LanguageChoice element : items) {
			if (!element.isSelected())
				continue;
			ids.add(element.getLanguage().getID());
		}
		return ids;
	}

	protected AnActionButtonRunnable createAddAction() {
		return null;
	}

	protected AnActionButtonRunnable createRemoveAction() {
		return null;
	}

	protected boolean isUpDownSupported() {
		return false;
	}

	protected ListTableModel<LanguageChoice> createListModel() {
		LanguageSelectedColumn checkboxColumn = new LanguageSelectedColumn();
		LanguageNameColumn nameColumn = new LanguageNameColumn();
		ListTableModel model = new ListTableModel(new ColumnInfo[] { checkboxColumn, nameColumn }, List.of(), 0,
				SortOrder.UNSORTED);
		model.setSortable(true);
		return model;
	}

	protected LanguageChoice createElement() {
		throw new UnsupportedOperationException("unsupported");
	}

	protected boolean isEmpty(LanguageChoice element) {
		return false;
	}

	protected LanguageChoice cloneElement(LanguageChoice item) {
		return new LanguageChoice(item.getLanguage(), item.isSelected());
	}

	protected boolean canDeleteElement(LanguageChoice selection) {
		return false;
	}

	private static class LanguageNameColumn extends ColumnInfo<LanguageChoice, String> {
		public LanguageNameColumn() {
			super(CopilotBundle.get("applicationConfigurable.languages.columnName"));
		}

		public Comparator<LanguageChoice> getComparator() {
			return Comparator.comparing(lang -> this.getLabel((LanguageChoice) lang).toLowerCase());
		}

		public String valueOf(LanguageChoice languageChoice) {
			return this.getLabel(languageChoice);
		}

		private String getLabel(LanguageChoice languageChoice) {
			Language lang = languageChoice.getLanguage();
			String string = StringUtil.defaultIfEmpty((String) lang.getDisplayName(), (String) lang.getID());
			if (string == null) {
				throw new IllegalStateException("string cannot be null!");
			}
			return string;
		}
	}

	private static class LanguageSelectedColumn extends ColumnInfo<LanguageChoice, Boolean> {
		private static final int CHECKBOX_COLUMN_WIDTH = new JCheckBox().getPreferredSize().width + 4;

		public LanguageSelectedColumn() {
			super("");
		}

		public int getWidth(JTable table) {
			return CHECKBOX_COLUMN_WIDTH;
		}

		public Class<?> getColumnClass() {
			return Boolean.class;
		}

		public boolean isCellEditable(LanguageChoice languageChoice) {
			return true;
		}

		public Boolean valueOf(LanguageChoice languageChoice) {
			return languageChoice.isSelected();
		}

		public void setValue(LanguageChoice languageChoice, Boolean value) {
			languageChoice.setSelected(value);
		}

		public Comparator<LanguageChoice> getComparator() {
			return Comparator.comparing(LanguageChoice::isSelected);
		}
	}
}
