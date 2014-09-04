package plugin.listeners.tests;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.LabelProvider;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.TestsController;

public class ButtonListener implements SelectionListener {
	public static enum BUTTON_TYPE {ADD, DEL, CLEAR}
	protected TestsController tc;
	private String title = "Select tests to run";
	private BUTTON_TYPE bt;

	
	public ButtonListener(BUTTON_TYPE bt, TestsController tc) {
		this.tc = tc;
		this.bt = bt;
	}
	
	public void setController(TestsController tc) {
		this.tc = tc;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		switch (this.bt) {
		case ADD: this.tc.updateTests(selectTests()); break;
		case CLEAR: this.tc.clearTests();break;
		case DEL: this.tc.deleteTest();break;
		default:
			break;
			
		}
	}

	private List<IJavaElement> selectTests() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new TestLabelProvider());
		IJavaProject javaProject = JavaCore.create(MuJavaPlugin.getParameters().getTestsProject());
		IPackageFragment[] packages;
		List<IJavaElement> classes = new LinkedList<IJavaElement>();
		List<IJavaElement> selectedTests = new LinkedList<IJavaElement>();
		try {
			packages = javaProject.getPackageFragments();
			for (IPackageFragment mypackage : packages) {
				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
					for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			             IType[] types = unit.getTypes();
			             for (IType t : types) {
			            	 if (t.isClass() && !(Flags.isAbstract(t.getFlags())) && isTestClass(t)) {
			            		 classes.add(t);
			            	 }
			             }
					}
				}
			}
			dialog.setTitle(this.title);
			dialog.setElements(classes.toArray());
			dialog.setMultipleSelection(true);
			dialog.open();
			Object[] result = dialog.getResult();
			if (result != null) {
				for (Object o : result) {
					selectedTests.add((IJavaElement) o);
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedTests;
	}

	private boolean isTestClass(IType t) throws JavaModelException {
		Annotation run_with = Annotation.RUN_WITH;
		Annotation test = Annotation.TEST;
		try {
			return run_with.annotatesTypeOrSuperTypes(t) || test.annotatesTypeOrSuperTypes(t);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	
	class TestLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			IType t = (IType) element;
			return t.getFullyQualifiedName();
		}
	
	}
	
	private static class Annotation {
		 
		 		private static final Annotation RUN_WITH= new Annotation("org.junit.runner.RunWith", "RunWith", true);
		 		private static final Annotation TEST= new Annotation("org.junit.Test", "Test", false);
		 		private boolean runWith;
		 
		 		private final String fName;
		 		private final String name;
		 		private List<String> validImports = new LinkedList<String>();
		 
		 		private Annotation(String fname, String name, boolean runWith) {
		 			this.fName= fname;
		 			this.name = name;
		 			this.runWith = runWith;
		 			String[] importSegments = this.fName.split("\\.");
		 			String currentImport = importSegments[0];
		 			for (int s = 1; s < importSegments.length; s++) {
		 				this.validImports.add(currentImport+".*");
		 				currentImport += "." + importSegments[s];
		 			}
		 		}
		 		
		 		
		 		private boolean importFound(IImportDeclaration[] iImportDeclarations) {
		 			for (IImportDeclaration id : iImportDeclarations) {
 						if (id.isOnDemand()) {
 							for (String validImport : this.validImports) {
 								if (id.getElementName().compareTo(validImport)==0) {
	 								return true;
	 							}
 							}
 						} else {
 							if (id.getElementName().compareTo(fName)==0) {
 								return true;
 							}
 						}
 					}
		 			return false;
		 		}
		 
		 
		 		private boolean annotates(IAnnotation[] annotations, IImportDeclaration[] iImportDeclarations) {
		 			for (int i= 0; i < annotations.length; i++) {
		 				String annotationName = annotations[i].getElementName();
		 				boolean fNameMatched = annotationName != null && (annotationName.equals(fName));
		 				boolean nameMatched = annotationName != null && (annotationName.equals(name));
		 				if (fNameMatched) {
		 					return true;
		 				} else if (nameMatched && iImportDeclarations != null) {
		 					if (importFound(iImportDeclarations)) {
		 						return true;
		 					}
		 				} else if (nameMatched) {
		 					return true;
		 				}
		 			}
		 			return  false;
		 		}
		 		
		 
		 		public boolean annotatesTypeOrSuperTypes(IType type) throws CoreException {
		 			while (type != null) {
		 				ICompilationUnit cu = type.getCompilationUnit();
		 				IImportDeclaration[] iImportDeclarations = cu==null?null:cu.getImports();
		 				if ((runWith && annotates(type.getAnnotations(), iImportDeclarations)) || (!runWith && annotatesAtLeastOneMethod(type, iImportDeclarations))) {
		 					return true;
		 				}
		 				type = getSupertypeOf(type, new NullProgressMonitor());
		 			}
		 			return false;
		 		}
		 		
		 		private static IType getSupertypeOf(final IType type, IProgressMonitor monitor) throws CoreException {
		 		    return type.newSupertypeHierarchy(monitor).getSuperclass(type);
		 		}
		 
		 		private boolean annotatesAtLeastOneMethod(IType type, IImportDeclaration[] iImportDeclarations) throws CoreException {
		 			while (type != null) {
		 				IMethod[] declaredMethods= type.getMethods();
		 				for (int i= 0; i < declaredMethods.length; i++) {
		 					IMethod curr= declaredMethods[i];
							if (annotates(curr.getAnnotations(), iImportDeclarations)) {
								return true;
							}
						}
						type = getSupertypeOf(type, new NullProgressMonitor());
					}
					return false;
				}
			}

}
