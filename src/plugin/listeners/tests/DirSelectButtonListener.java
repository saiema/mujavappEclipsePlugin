package plugin.listeners.tests;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.jface.viewers.LabelProvider;

import plugin.MuJavaPlugin;
import plugin.ui.controllers.TestsController;

public class DirSelectButtonListener implements SelectionListener {
	private TestsController tc;
	private String title = "Select binary folder";

	
	public DirSelectButtonListener(TestsController tc) {
		this.tc = tc;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		update(selectDirectory());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	public void update(IFolder f) {
		this.tc.updateTestBinDir(f);
	}
	
	private IFolder selectDirectory() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(MuJavaPlugin.getActiveWorkbenchShell(), new IFolderLabelProvider());
		IResource[] allResources = null;
		try {
			allResources = MuJavaPlugin.getParameters().getTestsProject().members();
			if (allResources == null) {
				return null;
			}
			List<IFolder> folders = new LinkedList<IFolder>();
			for (IResource f : allResources) {
				recursiveAdd(f, folders);
			}
			dialog.setTitle(this.title);
			dialog.setElements(folders.toArray());
			dialog.setMultipleSelection(false);
			dialog.open();
			Object[] result = dialog.getResult();
			if (result == null) {
				return null;
			} else {
				return ((IFolder)result[0]);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void recursiveAdd(IResource f, List<IFolder> folders) throws CoreException {
		if (f instanceof IFolder) {
			folders.add((IFolder) f);
			for (IResource r : ((IFolder) f).members()) {
				recursiveAdd(r, folders);
			}
		}
	}
	
	
	class IFolderLabelProvider extends LabelProvider{
		
		public String getText(Object element) {
			IFolder f = (IFolder) element;
			return f.getProjectRelativePath().toOSString();
		}
	
	}

}
