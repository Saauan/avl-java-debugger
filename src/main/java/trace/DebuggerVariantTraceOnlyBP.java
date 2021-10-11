package trace;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;
import lombok.SneakyThrows;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebuggerVariantTraceOnlyBP extends Debugger{

	VirtualMachine vm;

	private void main()
			throws IOException, IllegalConnectorArgumentsException, VMStartException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {
		LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
		Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
		Class<MyMain> debugClass = MyMain.class;
		arguments.get("main").setValue(debugClass.getName());

		vm = launchingConnector.launch(arguments);

		ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
		System.out.println(debugClass.getName());
		classPrepareRequest.addClassFilter(debugClass.getName());
		classPrepareRequest.enable();


		EventSet eventSet;
		boolean stop = false;
		while (!stop && (eventSet = vm.eventQueue().remove()) != null) {
			for (Event event : eventSet) {
				System.out.println(event.toString());
				if (event instanceof ClassPrepareEvent) {
					initBreakPoints();
				}

				if (event instanceof BreakpointEvent) {
					handleBreakPoint((BreakpointEvent) event);
				}

				if (event instanceof VMDisconnectEvent) {
					stop = true;
					System.out.println("End");
				}

				vm.resume();
			}
		}
		ProgramTrace.INSTANCE.printTraces();
	}
}
