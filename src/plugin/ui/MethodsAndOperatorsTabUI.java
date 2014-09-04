package plugin.ui;


import plugin.MuJavaPlugin;
import plugin.listeners.methodsAndOperators.ButtonListener;
import plugin.listeners.methodsAndOperators.ListListener;
import plugin.ui.controllers.MethodsAndOpsController;
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

@SuppressWarnings("restriction")
public class MethodsAndOperatorsTabUI {
	private Composite parent;

	public MethodsAndOperatorsTabUI(Composite parent) {
		this.parent = parent;
	}
	
	public Composite getMethodsAndOperatorsTab(Updater methodsAndOperatorsUpdater) {
		MethodsAndOpsController moc = MuJavaPlugin.moc;
		moc.setUpdater(methodsAndOperatorsUpdater);
		Composite methodsAndOperatorsTab = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		
		//Methods and operators composite
		Composite mao = SWTFactory.createComposite(methodsAndOperatorsTab, 4, 1, GridData.FILL_HORIZONTAL);
		
		Composite methodsComp = SWTFactory.createComposite(mao, 1, 1, GridData.FILL_HORIZONTAL);
		Label methodsLabel = SWTFactory.createLabel(methodsComp, "Methods to mutate", 1);
		methodsLabel.setAlignment(SWT.CENTER);
		List methodsList = new List(methodsComp, SWT.SINGLE);
		methodsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData)methodsList.getLayoutData()).heightHint = 300;
		methodsList.addSelectionListener(new ListListener(ListListener.SOURCE.METHODS, moc));
		
		Composite methodsButtonsComp = SWTFactory.createComposite(mao, 1, 1, GridData.FILL_HORIZONTAL);
		Button methodsAddButton = SWTFactory.createPushButton(methodsButtonsComp, "Add", null);
		Button methodsDelButton = SWTFactory.createPushButton(methodsButtonsComp, "Del", null);
		Button methodsClearButton = SWTFactory.createPushButton(methodsButtonsComp, "Clear", null);
		methodsAddButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.ADD, ButtonListener.SOURCE.METHODS, moc));
		methodsDelButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.DEL, ButtonListener.SOURCE.METHODS, moc));
		methodsClearButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.CLEAR, ButtonListener.SOURCE.METHODS, moc));
		
		moc.setMethodsElems(methodsList, methodsAddButton, methodsDelButton, methodsClearButton);
		
		Composite operatorsComp = SWTFactory.createComposite(mao, 1, 1, GridData.FILL_HORIZONTAL);
		Label operatorsLabel = SWTFactory.createLabel(operatorsComp, "Operators", 1);
		operatorsLabel.setAlignment(SWT.CENTER);
		List operatorsList = new List(operatorsComp, SWT.SINGLE);
		operatorsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData)operatorsList.getLayoutData()).heightHint = 300;
		operatorsList.addSelectionListener(new ListListener(ListListener.SOURCE.OPERATORS, moc));
		
		Composite operatorsButtonsComp = SWTFactory.createComposite(mao, 1, 1, GridData.FILL_HORIZONTAL);
		Button operatorsAddButton = SWTFactory.createPushButton(operatorsButtonsComp, "Add", null);
		Button operatorsDelButton = SWTFactory.createPushButton(operatorsButtonsComp, "Del", null);
		Button operatorsClearButton = SWTFactory.createPushButton(operatorsButtonsComp, "Clear", null);
		operatorsAddButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.ADD, ButtonListener.SOURCE.OPERATORS, moc));
		operatorsDelButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.DEL, ButtonListener.SOURCE.OPERATORS, moc));
		operatorsClearButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.CLEAR, ButtonListener.SOURCE.OPERATORS, moc));
		
		moc.setOperatorsElems(operatorsList, operatorsAddButton, operatorsDelButton, operatorsClearButton);
		
		//Allow field and class mutations composite
		Composite allowFieldAndClassMutationsComposite = SWTFactory.createComposite(methodsAndOperatorsTab, 1, 1, GridData.FILL_HORIZONTAL);
		Button allowFieldMutationsButton = SWTFactory.createCheckButton(allowFieldAndClassMutationsComposite, "Allow field mutations", null, false, 1);
		Button allowClassMutationsButton = SWTFactory.createCheckButton(allowFieldAndClassMutationsComposite, "Allow class mutations", null, false, 1);
		allowFieldMutationsButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.CHECK, ButtonListener.SOURCE.ALLOW_FIELD_MUTATIONS, moc));
		allowClassMutationsButton.addSelectionListener(new ButtonListener(ButtonListener.BUTTON_TYPE.CHECK, ButtonListener.SOURCE.ALLOW_CLASS_MUTATIONS, moc));
		
		moc.setAllowElems(allowFieldMutationsButton, allowClassMutationsButton);
		moc.init();
		
		return methodsAndOperatorsTab;
	}
	
	
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	public void initializeFrom(ILaunchConfiguration configuration) {

	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		
	}
	
	
	
}
