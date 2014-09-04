package plugin.listeners.main;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import plugin.ui.controllers.MainController;

public class RunTestsSelectButtonListener implements SelectionListener {
	protected MainController mainController;
	protected Text text;

	
	public void setMainController(MainController mc) {
		this.mainController = mc;
	}
	
	public void setText(Text t) {
		this.text = t;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Button runTestsButton = (Button) e.getSource();
		update(runTestsButton.getSelection());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		Button runTestsButton = (Button) e.getSource();
		update(runTestsButton.getSelection());
	}
	
	public void update(boolean b) {
		this.mainController.updateRunTests(b);
	}
	

}
