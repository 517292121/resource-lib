package cn.com.adtech.riss2.bso.text;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 文档资源库-接口
 *   
 * ITextLibraryService  
 *   
 * 2017-10-17 下午01:31:45  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public interface ITextLibraryService extends ICommonService {
    
    /**
     * 获取资源库列表
     * 
     * @athor lichangkun
     * 2017-10-17
     */
    public List<Object[]> getLibraryList(String sort, String dir, Pagination pagination);
    
    /**
     * 更新缓存
     */
    public void updateCache();
}
