package org.bouncycastle.cms;

public class CMSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CMSException(){
		super();
	}
	
	public CMSException(String message, Throwable cause) {
		super(message, cause);
		if(cause != null)
			cause.printStackTrace();
	}

	public CMSException(String message) {
		super(message);
	}

	public CMSException(Throwable cause) {
		super(cause);
	}
}
