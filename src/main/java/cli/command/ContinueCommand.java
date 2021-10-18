package cli.command;

import trace.Context;

import java.util.List;

public class ContinueCommand  implements Command{
	@Override
	public void execute(List<String> args, Context context) {}

	@Override
	public Integer argumentsNeeded() {
		return 0;
	}

	@Override
	public String argumentsDescription() {
		return "";
	}

	public Boolean isOnPlace() {
		return false;
	}
}
