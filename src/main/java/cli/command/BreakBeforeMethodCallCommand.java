package cli.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.MethodEntryRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import trace.Context;
import trace.Debugger;
import trace.MethodEntryReference;

import java.util.List;

@Slf4j
public class BreakBeforeMethodCallCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		setBreakPoint(args.get(0), args.get(1), context.vm(), textIo);
	}

	protected void setBreakPoint(String methodName, String className, VirtualMachine vm, TextIO textIO) throws AbsentInformationException {
		String msg = "Setting break point before method call for %s".formatted(methodName);
		textIO.getTextTerminal().println(msg);
		log.debug(msg);
		vm.allClasses().stream()
				.filter(cl -> cl.name().equals(className))
				.forEach(cl -> setBreakPointAtClass(vm, cl, methodName));
	}

	@SneakyThrows
	protected void setBreakPointAtClass(VirtualMachine vm, ReferenceType cl, String methodName) {
		MethodEntryRequest methodEntryRequest = createRequest(vm);
		methodEntryRequest.addClassFilter(cl);
		Debugger.METHOD_ENTRY_REFERENCES.add(createReference(cl, methodName));
		methodEntryRequest.enable();
	}

	private MethodEntryRequest createRequest(VirtualMachine vm) {
		return vm.eventRequestManager().createMethodEntryRequest();
	}

	private MethodEntryReference createReference(ReferenceType cl, String methodName) {
		return new MethodEntryReference(cl.methodsByName(methodName).get(0));
	}

	@Override
	public Integer argumentsNeeded() {
		return 2;
	}

	@Override
	public String argumentsDescription() {
		return "String methodName, String className";
	}

	public Boolean isOnPlace() {
		return true;
	}
}
