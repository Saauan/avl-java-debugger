package trace;

import java.util.ArrayList;
import java.util.List;

public class ProgramTrace {
	private final List<Trace> traces;
	private int currentTraceIndex;
	public static final ProgramTrace INSTANCE = new ProgramTrace();

	private ProgramTrace(){
		traces = new ArrayList<>();
		currentTraceIndex = 0;
	}

	public void log(Trace trace) {
		traces.add(trace);
	}

	public Trace next() {
		return traces.get(currentTraceIndex++);
	}

	public Trace previous() {
		return traces.get(--currentTraceIndex);
	}

	public void printTraces() {
		for(Trace trace : traces) {
			System.out.println(trace.toString());
		}
	}


}
