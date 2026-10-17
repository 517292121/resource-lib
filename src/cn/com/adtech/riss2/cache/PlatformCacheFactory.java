package cn.com.adtech.riss2.cache;

import java.util.ArrayList;
import java.util.List;

import cn.com.adtech.riss2.bean.ResCountBean;
import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * 平台数据缓存
 *
 * @author MaChao
 *
 * @date：Jan 5, 2018
 */
public class PlatformCacheFactory {

	private static PlatformCacheFactory self = null;
	
	//特色库列表
	private List<Object[]> metastoreList = new ArrayList<Object[]>();
	
	//文档类型数据库列表 ID-名称
	private List<Object[]> textstoreList = new ArrayList<Object[]>();
	
	//按资源类型存储的资源集合（用于默认系统首页精品推荐资源）
	private List<ResourceForTypeBean> resForTypeList = new ArrayList<ResourceForTypeBean>();
	
	//资源统计
	private List<ResCountBean> resCountList = new ArrayList<ResCountBean>();
	
	//信息分类
	private List<Object[]> infoClassList = new ArrayList<Object[]>();
	
	//平台语种
	private String currLan = null;
	
	private PlatformCacheFactory(){
	}

	public static PlatformCacheFactory instance(){
		if(self == null){
			self = new PlatformCacheFactory();
		}
		return self;
	}
	
	
	
	public List<Object[]> getMetastoreList() {
		return metastoreList;
	}

	public void setMetastoreList(List<Object[]> metastoreList) {
		this.metastoreList = metastoreList;
	}

	public List<Object[]> getTextstoreList() {
		return textstoreList;
	}

	public void setTextstoreList(List<Object[]> textstoreList) {
		this.textstoreList = textstoreList;
	}

	public List<ResourceForTypeBean> getResForTypeList() {
		return resForTypeList;
	}

	public void setResForTypeList(List<ResourceForTypeBean> resForTypeList) {
		this.resForTypeList = resForTypeList;
	}

	public List<ResCountBean> getResCountList() {
		return resCountList;
	}

	public void setResCountList(List<ResCountBean> resCountList) {
		this.resCountList = resCountList;
	}

	public List<Object[]> getInfoClassList() {
		return infoClassList;
	}

	public void setInfoClassList(List<Object[]> infoClassList) {
		this.infoClassList = infoClassList;
	}

	public String getCurrLan(String def) {
		if(!CommonUtil.isNotNull(currLan)){
			return def;
		}
		return currLan;
	}

	public void setCurrLan(String currLan) {
		this.currLan = currLan;
	}
	
}
