package cli.command;

import com.sun.jdi.StackFrame;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class PrintVarCommand  implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		String varName = args.get(0);
		StackFrame frame = context.threadReference().frame(0);
		textIo.getTextTerminal().println(frame.getValue(frame.visibleVariableByName(varName)).toString());
	}

	@Override
	public Integer argumentsNeeded() {
		return 1;
	}

	@Override
	public String argumentsDescription() {
		return "String varName";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
