package cli.command;

import com.sun.jdi.Method;
import com.sun.jdi.StackFrame;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class StackCommand implements Command {
	@SneakyThrows
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		context.threadReference().frames().forEach(frame -> printFrame(textIo, frame));
	}

	private void printFrame(TextIO textIo, StackFrame frame) {
		Method method = frame.location().method();
		int lineNumber = frame.location().lineNumber();
		textIo.getTextTerminal().println("%s %d|".formatted(method, lineNumber));
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
