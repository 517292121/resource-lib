package cn.com.adtech.riss2.wsClient;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;

public class FileConvertServiceClient {
	private static XFireProxyFactory proxyFactory = new XFireProxyFactory();

	private HashMap endpoints = new HashMap();

	private Service service0;

	public Object getEndpoint(Endpoint endpoint) {
		try {
			return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
		}catch(MalformedURLException e) {
			throw new XFireRuntimeException("Invalid URL", e);
		}
	}

	public Object getEndpoint(QName name) {
		Endpoint endpoint = ((Endpoint)endpoints.get((name)));
		if((endpoint) == null) {
			throw new IllegalStateException("No such endpoint!");
		}
		return getEndpoint((endpoint));
	}

	public Collection getEndpoints() {
		return endpoints.values();
	}

	private void create0() {
		TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
		HashMap props = new HashMap();
		props.put("annotations.allow.interface", true);
		AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(
				new JaxbTypeRegistry()));
		asf.setBindingCreationEnabled(false);
		service0 = asf.create((cn.com.adtech.riss2.wsClient.FileConvertServicePortType.class), props);
		{
			AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://ws.library.adtech.com",
					"FileConvertServicePortTypeLocalBinding"), "urn:xfire:transport:local");
		}
		{
			AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://ws.library.adtech.com",
					"FileConvertServiceHttpBinding"), "http://schemas.xmlsoap.org/soap/http");
		}
	}

	public FileConvertServicePortType getFileConvertServiceHttpPort() {
		return ((FileConvertServicePortType)(this).getEndpoint(new QName("http://ws.library.adtech.com", "FileConvertServiceHttpPort")));
	}

	public FileConvertServicePortType getFileConvertServiceHttpPort(String url) {
		FileConvertServicePortType var = getFileConvertServiceHttpPort();
		org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
		return var;
	}

	public FileConvertServicePortType getFileConvertServicePortTypeLocalEndpoint() {
		return ((FileConvertServicePortType)(this).getEndpoint(new QName("http://ws.library.adtech.com",
				"FileConvertServicePortTypeLocalEndpoint")));
	}

	public FileConvertServicePortType getFileConvertServicePortTypeLocalEndpoint(String url) {
		FileConvertServicePortType var = getFileConvertServicePortTypeLocalEndpoint();
		org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
		return var;
	}


	public FileConvertServiceClient() {
		String webServicePath = SysConfigInfo.WEBSERVICE_PATH;
		if(!CommonUtil.isNotNull(webServicePath)){
			webServicePath = CommonUtil.getProperties("/config.properties", "webServicePath");
		}
		create0();
		Endpoint FileConvertServiceHttpPortEP = service0.addEndpoint(
				new QName("http://ws.library.adtech.com", "FileConvertServiceHttpPort"), new QName("http://ws.library.adtech.com",
						"FileConvertServiceHttpBinding"), webServicePath + "/services/FileConvertService");
		endpoints.put(new QName("http://ws.library.adtech.com", "FileConvertServiceHttpPort"), FileConvertServiceHttpPortEP);
		Endpoint FileConvertServicePortTypeLocalEndpointEP = service0.addEndpoint(new QName("http://ws.library.adtech.com",
				"FileConvertServicePortTypeLocalEndpoint"), new QName("http://ws.library.adtech.com",
				"FileConvertServicePortTypeLocalBinding"), "xfire.local://FileConvertService");
		endpoints.put(new QName("http://ws.library.adtech.com", "FileConvertServicePortTypeLocalEndpoint"),
				FileConvertServicePortTypeLocalEndpointEP);
	}
}
