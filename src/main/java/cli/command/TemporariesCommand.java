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
		List<LocalVariable> variables = null;
		try {
			variables = frame.visibleVariables();
		} catch (AbsentInformationException e) {
			throw new InvalidCommandException("Cannot use this command here");
		}
		variables.forEach(localVar -> {
			var value = frame.getValue(localVar);
			textIo.getTextTerminal().println("%s = %s".formatted(localVar.name(), value));
		});
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
