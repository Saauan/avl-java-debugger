package cli.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.BreakpointRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import trace.Context;
import trace.Debugger;

import java.util.List;

@Slf4j
public class BreakCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context) {
		setBreakPoint(args.get(0), Integer.parseInt(args.get(1)), context.vm());
	}

	private void setBreakPoint(String className, int lineNumber, VirtualMachine vm) throws AbsentInformationException {
		log.debug("Setting breakpoint for %s line %d".formatted(className, lineNumber));
		vm.allClasses().stream().filter(cl -> cl.name().equals(className)).forEach(
				cl -> setBreakPointAtClass(lineNumber, vm, cl)
		);
	}

	@SneakyThrows
	private void setBreakPointAtClass(int lineNumber, VirtualMachine vm, ReferenceType cl) {
		Location location = cl.locationsOfLine(lineNumber).get(0);
		BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
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
