package plugin;


import java.io.IOException;
import java.net.URL;

import mujava.app.MutatorsInfo;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugin.ui.controllers.MainController;
import plugin.ui.controllers.MethodsAndOpsController;
import plugin.ui.controllers.TestsController;



public class MuJavaPlugin extends AbstractUIPlugin {
	  public static final String PLUGIN_ID = "MuJavapp";
	  public static final String LAUNCH_CONFIG_TYPE = "MuJava++.launchType";
	  private static Parameters params;
	  private static ConfigurationManager configManager;
	  public static final MutatorsInfo mi = MutatorsInfo.getInstance();
	  public static MainController mc;
	  public static MethodsAndOpsController moc;
	  public static TestsController tc;
	  private static MuJavaConsole console;
	  private static boolean configTabGroupRunned = false;
	  private static final IPath MUJAVA_JAR = new Path("lib/mujava++.jar");
	  private static ConfigReader configReader;


	  private static MuJavaPlugin plugin = null;


	  private static boolean isStopped = false;

	  public MuJavaPlugin() {
		  plugin = this;
		  params = new Parameters();
		  params.cleanAll();
		  mc = new MainController(params);
		  moc = new MethodsAndOpsController(params);
		  tc = new TestsController(params);
		  mc.setMethodsAndOperatorsController(moc);
		  mc.setTestsController(tc);
		  configManager = new ConfigurationManager(params, mc, moc, tc);
		  console = new MuJavaConsole();
	  }
	  
	  public static boolean configTabGroupRunned() {
		  return configTabGroupRunned;
	  }
	  
	  public static void setConfigTabGroupRunned(boolean b) {
		  configTabGroupRunned = b;
	  }
	  
	  public static MuJavaConsole getConsole() {
		  return console;
	  }
	  
	  public static ConfigurationManager getConfigurationManager() {
		  return configManager;
	  }
	  
	  public static Parameters getParameters() {
		  return params;
	  }
	  

	  public static MuJavaPlugin getDefault() {
	    return plugin;
	  }


	  @Override
	  public void start(BundleContext context) throws Exception {
	    super.start(context);
	    plugin = this;
	  }

	  @Override
	  public void stop(BundleContext context) throws Exception {
	    plugin = null;
	    isStopped = true;
	    super.stop(context);
	  }


	  public static boolean isStopped() {
	    return isStopped;
	  }

	  public static String getPluginId() {
	    return PLUGIN_ID;
	  }

	  public static void log(IStatus status) {
	    getDefault().getLog().log(status);
	  }
	  
	  public static IWorkbench workbench() {
		  return plugin.getWorkbench();
	  }

	  public static Shell getActiveWorkbenchShell() {
	    IWorkbenchWindow workBenchWindow = getActiveWorkbenchWindow();
	    if (workBenchWindow == null)
	      return null;
	    return workBenchWindow.getShell();
	  }
	  
	  public static IWorkbenchWindow getActiveWorkbenchWindow() {
	    if (plugin == null)
	      return null;
	    IWorkbench workBench = plugin.getWorkbench();
	    if (workBench == null)
	      return null;
	    return workBench.getActiveWorkbenchWindow();
	  }


	  public static Display getDisplay() {
	    Display display;
	    display = Display.getCurrent();
	    if (display == null)
	      display = Display.getDefault();
	    return display;
	  }
	  
	  public static IPath getMuJavaPath() {
		  try {
			return getFullPath(MUJAVA_JAR);
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  return null;
	  }
	  
	  private static IPath getFullPath(IPath localPath) throws IOException {
		  Bundle bundle = Platform.getBundle(getPluginId());
		  URL url = FileLocator.find(bundle, localPath, null);
		  url = FileLocator.toFileURL(url);
		  return new Path(url.getPath());
	  }
	  
	  public static IPath getjUnitLocation() {
		  Bundle muJavaBundle = Platform.getBundle(PLUGIN_ID);
		  IPath configPath;
		  String configFolderFullPath;
		  try {
			  configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
			  configPath = new Path(configFolderFullPath);
			  configReader = ConfigReader.newInstance(configPath.addTrailingSeparator().toOSString()+"config.xml");
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
		  IPath jUnitPath = new Path(configReader.getValue("jUnitPath"));
		  return jUnitPath;
	  }
	  
	  public static IPath getHamcrestLocation() {
		  Bundle muJavaBundle = Platform.getBundle(PLUGIN_ID);
		  IPath configPath;
		  String configFolderFullPath;
		  try {
			  configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
			  configPath = new Path(configFolderFullPath);
			  configReader = ConfigReader.newInstance(configPath.addTrailingSeparator().toOSString()+"config.xml");
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
		  IPath hamcrestPath = new Path(configReader.getValue("hamcrestPath"));
		  return hamcrestPath;
	  }
	  
	  public static IPath getGuavaLocation() {
		  Bundle muJavaBundle = Platform.getBundle(PLUGIN_ID);
		  IPath configPath;
		  String configFolderFullPath;
		  try {
			  configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
			  configPath = new Path(configFolderFullPath);
			  configReader = ConfigReader.newInstance(configPath.addTrailingSeparator().toOSString()+"config.xml");
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
		  IPath guavaPath = new Path(configReader.getValue("guava"));
		  return guavaPath;
	  }
	  
	  public static IPath getJavassistLocation() {
		  Bundle muJavaBundle = Platform.getBundle(PLUGIN_ID);
		  IPath configPath;
		  String configFolderFullPath;
		  try {
			  configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
			  configPath = new Path(configFolderFullPath);
			  configReader = ConfigReader.newInstance(configPath.addTrailingSeparator().toOSString()+"config.xml");
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
		  IPath javassistPath = new Path(configReader.getValue("javassist"));
		  return javassistPath;
	  }
	  
	  public static IPath getReflectionsLocation() {
		  Bundle muJavaBundle = Platform.getBundle(PLUGIN_ID);
		  IPath configPath;
		  String configFolderFullPath;
		  try {
			  configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
			  configPath = new Path(configFolderFullPath);
			  configReader = ConfigReader.newInstance(configPath.addTrailingSeparator().toOSString()+"config.xml");
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  
		  IPath reflectionsPath = new Path(configReader.getValue("reflections"));
		  return reflectionsPath;
	  }

}
