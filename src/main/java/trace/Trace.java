package trace;

import com.sun.jdi.LocalVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Trace {
	LocalDateTime date;
	String methodName;
	String sourcePath;
	int lineNumber;
	List<Variable> variables;

	@AllArgsConstructor
	@Data
	static
	class Variable {
		String name;
		Object value;
	}
}
