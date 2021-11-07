package cli.command;

import com.sun.jdi.IncompatibleThreadStateException;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class SenderCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		textIo.getTextTerminal().println(getSenderName(context));
	}

	private String getSenderName(Context context) throws IncompatibleThreadStateException {
		return context.threadReference().frame(0).location().declaringType().name();
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
