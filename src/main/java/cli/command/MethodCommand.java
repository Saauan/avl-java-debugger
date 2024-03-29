package cli.command;

import com.sun.jdi.IncompatibleThreadStateException;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class MethodCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		textIo.getTextTerminal().println(getMethodName(context));
	}

	@SneakyThrows
	public static String getMethodName(Context context) {
		return context.threadReference().frame(0).location().method().name();
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
