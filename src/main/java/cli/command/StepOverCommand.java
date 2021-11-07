package cli.command;

import com.sun.jdi.request.StepRequest;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class StepOverCommand implements Command {
	@Override
	public void execute(List<String> args, Context context, TextIO textIo) {
		assert context.vm().eventRequestManager().stepRequests().isEmpty() : "There is already a step request !";
		StepRequest stepRequest = createStepRequest(context);
		stepRequest.enable();
		textIo.getTextTerminal().println("Stepping over...");
	}

	private StepRequest createStepRequest(Context context) {
		return context.vm().eventRequestManager()
				.createStepRequest(context.threadReference(), StepRequest.STEP_LINE, StepRequest.STEP_OVER);
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
