package plugin;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ValueAdapter {
	
	
	public static String iProjectToString(IProject p) {
		if (p == null) return "";
		return p.getFullPath().toOSString();
	}
	
	public static IProject stringToIProject(String fullPathFromWorkspace) {
		if (fullPathFromWorkspace.isEmpty()) return null;
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(fullPathFromWorkspace);
		if (p != null && p.exists()) {
			return p;
		}
		return null;
	}
	
	public static String iFolderToString(IFolder f) {
		if (f == null) return "";
		return f.getProjectRelativePath().toOSString();
	}
	
	public static IFolder stringToIFolder(String relativePath, IProject project) {
		if (relativePath.isEmpty()) return null; 
		IFolder folder = project.getFolder(new Path(relativePath));
		if (folder != null && folder.exists()) {
			return folder;
		}
		return null;
	}
	
	public static String iJavaElementToString(IJavaElement je) {
		if (je == null) return "";
		return ((IType)je).getFullyQualifiedName();
	}
	
	public static IJavaElement stringToIJavaElement(String fullyQualifiedName, IProject p) {
		if (fullyQualifiedName.isEmpty()) return null;
		IJavaProject javaProject = JavaCore.create(p);
		try {
			return javaProject.findType(fullyQualifiedName);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String iJavaElementListToString(List<IJavaElement> jel) {
		String result = "[";
		int currentIndex = 0;
		for (IJavaElement je : jel) {
			result += iJavaElementToString(je);
			currentIndex++;
			if (currentIndex < jel.size()) {
				result += ",";
			}
		}
		result += "]";
		return result;
	}
	
	public static List<IJavaElement> stringToIJavaElementList(String sjel, IProject p) {
		List<IJavaElement> result = new LinkedList<IJavaElement>();
		if (p == null) {
			return result;
		}
		IJavaProject javaProject = JavaCore.create(p);
		String[] fqns = splitValues(sjel);
		try {
			for (String fqn : fqns) {
					result.add(javaProject.findType(fqn)); 
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String fileToString(File f) {
		if (f == null) return "";
		return f.getAbsolutePath();
	}
	
	public static File stringToFile(String fullPath) {
		if (fullPath.isEmpty()) return null;
		return new File(fullPath);
	}
	
	public static String booleanToString(boolean b) {
		return Boolean.toString(b);
	}
	
	public static boolean stringToBoolean(String b) {
		return Boolean.parseBoolean(b);
	}
	
	public static String stringListToString(List<String> sl) {
		String result = "[";
		int currentIndex = 0;
		for (String s : sl) {
			result += s;
			currentIndex++;
			if (currentIndex < sl.size()) {
				result += ",";
			}
		}
		result += "]";
		return result;
	}
	
	public static List<String> stringToStringList(String sl) {
		List<String> result = new LinkedList<String>();
		String[] sla = splitValues(sl);
		for (String s : sla) {
				result.add(s);
		}
		return result;
	}
	
	private static String[] splitValues(String orig) {
		String noBrackets = orig.replaceAll("[\\[\\]]", "");
		if (noBrackets.isEmpty()) {
			return new String[] {};
		} else {
			String[] result = noBrackets.split(",");
			return result;
		}
	}

}
