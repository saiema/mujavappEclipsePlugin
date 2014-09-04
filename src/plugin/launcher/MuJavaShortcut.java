package plugin.launcher;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import plugin.MuJavaPlugin;
import plugin.ValueAdapter;
import plugin.ConfigurationManager.CONFIG_PARAM;
import plugin.ConfigurationManager.TAB;

@SuppressWarnings("restriction")
public class MuJavaShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			searchAndLaunch(((IStructuredSelection)selection).toArray(), mode);
		} 
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
        IJavaElement javaElement = (IJavaElement) input.getAdapter(IJavaElement.class);
        if (javaElement != null) {
        	searchAndLaunch(new Object[] {javaElement}, mode);
        } 
	}

	
	protected void searchAndLaunch(Object[] search, String mode) {
        IType selectedType = null;
        Object selectedObject = search[0];
        if (selectedObject instanceof CompilationUnit) {
        	selectedType = ((CompilationUnit)selectedObject).findPrimaryType();
        }
		launch(selectedType, "run");
    }

    protected void launch(IType type, String mode) {
    	ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
    	
    	
    	//params
    	IProject project = type.getResource().getProject();
    	IFolder[] srcAndBin = searchSrcAndBinDirectories(project);
    	IFolder srcFolder;
    	IFolder binFolder;
    	IType clazz = type;
    	
    	//check src folder
    	if (srcAndBin[0] != null) {
    		srcFolder = srcAndBin[0];
    	} else {
    		srcFolder = askForFolder(project, "Select source folder");
    	}
    	
    	//check bin folder
    	if (srcAndBin[1] != null) {
    		binFolder = srcAndBin[1];
    	} else {
    		binFolder = askForFolder(project, "Select binary folder");
    	}
    	
		try {
			String configName=project.getName()+"_"+clazz.getFullyQualifiedName()+"-config";
			ILaunchConfiguration config = getConfig(configName);
			ILaunchConfigurationType lct = mgr.getLaunchConfigurationType(MuJavaPlugin.LAUNCH_CONFIG_TYPE);
	    	boolean setData = false;
	    	if (config == null) {
	    		lct = mgr.getLaunchConfigurationType(MuJavaPlugin.LAUNCH_CONFIG_TYPE);
	    		config = lct.newInstance(null, configName);
		    	setData = true;
	    	}
	    	ILaunchConfigurationWorkingCopy configWC = config.getWorkingCopy();
			if (setData) {
	    		setMainConfig(configWC, project, srcFolder, binFolder, clazz);
				setDefaultsMethodsAndOperators(configWC);
				setDefaultsTests(configWC);
			}
			DebugUITools.openLaunchConfigurationPropertiesDialog(MuJavaPlugin.getActiveWorkbenchShell(), configWC, "MuJava++.launchGroup");
			configWC.doSave();
			DebugUITools.launch(configWC, "run");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    
    private ILaunchConfiguration getConfig(String configName) {
    	ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
    	try {
			ILaunchConfiguration[] configs = mgr.getLaunchConfigurations();
			for (ILaunchConfiguration c : configs) {
				if (c.getType().getIdentifier().compareTo(MuJavaPlugin.LAUNCH_CONFIG_TYPE) == 0
						&& c.getName().compareTo(configName) == 0) {
					return c;
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private IFolder askForFolder(IProject project, String title) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new IFolderLabelProvider());
		IResource[] allResources = null;
		try {
			allResources = project.members();
			if (allResources == null) {
				return null;
			}
			List<IFolder> folders = new LinkedList<IFolder>();
			for (IResource f : allResources) {
				recursiveAdd(f, folders);
			}
			dialog.setTitle(title);
			dialog.setElements(folders.toArray());
			dialog.setMultipleSelection(false);
			dialog.open();
			Object[] result = dialog.getResult();
			while(((result = dialog.getResult())==null)) {dialog.setTitle(title+" YOU MUST SELECT A FOLDER!");}
			return ((IFolder)result[0]);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    class IFolderLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			IFolder f = (IFolder) element;
			return f.getProjectRelativePath().toOSString();
		}
	
	}

	private IFolder[] searchSrcAndBinDirectories(IProject project) {
		IResource[] allResources = null;
		IFolder[] srcAndBin = new IFolder[2];
		try {
			allResources = project.members();
			if (allResources != null) {
				List<IFolder> folders = new LinkedList<IFolder>();
				for (IResource f : allResources) {
					recursiveAdd(f, folders);
				}
				for (IFolder f : folders) {
					if (f.getProjectRelativePath().toOSString().compareToIgnoreCase("src")==0) {
						srcAndBin[0] = f;
					} else if (f.getProjectRelativePath().toOSString().compareToIgnoreCase("bin")==0) {
						srcAndBin[1] = f;
					}
				}
			}
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return srcAndBin;
	}
	
	private void recursiveAdd(IResource f, List<IFolder> folders) throws CoreException {
		if (f instanceof IFolder) {
			folders.add((IFolder) f);
			for (IResource r : ((IFolder) f).members()) {
				recursiveAdd(r, folders);
			}
		}
	}
	
	private void setMainConfig(ILaunchConfigurationWorkingCopy configuration, IProject project, IFolder srcFolder, IFolder binFolder, IType clazz) {
		Map<String, Object> mainParams = new HashMap<String, Object>();
		mainParams.put(CONFIG_PARAM.OUTPUT.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.OUTPUT), CONFIG_PARAM.OUTPUT));
		mainParams.put(CONFIG_PARAM.ROOT.name(), adaptObjectToString(project, CONFIG_PARAM.ROOT));
		mainParams.put(CONFIG_PARAM.SRC.name(), adaptObjectToString(srcFolder, CONFIG_PARAM.SRC));
		mainParams.put(CONFIG_PARAM.BIN.name(), adaptObjectToString(binFolder, CONFIG_PARAM.BIN));
		mainParams.put(CONFIG_PARAM.CLASS.name(), adaptObjectToString((IJavaElement)clazz, CONFIG_PARAM.CLASS));
		mainParams.put(CONFIG_PARAM.RUN_TESTS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.RUN_TESTS), CONFIG_PARAM.RUN_TESTS));
		configuration.setAttribute(TAB.MAIN.name(), mainParams);
	}
	
	private void setDefaultsMethodsAndOperators(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> methodsAndOperatorsParams = new HashMap<String, Object>();
		methodsAndOperatorsParams.put(CONFIG_PARAM.METHODS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.METHODS), CONFIG_PARAM.METHODS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.OPERATORS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.OPERATORS), CONFIG_PARAM.OPERATORS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS));
		configuration.setAttribute(TAB.METHODS_AND_OPERATORS.name(), methodsAndOperatorsParams);
	}
	
	private void setDefaultsTests(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> testsParams = new HashMap<String, Object>();
		testsParams.put(CONFIG_PARAM.TESTS_ROOT.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS_ROOT), CONFIG_PARAM.TESTS_ROOT));
		testsParams.put(CONFIG_PARAM.TESTS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS), CONFIG_PARAM.TESTS));
		testsParams.put(CONFIG_PARAM.TESTS_BIN.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS_BIN), CONFIG_PARAM.TESTS_BIN));
		configuration.setAttribute(TAB.TESTS.name(), testsParams);
	}
	
	@SuppressWarnings("unchecked")
	private String adaptObjectToString(Object value, CONFIG_PARAM cp) {
		switch(cp) {
			case ALLOW_CLASS_MUTATIONS: 
			case ALLOW_FIELD_MUTATIONS: 
			case RUN_TESTS: return ValueAdapter.booleanToString((boolean) value);
			case SRC:
			case TESTS_BIN:
			case BIN: return ValueAdapter.iFolderToString((IFolder) value);
			case METHODS:
			case OPERATORS: return ValueAdapter.stringListToString((List<String>) value);
			case CLASS: return ValueAdapter.iJavaElementToString((IJavaElement) value);
			case OUTPUT: return ValueAdapter.fileToString((File) value);
			case ROOT:
			case TESTS_ROOT: return ValueAdapter.iProjectToString((IProject) value);
			case TESTS: return ValueAdapter.iJavaElementListToString((List<IJavaElement>) value);
		}
		return null;
	}
	
	public Object getDefaultValue(CONFIG_PARAM cp) {
		switch (cp) {
			case ALLOW_CLASS_MUTATIONS:
			case ALLOW_FIELD_MUTATIONS:
			case RUN_TESTS : return false;
			case OUTPUT:
			case ROOT:
			case SRC:
			case BIN:
			case CLASS:
			case TESTS_BIN:
			case TESTS_ROOT: return null;
			case METHODS:
			case OPERATORS: return new LinkedList<String>();
			case TESTS: return new LinkedList<IJavaElement>();
		}
		return null;
	}
	
}
