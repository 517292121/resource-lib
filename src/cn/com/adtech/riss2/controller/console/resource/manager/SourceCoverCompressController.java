package cn.com.adtech.riss2.controller.console.resource.manager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ImgHandleUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 资源封面压缩控制器
 *      由于为了后期方便进行对老项目升级，故改动的文件最少最方便，本控制器不涉及数据库修改。
 *          故查询全部在controller层，不在bso层中新增接口或者方法。
 * @author MaChao
 * @date    2020/12/14
 * @version 1.0
 *
 */
public class SourceCoverCompressController extends MultiActionController {
    // 是否正在运行
    private volatile boolean run_status = false;
    
    // 图片总数
    private volatile int cover_count = 0;
    
    // 当前执行条数
    private volatile int run_index = 0;
    
	private IResourceDataService resourceService = null;
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * 异步获取作业列表json数据，前台做下拉筛选
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getResMissionInfoJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType("text/html; charset=UTF-8");
	    PrintWriter out = response.getWriter();
		long storeId = MyRequestUtil.getLongParameter(request, "RSDA_RMST_ID");
		long sourceType = MyRequestUtil.getLongParameter(request, "RSDA_RLTY_ID");
		
		List<Object[]> dataList = new ArrayList<Object[]>(0);
		if(storeId > 0L) {
		    StringBuffer sb = new StringBuffer("select rmin.rminId");
		    sb.append(" ,rmin.rminMissname");
		    sb.append(" from ResMissionInfo rmin");
		    // 所属资源库
		    sb.append(" where rmin.rminRmstId = "+storeId);
		    // 资源类型
		    if(sourceType > 0L) {
		        sb.append(" and rmin.rminRltyId = "+sourceType);
		    }
		    dataList =  this.resourceService.find(sb.toString());
		    sb = null;
		}
		out.print(JSONArray.fromObject(dataList).toString());
		out.flush();
	}

	/**
	 * 压缩开始
	 * @param request
	 */
	public void compressStart(HttpServletRequest request, HttpServletResponse response) {
	    response.setContentType("text/html; charset=UTF-8");
	    long rmstId = MyRequestUtil.getLongParameter(request, "rmstId");
        long rltyId = MyRequestUtil.getLongParameter(request, "rltyId");
        long rminId = MyRequestUtil.getLongParameter(request, "rminId");
        int minWidth = MyRequestUtil.getIntParameter(request, "minWidth");
        int minHeight = MyRequestUtil.getIntParameter(request, "minHeight");
        
        // 由于项目的默认图片大小为180*250，故封面尺寸也不能小于180*250
        if(minWidth < 180 || minHeight < 250 || run_status) {
            return;
        }
        
        try {
            int curr = 0;
            run_index = 0;
            cover_count = 0;
            run_status = true;
            List<String> fileList = null;
            Pagination p = new Pagination(1000);
            String hql = splicingHql(rmstId, rltyId, rminId);
            do {
                p.setCurrentPage(curr);
                fileList = this.resourceService.getServicePageHibernateTemplate().find(p, hql);
                cover_count = cover_count < 1 ? p.getTotalResults():cover_count;
                for(String path:fileList) {
                    run_index++;
                    if(path == null || path.trim().isEmpty()) {
                        continue;
                    }
                    coverCompress(path, minWidth, minHeight);
                }
                curr++;
            } while(curr < p.getTotalPages());
        } finally {
            run_status = false;
        }
	}
	
	/**
	 * 拼接查询hql，只查询文件地址字段
	 * @param rmstId   特色库ID
	 * @param rltyId   资源类型ID
	 * @param rminId   作业ID
	 * @return
	 */
	private String splicingHql(long rmstId, long rltyId, long rminId) {
	    StringBuffer sb = new StringBuffer("select rsda.rsdaFileUrl");
	    sb.append(" from ResSourceData rsda");
	    sb.append(" where rsda.rsdaSourceType > 0");
	    
	    if(rmstId > 0L) {
	        sb.append(" and rsda.rsdaRmstId = "+rmstId);
	    }
	    if(rltyId > 0L) {
	        sb.append(" and rsda.rsdaRltyId = "+rltyId);
	    }
	    if(rminId > 0L) {
	        sb.append(" and rsda.rsdaRminId = "+rminId);
	    }
	    return sb.toString();
	}
	
	/**
	 * 根据路径等比例压缩图片大小
	 * @param path         图片路径
	 * @param minWidth     压缩后的宽
	 * @param minHeight    压缩后的高
	 */
	private void coverCompress(String path, int minWidth, int minHeight) {
	    if(path == null || path.trim().isEmpty()) {
            return;
        }
	    // 根据路径获取cover.jpg
	    File cover = new File(path, "cover.jpg");
	    if(cover == null || !cover.exists() || !cover.isFile()) {
	        return;
	    }
	    // 压缩
	    ImgHandleUtil.pictureHandle(cover.getAbsolutePath(), minWidth, minHeight, false);
	}
	
	/**
	 * 获取压缩进度
	 * @throws IOException 
	 */
	public void getSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", run_index);
        map.put("count", cover_count);
        out.print(JSONObject.fromObject(map).toString());
        out.flush();
	}
}
