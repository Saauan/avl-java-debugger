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
								 TextIO textIO) throws AbsentInformationException {
		String msg = "Setting breakpoint for %s line %d with hit count %d".formatted(className, lineNumber,
				hitCount);
		log.debug(msg);
		textIO.getTextTerminal().println(msg);
		vm.allClasses().stream().filter(cl -> cl.name().equals(className)).findFirst().ifPresentOrElse(
				(cl -> setBreakPointAtClass(lineNumber, vm, cl, hitCount)),
				this::classDoesNotExist);
	}

	private void classDoesNotExist() {
		throw new InvalidCommandException("The class does not exist");
	}

	@SneakyThrows
	protected void setBreakPointAtClass(int lineNumber, VirtualMachine vm, ReferenceType cl, int hitCount) {
		List<Location> locations = cl.locationsOfLine(lineNumber);
		if(locations.isEmpty()) {
			throw new InvalidCommandException("The line you entered is not a valid line !");
		}
		Location location = locations.get(0);
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
