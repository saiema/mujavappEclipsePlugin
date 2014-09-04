package plugin.ui.controllers;

import java.util.LinkedList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import plugin.Parameters;
import plugin.ui.tabs.Updater;

public class TestsController {
	private Parameters params;
	private Text testsProject; private Button testsProjectButton;
	private Text testsBinDir; private Button testsBinDirButton;
	private List testsList; private Button testsAddButton, testsDelButton, testsClearButton;
	
	private String testSelected;
	private java.util.List<IJavaElement> tests;
	private Updater updater;
	
	private boolean saveOnModify = true;
	
	public TestsController(Parameters params) {
		this.params = params;
	}
	
	public void saveOnModify(boolean b) {
		this.saveOnModify = b;
	}
	
	public void init() {
		disable();
		clearTestsList();
	}
	
	public void setTestsProjectElems(Text testsProject, Button testsProjectButton) {
		this.testsProject = testsProject;
		this.testsProjectButton = testsProjectButton;
	}
	
	public void setTestsBinFolderElems(Text testsBinDir, Button testsBinDirButton) {
		this.testsBinDir = testsBinDir;
		this.testsBinDirButton = testsBinDirButton;
	}
	
	public void setTestsListElems(List testsList, Button testsAddButton, Button testsDelButton, Button testsClearButton) {
		this.testsList = testsList;
		this.testsAddButton = testsAddButton;
		this.testsDelButton = testsDelButton;
		this.testsClearButton = testsClearButton;
	}
	
	public void disable() {
		this.testsList.setEnabled(false);
		this.testsAddButton.setEnabled(false);
		this.testsDelButton.setEnabled(false);
		this.testsClearButton.setEnabled(false);
		this.testsProjectButton.setEnabled(false);
		this.testsBinDirButton.setEnabled(false);
	}
	
	public void enable() {
		this.testsProjectButton.setEnabled(true);
	}
	
	public void clearTests() {
		this.tests = new LinkedList<IJavaElement>();
		updateTests(this.tests);
	}
	
	public void updateTests(java.util.List<IJavaElement> tests) {
		this.tests = tests;
		this.params.setTests(this.tests);
		this.testsList.setItems(makeStringArray(tests));
		this.testsClearButton.setEnabled(!tests.isEmpty());
		this.testsDelButton.setEnabled(false);
		this.testSelected = null;
		this.testsList.setEnabled(!tests.isEmpty());
		if (this.saveOnModify) this.updater.update();
	}

	private String[] makeStringArray(java.util.List<IJavaElement> tests) {
		String[] testsArray = new String[tests.size()];
		for (int t = 0; t < tests.size(); t++) {
			testsArray[t] = ((IType)tests.get(t)).getFullyQualifiedName();
		}
		return testsArray;
	}
	
	public void deleteTest() {
		findAndDelete(this.testSelected);
		updateTests(this.tests);
		setTestSelected(null);
	}

	private void findAndDelete(String test) {
		for (int t = 0; t < this.tests.size(); t++) {
			IType currentTest = ((IType)this.tests.get(t));
			if (currentTest.getFullyQualifiedName().compareTo(test)==0) {
				this.tests.remove(t);
				return;
			}
		}
	}

	private void clearTestsList() {
		this.testsList.setItems(new String[] {});
	}
	
	public void setTestSelected(String t) {
		if (t == null) {
			this.testsDelButton.setEnabled(false);
		} else {
			this.testsDelButton.setEnabled(true);
		}
		this.testSelected = t;
	}
	
	public void updateTestsProject(IProject f) {
		this.params.setTestsProject(f);
		this.testsProject.setText(f==null?"":f.getLocation().toOSString());
		updateTestBinDir(null);
		this.testsBinDirButton.setEnabled(f != null);
		clearTests();
		this.testsAddButton.setEnabled(f != null);
		if (this.saveOnModify) this.updater.update();
	}

	public void setUpdater(Updater testsUpdater) {
		this.updater = testsUpdater;
	}

	public void updateTestBinDir(IFolder f) {
		this.params.setTestsBinFolder(f);
		this.testsBinDir.setText(f==null?"":f.getProjectRelativePath().toOSString());
	}
	
}
