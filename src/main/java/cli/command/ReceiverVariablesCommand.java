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
		ObjectReference thisObject = context.threadReference().frame(0).thisObject();
		if(thisObject != null) printAllFields(textIo, referenceType, thisObject);
		else printStaticFields(textIo, referenceType);
	}

	private void printStaticFields(TextIO textIo, ReferenceType referenceType) {
		referenceType.fields().forEach(field -> {
			printFieldIfStatic(textIo, referenceType, field);
		});
	}

	private void printFieldIfStatic(TextIO textIo, ReferenceType referenceType, Field field) {
		if(field.isStatic()) {
			Value fieldValue = referenceType.getValue(field);
			textIo.getTextTerminal().println("Field %s : %s".formatted(field.name(), fieldValue));
		}
	}

	private void printAllFields(TextIO textIo, ReferenceType referenceType, ObjectReference thisObject) {
		referenceType.fields().forEach(field -> {
			printFieldFromObject(textIo, thisObject, field);
		});
	}

	private void printFieldFromObject(TextIO textIo, ObjectReference thisObject, Field field) {
		Value value = thisObject.getValue(field);
		textIo.getTextTerminal().println("Field %s : %s".formatted(field.name(), value));
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
