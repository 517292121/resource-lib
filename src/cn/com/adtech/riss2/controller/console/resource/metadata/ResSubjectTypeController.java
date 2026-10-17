package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.bso.resource.ISubjectTypeService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

@SuppressWarnings("unchecked")
public class ResSubjectTypeController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ResSubjectTypeController.class);
	
	private ISubjectTypeService subjectTypeService = null; 
	public void setSubjectTypeService(ISubjectTypeService subjectTypeService) {
		this.subjectTypeService = subjectTypeService;
	}

//	private IMetadataManagementService metadataManagementService;
//
//	public IMetadataManagementService getMetadataManagementService() {
//		return metadataManagementService;
//	}
//
//	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
//		this.metadataManagementService = metadataManagementService;
//	}


	/**
	 * 学科分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-28
	 */
	public ModelAndView initResSubjectTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
////		List list = this.metadataManagementService.getResSubjectTypeList();
////		map.put("rstList", list);
//		
//		List rdtGrade1 = this.metadataManagementService.getResSubjectTypeList(null, null, null, 1L, 0, null);
//		List rdtGrade2 = this.metadataManagementService.getResSubjectTypeList(null, null, null, 2L, 0, null);
//		List rdtGrade3 = this.metadataManagementService.getResSubjectTypeList(null, null, null, 3L, 0, null);
//		map.put("rdtGrade1", rdtGrade1);
//		map.put("rdtGrade2", rdtGrade2);
//		map.put("rdtGrade3", rdtGrade3);
//		
//		return new ModelAndView("resource/metadata/resSubjectType", map);
		return new ModelAndView("resource/metadata/subjectGroupFrame");
	}

//	/**
//	 * 构造学科分类JSON树
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws Exception
//	 * @author MaChao
//	 * @date 2010-9-28
//	 */
//	public void createRSTYTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		response.setContentType("txt/plain;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		StringBuilder json = new StringBuilder();
//		try {
//			String nodeId = ServletRequestUtils.getStringParameter(request, "node");
//			if(nodeId == null || nodeId.length() == 0 || nodeId.equals("tge-root")) {
//				nodeId = "0";
//			}
//			json.append("[");
//			if("0".equals(nodeId))
//				json.append(createJsonTree(1L, null));
//			else
//				json.append(createJsonTree(new Long(2), Long.parseLong(nodeId)));
//			json.append("]");
//			out.write(json.toString());
//			System.out.println(json.toString());
//		}catch(Exception ex) {
//			ex.printStackTrace();
//			logger.error(ex.getMessage());
//		}
//		out.flush();
//	}

//	/**
//	 * 学科分类:增、删、改
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws Exception
//	 * @author MaChao
//	 * @date 2010-9-28
//	 */
//	public void handlerRSTYTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		response.setContentType("txt/plain;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		try {
//			String data = ServletRequestUtils.getStringParameter(request, "data", "");
//			String method = ServletRequestUtils.getStringParameter(request, "method", "");
//			TempObject tempObject = (TempObject)CommonUtil.jsonToObject(data, TempObject.class);
//			if(method.equalsIgnoreCase("add")) {
//				// 保存学科分类
//				ResSubjectType rst = new ResSubjectType();
//				rst.setRstyName(tempObject.getCity());
//				if("tge-root".equals(tempObject.getParentId())) {
//					rst.setRstyGrade(1L);
//					rst.setRstyPid(0L);
//				}else {
//					Long pid = Long.parseLong(tempObject.getParentId());
//					if(pid != null) {
//						ResSubjectType temp = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, pid);
//						if(temp != null) {
//							rst.setRstyGrade(temp.getRstyGrade() + 1);
//							rst.setRstyPid(pid);
//						}
//					}
//				}
//				this.metadataManagementService.save(rst);
//				// 更新页面上的ID值
//				out.print(rst.getRstyId());
//			}else if(method.equalsIgnoreCase("update")) {
//				ResSubjectType temp = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, tempObject.getId());
//				if(temp != null) {
//					temp.setRstyName(tempObject.getCity());
//					this.metadataManagementService.save(temp);
//				}
//			}else if(method.equalsIgnoreCase("delete")) {
//				ResSubjectType temp = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, tempObject.getId());
//				if(temp != null)
//					this.metadataManagementService.delete(temp);
//			}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//			logger.error(ex.getMessage());
//		}
//		out.flush();
//	}

//	private String createJsonTree(Long rstyGrade, Long rstyPid) {
//		StringBuilder json = new StringBuilder();
//		List list = null;
//		if(rstyGrade == 1)
//			list = this.metadataManagementService.getResSubjectTypeList(null, null, null, 1L);
//		else {
//			ResSubjectType rst = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, rstyPid);
//			if(rst != null)
//				list = this.metadataManagementService.getResSubjectTypeList(null, rstyPid, null, rst.getRstyGrade() + 1);
//		}
//		for(int i = 0; i < list.size(); i++) {
//			json.append("{city: '").append(((Object[])list.get(i))[1]).append("',id:").append(((Object[])list.get(i))[0]);
//			Object[] obj = (Object[])list.get(i);
//			List tempList = this.metadataManagementService.getResSubjectTypeList(null, (Long)obj[0], null, (Long)obj[2] + 1);
//			if(tempList.size() > 0)
//				json.append(",leaf: false");
//			else
//				json.append(",leaf: true");
//			// expanded：true 加载所有的子节点
//			json.append(",expanded: true,checked:false");
//			json.append("}");
//			if(i != list.size() - 1) {
//				json.append(",");
//			}
//		}
//		return json.toString();
//	}
	
	
	/**
	 * 初始化分类树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initGroupTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ResSubjectType> list = this.subjectTypeService.getSubjectList();
		
		request.setAttribute("subjectList", list);
		return new ModelAndView("resource/metadata/subjectGroupTree");
	}
	
	
	/**
	 * 学科详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView groupInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rstyId = ServletRequestUtils.getLongParameter(request, "rstyId", 0);
		ResSubjectType subject = this.subjectTypeService.getSubjectById(rstyId);
		
		Long pid = 0L; 
		if(subject != null){
			pid = subject.getRstyPid();
		}
		
		String options = this.subjectTypeService.getSubjectOptions(pid);
		request.setAttribute("subject", subject);
		request.setAttribute("options", options);
		return new ModelAndView("resource/metadata/subjectGroupInfo");
	}
}
