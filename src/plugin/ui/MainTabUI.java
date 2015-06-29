package plugin.ui;


import java.io.File;

import plugin.MuJavaPlugin;
import plugin.listeners.main.ClassSelectButtonListener;
import plugin.listeners.main.DirSelectButtonListener;
import plugin.listeners.main.ProjectSelectButtonListener;
import plugin.listeners.main.RunTestsSelectButtonListener;
import plugin.ui.controllers.MainController;
import plugin.ui.tabs.Updater;

import org.eclipse.core.resources.IFolder;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class MainTabUI {
	private Composite parent;

	public MainTabUI(Composite parent) {
		this.parent = parent;
	}
	
	public Composite getMainTab(Updater mainUpdater) {
		MainController mc = MuJavaPlugin.mc;
		mc.setUpdater(mainUpdater);
		Composite mainTab = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		DirSelectButtonListener outputListener = new DirSelectButtonListener("Select output folder") {
			
			
			@Override
			public void update(IFolder f) {}
			
			private void update(File f) {
				this.mainController.updateOutputDir(f);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				update(selectDirectory());
			}
			
			private File selectDirectory() {
				DirectoryDialog fd = new DirectoryDialog(MuJavaPlugin.getActiveWorkbenchShell());
				fd.setText("Select output folder");
				String outputDir = fd.open();
				File file = null;
				if (outputDir != null) {
					file = new File(outputDir);
				}
				return file;
			}
			
		};
		outputListener.setMainController(mc);
		ProjectSelectButtonListener rootListener = new ProjectSelectButtonListener();
		rootListener.setMainController(mc);
		DirSelectButtonListener srcListener = new DirSelectButtonListener("Select source folder") {
			
			@Override
			public void update(IFolder f) {
				this.mainController.updateSrcDir(f);
			}
			
		};
		srcListener.setMainController(mc);
		DirSelectButtonListener binListener = new DirSelectButtonListener("Select binary folder") {
			
			@Override
			public void update(IFolder f) {
				this.mainController.updateBinDir(f);
			}
			
		};
		binListener.setMainController(mc);
		ClassSelectButtonListener clazzListener = new ClassSelectButtonListener("Select a class");
		clazzListener.setMainController(mc);
		RunTestsSelectButtonListener runTestsListener = new RunTestsSelectButtonListener();
		runTestsListener.setMainController(mc);
		
		Composite outputComposite = SWTFactory.createComposite(mainTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label outputLabel = SWTFactory.createLabel(outputComposite, "Output directory", 1);
		((GridData)outputLabel.getLayoutData()).widthHint = 120;
		Text outputText = SWTFactory.createSingleText(outputComposite, 1);
		outputText.setEditable(false);
		outputText.setEnabled(false);
		mc.setOutputElem(outputText);
		Button outputButton = SWTFactory.createPushButton(outputComposite, "browse", null);
		outputButton.addSelectionListener(outputListener);
		
		Composite rootComposite = SWTFactory.createComposite(mainTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label rootLabel = SWTFactory.createLabel(rootComposite, "Root directory", 1);
		((GridData)rootLabel.getLayoutData()).widthHint = 120;
		Text rootText = SWTFactory.createSingleText(rootComposite, 1);
		rootText.setEditable(false);
		rootText.setEnabled(false);
		Button rootButton = SWTFactory.createPushButton(rootComposite, "browse", null);
		mc.setRootElem(rootText);
		rootButton.addSelectionListener(rootListener);
		
		Composite sourceComposite = SWTFactory.createComposite(mainTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label sourceLabel = SWTFactory.createLabel(sourceComposite, "Source directory", 1);
		((GridData)sourceLabel.getLayoutData()).widthHint = 120;
		Text sourceText = SWTFactory.createSingleText(sourceComposite, 1);
		sourceText.setEditable(false);
		sourceText.setEnabled(false);
		Button sourceButton = SWTFactory.createPushButton(sourceComposite, "browse", null);
		mc.setSrcElems(sourceText, sourceButton);
		sourceButton.addSelectionListener(srcListener);
		
		Composite binComposite = SWTFactory.createComposite(mainTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label binLabel = SWTFactory.createLabel(binComposite, "Binary directory", 1);
		((GridData)binLabel.getLayoutData()).widthHint = 120;
		Text binText = SWTFactory.createSingleText(binComposite, 1);
		binText.setEditable(false);
		binText.setEnabled(false);
		Button binButton = SWTFactory.createPushButton(binComposite, "browse", null);
		mc.setBinElems(binText, binButton);
		binButton.addSelectionListener(binListener);
		
		Composite classComposite = SWTFactory.createComposite(mainTab, 3, 1, GridData.FILL_HORIZONTAL);
		Label classLabel = SWTFactory.createLabel(classComposite, "Class", 1);
		((GridData)classLabel.getLayoutData()).widthHint = 120;
		Text classText = SWTFactory.createSingleText(classComposite, 1);
		classText.setEditable(false);
		classText.setEnabled(false);
		Button clazzButton = SWTFactory.createPushButton(classComposite, "browse", null);
		mc.setClazzElems(classText, clazzButton);
		clazzButton.addSelectionListener(clazzListener);
		
		
		Composite runTestsComposite = SWTFactory.createComposite(mainTab, 1, 1, GridData.FILL_HORIZONTAL);
		Button runTests = SWTFactory.createCheckButton(runTestsComposite, "Run tests and calculate mutation score", null, false, 1);
		mc.setRunTestsElem(runTests);
		runTests.addSelectionListener(runTestsListener);
		
		return mainTab;
	}
	
	
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	public void initializeFrom(ILaunchConfiguration configuration) {

	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		
	}
	
	
	
}
