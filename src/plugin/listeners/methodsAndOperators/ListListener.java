package plugin.listeners.methodsAndOperators;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;

import plugin.ui.controllers.MethodsAndOpsController;

public class ListListener implements SelectionListener {
	public static enum SOURCE {METHODS, OPERATORS}
	protected MethodsAndOpsController moc;
	private SOURCE s;
	
	
	public ListListener (SOURCE s, MethodsAndOpsController moc) {
		this.moc = moc;
		this.s = s;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		List list = (List)e.getSource();
		int selectedIndex = list.getSelectionIndex();
		String selectedElem = selectedIndex==-1?null:list.getItem(selectedIndex);
		updateSelection(selectedElem);
	}

	private void updateSelection(String selectedElem) {
		switch (this.s) {
			case METHODS: this.moc.setMethodSelected(selectedElem); break;
			case OPERATORS: this.moc.setOperatorSelected(selectedElem); break;
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

}
