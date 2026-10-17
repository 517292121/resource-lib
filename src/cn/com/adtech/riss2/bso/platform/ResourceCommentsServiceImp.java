package cn.com.adtech.riss2.bso.platform;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;

public class ResourceCommentsServiceImp extends CommonServiceImp implements IResourceCommentsService{

	@Override
	public List<Object[]> getCommentsList(Long rsdaId, Long userId, Pagination p) {
		StringBuffer sb = new StringBuffer();
		sb.append("select rsco.sysUserinfo.suinLoginName,");
		sb.append("rsco.sysUserinfo.suinName,");
		sb.append("rsco.sysUserinfo.suinImageUrl,");
		sb.append("rsco.rscoContent,");
		sb.append("rsco.rscoStatus,");
		sb.append("rsco.rscoDate");
		sb.append(" from ResSourceComt rsco");
		sb.append(" where rsco.rscoRsdaId=").append(rsdaId);
		if(userId != null && userId > 0L){
			sb.append(" and (rsco.rscoStatus=").append(ResSourceComt.STATUS_PAST).append(" or rsco.rscoSuinId=").append(userId).append(")");
		}else{
			sb.append(" and rsco.rscoStatus=").append(ResSourceComt.STATUS_PAST);
		}
		sb.append(" order by rsco.rscoDate DESC, rsco.rscoId DESC");
		
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = getPageHibernateTemplate().find(p, hql);
		
		if(dataList == null || dataList.size() < 1){
			return null;
		}
		String cmd = null;
		for(Object[] data : dataList){
			cmd = data[3] == null ? "" : data[3].toString();
			if(CommonUtil.isNotNull(cmd)){
				cmd = CommonUtil.hangleTextArea(cmd);
				data[3] = cmd;
			}
		}
		return dataList;
	}

}
