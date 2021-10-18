package cli.command;

import com.sun.jdi.StackFrame;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

/**
 * Methode, thread, arguments
 */
public class FrameCommand  implements Command{
	@SneakyThrows
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		StackFrame frame = context.threadReference().frame(0);
		var msg = "%s %d".formatted(frame.location().method(), frame.location().lineNumber()); // TODO
		textIo.getTextTerminal().println(msg);
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
