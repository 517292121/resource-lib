package cn.com.adtech.riss2.bso.text;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.unionlib.lucene.bean.ResMainBean;

/**
 * 
 * @description 资源录入
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Nov 17, 201510:35:24 AM
 *
 */
public interface IResourceInputService extends ICommonService {
	
	/**
	 * 验证Excel数据
	 * 
	 * @athor lichangkun
	 * 2017-10-19
	 */
	public String checkImportData(HttpServletRequest request, File excelfile);
	
	/**
	 * 获取分类列表
	 */
	public Map<String, Long> getResLibClassfiyList(Long rtliId);
	
	/**
	 * 保存导入数据
	 * 
	 * @athor lichangkun
	 * 2017-10-19
	 */
	public String saveImportData(HttpServletRequest request, File excelfile);
	
	/**
	 * 将资源转换为索引数据对象
	 * @param res
	 */
	public ResMainBean textResToDatabean(ResTextResource res);
}
