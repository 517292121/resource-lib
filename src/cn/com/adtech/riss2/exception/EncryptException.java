package cn.com.adtech.riss2.exception;

/**
 * DES加密，解密过程异常
 * 
 * @author MaChao
 */
@SuppressWarnings("serial")
public class EncryptException extends Exception {
	public EncryptException() {
		super();
	}

	public EncryptException(String message, Throwable cause) {
		super(message, cause);
		if(cause != null)
			cause.printStackTrace();
	}

	public EncryptException(String message) {
		super(message);
	}

	public EncryptException(Throwable cause) {
		super(cause);
	}
}
