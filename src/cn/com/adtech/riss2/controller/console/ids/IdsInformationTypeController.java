package cn.com.adtech.riss2.controller.console.ids;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
public class IdsInformationTypeController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(IdsInformationTypeController.class);

	private IIdsService idsServices;

	public IIdsService getIdsServices() {
		return idsServices;
	}

	public void setIdsServices(IIdsService idsServices) {
		this.idsServices = idsServices;
	}

	/**
	 * 初始化信息分类首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public ModelAndView initIdsInformationTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		List list = this.idsServices.getIdsInformationType(null, null, null);
//		request.setAttribute("idsInformationTypeList", list);
		return new ModelAndView("ids/idsInformationType");
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		String lan = ServletRequestUtils.getStringParameter(request, "lan", "");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> dataList = this.idsServices.getIdsInformationType(lan);
			
			map.put("success", true);
			map.put("data", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		} finally {
			PrintWriter out = response.getWriter();
			out.print(JSONObject.fromObject(map));
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 	进入分类修改编辑界面
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author MaChao
	 * @date	2020.04.15
	 * @version	1.0
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		Long id = ServletRequestUtils.getLongParameter(request, "id", 0L);
		if(id > 0L) {
			IdsInformationType t = (IdsInformationType) this.idsServices.get(IdsInformationType.class, id);
			request.setAttribute("t", t);
		}
		return "ids/edit";
	}
	
	/**
	 *	分类保存
	 */
	private final static String[] languages = new String[] {"zh", "zw", "mw", "en"};
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		Map<String, Object> map = new HashMap<String, Object>();
		
		Long id = ServletRequestUtils.getLongParameter(request, "id", 0L); 
		String lan = ServletRequestUtils.getStringParameter(request, "lan", "zh");
		String name = ServletRequestUtils.getStringParameter(request, "name", "").trim();
		Long index = ServletRequestUtils.getLongParameter(request, "index", 0L);
		
		IdsInformationType t = null;
		try {
			if(StringUtils.isBlank(name)) {
				throw new ErrorMessageException("分类名不能为空!");
			}
			if(id > 0L) {
				t = (IdsInformationType) this.idsServices.get(IdsInformationType.class, id);
			}
			if(t == null) {
				t = new IdsInformationType();
				t.setIdstNumber(0L);
			}
			//验证分类名是否重复
			long count = this.idsServices.getCountByName(name, t.getIdstId());
			if(count > 0L) {
				throw new ErrorMessageException("当前分类名已存在，请重新输入!");
			}
			
			//验证语种是否有效
			boolean flag = true;
			if(StringUtils.isNotBlank(lan)) {
				for(int i=0; i<languages.length; i++) {
					if(Objects.equals(lan, languages[i])) {
						flag = false;
						break;
					}
				}
			}
			//如果没有匹配的，则设置默认语种为中文
			if(flag) {
				lan = "zh";	
			}
			t.setIdstLanguage(lan);
			t.setIdstName(name);
			t.setIdstIndex(index);

			//保存
			this.idsServices.save(t);
			map.put("success", true);
		} catch(ErrorMessageException e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "系统错误，请联系管理员!");
			map.put("success", false);
		} finally {
			PrintWriter out = response.getWriter();
			out.print(JSONObject.fromObject(map));
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 	分类异步删除
	 * @param request
	 * @param response
	 * @throws Exception
	 * 
	 * @author MaChao
	 * @date	2020.04.15
	 */
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Long id = ServletRequestUtils.getLongParameter(request, "id", 0L);
		
		Map<String, Object> map = new HashMap<String, Object>();
		IdsInformationType t = null;
		try {
			if(id == null || id < 1L) {
				throw new ErrorMessageException("id不能为空!");
			}
			t = (IdsInformationType) this.idsServices.get(IdsInformationType.class, id);
			if(t == null) {
				throw new ErrorMessageException("数据获取失败!");
			}
			//验证分类是否已经关联数据
			if(this.idsServices.getInfoCountById(t.getIdstId()) > 0L) {
				throw new ErrorMessageException("此分类已关联信息数据，删除失败!");
			}
			
			this.idsServices.delete(t);
			map.put("success", true);
		} catch(ErrorMessageException e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "系统错误，请联系管理员!");
			map.put("success", false);
		} finally {
			PrintWriter out = response.getWriter();
			out.print(JSONObject.fromObject(map));
			out.flush();
			out.close();
		}
	}
	
}
