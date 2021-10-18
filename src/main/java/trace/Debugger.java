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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class Debugger {

	public static final List<BreakpointReference> BREAKPOINT_REFERENCES = new ArrayList<>();
	public static final List<MethodEntryReference> METHOD_ENTRY_REFERENCES = new ArrayList<>();
	VirtualMachine vm;

	public static void main(String[] args)
			throws IllegalConnectorArgumentsException, VMStartException, IOException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {
		new Debugger().main();
	}

	@SneakyThrows
	private void main() {
		LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
		Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
		Class<MyMain> debugClass = MyMain.class;
		arguments.get("main").setValue(debugClass.getName());

		vm = launchingConnector.launch(arguments);

		Commander commander = new CliCommander();

		ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
		System.out.println(debugClass.getName());
		classPrepareRequest.addClassFilter(debugClass.getName());
		classPrepareRequest.enable();


		EventSet eventSet = null;
		boolean stop = false;
		while (!stop && (eventSet = vm.eventQueue().remove()) != null) {
			for (Event event : eventSet) {
				System.out.println(event.toString());
				if (event instanceof ClassPrepareEvent classPrepareEvent) {
					log.debug("Entering ClassPrepareEvent " + MyMain.class.getName());
					commander.requestCommand(new Context(classPrepareEvent.thread(), vm));
				}

				if (event instanceof BreakpointEvent breakpointEvent) {
					log.debug("Entering BreakpointEvent");
					BREAKPOINT_REFERENCES.stream().filter(ref -> ref.getLocation().equals(breakpointEvent.location()))
							.findAny()
							.ifPresentOrElse(
									decrementOrRemoveBreakpointRef(),
									() -> log.debug("No breakpoint ref found"));
					commander.requestCommand(new Context(breakpointEvent.thread(), vm));
				}

				if (event instanceof StepEvent stepEvent) {
					log.debug("Entering StepEvent");
					stepEvent.request().disable();
					commander.requestCommand(new Context(stepEvent.thread(), vm));
				}

				if (event instanceof MethodEntryEvent methodEntryEvent) {
					log.debug("Entering MethodEntryEvent");
					METHOD_ENTRY_REFERENCES.stream()
							.filter(ref -> ref.getMethod().equals(methodEntryEvent.location().method()))
							.findFirst().ifPresentOrElse(
									ref -> commander.requestCommand(new Context(methodEntryEvent.thread(), vm)),
									() -> log.debug("Not correct method")
							);


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

	private Consumer<BreakpointReference> decrementOrRemoveBreakpointRef() {
		return ref -> {
			log.debug("Decrementing reference %s".formatted(ref));
			if (ref.getHitsRemaining() > 0) {
				ref.setHitsRemaining(ref.getHitsRemaining() - 1);
			}
			if (ref.getHitsRemaining() == 0) {
				log.debug("Removing reference %s".formatted(ref));
				BREAKPOINT_REFERENCES.remove(ref);
				ref.getRequest().disable();
			}
		};
	}

	@SneakyThrows
	protected Trace getTrace(LocatableEvent event) {
		List<Variable> variables = new ArrayList<>();
		List<LocalVariable> localVariables = event.location().method().variables();
		var stack = event.thread().frame(0);
		for (LocalVariable variable : stack.visibleVariables()) {
			variables.add(new Variable(variable.name(), stack.getValue(variable)));
		}
		List<Frame> frames = new ArrayList<>();
		var stackFrames = event.thread().frames();
		stackFrames.forEach(it -> {
			frames.add(new Frame(it.location().method().name(), it.location().lineNumber()));
		});
		return new Trace(LocalDateTime.now(), event.location().method().name(), event.location().sourcePath(),
				event.location().lineNumber(), variables, frames);
	}

	protected void handleStepEvent(StepEvent event, StepEvent stepEvent)
			throws AbsentInformationException, IncompatibleThreadStateException {
		//					event.request().disable();
		printLineInfo(event, stepEvent);
		printStackTrace(event);
		ProgramTrace.INSTANCE.log(getTrace(event));
	}

	protected void handleBreakPoint(BreakpointEvent event) {
		System.out.println("Breakpoint");
		ProgramTrace.INSTANCE.log(getTrace(event));
	}

	protected void createStepRequest(ThreadReference thread) {
		if (vm.eventRequestManager().stepRequests().isEmpty()) {
			StepRequest stepRequest = vm.eventRequestManager().createStepRequest(thread,
					StepRequest.STEP_LINE, StepRequest.STEP_OVER);
			stepRequest.enable();
		}
	}

	protected void initBreakPoints() throws AbsentInformationException {
		SetBreakpointDebugCommand setBPCommand = new SetBreakpointDebugCommand();
		setBPCommand.setBreakPoint(MyMain.class.getName(), 9);
		setBPCommand.setBreakPoint(MyMain.class.getName(), 27);
	}

	protected void printStackTrace(StepEvent event) throws IncompatibleThreadStateException {
		for (StackFrame frame : event.thread().frames()) {
			System.out.printf("%s %d|", frame.location().method(), frame.location().lineNumber());
		}
		System.out.println();
	}

	protected void printLineInfo(StepEvent event, StepEvent stepEvent)
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
				if (stack.visibleVariables().contains(it)) {
					System.out.println("Value of %s : %s".formatted(it.name(), stack.getValue(it)));
				}
			} catch (AbsentInformationException e) {
				throw new RuntimeException(e);
			}
		});
		System.out.println(msg);
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
