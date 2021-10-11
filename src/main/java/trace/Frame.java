package trace;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class Frame {
	String method;
	int line;
}
