package cn.com.adtech.riss2.controller.console.resource.manager;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.calisSchema.CalisSchemaFactory;
import cn.com.adtech.riss2.calisSchema.MetaDataEntity;
import cn.com.adtech.riss2.calisSchema.Schema;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;

/**
 * 导出Calis标准
 * @author MaChao
 */
public class ExportResToCalisController extends MultiActionController{
	
	private IResourceDataService resourceService = null;

	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	/**
	 * 界面入口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView initPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/manager/ExportResToCalisList");
	}
	
	/**
	 * 资源列表JSON数据
	 */
	public ModelAndView resourceListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long storeId = MyRequestUtil.getLongParameter(request, "RSDA_RMST_ID");
		long sourceType = MyRequestUtil.getLongParameter(request, "RSDA_RLTY_ID");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		String beginDay = ServletRequestUtils.getStringParameter(request, "beginTime", "");
		String endDay = ServletRequestUtils.getStringParameter(request, "endTime", "");
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RSDA_BUILDDATE");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		String jsondata = resourceService.getAllResourceListJson(storeId, sourceType, begin, pageNum, sort, dir, rsdaTitle , beginDay ,endDay);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
		return null;
	}

	/**
	 *  保存馆号
	 */
	public void saveLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String library = ServletRequestUtils.getStringParameter(request, "library","000000");
		Boolean b = resourceService.saveLibrary(library);
		String jsondata = "";
		if(b){
			jsondata = "保存成功!";
		}else{
			jsondata = "保存失败!";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 读取馆号
	 */
	public void initLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(ConstantsObj.getSysProperty(MmSettingConstant.getRiss_library()));
		out.flush();
	}
	/**
	 * 导出CALIS
	 */
	public void exportCalis(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sourceIdArr = MyRequestUtil.getStringParameter(request, "idlist");
		String sourceIsAll = MyRequestUtil.getStringParameter(request, "isAll");
		String progressBarReturnNum = MyRequestUtil.getStringParameter(request, "progressBarReturnNum");
		Boolean b = false;
		try {
			String basePath = SysConfigInfo.FILE_ROOT_DIR;
			String rootData = createFolder(basePath+"calis\\");
			String file_path = rootData;
			Schema schema ;
			//需导出的ID集合
			String[] sourceIds = null;
				if("true".equals(sourceIsAll)){
					Long storeId = MyRequestUtil.getLongParameter(request, "RSDA_RMST_ID");
					Long sourceType = MyRequestUtil.getLongParameter(request, "RSDA_RLTY_ID");
					String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
					String beginDay = ServletRequestUtils.getStringParameter(request, "beginTime", "");
					String endDay = ServletRequestUtils.getStringParameter(request, "endTime", "");
					sourceIds = resourceService.getResIdToAudit(storeId, sourceType, rsdaTitle, beginDay, endDay);
				}else{
					sourceIds = sourceIdArr.split(",");
				}
				//每一次导出清空流水号
				int calisNum =1;
				System.out.println("-----------开始导出----------------");
				for( int i=0;i<sourceIds.length-1;i++){
					String sourceId = sourceIds[i+1];
					//以1000个文件为界限建立文件夹
					if( i % 1000 == 0 ){
						file_path = rootData + createFolderDay()+createCalis_file_Num(calisNum)+"\\";
						calisNum++;
					}
					try {
						//设置当前资源类型
						String type = resourceService.getFirstData("select rlty_code from Res_Source_Data left join Res_Liter_Type on rsda_rlty_id = rlty_id where rsda_Id = '"+sourceId+"' ");
						//得到对象
						schema = CalisSchemaFactory.getCalis_schema(type, sourceId.toString());
						//设置保存路径
						schema.setPath(file_path);
						 schema.setType(type);
					    //类型名称
					    String rltyname = resourceService.getFirstData("select rlty_name from Res_Source_Data left join Res_Liter_Type on rsda_rlty_id = rlty_id where rsda_Id = '"+sourceId+"' ");
					    schema.setRltyname(rltyname);
					    //设置馆号
					    schema.setLibrary(ConstantsObj.getSysProperty(MmSettingConstant.getRiss_library()));
					    //设置资源ID号
					    schema.setResId(sourceId);
					    //得到元素
					    List<MetaDataEntity> metaDataEntityList = resourceService.getSourceToList(Long.parseLong(sourceId));
					    if(metaDataEntityList.size()>0){
					    	schema.body(metaDataEntityList);
//					    	Map<String, Object> mapList = new HashMap<String, Object>();
//					    		 mapList.put("basePath", basePath);
//				    	    String path = request.getContextPath();
//			    			String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
//					    		 mapList.put("webpath",rootPath);
//						    	 Object[] objects = (Object[])resourceService.find("select rsdaFileLocalName,rsdaFileName,rsdaFileType from ResSourceData where rsdaId = "+sourceId).get(0);
//						    	 mapList.put("rsdaFileLocalName", objects[0]);
//						    	 mapList.put("rsdaFileName", objects[1]);
//						    	 mapList.put("rsdaFileType", objects[2]);
//					        schema.about(mapList);
					    	schema.createXML();
					    }
					} catch (Exception e) {
						System.out.println("导出ID为"+sourceId+"出错！");
						e.printStackTrace();
					}
					//记录导出数
					System.out.println((i+1)+"/"+sourceIds.length);
					//进度条保存
					progressBar.put(request.getSession().getId()+":"+progressBarReturnNum, (i+1)+"/"+(sourceIds.length-1));
				}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsondata = "";
		if(b) {
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		progressBar.put(request.getSession().getId()+":"+progressBarReturnNum,jsondata);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * CALIS导出时建立文件夹,判断是否存在文件夹，如果存在则+1在验证
	 */
	private String createFolder(String fileBasePath){
	    Integer CALIS_BASEPATHNUM=1;
		String day = createFolderDay(); 
		File file = new File(fileBasePath+day+"-"+CALIS_BASEPATHNUM+"\\");
		
		while(file.isDirectory()){
			CALIS_BASEPATHNUM++;
			file = new File(fileBasePath+day+"-"+CALIS_BASEPATHNUM+"\\");
		}
		file.mkdirs();
		return fileBasePath+day+"-"+CALIS_BASEPATHNUM+"\\";
	}

	/**
	 * 得到流水号
	 */
	private static String createCalis_file_Num(Integer calisNum){
		if(calisNum<9){
			return "0000"+(calisNum++);
		}else if(calisNum>=9&&calisNum<99){
			return "000"+(calisNum++);
		}else if(calisNum>=99&&calisNum<999){
			return "00"+(calisNum++);
		}else if(calisNum>=999&&calisNum<9999){
			return "0"+(calisNum++);
		}else{
			return ""+calisNum;
		}
	}
	
	private String createFolderDay(){
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(date);
	}    
	
	/**
	 * 导出进度条
	 */
	private static Map<String, String> progressBar = new HashMap<String, String>(); 
	private static Integer progressBarNum = 0;
	public void progressBar(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String num = ServletRequestUtils.getStringParameter(request, "progressBarNum", "");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(progressBar.get(request.getSession().getId()+":"+num));
		out.flush();
	}
	public void progressBarReturnNum(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//返回进度条流水号
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(++progressBarNum);
		out.flush();
		out.close();
	}
	
}
