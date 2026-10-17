package cn.com.adtech.riss2.bso.sc;

import java.util.List;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.sc.ScIssue;

import com.jbetter.framework.session.Pagination;

/**
 * 服务管理
 * 
 * @author MaChao
 * @date 2010-11-1
 */
@SuppressWarnings("unchecked")
public interface IScService extends ICommonService {
	/**
	 * 获取求助分类列表
	 * 
	 * @param idstName
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-11-1
	 */
	List getScIssueTypeList(String scitName, String sort, String dir);

	/**
	 * 获取求助分类列表
	 * 
	 * @return ScIssueType对象
	 * @author MaChao
	 * @date 2010-11-2
	 */
	List getScIssueTypeList();

	/**
	 * 获取求助回复
	 * 
	 * @param pagination
	 * @param scisId
	 * @return
	 * @author MaChao
	 * @date 2010-11-2
	 */
	List getScIssueReplyList(Pagination pagination, Long scisId, Long scirId,String type);

	/**
	 * 获取求助信息列表
	 * 
	 * @param pagination
	 * @param scitId
	 *            求助类型
	 * @param condition
	 *            关键字
	 * @param scisState
	 *            状态
	 * @return
	 * @author MaChao
	 * @date 2010-11-2
	 */
	List getScIssueList(Pagination pagination, Long scitId, String condition, Long scisState, String sort, String dir);

	/**
	 * 获取求助回复信息列表
	 * 
	 * @param pagination
	 * @param scitId
	 *            求助类型
	 * @param condition
	 *            关键字
	 * @param scisGrade
	 *            满意度
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-11-3
	 */
	List getScIssueReplyList(Pagination pagination, Long scitId, String condition, Short scisGrade, String sort, String dir);

	/**
	 * 获取FAQ库分类列表
	 * 
	 * @param scftId
	 *            FAQ库ID
	 * @param scftParentId
	 *            上级ID
	 * @param scftName
	 *            分类名称
	 * @param scftGrade
	 *            级数
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-4
	 */
	List getScFaqTypeList(Long scftId, Long scftParentId, String scftName, Long scftGrade, String type, Long scftIndex);

	/**
	 * 获取FAQ列表
	 * 
	 * @param pagination
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-11-4
	 */
	List getScFaqLibraryList(Pagination pagination, String scflQuestion, String sort, String dir);

	/**
	 * 获取FAQ对象列表
	 * 
	 * @param pagination
	 * @param scftId
	 * @param scflQuestion
	 * @return
	 * @author MaChao
	 * @date 2010-11-11
	 */
	List getScFaqLibraryList(Pagination pagination, Long scftId, String scflQuestion);

	/**
	 * 求助分类
	 * 
	 * @param scitName
	 * @param scitIndex
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-8
	 */
	List getScIssueType(String scitName, Long scitIndex, String type);

	/**
	 * 前台页面获取求助列表
	 * 
	 * @param pagination
	 * @param scitId
	 *            求助类型
	 * @param suinId
	 *            求助人
	 * @param scisTitle
	 *            查询条件
	 * @param scisState
	 *            求助状态
	 * @return
	 * @author MaChao
	 * @date 2010-11-9
	 */
	List getScIssueList(Pagination pagination, Long scitId, Long suinId, String scisTitle, Long scisState, String type);

	/**
	 * 咨询分类列表
	 * 
	 * @param sctyName
	 * @param sctyIndex
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-15
	 */
	List getScConsultationType(String sctyName, Long sctyIndex, String type);

	/**
	 * @param pagination
	 * @param scitId
	 *            类型
	 * @param sccoTitle
	 *            标题
	 * @param suinLoginName
	 *            用户名
	 * @param scisState
	 *            状态
	 * @param sort
	 * @param dir
	 * @return
	 */
	List getScConsultationList(Pagination pagination, Long scitId, String sccoTitle, String suinLoginName, Long scisState, String sort,
			String dir,String type,Long suinId);
	
	/**
	 * 根据回复ID查看是否为最佳回复
	 * @param scirId
	 * @return
	 */
	ScIssue getScIssue(Long scirId);
}
