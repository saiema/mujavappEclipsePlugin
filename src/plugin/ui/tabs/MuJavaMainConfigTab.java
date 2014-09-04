package plugin.ui.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.widgets.Composite;

import plugin.ConfigurationManager;
import plugin.MuJavaPlugin;
import plugin.ui.MainTabUI;

public class MuJavaMainConfigTab extends AbstractLaunchConfigurationTab {
	
	private Updater mainUpdater = new Updater() {
		@Override
		public void update() {
			scheduleUpdateJob();
		}
	};
	
	@Override
	public void createControl(Composite parent) {
		MainTabUI mainTab = new MainTabUI(parent);
		setControl(mainTab.getMainTab(mainUpdater));
	}


	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().setDefaults(configuration, ConfigurationManager.TAB.MAIN);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			MuJavaPlugin.getConfigurationManager().initializeFrom(configuration, ConfigurationManager.TAB.MAIN);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().performApply(configuration, ConfigurationManager.TAB.MAIN);
	}

	@Override
	public String getName() {
		return "Main";
	}
	

}
