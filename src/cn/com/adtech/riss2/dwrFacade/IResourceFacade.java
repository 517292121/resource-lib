package cn.com.adtech.riss2.dwrFacade;

import javax.servlet.http.HttpServletRequest;

import com.jbetter.framework.session.Pagination;

/**
 * @author MaChao
 * @date 2010-10-19
 */
public interface IResourceFacade {
	/**
	 * 添加学科分类
	 * 
	 * @param rstyName
	 * @param request
	 * @return
	 * @author MaChao
	 * @date 2010-10-19
	 */
	public String addResSubjectType(Long tempId, Long pId , String rstyName);
	
	/**
	 * 删除学科分类
	 * 
	 * @param rstyId
	 * @param request
	 * @author MaChao
	 * @date 2010-10-19
	 */
	public String deleteResSubjectType(Long rstyId);

	/**
	 * 更新学科分类
	 * 
	 * @param rstyId
	 * @param rstyName
	 * @param request
	 * @author MaChao
	 * @date 2010-10-19
	 */
	public String updateResSubjectType(Long rstyId , String rstyName);
	
	/**
	 *  操作分类排序
	 * 
	 * @param rstyId
	 * @param type ： up 提前，down置后；
	 * @return
	 * @author MaChao
	 * @date 2010-10-20
	 */
	public String handlerResSubjectTypeIndex(Long rstyId,String type);
	
	/**
	 * 新增自定义分类
	 * @param tempId
	 * @param pId
	 * @param rstyId
	 * @param rdtyName
	 * @return
	 * @author MaChao
	 * @date 2010-10-21
	 */
	public String addResdynamicType(Long tempId,Long pId ,Long rmstId , String rdtyName, int rdtyType);
	
	/**
	 * 更新自定义分类
	 * @param rdtyId
	 * @param rdtyName
	 * @return
	 * @author MaChao
	 * @date 2010-10-21
	 */
	public String updateResDynamicType(Long rdtyId , String rdtyName, int rdtyType);
	
	/**
	 * 删除自定义分类
	 * @param rdtyId
	 * @return
	 * @author MaChao
	 * @date 2010-10-21
	 */
	public String deleteResDynamicType(Long rdtyId);
	
	/**
	 * 更新自定义分类排序
	 * @param rdtyId
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-10-21
	 */
	public String handlerResDynamicTypeIndex(Long rdtyId,String type, int rdtyType);
	
}
