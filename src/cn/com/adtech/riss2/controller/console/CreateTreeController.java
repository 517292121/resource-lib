package cn.com.adtech.riss2.controller.console;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
/**
 * 获得后台功能树json
 * 
 * @author MaChao
 *O
 */
public class CreateTreeController extends MultiActionController{
	private static Logger logger = Logger.getLogger(CreateTreeController.class);
	
	public void childCategory(HttpServletRequest request,HttpServletResponse response){
		SysManager sysUserinfo = ServiceUtil.getCurrentUser(request);
		if(sysUserinfo!=null){
			long catalogId = ServletRequestUtils.getLongParameter(request, "id", 0);
			CurrentUserSession userSession = CurrentUserSession.findUserSession(request);
			String json = userSession.getJsonFunctionTree(catalogId);
			
			if(logger.isDebugEnabled())
				logger.debug(json);
			
			response.setContentType("txt/plain;charset=utf-8");
			PrintWriter writer=null;
			try {
				writer = response.getWriter();
				writer.write(json);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.error("获得输出对象失败！",e);				
			}
		}
	}
}
