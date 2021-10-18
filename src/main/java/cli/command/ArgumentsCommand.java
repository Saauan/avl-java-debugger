package cli.command;

import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.StackFrame;
import com.sun.jdi.connect.Connector;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class ArgumentsCommand  implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		StackFrame frame = context.threadReference().frame(0);
		Method method = frame.location().method();
		for(LocalVariable argument : method.arguments()) {
			textIo.getTextTerminal().println("Argument %s : %s".formatted(argument.name(), frame.getValue(argument)));
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
