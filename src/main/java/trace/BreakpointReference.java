package trace;

import com.sun.jdi.Location;
import com.sun.jdi.request.BreakpointRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreakpointReference {
	public Location location;
	public Integer hitsRemaining;
	public BreakpointRequest request;
}
