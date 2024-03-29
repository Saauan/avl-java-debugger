package trace;

import cli.command.*;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.console.ConsoleTextTerminal;
import org.beryx.textio.jline.JLineTextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;
import org.beryx.textio.system.SystemTextTerminal;

import java.util.*;

@Slf4j
public class CliCommander implements Commander {

	private final TextIO textIO = new TextIO(new SystemTextTerminal());
	private Map<String, Command> commands;

	public CliCommander() {
		initCommands();
	}

	private void initCommands() {
		commands = Map.ofEntries(
				Map.entry("step", new StepCommand()),
				Map.entry("step-over", new StepOverCommand()),
				Map.entry("continue", new ContinueCommand()),
				Map.entry("frame", new FrameCommand()),
				Map.entry("temporaries", new TemporariesCommand()),
				Map.entry("stack", new StackCommand()),
				Map.entry("stack-top", new StackTopCommand()),
				Map.entry("receiver", new ReceiverCommand()),
				Map.entry("sender", new SenderCommand()),
				Map.entry("receiver-variables", new ReceiverVariablesCommand()),
				Map.entry("method", new MethodCommand()),
				Map.entry("arguments", new ArgumentsCommand()),
				Map.entry("print-var", new PrintVarCommand()),
				Map.entry("break", new BreakCommand()),
				Map.entry("breakpoints", new BreakpointsCommand()),
				Map.entry("break-once", new BreakOnceCommand()),
				Map.entry("break-on-count", new BreakOnCountCommand()),
				Map.entry("break-before-method-call", new BreakBeforeMethodCallCommand())
		);
	}

	@Override
	public void requestCommand(Context context) {
		boolean resumeExecution = false;
		while(!resumeExecution) {
			boolean incorrect = true;
			Command command = null;
			while(incorrect) {
				log.debug("Reading user input...");
				String userInput = textIO.newStringInputReader().read("Enter a command :");
				if(commands.containsKey(userInput)) {
					incorrect = false;
					command = commands.get(userInput);
				} else {
					textIO.getTextTerminal().println("This command is not valid !");
				}
			}

			List<String> arguments = new ArrayList<>();
			if(command.argumentsNeeded() > 0) {
				log.debug("Reading user arguments...");
				arguments = Arrays.stream(textIO.newStringInputReader()
						.read("Enter all arguments separated by spaces : " + command.argumentsDescription())
						.split(" ")).toList();
			}

			try {
				command.execute(arguments, context, textIO);
				resumeExecution = !command.isOnPlace();
			} catch (InvalidCommandException e) {
				log.warn(e.getMessage());
				textIO.getTextTerminal().println(e.getMessage());
			}

		}
		log.debug("Resuming execution");
	}
}
