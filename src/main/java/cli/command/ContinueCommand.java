package cli.command;

public class ContinueCommand  implements Command{
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
