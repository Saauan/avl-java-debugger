package cli.command;

public class InvalidCommandException extends RuntimeException {
	public InvalidCommandException(String message) {
		super(message);
	}
}
