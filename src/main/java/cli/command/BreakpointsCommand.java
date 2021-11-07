package cli.command;

import com.sun.jdi.AbsentInformationException;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.BreakpointReference;
import trace.Context;
import trace.Debugger;
import trace.MethodEntryReference;

import java.util.List;

public class BreakpointsCommand  implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		printBreakpoints(textIo);
		printMethodBreakpoints(textIo);
	}

	private void printMethodBreakpoints(TextIO textIo) {
		List<MethodEntryReference> methodBreaks = Debugger.METHOD_ENTRY_REFERENCES;
		if(methodBreaks.isEmpty()) textIo.getTextTerminal().println("There is no breakpoint before method entry");
		else printMethodBreakpointsFromList(textIo, methodBreaks);
	}

	private void printBreakpoints(TextIO textIo) throws AbsentInformationException {
		List<BreakpointReference> breakpoints = Debugger.BREAKPOINT_REFERENCES;
		if(breakpoints.isEmpty()) textIo.getTextTerminal().println("There is no breakpoint.");
		else printBreakpointsFromList(textIo, breakpoints);
	}

	private void printMethodBreakpointsFromList(TextIO textIo, List<MethodEntryReference> methodBreaks) {
		methodBreaks.forEach(req -> printMethodBreak(textIo, req));
	}

	private void printMethodBreak(TextIO textIo, MethodEntryReference req) {
		textIo.getTextTerminal()
				.println("Breakpoint before method entry for method %s".formatted(req.getMethod().name()));
	}

	private void printBreakpointsFromList(TextIO textIo, List<BreakpointReference> breakpoints) throws AbsentInformationException {
		for (BreakpointReference req : breakpoints) {
			printBreak(textIo, req);
		}
	}

	private void printBreak(TextIO textIo, BreakpointReference req) throws AbsentInformationException {
		int lineNumber = req.location.lineNumber();
		String source = req.location.sourceName();
		boolean isEnabled = req.request.isEnabled();
		textIo.getTextTerminal().println("Breakpoint at line %d in %s, enabled : %b. Number of hits remaining : %d".formatted(lineNumber, source, isEnabled,
				req.getHitsRemaining()));
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
}
