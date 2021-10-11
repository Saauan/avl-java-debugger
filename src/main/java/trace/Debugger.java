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

public class Debugger {

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


		EventSet eventSet = null;
		boolean stop = false;
		while (!stop && (eventSet = vm.eventQueue().remove()) != null) {
			for (Event event : eventSet) {
				System.out.println(event.toString());
				if (event instanceof ClassPrepareEvent) {
					initBreakPoints();
				}

				if (event instanceof BreakpointEvent) {
					handleBreakPoint((BreakpointEvent) event);
					createStepRequest(((BreakpointEvent) event).thread());
				}

				if (event instanceof StepEvent stepEvent) {
					if(!((StepEvent) event).location().sourcePath().equals("java\\lang\\Thread.java")) {
						handleStepEvent((StepEvent) event, stepEvent);
					}
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

	@SneakyThrows
	private Trace getTrace(LocatableEvent event) {
		List<Trace.Variable> variables = new ArrayList<>();
		List<LocalVariable> localVariables = event.location().method().variables();
		var stack = event.thread().frame(0);
		for (LocalVariable variable: localVariables) {
				if(stack.visibleVariables().contains(variable)) {
					variables.add(new Trace.Variable(variable.name(), stack.getValue(variable)));
				}
		}
		return new Trace(LocalDateTime.now(), event.location().method().name(), event.location().sourcePath(), event.location().lineNumber(), variables);
	}

	private void handleStepEvent(StepEvent event, StepEvent stepEvent)
			throws AbsentInformationException, IncompatibleThreadStateException {
		//					event.request().disable();
		printLineInfo(event, stepEvent);
		printStackTrace(event);
		ProgramTrace.INSTANCE.log(getTrace(event));
	}

	private void handleBreakPoint(BreakpointEvent event) {
		System.out.println("Breakpoint");
		ProgramTrace.INSTANCE.log(getTrace(event));
	}

	private void createStepRequest(ThreadReference thread) {
		if(vm.eventRequestManager().stepRequests().isEmpty()) {
			StepRequest stepRequest = vm.eventRequestManager().createStepRequest(thread,
					StepRequest.STEP_LINE, StepRequest.STEP_OVER);
			stepRequest.enable();
		}
	}

	private void initBreakPoints() throws AbsentInformationException {
		SetBreakpointDebugCommand setBPCommand = new SetBreakpointDebugCommand();
		setBPCommand.setBreakPoint(MyMain.class.getName(), 9);
		setBPCommand.setBreakPoint(MyMain.class.getName(), 27);
	}

	private void printStackTrace(StepEvent event) throws IncompatibleThreadStateException {
		for(StackFrame frame : event.thread().frames()) {
			System.out.printf("%s %d|", frame.location().method(), frame.location().lineNumber());
		}
		System.out.println();
	}

	private void printLineInfo(StepEvent event, StepEvent stepEvent)
			throws AbsentInformationException, IncompatibleThreadStateException {
		Location location = stepEvent.location();
		String fileName = location.sourcePath();
		String className = location.sourceName();
		String methodName = location.method().name();
		int lineNumber = location.lineNumber();
		List<LocalVariable> variables = location.method().variables();
		//					List<Object> values = variables.stream().map(LocalVariable::)
		var msg = "%s %s %s %d %s".formatted(fileName, className, methodName, lineNumber, variables);
		var stack = event.thread().frame(0);
		variables.forEach(it -> {
			try {
				if(stack.visibleVariables().contains(it)) {
					System.out.println("Value of %s : %s".formatted(it.name(), stack.getValue(it)));
				}
			} catch (AbsentInformationException e) {
				throw new RuntimeException(e);
			}
		});
		System.out.println(msg);
	}

	public static void main(String[] args)
			throws IllegalConnectorArgumentsException, VMStartException, IOException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {
		new Debugger().main();
	}

	class SetBreakpointDebugCommand {
		public void setBreakPoint(String className, int lineNumber) throws AbsentInformationException {
			for (ReferenceType targetClass : vm.allClasses()) {
				if (targetClass.name().equals(className)) {
					Location location = targetClass.locationsOfLine(lineNumber).get(0);
					BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
					bpReq.enable();
				}
			}
		}
	}
}