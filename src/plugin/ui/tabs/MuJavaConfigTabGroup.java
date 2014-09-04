package plugin.ui.tabs;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import plugin.MuJavaPlugin;

public class MuJavaConfigTabGroup extends AbstractLaunchConfigurationTabGroup {

	public MuJavaConfigTabGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new MuJavaMainConfigTab(),
				new MuJavaMethodsAndOperatorsConfigTab(),
				new MuJavaTestsConfigTab(),
				new CommonTab()
		};
		setTabs(tabs);
		MuJavaPlugin.setConfigTabGroupRunned(true);
	}

}
