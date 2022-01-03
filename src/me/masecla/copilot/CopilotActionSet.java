package me.masecla.copilot;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

public class CopilotActionSet implements IWorkbenchWindowPulldownDelegate {

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

	@Override
	public void run(IAction action) {
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public Menu getMenu(Control parent) {
		Menu result = new Menu(parent);

		MenuItem settingsItem = new MenuItem(result, SWT.NONE);
		settingsItem.setText("Settings");
		MenuItem solutionsItem = new MenuItem(result, SWT.NONE);
		solutionsItem.setText("Synthesize 10 Solutions");
		MenuItem aboutItem = new MenuItem(result, SWT.NONE);
		aboutItem.setText("About");
		MenuItem helpItem = new MenuItem(result, SWT.NONE);
		helpItem.setText("Help");

		return result;
	}

}
