package cli.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.BreakpointRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import trace.BreakpointReference;
import trace.Context;
import trace.Debugger;

import java.util.List;

@Slf4j
public class BreakCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		setBreakPoint(args.get(0), Integer.parseInt(args.get(1)), context.vm(), -1);
	}

	protected void setBreakPoint(String className, int lineNumber, VirtualMachine vm, int hitCount) throws AbsentInformationException {
		log.debug("Setting breakpoint for %s line %d with hit count %d".formatted(className, lineNumber, hitCount));
		vm.allClasses().stream().filter(cl -> cl.name().equals(className)).forEach(
				cl -> setBreakPointAtClass(lineNumber, vm, cl, hitCount)
		);
	}

	@SneakyThrows
	protected void setBreakPointAtClass(int lineNumber, VirtualMachine vm, ReferenceType cl, int hitCount) {
		Location location = cl.locationsOfLine(lineNumber).get(0);
		BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
		Debugger.BREAKPOINT_REFERENCES.add(new BreakpointReference(location, hitCount, bpReq));
		bpReq.enable();
	}

	@Override
	public Integer argumentsNeeded() {
		return 2;
	}

	@Override
	public String argumentsDescription() {
		return "String className, int lineNumber";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
