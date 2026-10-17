package cn.com.adtech.riss2.bso.time;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.DbmsBakHistory;

/**
 * 定时任务服务类
 * 
 * @author MaChao
 * 
 * @date Nov 27, 2015 - 5:04:17 PM
 */
public class TimingServiceImp extends CommonServiceCommonlyImp implements
		ITimingService {
	private static final Logger logger = Logger
			.getLogger(TimingServiceImp.class);
	private String filename;

	/**
	 * 备份数据
	 * 
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
		if (m.length() < 2) {
			m = "0" + m;
		}
		if (d.length() < 2) {
			d = "0" + d;
		}
		filename = "riss2-" + y + m + d + "-" + System.currentTimeMillis() + ".DMP";
		
		String dataBakFolder = SysConfigInfo.DATA_BAK_DIR;
		String url = SysConfigInfo.JDBC_URL;
		String _url = url.substring(url.lastIndexOf(":") + 1);
		String user = SysConfigInfo.JDBC_USERNAME;
		String pwd = SysConfigInfo.JDBC_PASSWORD;
		List rtnList = new ArrayList();

		rtnList.add("EXP ");
		rtnList.add(user + "/" + pwd + "@" + _url);
		rtnList.add(" file=" + dataBakFolder + "/" + filename);
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

	/**
	 * 编码
	 */
	public String encode(String str) throws UnsupportedEncodingException {
		if (str == null)
			return "";
		Base64 base64 = new Base64();
		str = java.net.URLEncoder.encode(str, "utf-8");
		return new String(base64.encode(str.getBytes()));
	}

	/**
	 * 定时任务：自动备份数据库-调用方法
	 * 
	 * @author MaChao
	 * @date Nov 27, 2015
	 * @time 5:43:57 PM
	 */
	@Override
	public void saveDataBak() {
		String dataBakFolder = SysConfigInfo.DATA_BAK_DIR;
		File outDir = new File(dataBakFolder);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		File[] fileList = outDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory() || pathname.getName().toUpperCase().endsWith(".DMP")) {
                    return true;
                }
                return false;
			}
		});
		if(fileList.length >= 2){
			if(fileList[1].lastModified() > fileList[0].lastModified()){
				boolean flag = fileList[0].delete();
			}
		}
		ProcessBuilder pbInst = buildProcessBuilder(outDir);
		String dbmsUrlpath = null;
		try {
			dbmsUrlpath = "/FileDownload?downloadPath="
					+ encode(dataBakFolder + "/" + filename) + "&fileName="
					+ encode(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbmsBakHistory dbms = new DbmsBakHistory();
		dbms.setDbmsFilename(filename);
		dbms.setDbmsBakdate(new Date());
		dbms.setDbmsUrlpath(dbmsUrlpath);
		boolean b = false;
		try {
			Process proInst = pbInst.start();
			try {
				save(dbms);
				b = true;
			} catch (Exception e) {
				b = false;
			}
			final InputStream ins = proInst.getInputStream();
			File outFile = new File(outDir, "exp.log");
			outFile.createNewFile();
			final FileChannel focInst = new FileOutputStream(outFile)
					.getChannel();
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
	}

	/** 清零用户的下载量 */
	@Override
	public void updateSuinDownNum() {
		logger
				.info("-------------------- 定时任务成功启动_清零用户的下载量 ：begin ----------------------------");
		String hql = "update SysUserinfo set suinDownNum=null";
		this.getHibernateTemplate().bulkUpdate(hql);
		logger
				.info("-------------------- 定时任务成功启动_清零用户的下载量 ：end ----------------------------");
	}
}
