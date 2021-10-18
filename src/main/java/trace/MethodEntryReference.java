package trace;

import com.sun.jdi.Method;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MethodEntryReference {
	public Method method;
}
