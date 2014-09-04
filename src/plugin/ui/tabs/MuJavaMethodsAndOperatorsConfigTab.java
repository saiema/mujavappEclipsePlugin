package plugin.ui.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.widgets.Composite;

import plugin.ConfigurationManager;
import plugin.MuJavaPlugin;
import plugin.ui.MethodsAndOperatorsTabUI;

public class MuJavaMethodsAndOperatorsConfigTab extends AbstractLaunchConfigurationTab {
	
	private Updater methodsAndOperatorsUpdater = new Updater() {
		@Override
		public void update() {
			scheduleUpdateJob();
		}
	};

	@Override
	public void createControl(Composite parent) {
		MethodsAndOperatorsTabUI methodsAndOperatorsTab = new MethodsAndOperatorsTabUI(parent);
		setControl(methodsAndOperatorsTab.getMethodsAndOperatorsTab(methodsAndOperatorsUpdater));
	}


	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().setDefaults(configuration, ConfigurationManager.TAB.METHODS_AND_OPERATORS);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			MuJavaPlugin.getConfigurationManager().initializeFrom(configuration, ConfigurationManager.TAB.METHODS_AND_OPERATORS);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		MuJavaPlugin.getConfigurationManager().performApply(configuration, ConfigurationManager.TAB.METHODS_AND_OPERATORS);
	}

	@Override
	public String getName() {
		return "methods and operators";
	}
	

}
