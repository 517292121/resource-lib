package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.File;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 
 * 资源相关服务扩展类，由于资源录入类（InputSingleController）等代码量都已较大，需要其它升
 * 级或改动时不宜再在里面进行代码处理，本类主要是为资源管理的扩展类，相关处理可以在本类中执行
 *
 * @author MaChao
 *
 * @date：2018-08
 */
public class ResourceExtendService {

	/**
	 * 验证目录树格式的资源文件夹中文件是否正确
	 * @param listDir
	 * @return
	 */
	public void checkListResourceFormat(String listDir){
		//如果没有目录，视为没有文件处理
		if(!CommonUtil.isNotNull(listDir)){
			return;
		}
		File listDirFile = new File(listDir);
		if(!listDirFile.exists()){
			throw new ErrorMessageException("目录：“"+listDir+"”不存在！");
		}
		if(!listDirFile.isDirectory()){
			throw new ErrorMessageException("“"+listDir+"”不是有效的目录！");
		}
		
		//TODO 目录内的文件验证
	}
}
