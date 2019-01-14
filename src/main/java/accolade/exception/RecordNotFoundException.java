package accolade.exception;

import java.util.UUID;

/** Exception to indicate record not found for given ID */
public class RecordNotFoundException extends Exception {

	/** Constructor */
	public RecordNotFoundException(UUID id) {
		super("Record not found for id: " + id.toString());
	}
}