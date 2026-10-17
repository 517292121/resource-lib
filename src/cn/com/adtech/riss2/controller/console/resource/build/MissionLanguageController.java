package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.bso.resource.IMissionLanguageTempService;
import cn.com.adtech.riss2.bso.resource.MissionLanguageTempImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;

public class MissionLanguageController extends MultiActionController{

	
	private IMissionLanguageTempService missionLanguageTempService = null;
	private IMissionInfoManagementService missionInfoService = null;
	public void setMissionLanguageTemp(IMissionLanguageTempService missionLanguageTempService) {
		this.missionLanguageTempService = missionLanguageTempService;
	}
	public void setMissionInfoService(IMissionInfoManagementService missionInfoService) {
		this.missionInfoService = missionInfoService;
	}
	
	public ModelAndView missionLanguageList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long missionId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		String missionName = missionInfoService.getMissionNameById(missionId);
		long sourceCount = missionInfoService.getMissionCountSource(missionId);
		request.setAttribute("rminId", missionId);
		request.setAttribute("sourceCount", sourceCount);
		request.setAttribute("missionName", missionName);
		String folder = ServletRequestUtils.getStringParameter(request, "folder","build");
		return new ModelAndView("resource/"+folder+"/missionLanguage");
	}
	
	public void missionLanguageListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		long missionId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		String usercolumn = ServletRequestUtils.getStringParameter(request, "usercolumn","");
		String jsondata = missionLanguageTempService.getMissionLanguageList(missionId,usercolumn, start, limit);
		
		out.write(jsondata);
		out.flush(); 
	}
	
	//保存作业其它语加工者
	public void saveMission(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long missionId = ServletRequestUtils.getLongParameter(request, "missionid", 0);
		long userid = ServletRequestUtils.getLongParameter(request, "userid", 0);
		long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);
		int statu = ServletRequestUtils.getIntParameter(request, "statu", 1);
		boolean b = missionLanguageTempService.saveMissionLanguageAuthor(missionId, languageId, statu, userid);
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	public void missionLanguageCheckedListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		long missionId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		short statu = Short.parseShort(ServletRequestUtils.getStringParameter(request, "statu", "0"));
		List<Object> list = missionLanguageTempService.getMissionLanguageByJson(pagination, missionId, statu);
		String[] keys = new String[]{"rmlaId","missionId","languageId","statu","assessor","proctime","assetime","isfirst","languageName","assessorName","sourceNum" };
		
		
		String jsondata = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults()+"");
		out.write(jsondata);
		out.flush();
	}
	
	/**
	 * 领取作业校验任务
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getCheckedMission(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long missionId = ServletRequestUtils.getLongParameter(request, "missionid", 0);
		long userid = ServletRequestUtils.getLongParameter(request, "userid", 0);
		long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);
		int model = ServletRequestUtils.getIntParameter(request, "model", 0);
		boolean b = missionInfoService.setMissionUser(model, userid, missionId, languageId);
		
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

}
