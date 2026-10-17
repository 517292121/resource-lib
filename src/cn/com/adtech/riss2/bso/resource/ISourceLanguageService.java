package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;

/**
 * 资源翻译语种记录
 * @author MaChao
 *
 */
public interface ISourceLanguageService {

	/**
	 * 保存资源语种记录
	 * @param language
	 * @return
	 */
	public Serializable saveSourceLanguage(ResSourceLanguage language);
	
	public Serializable saveSourceLanguage(HttpServletRequest request, List<ResElementPool> elementList);
	
	
	/**
	 * 获取资源拥有的语种
	 * @param sourceId
	 * @return
	 */
	public List<ResLanguageType> getSourceOnLanguage(long sourceId);
	
	/**
	 * 获取资源没有拥有的语种
	 * @param sourceId
	 * @return
	 */
	public List<ResLanguageType> getSourceUnLanguage(long sourceId);
	
	/**
	 * 获取资源语种的下拉Options,
	 * @param sourceId
	 * @param haveType 类型，1：拥有的类型，2：不拥有的类型, 3:不拥有的，但包括选中的, 4:拥有的且审核通过的
	 * @return
	 */
	public String getSourceLanguageOption(long sourceId, int haveType, long selectid);
	
	/**
	 * 获取资源审核通过的第一语种ID
	 * @return
	 */
	public long getSourceFirstLanguage(long sourceId);	
	
	/**
	 * 获取资源已加工的语种列表
	 * @param sourceId
	 * @return
	 */
	public String getSourceLanguageListJson(long sourceId, int begin, int limit, String wheres);
	
	public ResLanguageType getLanguage(long languageId);
	
	/**
	 * 修改其它语语种ID
	 * @param sourceId
	 * @param preLanguage
	 * @param nowLanguage
	 * @return
	 */
	public boolean updateSourceLanguage(long sourceId, long preLanguage, long nowLanguage);
	
	/**
	 * 获取下一条翻译的资源ID
	 * @param rminId 作业ID
	 * @param languageId
	 * @return
	 */
	public long getNextResourceId(long rminId, long languageId);
}
