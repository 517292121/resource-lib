package cn.com.adtech.riss2.bso.text;

import java.util.List;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;

/**
 * 资源库分类设置接口
 *   
 * ITextLibClassfiyService  
 *   
 * 2017-10-17 下午05:41:45  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public interface ITextLibClassfiyService extends ICommonService{

    /**
     * 根据ID获取分类列表
     * 
     * @athor lichangkun
     * 2017-10-17
     */
    public List<ResLibraryClassify> getLibClassfiyList(Long rtliId);
    
    /**
     * 查询资源库分类下拉选项
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public String getLibClassfiyOptions(Long rtliId, Long selectId);
    
    /**
     * 保存资源库分类信息
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public boolean saveLibClassfiy(Long rtliId,Long rstyId, Long rlclParentId, String rlclName, Integer rlclIndex);
    
    /**
     * 删除资源库分类
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public boolean deleteLibClassfiy(Long rlclId);
    /**
     * 根据ID获取分类列表
     * 
     */
    public List<Object[]> getLibClassfiyLists(Long rtliId);
}
