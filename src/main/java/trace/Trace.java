package trace;

import lombok.AllArgsConstructor;
import lombok.Data;

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
	List<Frame> frames;

}
