package plugin.listeners.methodsAndOperators;

import java.util.LinkedList;
import java.util.List;

import mujava.api.Mutant;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.LabelProvider;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.MethodsAndOpsController;

public class ButtonListener implements SelectionListener {
	public static enum BUTTON_TYPE {ADD, DEL, CLEAR, CHECK}
	public static enum SOURCE {METHODS, OPERATORS, ALLOW_CLASS_MUTATIONS, ALLOW_FIELD_MUTATIONS}
	protected MethodsAndOpsController moc;
	private String title;
	private BUTTON_TYPE bt;
	private SOURCE s;

	
	public ButtonListener(BUTTON_TYPE bt, SOURCE s, MethodsAndOpsController moc) {
		this.moc = moc;
		this.bt = bt;
		this.s = s;
		if (this.bt == BUTTON_TYPE.ADD) {
			switch (this.s) {
				case METHODS : this.title = "Select the methods to mutate"; break;
				case OPERATORS : this.title = "Select the operators to use"; break;
				default: break;
			}
		}
	}
	
	public void setController(MethodsAndOpsController moc) {
		this.moc = moc;
	}
	
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void widgetSelected(SelectionEvent e) {
		switch (this.bt) {
		case ADD: {
				switch (this.s) {
					case METHODS: this.moc.updateMethods(selectMethods()); break;
					case OPERATORS: this.moc.updateOperators(selectOperators()); break;
				}
				break;
			}
		case CHECK: {
				switch(this.s) {
					case ALLOW_CLASS_MUTATIONS : this.moc.updateAllowClassMutations(((Button)e.getSource()).getSelection()); break;
					case ALLOW_FIELD_MUTATIONS : this.moc.updateAllowFieldMutations(((Button)e.getSource()).getSelection()); break;
				}
 				break;
			}
		case CLEAR: {
				switch(this.s) {
					case METHODS: this.moc.clearMethods(); break;
					case OPERATORS: this.moc.clearOperators(); break;
				}
				break;
			}
		case DEL: {
				switch(this.s) {
					case METHODS: this.moc.deleteMethod(); break;
					case OPERATORS: this.moc.deleteOperator(); break;
				}
				break;
			}
		default:
			break;
			
		}
	}

	private List<String> selectOperators() {
		List<Mutant> operators = MuJavaPlugin.mi.allOps();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new MutantLabelProvider());
		dialog.setTitle(this.title);
		dialog.setElements(operators.toArray());
		dialog.setMultipleSelection(true);
		dialog.setSize(140, 40);
		dialog.open();
		Object[] result = dialog.getResult();
		List<String> ops = new LinkedList<String>();
		if (result != null) {
			for (Object o : result) {
				ops.add(((Mutant)o).name());
			}
		}
		return ops;
	}

	private List<String> selectMethods() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new StringLabelProvider());
		IJavaElement clazz = MuJavaPlugin.getParameters().getClazz();
		List<String> selectedMethods = new LinkedList<String>();
		List<String> methods = new LinkedList<String>();
		try {
			ICompilationUnit cu = (ICompilationUnit) clazz.getParent();
			IType[] classes = cu.getTypes();
			for (IType t : classes) {
				if (t.isClass() && t.getElementName().compareTo(clazz.getElementName())==0) {
					for (IMethod m : t.getMethods()) {
						if (!methods.contains(m.getElementName())) {
							methods.add(m.getElementName());
						}
					}
				}
			}
			dialog.setTitle(this.title);
			dialog.setElements(methods.toArray());
			dialog.setMultipleSelection(true);
			dialog.open();
			Object[] result = dialog.getResult();
			if (result != null) {
				for (Object o : result) {
					selectedMethods.add((String)o);
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedMethods;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	
	class MutantLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			Mutant m = (Mutant) element;
			return m.name() + " - " + MuJavaPlugin.mi.getFullDescription(m).replaceAll("[\t\n ]+", " ");
		}
	
	}
	
	class StringLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			return (String) element;
		}
	
	}

}
