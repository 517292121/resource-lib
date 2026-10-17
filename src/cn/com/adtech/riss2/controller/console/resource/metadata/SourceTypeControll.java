package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.ISourceTypeService;
import cn.com.adtech.riss2.bso.resource.MetadataElementPoolImp;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLiterType;

public class SourceTypeControll extends MultiActionController {
	
	private ISourceTypeService sourceTypeService = null;
	private IMetadataElementPoolService elementPool = null;
	private ICommonService commonService = null;
	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}
	public void setSourceTypeService(ISourceTypeService sourceTypeService) {
		this.sourceTypeService = sourceTypeService;
	}
	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	/**
	 * 进入资源类型列表
	 */
	public ModelAndView SourceTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/metadata/SourceTypeList");
	}

	/**
	 * 获取资源类型列表的JSON数据
	 */
	public ModelAndView SourceTypeListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rlty_id");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		String sql = "select rlty_id, rlty_name,rlty_code, rlty_type from RES_LITER_TYPE order by " + sort + " " + dir;
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String jsondata = commonService.getListToJson(sql, null, begin, pageNum);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 保存或修改资源类型名称
	 */
	public ModelAndView SaveSourceType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		long typeId = MyRequestUtil.getLongParameter(request, "RLTY_ID");
		String typeName = MyRequestUtil.getStringParameter(request, "RLTY_NAME");
		//添加了一个字段rlty_code
		String typeCode = MyRequestUtil.getStringParameter(request, "RLTY_CODE");
		short rlytType = Short.parseShort(ServletRequestUtils.getStringParameter(request, "rltyType","0"));
		String hql = "select rltyId from ResLiterType where rltyName='"+typeName+"'"; 
		long count = commonService.getCountNumber(hql);
		if(count > 0 && typeId < 1) {
			out.print("{success:false,msg:'" + typeName + " 已存在!'}");
		}else {
			ResLiterType rlty = new ResLiterType();
				if(typeId>0)
					rlty.setRltyId(typeId);
				rlty.setRltyName(typeName);
				rlty.setRltyCode(typeCode);
				rlty.setRltyType(rlytType);
			//调整了参数的传递方式
			boolean b = sourceTypeService.saveSourceType(rlty);
			if(b) {
				out.print("{success:true,msg:'" + typeName + " 保存成功!'}");
			}else {
				out.print("{success:false,msg:'" + typeName + " 保存失败!'}");
			}
		}
		return null;
	}

	/**
	 * 删除资源类型
	 */
	public ModelAndView DelSourceType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long typeId = MyRequestUtil.getLongParameter(request, "RLTY_ID");
		PrintWriter out = response.getWriter();
		boolean b = false;
		
		if(sourceTypeService.getExit(typeId)){
			sourceTypeService.deleteSourceType(typeId);
			out.print("删除成功");
		}else {
			out.print("该类型已关联的特色库,无法删除!");
		}
		return null;
	}

	/**
	 * 进入资源模板核心元数据编辑页面
	 */
	public ModelAndView OnTempletEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long typeId = MyRequestUtil.getLongParameter(request, "RLTY_ID");
		List<ResElementPool> elementList = elementPool.getElementList(typeId);
		request.setAttribute("rltyId", typeId);
		String typeName = sourceTypeService.getSourceTypeName(typeId);
		request.setAttribute("elementlist", elementList);
		request.setAttribute("rltyName", typeName);
		return new ModelAndView("resource/metadata/SourceTempletEdit");
	}
}