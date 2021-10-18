package cli.command;

import java.util.List;

public interface Command {
	void execute(List<String> args);
	Integer argumentsNeeded();
	String argumentsDescription();
}
