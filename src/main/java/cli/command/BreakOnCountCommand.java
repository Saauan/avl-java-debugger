package cli.command;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

@Slf4j
public class BreakOnCountCommand extends BreakCommand implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		setBreakPoint(args.get(0), Integer.parseInt(args.get(1)), context.vm(), Integer.parseInt(args.get(2)), textIo);
	}

	@Override
	public Integer argumentsNeeded() {
		return 3;
	}

	@Override
	public String argumentsDescription() {
		return "String className, int lineNumber, int count";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
