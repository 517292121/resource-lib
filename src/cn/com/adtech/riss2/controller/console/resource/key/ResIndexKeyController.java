package cn.com.adtech.riss2.controller.console.resource.key;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.resource.key.IResourceIndexKeyService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResIndexKey;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 检索词管理（后台） 控制器
 * 
 * @author MaChao
 * 
 * @date 2018-2-25
 */

public class ResIndexKeyController extends MultiActionController{
	
	/**
	 * 检索词服务
	 * 
	 * @author MaChao
	 * 
	 * @date 2018-2-25
	 */
	private IResourceIndexKeyService keyServices;
	public void setKeyServices(IResourceIndexKeyService keyServices) {
		this.keyServices = keyServices;
	}
	
	/**
	 * 初始化关键词管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2018-2-25
	 */
	public ModelAndView initKeyView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/key/indexKeyList");
	}
	
	/**
	 * 获取检索词信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2018-2-25
	 */
	public void getIndexKeyList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		String rikeName = ServletRequestUtils.getStringParameter(request, "rikeName","");
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rikeSearchCount");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(20);// 设置每页记录数
		
		// 查询条件
		List<ResIndexKey> list = this.keyServices.getIndexKeyList(pagination, rikeName, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"rikeId", "rikeName", "rikeSearchCount"},
				pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 根据id删除检索词信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2018-2-25
	 */
	public void delIndexKey(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		//获取检索词id
		Long rikeId = ServletRequestUtils.getLongParameter(request, "rikeId", 0);
		try {
			//根据检索词id获取检索词信息
			ResIndexKey rik = (ResIndexKey)this.keyServices.get(ResIndexKey.class, rikeId);
			if(rik == null){
				throw new ErrorMessageException("该检索词不存在！");
			}
			
			//删除检索词
			this.keyServices.delete(rik);
			out.write("{success:true,msg:'删除成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	/**
	 * 保存新增、修改检索词信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2018-2-26
	 */
	public void saveOrUpdateIndexKey(HttpServletRequest request,HttpServletResponse response)
										throws Exception{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long rikeId = ServletRequestUtils.getLongParameter(request, "rikeId",0);//检索词id
		String rikeName = ServletRequestUtils.getStringParameter(request, "rikeName","");//检索词
		int rikeSearchCount = ServletRequestUtils.getIntParameter(request, "rikeSearchCount",0);//检索量
		String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
		ResIndexKey rik = null;
		try {
			if(!CommonUtil.isNotNull(rikeName)){
				throw new ErrorMessageException("检索词不能为空！");
			}
			if(rikeName.length()>30){
				throw new ErrorMessageException("检索词长度不能超过30！");
			}
			if(rikeSearchCount>1000000000){
				throw new ErrorMessageException("检索量不能大于1000000000！");
			}
			//根据检索词查询
			boolean flg = this.keyServices.valIndexKey(rikeId, rikeName);
			//判断检索词是否存在
			if(flg){
				throw new ErrorMessageException("该检索词已存在！");
			}

			rik = new ResIndexKey();
			if("false".equals(isEditing)){
				/*** 新增 ***/
				rik.setRikeName(rikeName);
				rik.setRikeSearchCount(rikeSearchCount);
				this.keyServices.save(rik);
				out.write("{success:true,msg:'新增成功！'}");
			}else{
				/*** 编辑 ***/
				//根据id获取检索词信息
				rik = (ResIndexKey) this.keyServices.get(ResIndexKey.class, rikeId);
				//判断检索词是否存在
				if(rik == null){
					throw new ErrorMessageException("该检索词不存在！");
				}
				
				//写入数据
				rik.setRikeId(rikeId);
				rik.setRikeName(rikeName);
				rik.setRikeSearchCount(rikeSearchCount);
				this.keyServices.save(rik);
				out.write("{success:true,msg:'更新成功！'}");
			}
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	
	/**
	 * 获取详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2018-2-26
	 */
	public void getIndexKeyDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long rikeId = ServletRequestUtils.getLongParameter(request, "rikeId", 0);
		try {
			ResIndexKey rik = (ResIndexKey)this.keyServices.get(ResIndexKey.class, rikeId);
			if(rik == null){
				throw new ErrorMessageException("该检索词不存在！");
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rikeName", rik.getRikeName());
			result.put("rikeSearchCount", rik.getRikeSearchCount());
			result.put("rikeId", rik.getRikeId());
			result.put("success", true);
			String json = JSONObject.fromObject(result).toString();
			out.write(json);
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	
}
