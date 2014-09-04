package plugin.ui.controllers;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import plugin.Parameters;
import plugin.ui.tabs.Updater;


public class MainController {
	private Parameters params;
	private Text outputDir;
	private Text rootDir;
	private Text srcDir; private Button srcButton;
	private Text binDir; private Button binButton;
	private Text clazz; private Button clazzButton;
	private Button runTests;
	
	private boolean srcDirSet;
	private boolean binDirSet;
	
	private MethodsAndOpsController moc;
	private TestsController tc;
	
	private boolean saveOnModify = true;
	private boolean isLoading = false;
	private Updater updater;
	
	public MainController(Parameters params) {
		this.params = params;
		this.srcDirSet = false;
		this.binDirSet = false;
	}
	
	public void saveOnModify(boolean b) {
		this.saveOnModify = b;
	}
	
	public void loading(boolean b) {
		this.isLoading = b;
	}
	
	public void setMethodsAndOperatorsController(MethodsAndOpsController moc) {
		this.moc = moc;
	}
	
	public void setTestsController(TestsController tc) {
		this.tc = tc;
	}
	
	public void setOutputElem(Text outputDir) {
		this.outputDir = outputDir;
	}
	
	public void setRootElem(Text rootDir) {
		this.rootDir = rootDir;
	}
	
	public void setSrcElems(Text srcDir, Button srcButton) {
		this.srcDir = srcDir;
		this.srcButton = srcButton;
		this.srcButton.setEnabled(false);
	}
	
	public void setBinElems(Text binDir, Button binButton) {
		this.binDir = binDir;
		this.binButton = binButton;
		this.binButton.setEnabled(false);
	}
	
	public void setClazzElems(Text clazz, Button clazzButton) {
		this.clazz = clazz;
		this.clazzButton = clazzButton;
		this.clazzButton.setEnabled(false);
	}
	
	public void setRunTestsElem(Button runTests) {
		this.runTests = runTests;
		this.runTests.setEnabled(false);
	}
	
	public void updateRoot(IProject root) {
		this.params.setRoot(root);
		this.rootDir.setText(root==null?"":root.getLocation().toOSString());
		this.srcDir.setText("");
		this.binDir.setText("");
		this.srcButton.setEnabled(root != null);
		this.binButton.setEnabled(root != null);
		this.clazz.setText("");
		this.clazzButton.setEnabled(false);
		this.srcDirSet = false;
		this.binDirSet = false;
		if (!this.isLoading) {
			this.runTests.setEnabled(false);
			this.runTests.setSelection(false);
			this.moc.disable();
			this.moc.clearMethods();
			this.moc.clearOperators();
			this.moc.updateAllowClassMutations(false);
			this.moc.updateAllowFieldMutations(false);
			this.tc.clearTests();
			this.tc.updateTestsProject(null);
			this.tc.updateTestBinDir(null);
			this.tc.disable();
		}
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateSrcDir(IFolder src) {
		this.params.setSrc(src);
		this.srcDir.setText(src==null?"":src.getProjectRelativePath().toOSString());
		this.srcDirSet = src != null;
		if (this.binDirSet) {
			this.clazzButton.setEnabled(true);
		} else {
			this.clazzButton.setEnabled(false);
		}
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateBinDir(IFolder bin) {
		this.params.setBin(bin);
		this.binDir.setText(bin==null?"":bin.getProjectRelativePath().toOSString());
		this.binDirSet = bin != null;
		if (this.srcDirSet) {
			this.clazzButton.setEnabled(true);
		} else {
			this.clazzButton.setEnabled(false);
		}
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateClazz(IJavaElement clazz) {
		this.params.setClazz(clazz);
		this.clazz.setText(clazz==null?"":((IType)clazz).getFullyQualifiedName());
		boolean saveBackup = this.saveOnModify;
		saveOnModify(false);
		if (!this.isLoading) updateRunTests(false);
		saveOnModify(saveBackup);
		this.runTests.setEnabled(clazz != null);
		if (!this.isLoading) {
			this.moc.clearMethods();
			this.moc.clearOperators();
		}
		if (clazz == null) {
			this.moc.disable();
			this.moc.updateAllowClassMutations(false);
			this.moc.updateAllowFieldMutations(false);
		} else {
			this.moc.enable();
		}
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateRunTests(boolean runTests) {
		this.params.setRunTests(runTests);
		this.runTests.setSelection(runTests);
		if (!this.isLoading) {
			this.tc.clearTests();
			this.tc.updateTestsProject(null);
			this.tc.updateTestBinDir(null);
		}
		if (runTests) this.tc.enable();
		else this.tc.disable();
		if (this.saveOnModify) this.updater.update();
	}
	
	public void updateOutputDir(File f) {
		this.params.setOutput(f);
		this.outputDir.setText(f==null?"":f.toString());
		if (this.saveOnModify) this.updater.update();
	}

	public void setUpdater(Updater mainUpdater) {
		this.updater = mainUpdater;
	}
	
}
