package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IDataBakService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.DbmsBakHistory;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

public class DataBakController extends MultiActionController {
	
	private IDataBakService dataBakService;
	public void setDataBakService(IDataBakService dataBakService) {
		this.dataBakService = dataBakService;
	}
	private IUploadService localUploadService;
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}
   private String filename;
	/**
	 * 初始化备份列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-25
	 */
	public ModelAndView initView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("address", SysConfigInfo.FILE_URL_PATH);
		return new ModelAndView("/resource/metadata/dataBak");
	}
	
	/**
	 * 根据分页得到院系对象数据，并转换成json字符串
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author MaChao
	 * @date 2011-04-25
	 */
	public void getDataBakJson(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "dbmsBakdate");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		
		//获取结果数据
		String jsondata =dataBakService.getDataBakByJson(pagination, sort, dir);
		//打印结果数据
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 备份数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private List doBuildCommand() {  
		String y, m, d;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR) + "";
		m = (cal.get(Calendar.MONTH) + 1) + "";
		d = cal.get(Calendar.DATE) + "";
		if(m.length() < 2){
			m = "0" + m;
		}
		if(d.length() < 2){
			d = "0" + d;
		}	
	    filename = "riss2-"+ y + m+ d + "-"+System.currentTimeMillis() + ".DMP";
	    String	dataBakFolder = SysConfigInfo.DATA_BAK_DIR;
		String  basePath = SysConfigInfo.FILE_ROOT_DIR;
		String url = SysConfigInfo.JDBC_URL;
		String _url = url.substring(url.lastIndexOf(":")+1);
		String user = SysConfigInfo.JDBC_USERNAME;
		String pwd = SysConfigInfo.JDBC_PASSWORD;
        List rtnList = new ArrayList();  
 
       rtnList.add("EXP ");
	   rtnList.add( user+"/"+pwd+"@"+_url);
	   rtnList.add(" file="+basePath+dataBakFolder+"/"+filename);  
       rtnList.add("buffer=2000000");
       rtnList.add("direct=y");
       return rtnList;  
    }  
   
	 private ProcessBuilder buildProcessBuilder(File workDir) {    
		 new ProcessBuilder();  

         ProcessBuilder pbInst = new ProcessBuilder(doBuildCommand()); 
         
         Map envMap = pbInst.environment();  
         envMap.clear();  
         envMap.putAll(System.getenv());  
         pbInst.directory(workDir);  
         pbInst.redirectErrorStream(true);  
        return pbInst;  
     }  
	public void saveDataBak(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 String	dataBakFolder = SysConfigInfo.DATA_BAK_DIR;
		 String  basePath = SysConfigInfo.FILE_ROOT_DIR;		
         File outDir = new File(basePath + dataBakFolder); 
         if(!outDir.exists()){
        	 outDir.mkdirs();
         }
         ProcessBuilder pbInst = buildProcessBuilder(outDir); 	
         String dbmsUrlpath = "/FileDownload?downloadPath=" + localUploadService.encode(dataBakFolder + filename) + "&fileName="
		 + localUploadService.encode(filename);
	    DbmsBakHistory dbms = new DbmsBakHistory();
		dbms.setDbmsFilename(filename);
		dbms.setDbmsBakdate(new Date());
		dbms.setDbmsUrlpath(dbmsUrlpath);
		boolean b=false;
		  try {  
              Process proInst = pbInst.start();  
//             new CommonServiceImp().getHibernateTemplate().save(dbms);
          	 b = dataBakService.dataBak(null, dbms);
             final InputStream ins = proInst.getInputStream();  
             File outFile = new File(outDir,"exp.log");  
             outFile.createNewFile();  
              final FileChannel focInst = new FileOutputStream(outFile).getChannel();  
              Thread innerTh = new Thread() {  
                  ByteBuffer byteBuffer = ByteBuffer.allocate(1024);  
                public void run() {  
                      ReadableByteChannel rbcObj = Channels.newChannel(ins);  
                     try {      
                        while (rbcObj.read(byteBuffer) != -1) {  
                              byteBuffer.flip();  
                             focInst.write(byteBuffer);  
                            byteBuffer.clear();  
                         }  
                      } catch (IOException ioe) {  
                        ioe.printStackTrace();  
                      }  
                }  
             };  
             // innerTh.setDaemon(true);   
             innerTh.start();  
             
         } catch (IOException ioe) {  
             ioe.printStackTrace();  
         } 
		//打印结果数据
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(b){
			out.print("数据备份执行成功!");
		}else{
			out.print("数据备份执行失败!");
		}
		out.flush();
	}
	/**
	 * 删除备份
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void deleteDatabak(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long dbmsId = ServletRequestUtils.getLongParameter(request, "dbmsId",0L);
		
		boolean b = dataBakService.deleteDataBakByDbmsId(dbmsId);
		
		//打印结果数据
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		out.flush();
	}
	
}
