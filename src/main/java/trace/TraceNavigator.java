package trace;

import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceNavigator {
	public void navigate(ProgramTrace programTrace) {
		TextIO textIO = TextIoFactory.getTextIO();
		String userInput = "";
		Map<String, VariableHistory> varHistory = getAllVariables(programTrace);
		while(!userInput.equals("s")) {
			userInput = textIO.newStringInputReader().withPossibleValues("n", "p", "s", "v").read("What to do next ?");
			if (userInput.equals("n")) {
				if (!programTrace.hasNext()) {
					textIO.getTextTerminal().println("There is no next trace");
				} else {
					printTraceAndSource(textIO, programTrace.next());
				}
			}
			if (userInput.equals("p")) {
				if (!programTrace.hasPrevious()) {
					textIO.getTextTerminal().println("There is no previous frame");
				} else {
					printTraceAndSource(textIO, programTrace.previous());
				}
			}
			if (userInput.equals("v")) {
				List<String> prompts = varHistory.keySet().stream().toList();
				Integer varIndex = textIO.newIntInputReader().withMinVal(0).withMaxVal(prompts.size() - 1).read(prompts);
				String key = prompts.get(varIndex);
				VariableHistory history = varHistory.get(key);
				textIO.getTextTerminal().println(history.toString());
			}
		}

	}

	private Map<String, VariableHistory> getAllVariables(ProgramTrace traces) {
		Map<String, VariableHistory> varHistory = new HashMap<>();
		for(Trace trace : traces.traces) {
			var variables = trace.variables;
			for(Variable variable: variables) {
				String key = getHistoryKey(variable.name, trace.methodName);
				varHistory.computeIfAbsent(key, k -> new VariableHistory(trace.methodName, variable.name));
				varHistory.get(key).history.add(variable.value);
			}
		}
		return varHistory;
	}

	private String getHistoryKey(String name, String methodName) {
		return methodName + name;
	}

	@SneakyThrows
	private void printTraceAndSource(TextIO textIO, Trace trace) {
		textIO.getTextTerminal().println(trace.toString());
		File source = new File("../../src/main/java/" + trace.sourcePath);
		var lines = Files.readAllLines(source.toPath());
		for (String line : lines.subList(getMinLine(trace), getMaxLine(trace, lines.size()))) {
			textIO.getTextTerminal().println(line);
		}
	}

	private int getMaxLine(Trace trace, int nbLines) {
		int offset = 4;
		if(trace.lineNumber < nbLines - offset) {
			return trace.lineNumber + offset;
		} else {
			return nbLines;
		}
	}

	private int getMinLine(Trace trace) {
		int offset = 2;
		if(trace.lineNumber > offset) {
			return trace.lineNumber - offset;
		} else {
			return 0;
		}
	}
}
