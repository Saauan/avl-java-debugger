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
	public VirtualMachine vm;
	public Commander commander;

	public static void main(String[] args)
			throws IllegalConnectorArgumentsException, VMStartException, IOException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {
		new Debugger().main();
	}

	@SneakyThrows
	private void main() {
		prepareDebugger();
		EventSet eventSet = null;
		boolean stop = false;
		while (!stop && ((eventSet = vm.eventQueue().remove()) != null)) {
			for (Event event : eventSet) {
				System.out.println(event.toString());
				if (event instanceof ClassPrepareEvent classPrepareEvent) {
					log.debug("Entering ClassPrepareEvent " + MyMain.class.getName());
					commander.requestCommand(createContext(classPrepareEvent.thread()));
				}

				if (event instanceof BreakpointEvent breakpointEvent) {
					handleBreakEvent(commander, breakpointEvent);
				}

				if (event instanceof StepEvent stepEvent) {
					handleStepEvent(commander, stepEvent);
				}

				if (event instanceof MethodEntryEvent methodEntryEvent) {
					handleMethodEntryEvent(commander, methodEntryEvent);
				}

				if (event instanceof VMDisconnectEvent) {
					stop = true;
					System.out.println("End");
				}

				vm.resume();
			}
		}
	}

	private void prepareDebugger() throws IOException, IllegalConnectorArgumentsException, VMStartException {
		LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
		Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
		Class<MyMain> debugClass = MyMain.class;
		arguments.get("main").setValue(debugClass.getName());

		this.vm = launchingConnector.launch(arguments);

		this.commander = new CliCommander();

		ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
		System.out.println(debugClass.getName());
		classPrepareRequest.addClassFilter(debugClass.getName());
		classPrepareRequest.enable();
	}

	private void handleMethodEntryEvent(Commander commander, MethodEntryEvent methodEntryEvent) {
		log.debug("Entering MethodEntryEvent");
		METHOD_ENTRY_REFERENCES.stream()
				.filter(ref -> methodsNamesAreEquals(methodEntryEvent, ref))
				.findFirst().ifPresentOrElse(
						ref -> commander.requestCommand(createContext(methodEntryEvent.thread())),
						() -> log.debug("Not correct method")
				);
	}

	private boolean methodsNamesAreEquals(MethodEntryEvent methodEntryEvent, MethodEntryReference ref) {
		return ref.getMethod().equals(methodEntryEvent.location().method());
	}

	private void handleStepEvent(Commander commander, StepEvent stepEvent) {
		log.debug("Entering StepEvent");
		stepEvent.request().disable();
		decrementBreakpointIfFound(stepEvent);
		commander.requestCommand(createContext(stepEvent.thread()));
	}

	private void handleBreakEvent(Commander commander, BreakpointEvent breakpointEvent) {
		log.debug("Entering BreakpointEvent");
		decrementBreakpointIfFound(breakpointEvent);
		commander.requestCommand(createContext(breakpointEvent.thread()));
	}

	private Context createContext(ThreadReference thread) {
		return new Context(thread, vm);
	}

	private void decrementBreakpointIfFound(LocatableEvent locatableEvent) {
		BREAKPOINT_REFERENCES.stream()
				.filter(ref -> ref.getLocation().equals(locatableEvent.location()))
				.findAny()
				.ifPresentOrElse(
						decrementOrRemoveBreakpointRef(),
						() -> log.debug("No breakpoint ref found")
				);
	}

	private Consumer<BreakpointReference> decrementOrRemoveBreakpointRef() {
		return this::decrementOrRemoveBreakpoint;
	}

	private void decrementOrRemoveBreakpoint(BreakpointReference ref) {
		log.debug("Decrementing reference %s".formatted(ref));
		if (ref.getHitsRemaining() > 0) {
			decrementBreakpoint(ref);
		}
		if (ref.getHitsRemaining() == 0) {
			removeBreakpoint(ref);
		}
	}

	private void decrementBreakpoint(BreakpointReference ref) {
		ref.setHitsRemaining(ref.getHitsRemaining() - 1);
	}

	private void removeBreakpoint(BreakpointReference ref) {
		log.debug("Removing reference %s".formatted(ref));
		BREAKPOINT_REFERENCES.remove(ref);
		ref.getRequest().disable();
	}
}
