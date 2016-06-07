package exceptions;

public class SearchFailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7169361495930715336L;
	boolean type; // true --> alpha fail, false --> beta fail
	
	public SearchFailException(boolean type) {
		this.type = type;
	}
	
	public boolean isAlphaFail() {
		return type;
	}
}
