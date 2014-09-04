package plugin;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;

import plugin.ui.controllers.MainController;
import plugin.ui.controllers.MethodsAndOpsController;
import plugin.ui.controllers.TestsController;

public class ConfigurationManager {
	public static enum CONFIG_PARAM {
		OUTPUT,
		ROOT,
		SRC,
		BIN,
		CLASS,
		RUN_TESTS,
		METHODS,
		OPERATORS,
		ALLOW_FIELD_MUTATIONS,
		ALLOW_CLASS_MUTATIONS,
		TESTS_ROOT,
		TESTS_BIN,
		TESTS
	}
	public static enum TAB {
		MAIN,
		METHODS_AND_OPERATORS,
		TESTS
	}
	private Parameters params;
	private MainController mc;
	private MethodsAndOpsController moc;
	private TestsController tc;
	
	public ConfigurationManager(Parameters params, MainController mc, MethodsAndOpsController moc, TestsController tc) {
		this.params = params;
		this.mc = mc;
		this.moc = moc;
		this.tc = tc;
	}
	
	public Object getDefaultValue(CONFIG_PARAM cp) {
		switch (cp) {
			case ALLOW_CLASS_MUTATIONS:
			case ALLOW_FIELD_MUTATIONS:
			case RUN_TESTS : return false;
			case OUTPUT:
			case ROOT:
			case SRC:
			case BIN:
			case CLASS:
			case TESTS_BIN:
			case TESTS_ROOT: return null;
			case METHODS:
			case OPERATORS: return new LinkedList<String>();
			case TESTS: return new LinkedList<IJavaElement>();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void saveValue(Object value, CONFIG_PARAM cp) {
		switch (cp) {
			case ALLOW_CLASS_MUTATIONS: {
				boolean bolValue = (boolean) value;
				this.moc.updateAllowClassMutations(bolValue);
				break;
			}
			case ALLOW_FIELD_MUTATIONS: {
				boolean bolValue = (boolean) value;
				this.moc.updateAllowFieldMutations(bolValue);
				break;
			}
			case BIN: {
				IFolder folderValue = (IFolder) value;
				this.mc.updateBinDir(folderValue);
				break;
			}
			case CLASS: {
				IJavaElement classValue = (IJavaElement) value;
				this.mc.updateClazz(classValue);
				break;
			}
			case METHODS: {
				List<String> stringListValue = (List<String>) value;
				this.moc.updateMethods(stringListValue);
				break;
			}
			case OPERATORS: {
				List<String> stringListValue = (List<String>) value;
				this.moc.updateOperators(stringListValue);
				break;
			}
			case OUTPUT: {
				File fileValue = (File) value;
				this.mc.updateOutputDir(fileValue);
				break;
			}
			case ROOT: {
				IProject projectValue = (IProject) value;
				this.mc.updateRoot(projectValue);
				break;
			}
			case RUN_TESTS: {
				boolean bolValue = (boolean) value;
				this.mc.updateRunTests(bolValue);
				break;
			}
			case SRC: {
				IFolder folderValue = (IFolder) value;
				this.mc.updateSrcDir(folderValue);
				break;
			}
			case TESTS: {
				List<IJavaElement> classesListValue = (List<IJavaElement>) value;
				this.tc.updateTests(classesListValue);
				break;
			}
			case TESTS_ROOT: {
				IProject projectValue = (IProject) value;
				this.tc.updateTestsProject(projectValue);
				break;
			}
			case TESTS_BIN: {
				IFolder testsBinDir = (IFolder) value;
				this.tc.updateTestBinDir(testsBinDir);
				break;
			}
		}
	}
	
	public Object getValue(CONFIG_PARAM cp) {
		switch (cp) {
			case ALLOW_CLASS_MUTATIONS:	return this.params.allowClassMutations();
			case ALLOW_FIELD_MUTATIONS: return this.params.allowFieldMutations();
			case BIN: 					return this.params.getBin();
			case CLASS: 				return this.params.getClazz();
			case METHODS: 				return this.params.getMethods();
			case OPERATORS: 			return this.params.getOperators();
			case OUTPUT: 				return this.params.getOutput();
			case ROOT: 					return this.params.getRoot();
			case RUN_TESTS: 			return this.params.runTests();
			case SRC: 					return this.params.getSrc();
			case TESTS: 				return this.params.getTests();
			case TESTS_BIN:				return this.params.getTestsBinFolder();
			case TESTS_ROOT: 			return this.params.getTestsProject();
			default: 					return null;
		}
	}
	
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration, TAB tab) {
		switch (tab) {
			case MAIN: 					setDefaultsMain(configuration); break;
			case METHODS_AND_OPERATORS: setDefaultsMethodsAndOperators(configuration); break;
			case TESTS: 				setDefaultsTests(configuration); break;
		}
	}
	
	private void setDefaultsMain(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> mainParams = new HashMap<String, Object>();
		mainParams.put(CONFIG_PARAM.OUTPUT.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.OUTPUT), CONFIG_PARAM.OUTPUT));
		mainParams.put(CONFIG_PARAM.ROOT.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.ROOT), CONFIG_PARAM.ROOT));
		mainParams.put(CONFIG_PARAM.SRC.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.SRC), CONFIG_PARAM.SRC));
		mainParams.put(CONFIG_PARAM.BIN.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.BIN), CONFIG_PARAM.BIN));
		mainParams.put(CONFIG_PARAM.CLASS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.CLASS), CONFIG_PARAM.CLASS));
		mainParams.put(CONFIG_PARAM.RUN_TESTS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.RUN_TESTS), CONFIG_PARAM.RUN_TESTS));
		configuration.setAttribute(TAB.MAIN.name(), mainParams);
	}
	
	private void setDefaultsMethodsAndOperators(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> methodsAndOperatorsParams = new HashMap<String, Object>();
		methodsAndOperatorsParams.put(CONFIG_PARAM.METHODS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.METHODS), CONFIG_PARAM.METHODS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.OPERATORS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.OPERATORS), CONFIG_PARAM.OPERATORS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS));
		configuration.setAttribute(TAB.METHODS_AND_OPERATORS.name(), methodsAndOperatorsParams);
	}
	
	private void setDefaultsTests(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> testsParams = new HashMap<String, Object>();
		testsParams.put(CONFIG_PARAM.TESTS_ROOT.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS_ROOT), CONFIG_PARAM.TESTS_ROOT));
		testsParams.put(CONFIG_PARAM.TESTS.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS), CONFIG_PARAM.TESTS));
		testsParams.put(CONFIG_PARAM.TESTS_BIN.name(), adaptObjectToString(getDefaultValue(CONFIG_PARAM.TESTS_BIN), CONFIG_PARAM.TESTS_BIN));
		configuration.setAttribute(TAB.TESTS.name(), testsParams);
	}

	public void initializeFrom(ILaunchConfiguration configuration, TAB tab) throws CoreException {
		switch (tab) {
			case MAIN: 					initializeFromMain(configuration); break;
			case METHODS_AND_OPERATORS: initializeFromMethodsAndOperators(configuration); break;
			case TESTS: 				initializeFromTests(configuration); break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initializeFromMain(ILaunchConfiguration configuration) throws CoreException {
		this.mc.saveOnModify(false);
		this.moc.saveOnModify(false);
		this.tc.saveOnModify(false);
		this.mc.loading(true);
		Map<String, String> mainParams = configuration.getAttribute(TAB.MAIN.name(), (Map<String, String>)null);
		if (mainParams == null) {
			throw new CoreException(new MuJavaPluginStatus(MuJavaPluginStatus.MISSING_PARAMS, TAB.MAIN));
		}
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.OUTPUT.name()), CONFIG_PARAM.OUTPUT), CONFIG_PARAM.OUTPUT);
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.ROOT.name()), CONFIG_PARAM.ROOT), CONFIG_PARAM.ROOT);
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.SRC.name()), CONFIG_PARAM.SRC), CONFIG_PARAM.SRC);
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.BIN.name()), CONFIG_PARAM.BIN), CONFIG_PARAM.BIN);
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.CLASS.name()), CONFIG_PARAM.CLASS), CONFIG_PARAM.CLASS);
		saveValue(adaptStringToObject(mainParams.get(CONFIG_PARAM.RUN_TESTS.name()), CONFIG_PARAM.RUN_TESTS), CONFIG_PARAM.RUN_TESTS);
		this.mc.saveOnModify(true);
		this.moc.saveOnModify(true);
		this.tc.saveOnModify(true);
		this.mc.loading(false);
	}
	
	@SuppressWarnings("unchecked")
	private void initializeFromMethodsAndOperators(ILaunchConfiguration configuration) throws CoreException {
		this.mc.saveOnModify(false);
		this.moc.saveOnModify(false);
		this.tc.saveOnModify(false);
		this.mc.loading(true);
		Map<String, String> methodsAndOperatorsParams = configuration.getAttribute(TAB.METHODS_AND_OPERATORS.name(), (Map<String, String>)null);
		if (methodsAndOperatorsParams == null) {
			throw new CoreException(new MuJavaPluginStatus(MuJavaPluginStatus.MISSING_PARAMS, TAB.METHODS_AND_OPERATORS));
		}
		saveValue(adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.METHODS.name()), CONFIG_PARAM.METHODS), CONFIG_PARAM.METHODS);
		saveValue(adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.OPERATORS.name()), CONFIG_PARAM.OPERATORS), CONFIG_PARAM.OPERATORS);
		saveValue(adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS.name()), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS);
		saveValue(adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS.name()), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS);
		this.mc.saveOnModify(true);
		this.moc.saveOnModify(true);
		this.tc.saveOnModify(true);
		this.mc.loading(false);
	}

	@SuppressWarnings("unchecked")
	private void initializeFromTests(ILaunchConfiguration configuration) throws CoreException {
		this.mc.saveOnModify(false);
		this.moc.saveOnModify(false);
		this.tc.saveOnModify(false);
		this.mc.loading(true);
		Map<String, String> testsParams = configuration.getAttribute(TAB.TESTS.name(), (Map<String, String>)null);
		if (testsParams == null) {
			throw new CoreException(new MuJavaPluginStatus(MuJavaPluginStatus.MISSING_PARAMS, TAB.TESTS));
		}
		saveValue(adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS_ROOT.name()), CONFIG_PARAM.TESTS_ROOT), CONFIG_PARAM.TESTS_ROOT);
		saveValue(adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS.name()), CONFIG_PARAM.TESTS), CONFIG_PARAM.TESTS);
		saveValue(adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS_BIN.name()), CONFIG_PARAM.TESTS_BIN), CONFIG_PARAM.TESTS_BIN);
		this.mc.saveOnModify(true);
		this.moc.saveOnModify(true);
		this.tc.saveOnModify(true);
		this.mc.loading(false);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration, TAB tab) {
		switch (tab) {
			case MAIN: 					performApplyMain(configuration); break;
			case METHODS_AND_OPERATORS: performApplyMethodsAndOperators(configuration); break;
			case TESTS: 				performApplyTests(configuration); break;
		}
//		performApplyMain(configuration);
//		performApplyMethodsAndOperators(configuration);
//		performApplyTests(configuration);
	}
	
	private void performApplyMain(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> mainParams = new HashMap<String, Object>();
		mainParams.put(CONFIG_PARAM.OUTPUT.name(), adaptObjectToString(getValue(CONFIG_PARAM.OUTPUT), CONFIG_PARAM.OUTPUT));
		mainParams.put(CONFIG_PARAM.ROOT.name(), adaptObjectToString(getValue(CONFIG_PARAM.ROOT), CONFIG_PARAM.ROOT));
		mainParams.put(CONFIG_PARAM.SRC.name(), adaptObjectToString(getValue(CONFIG_PARAM.SRC), CONFIG_PARAM.SRC));
		mainParams.put(CONFIG_PARAM.BIN.name(), adaptObjectToString(getValue(CONFIG_PARAM.BIN), CONFIG_PARAM.BIN));
		mainParams.put(CONFIG_PARAM.CLASS.name(), adaptObjectToString(getValue(CONFIG_PARAM.CLASS), CONFIG_PARAM.CLASS));
		mainParams.put(CONFIG_PARAM.RUN_TESTS.name(), adaptObjectToString(getValue(CONFIG_PARAM.RUN_TESTS), CONFIG_PARAM.RUN_TESTS));
		configuration.setAttribute(TAB.MAIN.name(), mainParams);
	}
	
	private void performApplyMethodsAndOperators(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> methodsAndOperatorsParams = new HashMap<String, Object>();
		methodsAndOperatorsParams.put(CONFIG_PARAM.METHODS.name(), adaptObjectToString(getValue(CONFIG_PARAM.METHODS), CONFIG_PARAM.METHODS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.OPERATORS.name(), adaptObjectToString(getValue(CONFIG_PARAM.OPERATORS), CONFIG_PARAM.OPERATORS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS.name(), adaptObjectToString(getValue(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS));
		methodsAndOperatorsParams.put(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS.name(), adaptObjectToString(getValue(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS));
		configuration.setAttribute(TAB.METHODS_AND_OPERATORS.name(), methodsAndOperatorsParams);
	}
	
	private void performApplyTests(ILaunchConfigurationWorkingCopy configuration) {
		Map<String, Object> testsParams = new HashMap<String, Object>();
		testsParams.put(CONFIG_PARAM.TESTS_ROOT.name(), adaptObjectToString(getValue(CONFIG_PARAM.TESTS_ROOT), CONFIG_PARAM.TESTS_ROOT));
		testsParams.put(CONFIG_PARAM.TESTS.name(), adaptObjectToString(getValue(CONFIG_PARAM.TESTS), CONFIG_PARAM.TESTS));
		testsParams.put(CONFIG_PARAM.TESTS_BIN.name(), adaptObjectToString(getValue(CONFIG_PARAM.TESTS_BIN), CONFIG_PARAM.TESTS_BIN));
		configuration.setAttribute(TAB.TESTS.name(), testsParams);
	}
	
	@SuppressWarnings("unchecked")
	private String adaptObjectToString(Object value, CONFIG_PARAM cp) {
		switch(cp) {
			case ALLOW_CLASS_MUTATIONS: 
			case ALLOW_FIELD_MUTATIONS: 
			case RUN_TESTS: return ValueAdapter.booleanToString((boolean) value);
			case SRC:
			case TESTS_BIN:
			case BIN: return ValueAdapter.iFolderToString((IFolder) value);
			case METHODS:
			case OPERATORS: return ValueAdapter.stringListToString((List<String>) value);
			case CLASS: return ValueAdapter.iJavaElementToString((IJavaElement) value);
			case OUTPUT: return ValueAdapter.fileToString((File) value);
			case ROOT:
			case TESTS_ROOT: return ValueAdapter.iProjectToString((IProject) value);
			case TESTS: return ValueAdapter.iJavaElementListToString((List<IJavaElement>) value);
		}
		return null;
	}
	
	private Object adaptStringToObject(String value, CONFIG_PARAM cp) {
		switch(cp) {
			case ALLOW_CLASS_MUTATIONS: 
			case ALLOW_FIELD_MUTATIONS: 
			case RUN_TESTS: return ValueAdapter.stringToBoolean(value);
			case SRC:
			case BIN: return ValueAdapter.stringToIFolder(value, this.params.getRoot());
			case TESTS_BIN: return ValueAdapter.stringToIFolder(value, this.params.getTestsProject());
			case METHODS:
			case OPERATORS: return ValueAdapter.stringToStringList(value);
			case CLASS: return ValueAdapter.stringToIJavaElement(value, this.params.getRoot());
			case OUTPUT: return ValueAdapter.stringToFile(value);
			case ROOT:
			case TESTS_ROOT: return ValueAdapter.stringToIProject(value);
			case TESTS: return ValueAdapter.stringToIJavaElementList(value, this.params.getTestsProject());
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public void loadConfigurationToParameters(ILaunchConfiguration configuration) throws CoreException {
		Map<String, String> mainParams = configuration.getAttribute(TAB.MAIN.name(), (Map<String, String>)null);
		params.setOutput((File) adaptStringToObject(mainParams.get(CONFIG_PARAM.OUTPUT.name()), CONFIG_PARAM.OUTPUT));
		params.setRoot((IProject) adaptStringToObject(mainParams.get(CONFIG_PARAM.ROOT.name()), CONFIG_PARAM.ROOT));
		params.setSrc((IFolder) adaptStringToObject(mainParams.get(CONFIG_PARAM.SRC.name()), CONFIG_PARAM.SRC));
		params.setBin((IFolder) adaptStringToObject(mainParams.get(CONFIG_PARAM.BIN.name()), CONFIG_PARAM.BIN));
		params.setClazz((IJavaElement) adaptStringToObject(mainParams.get(CONFIG_PARAM.CLASS.name()), CONFIG_PARAM.CLASS));
		params.setRunTests((boolean) adaptStringToObject(mainParams.get(CONFIG_PARAM.RUN_TESTS.name()), CONFIG_PARAM.RUN_TESTS));
		Map<String, String> methodsAndOperatorsParams = configuration.getAttribute(TAB.METHODS_AND_OPERATORS.name(), (Map<String, String>)null);
		params.setMethods((List<String>) adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.METHODS.name()), CONFIG_PARAM.METHODS));
		params.setOperators((List<String>) adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.OPERATORS.name()), CONFIG_PARAM.OPERATORS));
		params.setAllowClassMutations((boolean) adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.ALLOW_CLASS_MUTATIONS.name()), CONFIG_PARAM.ALLOW_CLASS_MUTATIONS));
		params.setAllowFieldMutations((boolean) adaptStringToObject(methodsAndOperatorsParams.get(CONFIG_PARAM.ALLOW_FIELD_MUTATIONS.name()), CONFIG_PARAM.ALLOW_FIELD_MUTATIONS));
		Map<String, String> testsParams = configuration.getAttribute(TAB.TESTS.name(), (Map<String, String>)null);
		params.setTestsProject((IProject) adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS_ROOT.name()), CONFIG_PARAM.TESTS_ROOT));
		params.setTests((List<IJavaElement>) adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS.name()), CONFIG_PARAM.TESTS));
		params.setTestsBinFolder((IFolder) adaptStringToObject(testsParams.get(CONFIG_PARAM.TESTS_BIN.name()), CONFIG_PARAM.TESTS_BIN));
	}
	
}
