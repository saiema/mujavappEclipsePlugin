package plugin.ui.controllers;

import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;

import plugin.Parameters;
import plugin.ui.tabs.Updater;


public class MethodsAndOpsController {
	private Parameters params;
	private List methodsList; private Button methodsAddButton; private Button methodsDelButton; private Button methodsClearButton;
	private List operatorsList; private Button operatorsAddButton; private Button operatorsDelButton; private Button operatorsClearButton;
	private Button allowFieldMutationsButton;
	private Button allowClassMutationsButton;
	
	private String methodSelected = null;
	private String operatorSelected = null;
	private Updater updater;
	
	private boolean saveOnModify = true;
	
	
	public MethodsAndOpsController(Parameters params) {
		this.params = params;
	}
	
	public void saveOnModify(boolean b) {
		this.saveOnModify = b;
	}
	
	public void init() {
		disable();
		clearMethodsList();
		clearOperatorsList();
	}
	
	public void setMethodsElems(List methodsList, Button methodsAddButton, Button methodsDelButton, Button methodsClearButton) {
		this.methodsList = methodsList;
		this.methodsAddButton = methodsAddButton;
		this.methodsDelButton = methodsDelButton;
		this.methodsClearButton = methodsClearButton;
	}
	
	public void setOperatorsElems(List operatorsList, Button operatorsAddButton, Button operatorsDelButton, Button operatorsClearButton) {
		this.operatorsList = operatorsList;
		this.operatorsAddButton = operatorsAddButton;
		this.operatorsDelButton = operatorsDelButton;
		this.operatorsClearButton = operatorsClearButton;
	}
	
	public void setAllowElems(Button allowFieldMutationsButton, Button allowClassMutationsButton) {
		this.allowFieldMutationsButton = allowFieldMutationsButton;
		this.allowClassMutationsButton = allowClassMutationsButton;
	}
	
	public void disable() {
		//methods list
		methodsList.setEnabled(false);
		methodsAddButton.setEnabled(false);
		methodsDelButton.setEnabled(false);
		methodsClearButton.setEnabled(false);
		
		//operators list
		operatorsList.setEnabled(false);
		operatorsAddButton.setEnabled(false);
		operatorsDelButton.setEnabled(false);
		operatorsClearButton.setEnabled(false);
		
		//allow field and class mutations
		allowFieldMutationsButton.setEnabled(false);
		allowFieldMutationsButton.setSelection(false);
		allowClassMutationsButton.setEnabled(false);
		allowClassMutationsButton.setSelection(false);
	}
	
	public void enable() {
		methodsAddButton.setEnabled(true);
		operatorsAddButton.setEnabled(true);
		allowFieldMutationsButton.setEnabled(true);
		allowClassMutationsButton.setEnabled(true);
	}
	
	public void clearMethods() {
		methodsList.setItems(new String[] {});
		this.params.setMethods(new LinkedList<String>());
		this.methodsClearButton.setEnabled(false);
		this.methodsList.setEnabled(false);
		this.methodsDelButton.setEnabled(false);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void clearOperators() {
		operatorsList.setItems(new String[] {});
		this.params.setOperators(new LinkedList<String>());
		this.operatorsClearButton.setEnabled(false);
		this.operatorsList.setEnabled(false);
		this.operatorsDelButton.setEnabled(false);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void clearMethodsList() {
		methodsList.setItems(new String[] {});
		this.methodsList.setEnabled(false);
	}
	
	public void clearOperatorsList() {
		operatorsList.setItems(new String[] {});
		this.operatorsList.setEnabled(false);
	}
	
	public void updateMethods(java.util.List<String> methods) {
		methodsList.setItems(methods.toArray(new String[methods.size()]));
		methodsList.setEnabled(!methods.isEmpty());
		this.methodsClearButton.setEnabled(!methods.isEmpty());
		this.methodsDelButton.setEnabled(false);
		this.methodSelected = null;
		this.methodsList.setEnabled(!methods.isEmpty());
		this.params.setMethods(methods);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateOperators(java.util.List<String> operators) {
		operatorsList.setItems(operators.toArray(new String[operators.size()]));
		operatorsList.setEnabled(!operators.isEmpty());
		this.operatorsClearButton.setEnabled(!operators.isEmpty());
		this.operatorsDelButton.setEnabled(false);
		this.operatorSelected = null;
		this.operatorsList.setEnabled(!operators.isEmpty());
		this.params.setOperators(operators);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateAllowFieldMutations(boolean allow) {
		this.params.setAllowFieldMutations(allow);
		this.allowFieldMutationsButton.setSelection(allow);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateAllowClassMutations(boolean allow) {
		this.params.setAllowClassMutations(allow);
		this.allowClassMutationsButton.setSelection(allow);
		if (this.saveOnModify) this.updater.update();
	}
	
	public void setMethodSelected(String m) {
		if (m == null) {
			this.methodSelected = null;
			this.methodsDelButton.setEnabled(false);
		} else {
			this.methodSelected = m;
			this.methodsDelButton.setEnabled(true);
		}
	}
	
	public void setOperatorSelected(String o) {
		if (o == null) {
			this.operatorSelected = null;
			this.operatorsDelButton.setEnabled(false);
		} else {
			this.operatorSelected = o;
			this.operatorsDelButton.setEnabled(true);
		}
	}
	
	public void deleteMethod() {
		this.methodsList.remove(methodSelected);
		this.methodSelected = null;
		this.methodsDelButton.setEnabled(false);
		this.methodsClearButton.setEnabled(this.methodsList.getItemCount() > 0);
		this.methodsList.setEnabled(this.methodsList.getItemCount() > 0);
		this.params.setMethods(Arrays.asList(this.methodsList.getItems()));
		if (this.saveOnModify) this.updater.update();
	}
	
	public void deleteOperator() {
		this.operatorsList.remove(operatorSelected);
		this.operatorSelected = null;
		this.operatorsDelButton.setEnabled(false);
		this.operatorsClearButton.setEnabled(this.operatorsList.getItemCount() > 0);
		this.operatorsList.setEnabled(this.operatorsList.getItemCount() > 0);
		this.params.setOperators(Arrays.asList(this.operatorsList.getItems()));
		if (this.saveOnModify) this.updater.update();
	}

	public void setUpdater(Updater methodsAndOperatorsUpdater) {
		this.updater = methodsAndOperatorsUpdater;
	}
	
}
