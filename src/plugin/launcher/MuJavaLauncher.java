package plugin.launcher;

import java.io.PrintStream;

import mujava.app.Console;
import mujava.app.Core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.osgi.framework.Bundle;

import plugin.MuJavaConsole;
import plugin.MuJavaPlugin;
import plugin.Parameters;
import plugin.ValueAdapter;

public class MuJavaLauncher extends AbstractJavaLaunchConfigurationDelegate {
	
	

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (ILaunchManager.RUN_MODE.compareTo(mode)==0) {
			Bundle muJavaBundle = Platform.getBundle("MuJavapp");
			String pluginVersion = muJavaBundle==null?"Bundle not found!":(muJavaBundle.getHeaders().get("Bundle-Version"));
			int mujavaCoreVersion = Core.mujavappVersion;
			
			if (!MuJavaPlugin.configTabGroupRunned()) MuJavaPlugin.getConfigurationManager().loadConfigurationToParameters(configuration);
			Parameters params = MuJavaPlugin.getParameters();
			MuJavaConsole console = MuJavaPlugin.getConsole();
			console.showConsole();
			
			boolean configIsValid = MuJavaPlugin.getParameters().validate();
			if (!configIsValid) {
				console.write("Parameters are not valid... aborting launch");
				return;
			}
			
			String root = params.getRoot().getLocation().toOSString();
			String clazz = ValueAdapter.iJavaElementToString(params.getClazz());
			String srcDir = ValueAdapter.iFolderToString(params.getSrc());
			String binDir = ValueAdapter.iFolderToString(params.getBin());
			String outDir = ValueAdapter.fileToString(params.getOutput());
			String methods = ValueAdapter.stringListToString(params.getMethods());
			String operators = ValueAdapter.stringListToString(params.getOperators());
			String allowFieldMutations = ValueAdapter.booleanToString(params.allowFieldMutations());
			String allowClassMutations = ValueAdapter.booleanToString(params.allowClassMutations());
			String runTests = ValueAdapter.booleanToString(params.runTests());
			String testsProject = "N/A";
			String testsBinDir = "N/A";
			String tests = "N/A";
			if (params.runTests()) {
				testsProject = params.getTestsProject().getLocation().toOSString();
				testsBinDir = params.getTestsBinFolder().getLocation().toOSString();
				tests = ValueAdapter.iJavaElementListToString(params.getTests());
			}
			
//			IPath configPath;
//			String configFolderFullPath;
//			try {
//				configFolderFullPath = FileLocator.getBundleFile(muJavaBundle).getParent().toString();
//				configPath = new Path(configFolderFullPath);
//				console.write("Config folder : " + configPath.addTrailingSeparator().toOSString() + "\n");
//				console.write("Config file path : " + configPath.addTrailingSeparator().toOSString() + "config.xml" + "\n");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			
			String hamcrestLocation = MuJavaPlugin.getHamcrestLocation().toOSString();
			String jUnitLocation = MuJavaPlugin.getjUnitLocation().toOSString();
			
			
			console.write("MuJava++\n================================================================================================\n");
			console.write("Core version : " + mujavaCoreVersion + " | Plugin version : " + pluginVersion + '\n');
			console.write("jUnit location : " + jUnitLocation + '\n');
			console.write("hamcrest location : " + hamcrestLocation + '\n');
			console.write("Root project : " + root + '\n');
			console.write("Selected class (class to mutate) : " + clazz + '\n');
			console.write("Source directory : " + srcDir + '\n');
			console.write("Binary directory : " + binDir + '\n');
			console.write("Output directory : " + outDir + '\n');
			console.write("Methods to mutate : " + methods + '\n');
			console.write("Mutation operators selected : " + operators + '\n');
			console.write("Allow field mutations : " + allowFieldMutations + '\n');
			console.write("Allow class mutations : " + allowClassMutations + '\n');
			console.write("Run tests : " + runTests + '\n');
			console.write("Tests project : " + testsProject + '\n');
			console.write("Tests binary folder : " + testsBinDir + '\n');
			console.write("Tests to run : " + tests + '\n');
			console.write("================================================================================================\n");
			
			System.setOut(new PrintStream(console.getOutputStream()));
			MuJavaPlugin.setConfigTabGroupRunned(false);
			IVMInstall vm = verifyVMInstall(configuration);
			IVMRunner runner = vm.getVMRunner(mode);
			IPath mujavaPath = MuJavaPlugin.getMuJavaPath();
			console.write("mujava++ path: " + (mujavaPath!=null?mujavaPath.toOSString():"NOT FOUND!") + '\n');
			
			String mujavaArguments = MuJavaPlugin.getParameters().translateToMuJavaArguments();
			console.write("Calling mujava.app.Console with arguments\n"+mujavaArguments+"\n");
			
			String rootDir = params.getRoot().getLocation().addTrailingSeparator().toOSString();
			String testDir = params.runTests()?params.getTestsProject().getFullPath().addTrailingSeparator().toOSString():"";
			String srcPath = rootDir+srcDir;
			String binPath = rootDir+binDir;
			String[] classpath;
			if (testDir.isEmpty()) {
				classpath = new String[] {mujavaPath!=null?adjustMuJavaPath(mujavaPath).toOSString():"", hamcrestLocation, jUnitLocation, rootDir, srcPath, binPath};
			} else {
				classpath = new String[] {mujavaPath!=null?adjustMuJavaPath(mujavaPath).toOSString():"", hamcrestLocation, jUnitLocation, rootDir, testDir, srcPath, binPath};
			}
			
			VMRunnerConfiguration runConfig = new VMRunnerConfiguration(Console.class.getName(), classpath);
			String[] args = mujavaArguments.split(" ");
			runConfig.setProgramArguments(args);
			runConfig.setWorkingDirectory(rootDir);
			console.write("Working directory: " + runConfig.getWorkingDirectory() + "\n");
			runner.run(runConfig, launch, monitor);
		} else {
			abort("Mujava++ plugin only support run mode", null, 0);
		}
		
	}
	
	private IPath adjustMuJavaPath(IPath original) {
		if (original.getFileExtension() == null) {
			return original.addTrailingSeparator();
		}
		return original;
	}
	


}
