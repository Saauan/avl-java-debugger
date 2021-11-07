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
		setBreakPoint(args.get(0), Integer.parseInt(args.get(1)), context.vm(), -1, textIo);
	}

	protected void setBreakPoint(String className, int lineNumber, VirtualMachine vm, int hitCount,
								 TextIO textIO) {
		String msg = "Setting breakpoint for %s line %d with hit count %d".formatted(className, lineNumber,
				hitCount);
		log.debug(msg);
		textIO.getTextTerminal().println(msg);
		vm.allClasses().stream()
				.filter(cl -> cl.name().equals(className))
				.findFirst()
				.ifPresentOrElse(
						(cl -> setBreakPointAtClass(lineNumber, vm, cl, hitCount)),
						this::classDoesNotExist
				);
	}

	private void classDoesNotExist() {
		throw new InvalidCommandException("The class does not exist");
	}

	@SneakyThrows
	protected void setBreakPointAtClass(int lineNumber, VirtualMachine vm, ReferenceType cl, int hitCount) {
		Location location = getLocationOfBreakpoint(lineNumber, cl);
		BreakpointRequest bpReq = createRequest(vm, location);
		Debugger.BREAKPOINT_REFERENCES.add(createReference(hitCount, location, bpReq));
		bpReq.enable();
	}

	private BreakpointReference createReference(int hitCount, Location location, BreakpointRequest bpReq) {
		return new BreakpointReference(location, hitCount, bpReq);
	}

	private BreakpointRequest createRequest(VirtualMachine vm, Location location) {
		return vm.eventRequestManager().createBreakpointRequest(location);
	}

	private Location getLocationOfBreakpoint(int lineNumber, ReferenceType cl) throws AbsentInformationException {
		List<Location> locations = cl.locationsOfLine(lineNumber);
		if(locations.isEmpty()) throw new InvalidCommandException("The line you entered is not a valid line !");
		return locations.get(0);
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
