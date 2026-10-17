package cn.com.adtech.riss2.bso.platform;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.LanguageUtil;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

/**
 * 首页缓存基本服务类
 *
 * @author MaChao
 *
 * @date 2011-09-05
 *
 */
public class PlatformBasicService {

	//////////////////////////////////////////// 基本操作方法 ///////////////////////
	/**
	 * 获取查询资源的基本通用条件,现已包含如下条件<br>
	 * 		资源审核状态为——审核通过<br>
	 * 		资源是否可用状态为——可用<br>
	 * 		资源所属特色库状态为——可用<br>
	 * 		资源所属作业状态为审核通过或资源来源类型大于2<br>
	 * 		资源权限为空为小于1(游客)<br>
	 * <br>
	 * @param ext 别名<br>
	 * @return 组装的hql
	 */
	final public static String getSearchCommonConditionByHql(String ext, boolean isCurrLan){
		StringBuffer buffer = new StringBuffer();

		//基本条件——资源要审核通过
		buffer.append(ext).append(".rsdaStatus=").append(ResSourceData.STATUS_PASS);

		//基本条件——资源是否可用状态为可用
		buffer.append(" and ").append(ext).append(".rsdaIsuse=").append(ResSourceData.ISUSE_USEABLENESS);

		//基本条件——资源所属特色库是否可用状态为可用
		buffer.append(" and ").append(ext).append(".rsdaRmstStatus=").append(ResMetaStore.STATUS_USEABLENESS);

		//基本条件——资源所属作业为审核通过或资源来源类型大于2(导入资源)即不属于系统加工资源
//		buffer.append(" and (").append(ext).append(".rsdaRminStatus=").append(ResMissionInfo.RMINSTATUS_COMMIT_PASS);
//		buffer.append(" or ").append(ext).append(".rsdaAuthortype>").append(ResSourceData.AUTHORTYPE_ADMIN_IMPORT).append(")");

		//基本条件——资源权限小于1(游客)或为null
//		buffer.append(" and (").append(ext).append(".rsdaRights is null or ").append(ext).append(".rsdaRights<2)");

		if(isCurrLan){
			String lan = PlatformCacheFactory.instance().getCurrLan("zh"); //默认语种
			lan = LanguageUtil.codeToTxt(lan);
			buffer.append(" and ").append(ext).append(".rsdaLanguage='").append(lan).append("'");
		}

		String hql = buffer.toString();
		buffer = null;

		return hql;
	}

}
