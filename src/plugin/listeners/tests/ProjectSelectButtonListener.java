package plugin.listeners.tests;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.TestsController;

public class ProjectSelectButtonListener implements SelectionListener {
	protected TestsController tc;

	
	public ProjectSelectButtonListener(TestsController tc) {
		this.tc = tc;
	}
	
	public void setTestsController(TestsController tc) {
		this.tc = tc;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		update(selectProject());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	public void update(IProject f) {
		this.tc.updateTestsProject(f);
		if (f != null) {
			setTestsBinDir();
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
		dialog.setTitle("Select tests project");
		dialog.setElements(allProjects);
		dialog.setMultipleSelection(false);
		dialog.open();
		Object[] result = dialog.getResult();
		if (result != null) {
			return ((IProject)result[0]);
		}
		return null;
	}
	
	private void setTestsBinDir() {
		IResource[] allResources = null;
		try {
			allResources = MuJavaPlugin.getParameters().getTestsProject().members();
			if (allResources == null) {
				return;
			}
			List<IFolder> folders = new LinkedList<IFolder>();
			for (IResource f : allResources) {
				recursiveAdd(f, folders);
			}
			for (IFolder f : folders) {
				if (f.getProjectRelativePath().toOSString().compareToIgnoreCase("bin")==0) {
					this.tc.updateTestBinDir(f);
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
