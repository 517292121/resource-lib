package cn.com.adtech.riss2.wsClient;

import cn.com.adtech.riss2.core.ServiceLocator;
import cn.com.adtech.riss2.fileservice.FileServiceSettings;

public class FileConvertServiceUtil {
	private static FileConvertServicePortType getFileConvertService(String webServicePath) {
		if (FileServiceSettings.embedded()) {
			return (FileConvertServicePortType) ServiceLocator.getService("localFileConvertService");
		}
		FileConvertServiceClient fileConvertServiceClient = (FileConvertServiceClient)ServiceLocator.getInstance().getService(
				"fileConvertService");
		return webServicePath != null && webServicePath.startsWith("http")
				? fileConvertServiceClient.getFileConvertServiceHttpPort(webServicePath.replaceAll("/+$", "") + "/services/FileConvertService")
				: fileConvertServiceClient.getFileConvertServiceHttpPort();
	}

	/**
	 * 文档转换
	 * 
	 * @author MaChao
	 * @date 2010-11-18
	 * @param inputFileName[被转换的文件，相对路径+文件名]
	 * @param outputFileName[欲生成的文件，相对路径+文件名]
	 * @param webServicePath[远程服务器地址]
	 */
	public static void documentConvert(String inputFileName, String outputFileName, String webServicePath) {
		getFileConvertService(webServicePath).documentConvert(inputFileName, outputFileName);
	}

	/**
	 * 音频转换
	 * 
	 * @author MaChao
	 * @date 2010-11-18
	 * @param inputFileName[被转换的文件，相对路径+文件名]
	 * @param outputFileName[欲生成的文件，相对路径+文件名]
	 * @param webServicePath[远程服务器地址]
	 */
	public static void audioConvert(String inputFileName, String outputFileName, String webServicePath) {
		getFileConvertService(webServicePath).audioConvert(inputFileName, outputFileName);
	}

	/**
	 * 视频转换
	 * 
	 * @author MaChao
	 * @date 2010-11-18
	 * @param inputFileName[被转换的文件，相对路径+文件名]
	 * @param outputFileName[欲生成的文件，相对路径+文件名]
	 * @param time[自动生成预览文件的截取时间，在ConstantsObj.FileConvertName.VIEW_FILE_TIME中定义]
	 * @param webServicePath[远程服务器地址]
	 */
	public static void videoConvert(String inputFileName, String outputFileName, String time, String webServicePath) {
		getFileConvertService(webServicePath).videoConvert(inputFileName, outputFileName, time);
	}

	/**
	 * 根据视频文件自动生成图片
	 * 
	 * @author MaChao
	 * @date 2010-11-18
	 * @param inputFileName[被转换的文件，相对路径+文件名]
	 * @param outputFileName[欲生成的文件，相对路径+文件名]
	 * @param time[取原视频文件的时间点，在ConstantsObj.FileConvertName.PHOTO_CUT_TIME中定义]
	 * @param webServicePath[远程服务器地址]
	 */
	public static void photoConvertFromVideo(String inputFileName, String outputFileName, String time, String webServicePath) {
		getFileConvertService(webServicePath).photoConvertFromVideo(inputFileName, outputFileName, time);
	}
	
	/**
	 * 将pdf文件第一页生成尺寸为100*120的封面图片文件
	 * 
	 * @author MaChao
	 * @date 201-07-14
	 * @param inputFile[被转换的文件，相对路径+文件名]
	 * @param outFile[欲生成的文件，相对路径+文件名]
	 * @param webServicePath[远程服务器地址]
	 */
	public static boolean photoConvertFromDocument(String inputFile, String outFile, String webServicePath) {
		try {
			getFileConvertService(webServicePath).photoConvertFromDocument(inputFile, outFile);
			return true;
		} catch (Exception e) {
			System.out.println("PDF创建封面发生异常：");
			e.printStackTrace();
			return false;
		}
	}
}
