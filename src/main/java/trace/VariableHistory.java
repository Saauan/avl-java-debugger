package trace;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class VariableHistory {
	public final String methodName;
	public final String variableName;
	public final List<Object> history;

	public VariableHistory(String methodName, String variableName) {
		this.methodName = methodName;
		this.variableName = variableName;
		history = new ArrayList<>();
	}
}
