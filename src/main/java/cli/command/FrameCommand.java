package cli.command;

import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Methode, thread, arguments
 */
public class FrameCommand  implements Command{
	@SneakyThrows
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		StackFrame frame = context.threadReference().frame(0);
		String methodName = MethodCommand.getMethodName(context);
		int lineNumber = frame.location().lineNumber();
		var msg = "%s %d".formatted(methodName, lineNumber);
		textIo.getTextTerminal().println(msg);
		TemporariesCommand.printVariables(textIo, frame);
		Location location = context.threadReference().frame(0).location();
		printTraceAndSource(textIo, location.sourcePath(), location.lineNumber());
	}

	@Override
	public Integer argumentsNeeded() {
		return 0;
	}

	@Override
	public String argumentsDescription() {
		return "";
	}

	public Boolean isOnPlace() {
		return true;
	}

	@SneakyThrows
	private void printTraceAndSource(TextIO textIO, String sourcePath, int lineNumber) {
		File source = new File("../../src/main/java/" + sourcePath);
		if(!source.exists()) {
			throw new InvalidCommandException("Cannot use frame on a library class");
		}
		var lines = Files.readAllLines(source.toPath());
		for (String line : lines.subList(getMinLine(lineNumber), getMaxLine(lineNumber, lines.size()))) {
			textIO.getTextTerminal().println(line);
		}
	}

	private int getMaxLine(int lineNumber, int nbLines) {
		int offset = 4;
		if(lineNumber < nbLines - offset) {
			return lineNumber + offset;
		} else {
			return nbLines;
		}
	}

	private int getMinLine(int lineNumber) {
		int offset = 2;
		if(lineNumber > offset) {
			return lineNumber - offset;
		} else {
			return 0;
		}
	}
}
