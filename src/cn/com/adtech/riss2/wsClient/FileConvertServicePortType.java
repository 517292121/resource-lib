package cn.com.adtech.riss2.wsClient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "FileConvertServicePortType", targetNamespace = "http://ws.library.adtech.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface FileConvertServicePortType {
	@WebMethod(operationName = "audioConvert", action = "")
	public void audioConvert(@WebParam(name = "in0", targetNamespace = "http://ws.library.adtech.com")
	String in0, @WebParam(name = "in1", targetNamespace = "http://ws.library.adtech.com")
	String in1);

	@WebMethod(operationName = "photoConvertFromVideo", action = "")
	public void photoConvertFromVideo(@WebParam(name = "in0", targetNamespace = "http://ws.library.adtech.com")
	String in0, @WebParam(name = "in1", targetNamespace = "http://ws.library.adtech.com")
	String in1, @WebParam(name = "in2", targetNamespace = "http://ws.library.adtech.com")
	String in2);

	@WebMethod(operationName = "documentConvert", action = "")
	public void documentConvert(@WebParam(name = "in0", targetNamespace = "http://ws.library.adtech.com")
	String in0, @WebParam(name = "in1", targetNamespace = "http://ws.library.adtech.com")
	String in1);

	@WebMethod(operationName = "videoConvert", action = "")
	public void videoConvert(@WebParam(name = "in0", targetNamespace = "http://ws.library.adtech.com")
	String in0, @WebParam(name = "in1", targetNamespace = "http://ws.library.adtech.com")
	String in1, @WebParam(name = "in2", targetNamespace = "http://ws.library.adtech.com")
	String in2);
	
	@WebMethod(operationName = "photoConvertFromDocument", action = "")
	public void photoConvertFromDocument(@WebParam(name = "in0", targetNamespace = "http://ws.library.adtech.com")
	String in0, @WebParam(name = "in1", targetNamespace = "http://ws.library.adtech.com")
	String in1);
}
