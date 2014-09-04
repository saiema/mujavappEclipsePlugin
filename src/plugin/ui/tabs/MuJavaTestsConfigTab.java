package plugin.ui.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.widgets.Composite;

import plugin.ConfigurationManager;
import plugin.MuJavaPlugin;
import plugin.ui.TestsTabUI;

public class MuJavaTestsConfigTab extends AbstractLaunchConfigurationTab {
	
	private Updater testsUpdater = new Updater() {
		@Override
		public void update() {
			scheduleUpdateJob();
		}
	};
	
	
	@Override
	public void createControl(Composite parent) {
		TestsTabUI testsTab = new TestsTabUI(parent);
		setControl(testsTab.getTestsTab(testsUpdater));
	}


	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().setDefaults(configuration, ConfigurationManager.TAB.TESTS);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			MuJavaPlugin.getConfigurationManager().initializeFrom(configuration, ConfigurationManager.TAB.TESTS);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().performApply(configuration, ConfigurationManager.TAB.TESTS);
	}

	@Override
	public String getName() {
		return "Tests";
	}
	

}
