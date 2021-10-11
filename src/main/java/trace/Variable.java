package trace;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class Variable {
	String name;
	Object value;
}
