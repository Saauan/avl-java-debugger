package cli.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class TemporariesCommand implements Command {
	@SneakyThrows
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		StackFrame frame = context.threadReference().frame(0);
		printVariables(textIo, frame);
	}

	public static void printVariables(TextIO textIo, StackFrame frame) {
		List<LocalVariable> variables = getLocalVariables(frame);
		printVariablesValues(textIo, frame, variables);
	}

	private static void printVariablesValues(TextIO textIo, StackFrame frame, List<LocalVariable> variables) {
		variables.forEach(localVar -> printVariableValue(textIo, frame, localVar));
	}

	private static void printVariableValue(TextIO textIo, StackFrame frame, LocalVariable localVar) {
		var value = frame.getValue(localVar);
		String name = localVar.name();
		textIo.getTextTerminal().println("%s = %s".formatted(name, value));
	}

	private static List<LocalVariable> getLocalVariables(StackFrame frame) {
		try {
			return frame.visibleVariables();
		} catch (AbsentInformationException e) {
			throw new InvalidCommandException("Cannot use this command here");
		}
	}

	@Override
	public Integer argumentsNeeded() {
		return 0;
	}

	@Override
	public String argumentsDescription() {
		return "";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
