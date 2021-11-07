package cli.command;

import com.sun.jdi.request.BreakpointRequest;
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
		List<BreakpointReference> breakpoints = Debugger.BREAKPOINT_REFERENCES;
		if(breakpoints.isEmpty()) {
			textIo.getTextTerminal().println("There is no breakpoint.");
		} else {
			for(BreakpointReference req : breakpoints) {
				int lineNumber = req.location.lineNumber();
				String source = req.location.sourceName();
				boolean isEnabled = req.request.isEnabled();
				textIo.getTextTerminal().println("Breakpoint at line %d in %s, enabled : %b. Number of hits remaining : %d".formatted(lineNumber, source, isEnabled,
						req.getHitsRemaining()));
			}
		}
		List<MethodEntryReference> methodBreaks = Debugger.METHOD_ENTRY_REFERENCES;
		if(methodBreaks.isEmpty()) {
			textIo.getTextTerminal().println("There is no breakpoint before method entry");
		} else {
			for(MethodEntryReference req : methodBreaks) {
				textIo.getTextTerminal().println("Breakpoint before method entry for method %s".formatted(req.getMethod().name()));
			}
		}

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
