package trace;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ProgramTrace {
	public final List<Trace> traces;
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

	public boolean hasNext() {
		return currentTraceIndex < traces.size();
	}

	public Trace previous() {
		return traces.get(--currentTraceIndex);
	}

	public boolean hasPrevious() {
		return currentTraceIndex > 0;
	}

	public void printTraces() {
		for(Trace trace : traces) {
			System.out.println(trace.toString());
		}
	}
}
