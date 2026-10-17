package cn.com.adtech.riss2.bso.count;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;

import com.jbetter.framework.session.Pagination;

/**
 * 后台统计报表相关功能服务类
 * 
 * @author MaChao
 * @date 2011-1-12
 */
public interface ISysCountService extends ICommonService {
	/**
	 * 获得站点访问量统计列表
	 * 
	 * @author MaChao
	 * @date 2011-1-122
	 * @param beginYear
	 * @param endYear
	 * @return
	 */
	public List getAccessCounterList(int beginYear,int endYear);
	
	/**
	 * 获取资源分布列表
	 * 
	 * @param endTime
	 * @return
	 * @author MaChao
	 * @date 2011-1-17
	 */
	public Object[] getResourceDistributingCountList(String endTime,String sort,String dir,Pagination pagination,String type);
	
	/**
	 * 说明：得到会员上传资源统计列表
	 *@param page 分页器
	 *@return list
	 *@author MaChao
	 *@date 2011-1-12
	 */
	public Wrapper getUploadResourcesCountList(PageExtNative page);
	
	/**
	 * 说明：会员登录统计列表
	 *@param page 分页器
	 *@return Wrapper
	 *@author MaChao
	 *@date 2011-1-14
	 */
	public Wrapper getUserLoginCountList(PageExtNative page);
	
	
	/**
	 * 说明：资源使用情况统计
	 *@param page 分页器
	 *@return Wrapper
	 *@author MaChao
	 *@date 2011-1-19
	 */
	public Wrapper getResourcesUsedCountList(PageExtNative page);
	
	
	/**
	 * 说明：资源增长情况统计
	 * @param rmstId
	 * @param beginYear
	 * @param endYear
	 * @return
	 */
	public String getResourceAddInfoJson(long rmstId, String beginYear,
			String endYear, int beginNum, int pageNum, String orderType,
			String orderColumn);
	
	/**
	 * 获取资源增长数据集合
	 * @param rmstId
	 * @param beginYear
	 * @param endYear
	 * @return
	 */
	public List<String[]> getResourceAddInfoList(long rmstId, String beginYear, String endYear);
	
	/**
	 * 资源工作量统计
	 * @param page
	 * @return
	 * @author MaChao
	 * @date 2011-1-20
	 */
	public Wrapper getResourcesHandlerCountList(PageExtNative page);
	/**
	 * 特色库资源访问量统计
	 * @param page
	 * @return
	 * @author MaChao
	 * @date 2011-6-15
	 */
	public String getRisMetaStoreJson(Pagination pagination , String  sort,String dir, int year, int month);

	/**
	 * 特色库详细资源访问量统计
	 * @param page
	 * @return
	 * @author MaChao
	 * @date 2011-6-15
	 */
	public String getRisMetaStoreTwoJson(Pagination pagination ,Long MetaId,int year,int month, String sort,String dir);
	
	/**
	 * 获得用户积分统计
	 */
	public String getPointcountToJson(Pagination pagination,String loginname,String realname,String sort,String dir);
	
	/**
	 * 用户操作统计列表
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 12:56:51 PM
	 */
	public Wrapper getUserAccessCountList(PageExtNative page);
	
	/**
	 * 用户操作日志列表-json
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 3:20:00 PM
	 */
	public String getLoglistJson(Pagination pagination,String name,String type,String beginD,String endD,String orderColumn,String orderType);
	
	/**
	 * 用户操作日志列表-obj
	 *
	 * @author MaChao
	 * @date Dec 1, 2015
	 * @time 11:34:42 AM
	 */
	public List getLoglistObj(Pagination pagination, String name,String type,String beginD,String endD,String orderColumn,String orderType);
	
}
