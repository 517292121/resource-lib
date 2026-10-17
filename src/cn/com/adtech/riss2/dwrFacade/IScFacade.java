package cn.com.adtech.riss2.dwrFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IScFacade {
	/**
	 * 添加分类
	 * 
	 * @param scftName
	 * @param request
	 * @return
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public String addScFaqType(Long tempId, Long pId, String scftName);

	/**
	 * 删除分类
	 * 
	 * @param scftId
	 * @param request
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public String deleteScFaqType(Long scftId);

	/**
	 * 更新分类
	 * 
	 * @param scftId
	 * @param scftName
	 * @param request
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public String updateScFaqType(Long scftId, String scftName);

	/**
	 * 操作分类排序
	 * 
	 * @param scftId
	 * @param type ：
	 *            up 提前，down置后；
	 * @return
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public String handlerScFaqTypeIndex(Long scftId, String type);
	
	/**
	 * 添加求助分类
	 * 
	 * @param tempId
	 * @param pId
	 * @param scftName
	 * @return
	 * @author MaChao
	 * @date 2010-11-8
	 */
	public String addScIssueType(Long tempId, Long pId, String scitName);
	
	/**
	 * 删除求助分类
	 * 
	 * @param scitId
	 * @return
	 * @author MaChao
	 * @date 2010-11-8
	 */
	public String deleteScIssueType(Long scitId);
	
	/**
	 * 更新求助分类
	 * @param scitId
	 * @param scitName
	 * @return
	 * @author MaChao
	 * @date 2010-11-8
	 */
	public String updateScIssueType(Long scitId, String scitName);
	
	/**
	 * 操作求助分类排序
	 * 
	 * @param scftId
	 * @param type ：
	 *            up 提前，down置后；
	 * @return
	 * @author MaChao
	 * @date 2010-11-8
	 */
	public String handlerScIssueTypeIndex(Long scitId, String type);
	
	/**
	 * 添加咨询分类
	 * @param tempId
	 * @param pId
	 * @param scitName
	 * @return
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public String addScConsultationType(Long tempId, Long pId, String scitName);
	
	/**
	 * 删除咨询分类
	 * @param scitId
	 * @return
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public String deleteScConsultationType(Long scitId);
	
	/**
	 * 更新咨询分类
	 * @param scitId
	 * @param scitName
	 * @return
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public String updateScConsultationType(Long scitId, String scitName);
	
	/**
	 * 处理排序
	 * @param scitId
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public String handlerScConsultationTypeIndex(Long scitId, String type);
	
	/**
	 * 添加信息分类
	 * 
	 * @param tempId
	 * @param pId
	 * @param idstName
	 * @return
	 * @author MaChao
	 * @date 2010-11-23
	 */
	public String addInformationType(Long tempId, Long pId, String idstName);
	
	/**
	 * 删除信息分类
	 * 
	 * @param idstId
	 * @return
	 * @author MaChao
	 * @date 2010-11-23
	 */
	public String deleteInformationType(Long idstId);
	
	/**
	 * 更新信息分类
	 * 
	 * @param idstId
	 * @param idstName
	 * @return
	 * @author MaChao
	 * @date 2010-11-23
	 */
	public String updateInformationType(Long idstId, String idstName);
	
	/**
	 * 更新信息分类排序
	 * 
	 * @param scitId
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-23
	 */
	public String handlerInformationTypeIndex(Long idstId, String type);
	
	/**
	 * 获取文献求助回复信息
	 * 
	 * @param currentPage
	 * @param scisId
	 * @param request
	 * @param response
	 * @return
	 * @author MaChao
	 * @date 2010-11-24
	 */
	public String getIssueReplyList(int currentPage, Long scisId, HttpServletRequest request, HttpServletResponse response);
}
