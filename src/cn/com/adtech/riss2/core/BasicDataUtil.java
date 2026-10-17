package cn.com.adtech.riss2.core;

/**
 * 获得馆藏位置数据
 * @modify yangY
 * 2011-04-21
 */

import java.io.IOException;
import java.util.List;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.domain.SysCollectionPosition;
import cn.com.adtech.riss2.jdbcms.ToolUnit;


public class BasicDataUtil{

	final public static String getLocationOptions(String selectOption){
		
		ICommonServiceCommonlyService commonlyService = (ICommonServiceCommonlyService)
			ServiceLocator.getInstance().getService("commonCommonlyService");
		List list = commonlyService.getSCPList();
		
		StringBuffer buffer = new StringBuffer();
		String temp = "";
		for(int i=0; i<list.size(); i++){
			temp = ToolUnit.NullToBlank(((SysCollectionPosition)list.get(i)).getScpName());
			buffer.append("<option value='"+temp+"' ");
			if(temp.equals(selectOption)){
				buffer.append("selected=true ");
			}
			buffer.append(">"+temp+"</option>\n");
		}
		String option = buffer.toString();
		buffer = null;
		return option;
	}
	
	public static void main(String[] args){
		String filename = "riss2-" + System.currentTimeMillis() + ".dmp";
		String exec = "exp riss2/riss2@oradata file=D:\\" + filename;
		try {
			Runtime.getRuntime().exec(exec);
			System.out.println(filename + " 备份成功");
		} catch (IOException e) {
			System.out.println("备份失败");
			e.printStackTrace();
		} 
	}
}
