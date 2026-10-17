package cn.com.adtech.riss2.controller.dataapi;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.sys.SysApiData;
import cn.com.adtech.riss2.exception.ErrorMessageException;


/**
 *
 * 说明：接口创建、修改可视化管理
 * 由于该模块只有简单的数据库操作，因此未单独开发数据服务类，采用CommonService完成
 *
 * 创建时间：2019-11-1 下午3:19:38
 *
 * 创建者：王林
 *
 */
@Controller
public class ApiManagerController {
	
	@Resource(name="commonService")
	private ICommonService commonService;

	//API接口列表
	@RequestMapping(value="/dataapi/list")
	public String apiList(HttpServletRequest request){
		getList(request);
		return "platform/api/list";
	}
	
	//获取数据列表，并保存到request
	private void getList(HttpServletRequest request){
		String hql = "from SysApiData sada order by sada.sadaIndex ASC";
		List<SysApiData> apiList = this.commonService.find(hql);
		
		List<SysApiData> ty_list = new LinkedList<SysApiData>();
		List<SysApiData> dz_list = new LinkedList<SysApiData>();
		List<SysApiData> qt_list = new LinkedList<SysApiData>();
		
		for(SysApiData api : apiList){
			String t = api.getSadaType();
			if(t.equals("通用接口")){
				ty_list.add(api);
			}else if(t.equals("定制接口")){
				dz_list.add(api);
			}else{
				qt_list.add(api);
			}
		}
		
		request.setAttribute("ty_list", ty_list);
		request.setAttribute("dz_list", dz_list);
		request.setAttribute("qt_list", qt_list);
		request.setAttribute("apiList", apiList);
		request.setAttribute("counts", apiList == null ? 0 : apiList.size());
	}
	
	//新增API
	@RequestMapping(value="/dataapi/add")
	public String apiAdd(){
		return "platform/api/add";
	}
	
	//修改API
	@RequestMapping(value="/dataapi/edit/{sadaId}")
	public String apiAdd(@PathVariable Long sadaId, HttpServletRequest request){
		SysApiData apidata = (SysApiData) this.commonService.get(SysApiData.class, sadaId);
		request.setAttribute("apidata", apidata);
		return "platform/api/add";
	}
	
	//API接口说明文档
	@RequestMapping(value="/dataapi/save")
	public String apiSave(Model model,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Long sadaId = ServletRequestUtils.getLongParameter(request, "sadaId",0L);
		String sadaType = ServletRequestUtils.getStringParameter(request, "sadaType","");
		
		String sadaName = ServletRequestUtils.getStringParameter(request, "sadaName","");
		String sadaReqUrl = ServletRequestUtils.getStringParameter(request, "sadaReqUrl","");
		String sadaDemoUrl = ServletRequestUtils.getStringParameter(request, "sadaDemoUrl","");
		String sadaReqType = ServletRequestUtils.getStringParameter(request, "sadaReqType","");
		String sadaReqParams = ServletRequestUtils.getStringParameter(request, "sadaReqParams","");
		String sadaResult = ServletRequestUtils.getStringParameter(request, "sadaResult","");
		String sadaRemark = ServletRequestUtils.getStringParameter(request, "sadaRemark","");
		String sadaUpdateRemark = ServletRequestUtils.getStringParameter(request, "sadaUpdateRemark","");
		int sadaIndex = ServletRequestUtils.getIntParameter(request, "sadaIndex", 0);
		
		SysApiData api = null;
		
		try {
			if(sadaId < 1L){
				api = new SysApiData();
			}else{
				api = (SysApiData) this.commonService.get(SysApiData.class, sadaId);
			}
			api.setSadaType(sadaType);
			api.setSadaName(sadaName);
			api.setSadaReqUrl(sadaReqUrl);
			api.setSadaDemoUrl(sadaDemoUrl);
			api.setSadaReqType(sadaReqType);
			api.setSadaReqParams(sadaReqParams);
			api.setSadaResult(sadaResult);
			api.setSadaRemark(sadaRemark);
			api.setSadaIndex(sadaIndex);
			api.setSadaLastUpdate(new Date());
			api.setSadaUpdateRemark(sadaUpdateRemark);
			
			if(!CommonUtil.isNotNull(sadaName)){
				throw new ErrorMessageException("请录入接口名称！");
			}
			if(!CommonUtil.isNotNull(sadaReqUrl)){
				throw new ErrorMessageException("请录入接口请求地址！");
			}
			
			this.commonService.save(api);
			request.setAttribute("message", "<script>alert('保存成功')</script>");
			return apiList(request);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("apidata", api);
			request.setAttribute("message", "<script>alert('保存失败:"+e.getMessage()+"')</script>");
			return apiAdd();
		}
	}
	
	//修改API
	@RequestMapping(value="/dataapi/del/{sadaId}")
	public String apiDel(@PathVariable Long sadaId, HttpServletRequest request){
		String hql = "delete from SYS_API_DATA sada where SADA_ID="+sadaId;
		this.commonService.executeUpdate(hql);
		return apiList(request);
	}
	

	//API接口说明文档
	@RequestMapping(value="/dataapi/doc")
	public String apiDoc(HttpServletRequest request){
		getList(request);
		return "platform/api/doc";
	}
}
