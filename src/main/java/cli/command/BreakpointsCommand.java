package cli.command;

import com.sun.jdi.request.BreakpointRequest;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class BreakpointsCommand  implements Command{
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		List<BreakpointRequest> breakpoints = context.vm().eventRequestManager().breakpointRequests();
		if(breakpoints.isEmpty()) {
			textIo.getTextTerminal().println("There is no breakpoint.");
		} else {
			for(BreakpointRequest req : breakpoints) {
				textIo.getTextTerminal().println("Breakpoint at line %d in %s, enabled : %b".formatted(req.location().lineNumber(), req.location().sourceName(), req.isEnabled()));
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
