package cn.com.adtech.riss2.bso.time;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;

/**
 *  定时任务服务接口
 * 
 * @author MaChao
 *
 * @date Nov 27, 2015 - 5:04:42 PM
 */

public interface ITimingService extends ICommonServiceCommonlyService {
	
	/**
	 * 定时人物：备份数据库
	 * 
	 * @author MaChao
	 * @date Nov 27, 2015
	 * @time 5:22:28 PM
	 */
	public void saveDataBak();
	
	/**
	 * 定时任务：用户下载量 清零
	 *
	 * @author MaChao
	 * @date Nov 27, 2015
	 * @time 5:22:43 PM
	 */
	public void updateSuinDownNum();
	
}
