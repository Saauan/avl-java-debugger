package cli.command;

import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public interface Command {
	void execute(List<String> args, Context context, TextIO textIo);
	Integer argumentsNeeded();
	String argumentsDescription();
	Boolean isOnPlace();
}
