package plugin;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class MuJavaConsole {
	private static final String consoleName = "MuJava++ Console";
	private MessageConsole console;
	private MessageConsoleStream out;

	public MuJavaConsole() {
		this.console = findConsole(consoleName);
		out = this.console.newMessageStream();
	}

	public void showConsole() {
		//I CAN'T MAKE THIS WORK ARGHHHHHHHHHHHH!!!!!!!!!!
	}
	
	public MessageConsoleStream getOutputStream() {
		return this.out;
	}
	
	public void writeln(String msg) {
		this.out.println(msg);
	}
	
	public void write(String msg) {
		this.out.print(msg);
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

}
