package plugin;

import org.eclipse.core.runtime.Status;

import plugin.ConfigurationManager.TAB;

public class MuJavaPluginStatus extends Status {

	public static final int MISSING_PARAMS = 0x16;
	
	public MuJavaPluginStatus(int severity, String pluginId, int code, String message, Throwable exception) {
		super(severity, MuJavaPlugin.PLUGIN_ID, code, message, exception);
	}

	public MuJavaPluginStatus(int severity, String pluginId, String message, Throwable exception) {
		super(severity, MuJavaPlugin.PLUGIN_ID, message, exception);
	}

	public MuJavaPluginStatus(int severity, String pluginId, String message) {
		super(severity, MuJavaPlugin.PLUGIN_ID, message);
	}
	
	public MuJavaPluginStatus(int code, TAB tab) {
		super(code == MISSING_PARAMS?ERROR:OK, MuJavaPlugin.PLUGIN_ID, (code == MISSING_PARAMS?("Missing parameters for " + tab.name() + " in configuration"):""));
	}


}
