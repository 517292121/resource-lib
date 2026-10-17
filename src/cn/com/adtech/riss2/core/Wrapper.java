package cn.com.adtech.riss2.core;

/**
 * 系统执行结果封装对象
 * 
 * @author MaChao
 * @date 2009-7-15
 * @version 1.0
 */
public class Wrapper 
{

	// 结果对象封装,包含所有的数据结构
	private Object result; 
	
	// 执行结果信息
	private String message;
	
	// 是否执行成功
	private boolean isSuccess = true;
	
	// 执行失败所抛出的异常
	private Exception e;

	public Object getResult() {
		return result;
	}
	
	public Wrapper()
	{
		
	}
	
	public Wrapper(Object result, String message)
	{
		this.result = result;
		this.message = message;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}
}
