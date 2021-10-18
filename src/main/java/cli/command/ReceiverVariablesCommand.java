package cli.command;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import trace.Context;

import java.util.List;

public class ReceiverVariablesCommand implements Command {
	@Override
	@SneakyThrows
	public void execute(List<String> args, Context context, TextIO textIo) {
		ReferenceType referenceType = context.threadReference().frame(0).location().declaringType();
		for (Field field : referenceType.fields()) {
			ObjectReference thisObject = context.threadReference().frame(0).thisObject();
			if(thisObject != null) {
				Value value = thisObject.getValue(field);
				textIo.getTextTerminal().println("Field %s : %s".formatted(field.name(), value));
			} else {
				if(field.isStatic()) {
					textIo.getTextTerminal().println("Field %s : %s".formatted(field.name(), referenceType.getValue(field)));
				}
			}
		}
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
		return true;
	}
}
