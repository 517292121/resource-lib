package cn.com.unionlib.parser.core.exception;

/**
 * 自定义异常
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class ParserException extends RuntimeException {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	
	public ParserException(String msg) {
		super(msg);
		
	}
	
	public ParserException(String msg,Throwable throwable){
		super(msg, throwable);
	}
	
}
