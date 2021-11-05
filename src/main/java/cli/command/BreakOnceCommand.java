package cli.command;

import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class BreakOnceCommand extends BreakCommand implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		setBreakPoint(args.get(0), Integer.parseInt(args.get(1)), context.vm(), 1, textIo);
	}

	@Override
	public Integer argumentsNeeded() {
		return 2;
	}

	@Override
	public String argumentsDescription() {
		return "String className, int lineNumber";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
