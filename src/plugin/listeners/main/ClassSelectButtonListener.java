package plugin.listeners.main;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.MainController;

public class ClassSelectButtonListener implements SelectionListener {
	protected MainController mainController;
	private String title;
	protected Text text;

	
	public ClassSelectButtonListener(String t) {
		this.title = t;
	}
	
	public void setMainController(MainController mc) {
		this.mainController = mc;
	}
	
	public void setText(Text t) {
		this.text = t;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		update(selectJavaClass());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		//update(selectDirectory());
	}
	
	public void update(IJavaElement f) {
		this.mainController.updateClazz(f);
	}
	
	private IJavaElement selectJavaClass() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new ClassLabelProvider());
		IJavaProject javaProject = JavaCore.create(MuJavaPlugin.getParameters().getRoot());
		List<IJavaElement> classes = new LinkedList<IJavaElement>();
		IPackageFragment[] packages;
		try {
			packages = javaProject.getPackageFragments();
			for (IPackageFragment mypackage : packages) {
				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
					for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			             IType[] types = unit.getTypes();
			             for (IType t : types) {
			            	 if (t.isClass()) {
			            		 classes.add(t);
			            	 }
			             }
					}
				}
			}
			dialog.setTitle(this.title);
			dialog.setElements(classes.toArray());
			dialog.setMultipleSelection(false);
			dialog.open();
			Object[] result = dialog.getResult();
			if (result == null) {
				return null;
			} else {
				return ((IJavaElement)result[0]);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	class ClassLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			IType t = (IType) element;
			boolean isAbstract = false;
			try {
				isAbstract = Flags.isAbstract(t.getFlags());
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return t.getFullyQualifiedName() + (isAbstract?" - abstract class":"");
		}
		
	}

}
