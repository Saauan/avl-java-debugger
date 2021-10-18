package cli.command;

import com.sun.jdi.request.StepRequest;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class StepCommand implements Command {
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		StepRequest stepRequest = context.vm().eventRequestManager().createStepRequest(context.threadReference(),
				StepRequest.STEP_LINE, StepRequest.STEP_INTO);
		stepRequest.enable();
	}

	@Override
	public Integer argumentsNeeded() {
		return 0;
	}

	@Override
	public String argumentsDescription() {
		return "";
	}

	public Boolean isOnPlace() {
		return false;
	}
}
