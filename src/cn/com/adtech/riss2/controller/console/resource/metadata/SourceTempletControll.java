package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.resource.ResElementPool;

public class SourceTempletControll extends MultiActionController {
	private IMetadataElementPoolService elementPool = null;

	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	/**
	 * 弹出新增元素界面
	 */
	public ModelAndView OnCreateElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long eleType = MyRequestUtil.getLongParameter(request, "eleType");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		long parentId = MyRequestUtil.getLongParameter(request, "parentId");
		request.setAttribute("eleType", eleType);
		request.setAttribute("rltyId", sourceType);
		request.setAttribute("parentId", parentId);
		return new ModelAndView("resource/metadata/CreateElement");
	}

	/**
	 * 新增自定义扩展元素
	 */
	public ModelAndView CreateElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResElementPool element = ParseElement(request);
		// 将换行符替换为逗号保存到数据库
		String code = element.getRepoCode();
		String resultCode = "";
		if(ServiceUtil.isNotNull(code)) {
			String[] codeArr;
			if(code.contains("\r\n"))
				codeArr = code.split("\r\n");
			else
				codeArr = code.split("\n");
			for(int i = 0; i < codeArr.length; i++) {
				resultCode += codeArr[i];
				if(i < codeArr.length - 1) {
					resultCode += ",";
				}
			}
		}
		element.setRepoCode(resultCode);
		try {
			Serializable s = elementPool.saveElement(element);
			long id = Long.parseLong(s.toString());
			if(id > 0) {
				//request.setAttribute("result", "<span style='color:blue'>保存成功</span>");
				request.setAttribute("result", "<script>alert('保存成功！');window.close();</script>");
				request.setAttribute("eleId", id);
			}else {
				request.setAttribute("result", "<span style='color:red'>保存失败!</span>");
				request.setAttribute("eleId", 0);
			}
		}catch(Exception e) {
			request.setAttribute("result", "<span style='color:red'>保存失败:" + e.getMessage() + "</span>");
			request.setAttribute("eleId", 0);
		}
		return OnCreateElement(request, response);
	}

	/**
	 * 弹出修改元素界面
	 */
	public ModelAndView OnEditElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long eleid = MyRequestUtil.getLongParameter(request, "eleId");
		ResElementPool element = elementPool.getElementById(eleid);
		request.setAttribute("element", element);
		return new ModelAndView("resource/metadata/EditElement");
	}

	/**
	 * 新增自定义扩展元素
	 */
	public ModelAndView UpdateElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResElementPool element = ParseElement(request);
		// 将换行符替换为逗号保存到数据库
		String code = element.getRepoCode();
		String resultCode = "";
		if(ServiceUtil.isNotNull(code)) {
			String[] codeArr;
			if(code.contains("\r\n"))
				codeArr = code.split("\r\n");
			else
				codeArr = code.split("\n");
			for(int i = 0; i < codeArr.length; i++) {
				resultCode += codeArr[i];
				if(i < codeArr.length - 1) {
					resultCode += ",";
				}
			}
		}
		element.setRepoCode(resultCode);
		try {
			boolean b = elementPool.updateElement(element);
			if(b) {
				request.setAttribute("result", "<span style='color:blue'>修改成功!!</span>");
				request.setAttribute("eleId", 1);
			}else {
				request.setAttribute("result", "<span style='color:red'>修改失败...</span>");
				request.setAttribute("eleId", 0);
			}
		}catch(Exception e) {
			request.setAttribute("result", "<span style='color:red'>保存失败:" + e.getMessage() + "</span>");
			request.setAttribute("eleId", 0);
		}
		return OnEditElement(request, response);
	}

	/**
	 * 根据request请求封装元素对象
	 */
	public ResElementPool ParseElement(HttpServletRequest request) {
		long eleId = MyRequestUtil.getLongParameter(request, "eleId");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		String attName = MyRequestUtil.getStringParameter(request, "attName").trim();
		String attTitle = MyRequestUtil.getStringParameter(request, "attTitle").trim();
		long eleType = MyRequestUtil.getLongParameter(request, "eleType");
		String attCode = MyRequestUtil.getStringParameter(request, "attCode");
		String attRestrict = MyRequestUtil.getStringParameter(request, "attRestrict");
		long parentId = MyRequestUtil.getLongParameter(request, "parentId");
		int inputType = ServletRequestUtils.getIntParameter(request, "inputType", 0);
		int repoCalis = ServletRequestUtils.getIntParameter(request, "repoCalis", 0);
		
		int repoIsShowList = ServletRequestUtils.getIntParameter(request, "repoIsShowList", 0);
		int repoIndex = ServletRequestUtils.getIntParameter(request, "repoIndex", 0);
		
		ResElementPool element = new ResElementPool();
		element.setRepoId(eleId);
		element.setRepoPltyId(sourceType);
		element.setRepoName(attName);
		element.setRepoTitle(attTitle);
		element.setRepoType(eleType);
		element.setRepoCode(attCode);
		element.setRepoRestrict(attRestrict);
		element.setRepoSerianum(0);
		element.setRepoParent(parentId);
		element.setRepoDefaultdata(ServletRequestUtils.getStringParameter(request, "attDefaultdata",""));
		element.setRepoShowmode(ServletRequestUtils.getIntParameter(request, "repoShowmode",0));
		element.setRepoCalis(repoCalis);
		
		element.setRepoIsShowList(repoIsShowList);
		element.setRepoIndex(repoIndex);
		
		element.setRepoDatatype(inputType);
		String optionStr = "";
		if(inputType == 1){
			String inputOptions = ServletRequestUtils.getStringParameter(request, "dataType","");
			if(ServiceUtil.isNotNull(inputOptions)) {
				String[] optionArr;
				if(inputOptions.contains("\r\n"))
					optionArr = inputOptions.split("\r\n");
				else
					optionArr = inputOptions.split("\n");
				for(int i = 0; i < optionArr.length; i++) {
					optionStr += optionArr[i];
					if(i < optionArr.length - 1) {
						optionStr += ",";
					}
				}
			}
		}
		element.setRepoDataOptions(optionStr);
		return element;
	}
}