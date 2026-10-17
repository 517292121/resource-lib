package cn.com.adtech.riss2.bso.resource;

import java.util.*;

import cn.com.adtech.riss2.domain.resource.ResLiterType;

public interface ISourceTypeService {
	public boolean saveSourceType(ResLiterType rlty);

	public boolean deleteSourceType(long typeId);
	
	/**
	 * 判断该类型是否被引用
	 * @param typeId
	 * @return
	 */
	public boolean getExit(long typeId);
	
	public String getSourceTypeName(long typeId);
	
	/**
	 * 根据资源类别获取所有的资源类型
	 * @author MaChao
	 * @param type
	 * @return
	 */
	public List<ResLiterType> getAllTypesByRltyType(Short type);
	
	/**
	 * 获取所有的资源类型
	 * @author MaChao
	 * @return
	 */
	public List<ResLiterType> getAllResTypes();
}
