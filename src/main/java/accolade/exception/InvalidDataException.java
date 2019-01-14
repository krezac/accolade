package accolade.exception;

/** exception to indicate invalid data */
public class InvalidDataException extends Exception {

	/** Constructor */
	public InvalidDataException(String s) {
		super("Invalid data: " + s);
	}
}