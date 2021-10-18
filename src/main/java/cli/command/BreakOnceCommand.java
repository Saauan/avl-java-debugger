package cli.command;

import java.util.List;

public class BreakOnceCommand  implements Command{
	@Override
	public void execute(List<String> args) {

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
