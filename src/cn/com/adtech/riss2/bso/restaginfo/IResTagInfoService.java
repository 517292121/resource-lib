package cn.com.adtech.riss2.bso.restaginfo;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;

/**
 * 个性tag操作
 * @author MaChao
 *
 */
public interface IResTagInfoService extends ICommonService {
	
	Long addTag(ResTagInfo resTagInfo);
	
	List<ResTagInfo> getResTagInfosBySubject(String subject, Pagination pagination);

}
