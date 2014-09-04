package plugin.listeners.tests;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;

import plugin.ui.controllers.TestsController;

public class ListListener implements SelectionListener {
	protected TestsController tc;
	
	
	public ListListener (TestsController tc) {
		this.tc = tc;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		List list = (List)e.getSource();
		int selectedIndex = list.getSelectionIndex();
		String selectedElem = selectedIndex==-1?null:list.getItem(selectedIndex);
		updateSelection(selectedElem);
	}

	private void updateSelection(String selectedElem) {
		this.tc.setTestSelected(selectedElem);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

}
