package cn.com.adtech.riss2.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 获得bean
 * 
 * @author MaChao
 */
public class ServiceLocator implements BeanFactoryAware {
	private static BeanFactory beanFactory = null;

	private static ServiceLocator servlocator = null;

	public void setBeanFactory(BeanFactory factory) throws BeansException {
		this.beanFactory = factory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * 创建读取Bean服务类实例(从spring.xml中加载) <bean id="serviceLocator" class="com.adtech.library.core.ServiceLocator" singleton="true" />
	 */
	public static ServiceLocator getInstance() {
		if(servlocator == null)
			servlocator = (ServiceLocator)beanFactory.getBean("serviceLocator");
		return servlocator;
	}

	/**
	 * 根据提供的bean名称得到相应的服务类
	 * 
	 * @param servName
	 *            bean名称
	 */
	public static Object getService(String servName) {
		return beanFactory.getBean(servName);
	}

	/**
	 * 根据提供的bean名称得到对应于指定类型的服务类
	 * 
	 * @param servName
	 *            bean名称
	 * @param clazz
	 *            返回的bean类型,若类型不匹配,将抛出异常
	 */
	public static Object getService(String servName, Class clazz) {
		return beanFactory.getBean(servName, clazz);
	}
}
