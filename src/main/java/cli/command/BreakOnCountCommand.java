package cli.command;

import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class BreakOnCountCommand  implements Command{
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {

	}

	@Override
	public Integer argumentsNeeded() {
		return null;
	}

	@Override
	public String argumentsDescription() {
		return null;
	}

	public Boolean isOnPlace() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
