package plugin.ui;


import plugin.MuJavaPlugin;
import plugin.listeners.tests.ButtonListener;
import plugin.listeners.tests.DirSelectButtonListener;
import plugin.listeners.tests.ListListener;
import plugin.listeners.tests.ProjectSelectButtonListener;
import plugin.ui.controllers.TestsController;
import plugin.ui.tabs.Updater;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class TestsTabUI {
	private Composite parent;

	public TestsTabUI(Composite parent) {
		this.parent = parent;
	}
	
	public Composite getTestsTab(Updater testsUpdater) {
		TestsController tc = MuJavaPlugin.tc;
		tc.setUpdater(testsUpdater);
		Composite testsTab = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		
		//Tests project
		Composite testsProjectComp = SWTFactory.createComposite(testsTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label testsProjectLabel = SWTFactory.createLabel(testsProjectComp, "Tests project", 1);
		((GridData)testsProjectLabel.getLayoutData()).widthHint = 120;
		Text testsProjectText = SWTFactory.createSingleText(testsProjectComp, 1);
		testsProjectText.setEditable(false);
		testsProjectText.setEnabled(false);
		Button testsProjectButton = SWTFactory.createPushButton(testsProjectComp, "browse", null);
		testsProjectButton.addSelectionListener(new ProjectSelectButtonListener(tc));
		
		tc.setTestsProjectElems(testsProjectText, testsProjectButton);
		
		//Tests bin dir
		Composite testsBinDirComp = SWTFactory.createComposite(testsTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label testsBinDirLabel = SWTFactory.createLabel(testsBinDirComp, "Tests binary folder", 1);
		((GridData)testsBinDirLabel.getLayoutData()).widthHint = 120;
		Text testsBinDirText = SWTFactory.createSingleText(testsBinDirComp, 1);
		testsBinDirText.setEditable(false);
		testsBinDirText.setEnabled(false);
		Button testsBinDirButton = SWTFactory.createPushButton(testsBinDirComp, "browse", null);
		testsBinDirButton.addSelectionListener(new DirSelectButtonListener(tc));
		
		tc.setTestsBinFolderElems(testsBinDirText, testsBinDirButton);
		
		//Space separator
		SWTFactory.createHorizontalSpacer(testsTab, 5);
		
		//Tests Classes
		Composite testsClassesComp = SWTFactory.createComposite(testsTab, 2, 200, GridData.FILL_HORIZONTAL);
		
		Composite testsClassesListComp = SWTFactory.createComposite(testsClassesComp, 1, 1, GridData.FILL_HORIZONTAL);
		Label testsClassesLabel = SWTFactory.createLabel(testsClassesListComp, "Tests classes", 1);
		testsClassesLabel.setAlignment(SWT.CENTER);
		List testsClassesList = new List(testsClassesListComp, SWT.SINGLE);
		testsClassesList.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData)testsClassesList.getLayoutData()).heightHint = 300;
		testsClassesList.addSelectionListener(new ListListener(tc));
		
		Composite testsClassesButtonsComp = SWTFactory.createComposite(testsClassesComp, 1, 1, GridData.FILL_HORIZONTAL);
		Button testsClassesAddButton = SWTFactory.createPushButton(testsClassesButtonsComp, "Add", null);
		Button testsClassesDelButton = SWTFactory.createPushButton(testsClassesButtonsComp, "Del", null);
		Button testsClassesClearButton = SWTFactory.createPushButton(testsClassesButtonsComp, "Clear", null);
		testsClassesAddButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.ADD, tc));
		testsClassesDelButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.DEL, tc));
		testsClassesClearButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.CLEAR, tc));
		
		tc.setTestsListElems(testsClassesList, testsClassesAddButton, testsClassesDelButton, testsClassesClearButton);
		tc.init();
		
		return testsTab;
	}
	
	
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	public void initializeFrom(ILaunchConfiguration configuration) {

	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		
	}
	
	
	
}
