package plugin;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.List;

import mujava.api.Mutant;
import mujava.app.MutatorsInfo;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;

public class Parameters {
	private MutatorsInfo mi = MutatorsInfo.getInstance();
	
	private File output;
	private IProject root;
	private IFolder src;
	private IFolder bin;
	private IJavaElement clazz;
	private boolean runTests;
	private List<String> methods;
	private boolean allowFieldMutations;
	private boolean allowClassMutations;
	private List<String> operators;
	private IProject testsProject;
	private IFolder testsBinFolder;
	private List<IJavaElement> tests;
	
	

	public Parameters() {
		cleanAll();
	}
	
	public void cleanAll() {
		setRoot(null);
		cleanTests();
		cleanOperators();
		this.testsProject = null;
		this.testsBinFolder = null;
		this.allowClassMutations = false;
		this.allowFieldMutations = false;
		this.runTests = false;
	}
	
	public boolean validate() {
		boolean validateMain = checkMain();
		boolean validateMethodsAndOperators = checkMethodsAndOperators();
		boolean validateTests = checkTests();
		return validateMain && validateMethodsAndOperators && validateTests;
	}
	
	private boolean checkTests() {
		if (!this.runTests) {
			return this.testsProject == null && this.testsBinFolder == null && this.tests.isEmpty();
		} else {
			return this.testsProject != null && this.testsBinFolder != null && !this.tests.isEmpty();
		}
	}

	private boolean checkMethodsAndOperators() {
		boolean methodsSelected = !this.methods.isEmpty();
		boolean fieldOrClassMutationsSelected = this.allowClassMutations || this.allowFieldMutations;
		boolean operatorsSelected = !this.operators.isEmpty();
		return (methodsSelected || fieldOrClassMutationsSelected) &&  operatorsSelected;
	}

	private boolean checkMain() {
		boolean projectSelected = this.root != null;
		boolean srcAndBinFoldersSelected = this.src != null && this.bin != null;
		boolean outputDirSelected = this.output != null;
		boolean clazzSelected = this.clazz != null;
		return projectSelected && srcAndBinFoldersSelected && outputDirSelected && clazzSelected;
	}

	public List<Mutant> getSupportedOperators() {
		return mi.allOps();
	}
	
	public String getShortDescription(Mutant m) {
		return mi.getShortDescription(m);
	}
	
	public String getFullDescription(Mutant m) {
		return mi.getFullDescription(m);
	}

	private void cleanMethods() {
		this.methods = new LinkedList<String>();
	}
	
	private void cleanTests() {
		this.tests = new LinkedList<IJavaElement>();
	}
	
	private void cleanOperators() {
		this.operators = new LinkedList<String>();
	}
	

	public File getOutput() {
		return output;
	}



	public void setOutput(File output) {
		this.output = output;
	}



	public IProject getRoot() {
		return root;
	}

	public void setRoot(IProject root) {
		if (this.root != null) {
			this.src = null;
			this.bin = null;
			this.clazz = null;
			cleanMethods();
		}
		this.root = root;
	}



	public IFolder getSrc() {
		return src;
	}



	public void setSrc(IFolder src) {
		if (this.src != null) {
			this.clazz = null;
			cleanMethods();
		}
		this.src = src;
	}



	public IFolder getBin() {
		return bin;
	}



	public void setBin(IFolder bin) {
		if (this.bin != null) {
			this.clazz = null;
			cleanMethods();
		}
		this.bin = bin;
	}



	public IJavaElement getClazz() {
		return clazz;
	}



	public void setClazz(IJavaElement clazz) {
		if (this.clazz != null) {
			cleanMethods();
		}
		this.clazz = clazz;
	}



	public boolean runTests() {
		return runTests;
	}



	public void setRunTests(boolean runTests) {
		this.runTests = runTests;
	}



	public List<String> getMethods() {
		return methods;
	}



	public void setMethods(List<String> methods) {
		this.methods = methods;
	}



	public boolean allowFieldMutations() {
		return allowFieldMutations;
	}



	public void setAllowFieldMutations(boolean allowFieldMutations) {
		this.allowFieldMutations = allowFieldMutations;
	}



	public boolean allowClassMutations() {
		return allowClassMutations;
	}



	public void setAllowClassMutations(boolean allowClassMutations) {
		this.allowClassMutations = allowClassMutations;
	}



	public List<String> getOperators() {
		return operators;
	}



	public void setOperators(List<String> operators) {
		this.operators = operators;
	}


	public IProject getTestsProject() {
		return testsProject;
	}



	public void setTestsProject(IProject testsProject) {
		if (this.testsProject != null) {
			cleanTests();
		}
		this.testsProject = testsProject;
	}

	
	public void setTestsBinFolder(IFolder f) {
		this.testsBinFolder = f;
	}
	
	public IFolder getTestsBinFolder() {
		return this.testsBinFolder;
	}


	public List<IJavaElement> getTests() {
		return tests;
	}



	public void setTests(List<IJavaElement> tests) {
		this.tests = tests;
	}
	
	
	public String translateToMuJavaArguments() {
		List<String> stringParams = new LinkedList<String>();
		String originalSourceArg = "-O " + addTrailingSeparator(this.getSrc().getProjectRelativePath().toOSString());
		String originalBinArg = "-B " + addTrailingSeparator(this.getBin().getProjectRelativePath().toOSString());
		String mutantsSourceDir = "-M " + addTrailingSeparator(this.getOutput().getPath());
		String classToMutate = "-m " + ((IType)this.getClazz()).getFullyQualifiedName();
		String operatorsToUse = "-x " + listToString(this.getOperators());
		stringParams.add(originalSourceArg);
		stringParams.add(originalBinArg);
		stringParams.add(mutantsSourceDir);
		stringParams.add(classToMutate);
		stringParams.add(operatorsToUse);
		
		
		if (!this.getMethods().isEmpty()) {
			String methodsToMutate = "-s " + listToString(this.getMethods());
			stringParams.add(methodsToMutate);
		}
		
		
		if (this.runTests) {
			String testProject = "-T " + addTrailingSeparator(this.getTestsBinFolder().getLocation().toOSString());
			String testsToRun = "-t " + convertTestClassesToString(this.getTests());
			stringParams.add(testProject);
			stringParams.add(testsToRun);
			stringParams.add("-S");
		}
		
		
		if (this.allowClassMutations) {
			stringParams.add("-C");
		}
		if (this.allowFieldMutations) {
			stringParams.add("-F");
		}
		
		stringParams.add("-A"); //force ignoring mutGenLimit annotations
		
		return 	listToString(stringParams);
	}

	private String convertTestClassesToString(List<IJavaElement> tests) {
		String result = "";
		int current = 0;
		for (IJavaElement t : tests) {
			result += ValueAdapter.iJavaElementToString(t);
			current++;
			if (current < tests.size()) {
				result += " ";
			}
		}
		return result;
	}

	private String listToString(List<String> methods) {
		String result = "";
		int current = 0;
		for (String m : methods) {
			result += m;
			current++;
			if (current < methods.size()) {
				result += " ";
			}
		}
		return result;
	}
	
	private String addTrailingSeparator(String orig) {
		String separator = FileSystems.getDefault().getSeparator();
		if (!orig.endsWith(separator)) {
			return orig + separator;
		} else {
			return orig;
		}
	}
	
	
}
