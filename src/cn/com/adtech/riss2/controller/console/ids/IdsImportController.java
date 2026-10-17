package cn.com.adtech.riss2.controller.console.ids;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.upload.IUploadService;

@SuppressWarnings("unchecked")
public class IdsImportController extends MultiActionController {
	private long beginTimes;
	private long uploadEndTime;
	private long checkedEndtime;
	private long importEndTime;
	
	private IIdsService idsServices;
	public void setIdsServices(IIdsService idsServices) {
		this.idsServices = idsServices;
	}
	/**本地文件上传处理对象**/
	protected IUploadService localUploadService = null;
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	/**
	 * 进入导入模板设置界面
	 */
	public ModelAndView initImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("ids/idsImport");
	}
	
	/**
	 * 批量导入
	 */
	public ModelAndView importInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		beginTimes = System.currentTimeMillis(); //开始执行时间
		int beginRows = ServletRequestUtils.getIntParameter(request, "beginRow", 2); //起始行
		File xlsFile = uploadImportFile(request);
		if(xlsFile == null){
			request.setAttribute("result", "<script>Msg.fail('导入失败，文件上传出错!');</script>");
			return initImport(request, response);
		}
		String message = this.idsServices.checkExcel(xlsFile);
		if(CommonUtil.isNotNull(message)){
			request.setAttribute("result", "<script>Msg.fail('" + message + "');</script>");
			return initImport(request, response);
		}
//		message = this.idsServices.checkExcelData(xlsFile, beginRows);
//		if(CommonUtil.isNotNull(message)){
//			response.setContentType("text/html; charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
//			out.print("<span style='font-size:12px;'>" + message + "<br>");
//			out.print("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
//			out.flush();
//			return null;
//		}
//		checkedEndtime = System.currentTimeMillis(); //校验完成时间
		
		String impmessage = this.idsServices.saveImportExcel(xlsFile, request);
		importEndTime = System.currentTimeMillis(); //导入完成时间
		StringBuffer resultMessage = new StringBuffer();
		if(CommonUtil.isNotNull(impmessage)){
			if(xlsFile.exists()){
				xlsFile.delete();
			}
			resultMessage.append("<span style='color:red'>导入失败!</span><hr>");
		}else{
			resultMessage.append("数据总量："+idsServices.getTotalNumber()+" 条<br>");
			resultMessage.append("<span style='color:green'>导入成功："+idsServices.getSuccessNumber()+" 条</span><br>");
			resultMessage.append("<span style='color:red'>导入失败："+idsServices.getFailNumber()+" 条</span><br>");
			resultMessage.append("上传文件用时：" + ((uploadEndTime - beginTimes) / 1000.000) + "秒<br>");
//			resultMessage.append("数据验证用时：" + ((checkedEndtime - uploadEndTime) / 1000.000) + "秒<br>");
//			resultMessage.append("保存数据用时：" + ((importEndTime - checkedEndtime) / 1000.000) + "秒<br>");
			resultMessage.append("保存数据用时：<span style='color:red'>" + ((importEndTime - beginTimes) / 1000.000) + "</span>秒<hr>");
			resultMessage.append(this.idsServices.getMessage() + "<br>");
		}
		request.setAttribute("result", resultMessage.toString());
		return initImport(request, response);
	}
	
	/**上传EXCEL数据文件**/
	public File uploadImportFile(HttpServletRequest request){
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("excelfile");
		String fileFolder = SysConfigInfo.FILE_ROOT_DIR + "infoImport/";
		String filename = file.getOriginalFilename();
		try {
			String localname = filename.substring(filename.lastIndexOf(".")); // 扩展名
			localname = System.currentTimeMillis() + localname;
			localUploadService.saveFile(fileFolder, localname, file.getBytes());
			String filepath = SysConfigInfo.FILE_ROOT_DIR;
			filepath += fileFolder + localname;
			
			File localfile = new File(filepath);
			uploadEndTime = System.currentTimeMillis(); //文件上传结束时间
			return localfile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
