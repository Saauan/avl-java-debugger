package cli.command;

import trace.Context;

import java.util.List;

public interface Command {
	void execute(List<String> args, Context context);
	Integer argumentsNeeded();
	String argumentsDescription();
	Boolean isOnPlace();
}
