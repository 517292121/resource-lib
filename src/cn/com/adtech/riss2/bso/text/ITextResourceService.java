package cn.com.adtech.riss2.bso.text;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;

public interface ITextResourceService extends ICommonService {
    /**
     * 获取资源列表
     *
     * @author MaChao
     * @date Nov 18, 2015
     *
     * @param pagination
     * @param paramMap
     * @return
     */
    public List<Object[]> getResourceList(Pagination pagination, Long rtliId, Long rlclId, String keyStr, String key, String val, String sort, String dir);

    /**
     * 设置资源是否推荐
     */
    public boolean updateRcmdStatus(String sourceIdlist);
    
    /**
     * 设置资源是否启用
     */
    public boolean updateUseStatus(String sourceIdlist);
    
    /**
     * 获取资源json数据
     * 
     * @athor lichangkun
     * 2017-10-29
     */
    public String getJsonByType(Long store);
    
    /**
     * 获取文本库语种类型
     * @param rtliId
     * @return
     */
    public String getLibLanguage(Long rtliId);
}
