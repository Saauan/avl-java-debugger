package cli.command;

import trace.Context;

import java.util.List;

public class PrintVarCommand  implements Command{
	@Override
	public void execute(List<String> args, Context context) {

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
