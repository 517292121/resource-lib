package cn.com.adtech.riss2.controller.console.resource.build;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BuildController extends MultiActionController{


	public ModelAndView SelectMission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		return new ModelAndView("resource/build/input_select_mission");
	}
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		return new ModelAndView("resource/build/input_select_mission");
	}

}
