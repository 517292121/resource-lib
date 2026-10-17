package cn.com.adtech.riss2.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.core.ServiceLocator;

/**
 * 站点访问量统计监听
 * 
 * @author MaChao
 * @date 2011-1-12
 */
public class AccessCounterListener implements HttpSessionListener, ServletRequestListener {
	private static final Logger logger = Logger.getLogger(AccessCounterListener.class);

	private HttpServletRequest request;

	public void sessionCreated(HttpSessionEvent event) {
		ISysUserPlatformService sysUserPlatformService = (ISysUserPlatformService)ServiceLocator.getInstance().getService(
				"sysUserPlatformService");
		if(!request.getServletPath().startsWith("/console/")) {
			String ip = request.getRemoteAddr();
			sysUserPlatformService.saveAccessCounterListener(ip);
			logger.info("来自IP：" + ip + "的访问！");
		}
	}

	public void sessionDestroyed(HttpSessionEvent event) {}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		request = (HttpServletRequest)event.getServletRequest();
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {}
}
