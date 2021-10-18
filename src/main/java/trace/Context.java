package trace;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

public record Context(ThreadReference threadReference, VirtualMachine vm) {}
