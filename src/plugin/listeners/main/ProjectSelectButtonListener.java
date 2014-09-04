package plugin.listeners.main;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.MainController;

public class ProjectSelectButtonListener implements SelectionListener {
	public static enum MODE {FILE, DIRECTORY};
	public static enum FILTER {NONE, JAVA, TEST, CLASS}
	private MainController mainController;

	public ProjectSelectButtonListener() {
		
	}
	
	public void setMainController(MainController mc) {
		this.mainController = mc;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		update(selectProject());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	private void update(IProject p) {
		this.mainController.updateRoot(p);
		if (p != null) {
			setSrcAndBinDirectories();
		}
	}
	
	private IProject selectProject() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> openProjects = new LinkedList<IProject>();
		for (IProject p : allProjects) {
			if (p.isAccessible()) {
				openProjects.add(p);
			}
		}
		dialog.setTitle("Select a project");
		dialog.setElements(allProjects);
		dialog.setMultipleSelection(false);
		dialog.open();
		Object[] result = dialog.getResult();
		if (result != null) {
			return ((IProject)result[0]);
		}
		return null;
	}
	
	
	private void setSrcAndBinDirectories() {
		IResource[] allResources = null;
		try {
			allResources = MuJavaPlugin.getParameters().getRoot().members();
			if (allResources == null) {
				return;
			}
			List<IFolder> folders = new LinkedList<IFolder>();
			for (IResource f : allResources) {
				recursiveAdd(f, folders);
			}
			for (IFolder f : folders) {
				if (f.getProjectRelativePath().toOSString().compareToIgnoreCase("src")==0) {
					this.mainController.updateSrcDir(f);
				} else if (f.getProjectRelativePath().toOSString().compareToIgnoreCase("bin")==0) {
					this.mainController.updateBinDir(f);
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void recursiveAdd(IResource f, List<IFolder> folders) throws CoreException {
		if (f instanceof IFolder) {
			folders.add((IFolder) f);
			for (IResource r : ((IFolder) f).members()) {
				recursiveAdd(r, folders);
			}
		}
	}


}
