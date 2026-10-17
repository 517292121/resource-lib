package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.platform.ITextResourceService;
import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.json.DateJsonValueProcessor;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;
import cn.com.adtech.riss2.domain.text.ResTextResource;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 资源详细页面控制器
 *
 * @author MaChao
 *
 * @date：Jan 8, 2018
 */
@Controller
public class TextLibraryController {
	
	//资源收藏服务对象
	@Resource(name="textLibClassfiyService")
	private ITextLibClassfiyService textLibClassiService;
	
	//资源收藏服务对象
	@Resource(name="textResService")
	private ITextResourceService textResourceService;
	
	//按特色库进入资源列表
	@RequestMapping(value="/text/lib/{rtliId}")
	public String textResList(@PathVariable String rtliId, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Long numId = null;
		try {
			numId = Long.parseLong(rtliId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rtliId);
		}
		
		//获取资源库核心对象
		ResTextLibrary textLib = (ResTextLibrary) this.textResourceService.get(ResTextLibrary.class, numId);
		
		//获取该资源库的自定义分类
		List<ResLibraryClassify> classList = this.textLibClassiService.getLibClassfiyList(numId);
		
		//保存参数
		map.put("rtliId", rtliId);
		map.put("numId", numId);
		map.put("title", textLib.getRtliName());
		map.put("textLib", textLib);
		map.put("classList", classList);
		model.addAllAttributes(map);
		return "platform/pc/textResList";
	}
	
	//进入资源列表
	@RequestMapping(value="/text/reslist")
	public void getResList(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		
		//获取参数
		//分类ID
		Long rlclId = ServletRequestUtils.getLongParameter(request, "rlclId", 0L);
		Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
		int isCmd = ServletRequestUtils.getIntParameter(request, "isCmd",0); //是否获取推荐资源   1是 0不是 即所有
		if(!CommonUtil.isNotNull(sort)){
			sort = "rtreDate";
		}
		
		//分页设置
		int pagesize = ServletRequestUtils.getIntParameter(request, "pagesize", 0);
		pagesize = pagesize < 1 ? Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize())) : pagesize;
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination pagination = new Pagination();
		pagination.setPageResults(pagesize);
		pagination.setCurrentPage(currentPage);
		
		
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		try {
			List<Object[]> dataList = this.textResourceService.getResourceList(pagination, rtliId, rlclId, sort,isCmd);
			map.put("success", true);
			map.put("currentPage", pagination.getCurrentPage());
			map.put("totalPage", pagination.getTotalPages());
			map.put("totalLine", pagination.getTotalResults());
			map.put("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map, config).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//按特色库进入资源列表
	@RequestMapping(value="/text/{rtreId}")
	public String resDetail(@PathVariable String rtreId, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Long numId = null;
		try {
			numId = Long.parseLong(rtreId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rtreId);
		}
		//获取资源库核心对象
		ResTextResource resource = (ResTextResource) this.textResourceService.get(ResTextResource.class, numId);
		
		if(resource.getRtreUsed() == 0L){
			map.put("errormsg", "对不起，该资源已禁用！");
			model.addAllAttributes(map);
			return "platform/pc/error";
		}
		
		if(null != resource.getRtreRtliId() && resource.getRtreRtliId() > 0L){
			ResTextLibrary textLib = (ResTextLibrary)this.textResourceService.get(ResTextLibrary.class, resource.getRtreRtliId());
            map.put("textLib", textLib);
        }
		
		//更新点击量
		long viewNum = resource.getRtreAccess() == null ? 1 : resource.getRtreAccess() + 1;
		String hql = "update ResTextResource r set r.rtreAccess="+viewNum+" where r.rtreId=" + numId;
		this.textResourceService.getServiceHibernateTemplate().bulkUpdate(hql);
		
		//以下字段为textarea框输入，因此展示前需要转换一下格式
		resource.setRtreCol11(CommonUtil.hangleTextArea(resource.getRtreCol11()));
		resource.setRtreCol12(CommonUtil.hangleTextArea(resource.getRtreCol12()));
		
		map.put("title", resource.getRtreTitle());
		map.put("resource", resource);
		model.addAllAttributes(map);
		return "platform/pc/textResDetail";
	}
}
