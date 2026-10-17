package cn.com.adtech.riss2.bso.restaginfo;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;

/**
 * 个性tag
 * @author MaChao
 *
 */
public class ResTagInfoServiceImp extends CommonServiceCommonlyImp implements IResTagInfoService {

	@Override
	public Long addTag(ResTagInfo resTagInfo) {
		this.save(resTagInfo);
		return resTagInfo.getRtinId();
	}

	@Override
	public List<ResTagInfo> getResTagInfosBySubject(String subject, Pagination pagination) {
		String hql = "select tag from ResTagInfo tag where tag.rtinSubject like ?";
		return pagination == null ? 
				this.getHibernateTemplate().find(hql, "%" + subject + "%") : 
				this.getPageHibernateTemplate().find(pagination, hql, "%" + subject + "%");
	}

}
