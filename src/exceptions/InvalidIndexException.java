package exceptions;

public class InvalidIndexException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String msg;
	
	public InvalidIndexException(int i) {
		msg = "Invalid index " + i;
	}
	
	@Override
	public String getMessage() {
		return this.getClass().getName().toString() + ": " + msg;
	}
}
