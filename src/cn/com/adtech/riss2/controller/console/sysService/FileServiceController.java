package cn.com.adtech.riss2.controller.console.sysService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.sc.IResFilesystemService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;

public class FileServiceController extends MultiActionController {
	private IResFilesystemService fileSystemService = null;

	public void setFileSystemService(IResFilesystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}

	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView initView(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sysService/fileService");
	}

	public void fileSystemList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RFIL_NUM");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "asc");
		String jsondata = fileSystemService.getFilesystemJson(start, limit, sort, dir);
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(jsondata);
		out.flush();
	}

	/**
	 * 保存或修改资源类型名称
	 */
	public ModelAndView saveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		long rfilId = MyRequestUtil.getLongParameter(request, "RFIL_ID");
		String rfilName = ServletRequestUtils.getStringParameter(request, "RFIL_NAME", "");
		String rfilAddress = ServletRequestUtils.getStringParameter(request, "RFIL_ADDRESS", "");
		String rfilFtpHost = ServletRequestUtils.getStringParameter(request, "RFIL_FTP_HOST", "");
		String rfilFtpUsername = ServletRequestUtils.getStringParameter(request, "RFIL_FTP_USERNAME", "");
		String rfilFtpPassword = ServletRequestUtils.getStringParameter(request, "RFIL_FTP_PASSWORD", "");
		long num = ServletRequestUtils.getLongParameter(request, "RFIL_NUM", 0);
		String hql = "select count(rf.rfilId) from ResFilesystem rf where rf.rfilName='" + rfilName + "'";
		long count = fileSystemService.getCountNumber(hql);
		if(count > 0 && rfilId < 1) {
			out.print("{success:false,msg:'" + rfilName + " 已存在!'}");
		}else {
			System.out.println("id:" + rfilId);
			ResFilesystem rfs = new ResFilesystem();
			rfs.setRfilId(rfilId);
			rfs.setRfilName(rfilName);
			rfs.setRfilAddress(rfilAddress);
			rfs.setRfilFtpHost(rfilFtpHost);
			rfs.setRfilFtpUsername(rfilFtpUsername);
			rfs.setRfilFtpPassword(rfilFtpPassword);
			rfs.setRfilNum(num);
			try {
				if(rfilId < 1) {
					fileSystemService.saveFilesystem(rfs);
				}else {
					fileSystemService.updateFilesystem(rfs);
				}
				out.print("{success:true,msg:'" + rfilName + " 保存成功!'}");
			}catch(Exception e) {
				out.print("{success:false,msg:'" + rfilName + " 保存失败!'}");
			}
		}
		return null;
	}

	/**
	 * 删除资源类型
	 */
	public void DelFilesystem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rfilId = MyRequestUtil.getLongParameter(request, "RFIL_ID");
		boolean b = fileSystemService.deleteResFilesystem(rfilId);
		PrintWriter out = response.getWriter();
		if(b) {
			out.print("{success:true}");
		}else {
			out.print("{success:false,msg:'删除失败!'}");
		}
	}
}