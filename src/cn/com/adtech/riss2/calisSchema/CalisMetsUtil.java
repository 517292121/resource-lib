package cn.com.adtech.riss2.calisSchema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Encoder;


public class CalisMetsUtil {
	
	public static String getProFile(String str){
		 return "LT"+"/ID"+str;
	 }
	
	public static String getLable(String dataId){
		
		 return "lable";
	 }
	
	/**
	 * 馆号，集合，ID
	 */
	public static String getObjId(String num, String type , String dataId){
		return "urn:CALIS:"+num+"-"+type+"/ID"+dataId;
	}
	 
	public static String getLastModDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		return simpleDateFormat.format(date)+"T"+simpleDateFormat2.format(date)+"Z";
	}
	 
	public static String getFileMimeType(){
		return "";
	}
	
	public static String getFileId(){
		return "";
	}
	
	public static String getFileContent(File file) throws IOException{
		if(!file.isFile()){
			System.out.println("文件没找到！");
			return "";
		}
		FileInputStream fileInputStream = new FileInputStream(file);
			Integer i = 0;
			byte[] bt = new byte[1024];
			while((i = fileInputStream.read(bt))!=-1){
			} 
			fileInputStream.close();
		return new BASE64Encoder().encode(bt);
	} 
	
	
	
	/**
	 * MIMETYPE的内容规范
	 */
	public static String getMIMETYPE(String type){
		if(type.equals("dwg")){
			return "application/acad";
		}else if(type.equals("asd")||type.equals("asn")){
			return "application/astound";
		}else if(type.equals("tsp")){
			return "application/dsptype";
		}else if(type.equals("dxf")){
			return "application/dxf";
		}else if(type.equals("spl")){
			return "application/futuresplash";
		}else if(type.equals("gz")){
			return "application/gzip";
		}else if(type.equals("ptlk")){
			return "application/listenup";
		}else if(type.equals("hqx")){
			return "application/mac-binhex40";
		}else if(type.equals("mbd")){
			return "application/mbedlet";
		}else if(type.equals("mif")){
			return "application/mif";
		}else if(type.equals("xls") || type.equals("xla")){
			return "application/msexcel";
		}else if(type.equals("hlp") || type.equals("chm")){
			return "application/mshelp";
		}else if(type.equals("ppt") || type.equals("ppz") || type.equals("pps") || type.equals("pot")){
			return "application/mspowerpoint";
		}else if(type.equals("doc") || type.equals("dot")){
			return "application/msword";
		}else if(type.equals("bin") || type.equals("exe") || type.equals("com") || type.equals("dll") || type.equals("class")){
			return "application/octet-stream";
		}else if(type.equals("oda")){
			return "application/oda";
		}else if(type.equals("pdf")){
			return "application/pdf";
		}else if(type.equals("ai") || type.equals("eps") || type.equals("ps")){
			return "application/postscript";
		}else if(type.equals("rtc")){
			return "application/rtc";
		}else if(type.equals("rtf")){
			return "application/rtf";
		}else if(type.equals("smp")){
			return "application/studiom";
		}else if(type.equals("tbk")){
			return "application/toolbook";
		}else if(type.equals("vmd")){
			return "application/vocaltec-media-desc";
		}else if(type.equals("vmf")){
			return "application/vocaltec-media-file";
		}else if(type.equals("bcpio")){
			return "application/x-bcpio";
		}else if(type.equals("z")){
			return "application/x-compress";
		}else if(type.equals("cpio")){
			return "application/x-cpio";
		}else if(type.equals("csh")){
			return "application/x-csh";
		}else if(type.equals("dcr") || type.equals("dir") || type.equals("dxr")){
			return "application/x-director";
		}else if(type.equals("dvi")){
			return "application/x-dvi";
		}else if(type.equals("evy")){
			return "application/x-envoy";
		}else if(type.equals("gtar")){
			return "application/x-gtar";
		}else if(type.equals("hdf")){
			return "application/x-hdf";
		}else if(type.equals("php") || type.equals("phtml")){
			return "application/x-httpd-php";
		}else if(type.equals("js")){
			return "application/x-javascript";
		}else if(type.equals("latex")){
			return "application/x-latex";
		}else if(type.equals("bin")){
			return "application/x-macbinary";
		}else if(type.equals("mif")){
			return "application/x-mif";
		}else if(type.equals("nc") || type.equals("cdf")){
			return "application/x-netcdf";
		}else if(type.equals("nsc")){
			return "application/x-nschat";
		}else if(type.equals("sh")){
			return "application/x-sh";
		}else if(type.equals("shar")){
			return "application/x-shar";
		}else if(type.equals("swf") || type.equals("cab")){
			return "application/x-shockwave-flash";
		}else if(type.equals("spr") || type.equals("sprite")){
			return "application/x-sprite";
		}else if(type.equals("sit")){
			return "application/x-stuffit";
		}else if(type.equals("sca")){
			return "application/x-supercard";
		}else if(type.equals("sv4cpio")){
			return "application/x-sv4cpio";
		}else if(type.equals("sv4crc")){
			return "application/x-sv4crc";
		}else if(type.equals("tar")){
			return "application/x-tar";
		}else if(type.equals("tcl")){
			return "application/x-tcl";
		}else if(type.equals("tex")){
			return "application/x-tex";
		}else if(type.equals("texinfo") || type.equals("texi")){
			return "application/x-texinfo";
		}else if(type.equals("roff") || type.equals("tr") || type.equals("t")){
			return "application/x-troff";
		}else if(type.equals("man") || type.equals("troff")){
			return "application/x-troff-man";
		}else if(type.equals("me")){
			return "application/x-troff-me";
		}else if(type.equals("ms")){
			return "application/x-troff-ms";
		}else if(type.equals("ustar")){
			return "application/x-ustar";
		}else if(type.equals("src")){
			return "application/x-wais-source";
		}else if(type.equals("zip")){
			return "application/zip";
		}else if(type.equals("kdh")){
			return "application/kdh";
		}else if(type.equals("nh")){
			return "application/nh";
		}else if(type.equals("caj")){
			return "application/caj";
		}else if(type.equals("au") || type.equals("snd")){
			return "audio/basic";
		}else if(type.equals("es")){
			return "audio/echospeech";
		}else if(type.equals("tsi")){
			return "audio/tsplayer";
		}else if(type.equals("vox")){
			return "audio/voxware";
		}else if(type.equals("aif") || type.equals("aiff") || type.equals("aifc")){
			return "audio/x-aiff";
		}else if(type.equals("dus") || type.equals("cht")){
			return "audio/x-dspeeh";
		}else if(type.equals("mid") || type.equals("midi")){
			return "audio/x-midi";
		}else if(type.equals("mp2")){
			return "audio/x-mpeg";
		}else if(type.equals("ram") || type.equals("ra")){
			return "audio/x-pn-realaudio";
		}else if(type.equals("rpm")){
			return "audio/x-pn-realaudio-plugin";
		}else if(type.equals("stream")){
			return "audio/x-qt-stream";
		}else if(type.equals("wav")){
			return "audio/x-wav";
		}else if(type.equals("dwf")){
			return "drawing/x-dwf";
		}else if(type.equals("cod")){
			return "image/cis-cod";
		}else if(type.equals("ras")){
			return "image/cmu-raster";
		}else if(type.equals("fif")){
			return "image/fif";
		}else if(type.equals("gif")){
			return "image/gif";
		}else if(type.equals("ief")){
			return "image/ief";
		}else if(type.equals("jpe") || type.equals("jpeg") || type.equals("jpg")){
			return "image/jpeg";
		}else if(type.equals("tiff") || type.equals("tif")){
			return "image/tiff";
		}else if(type.equals("mcf")){
			return "image/vasa";
		}else if(type.equals("wbmp")){
			return "image/vnd.wap.wbmp";
		}else if(type.equals("fh4") || type.equals("fh5") || type.equals("fhc")){
			return "image/x-freehand";
		}else if(type.equals("pnm")){
			return "image/x-portable-anymap";
		}else if(type.equals("pbm")){
			return "image/x-portable-bitmap";
		}else if(type.equals("pgm")){
			return "image/x-portable-graymap";
		}else if(type.equals("ppm")){
			return "image/x-portable-pixmap";
		}else if(type.equals("rgb")){
			return "image/x-rgb";
		}else if(type.equals("xwd")){
			return "image/x-windowdump";
		}else if(type.equals("xbm")){
			return "image/x-xbitmap";
		}else if(type.equals("xpm")){
			return "image/x-xpixmap";
		}else if(type.equals("wrl")){
			return "model/vrml";
		}else if(type.equals("csv")){
			return "text/comma-separated-values";
		}else if(type.equals("css")){
			return "text/css";
		}else if(type.equals("htm") || type.equals("html") || type.equals("shtml")){
			return "text/html";
		}else if(type.equals("js")){
			return "text/javascript";
		}else if(type.equals("txt")){
			return "text/plain";
		}else if(type.equals("rtx")){
			return "text/richtext";
		}else if(type.equals("rtf")){
			return "text/rtf";
		}else if(type.equals("tsv")){
			return "text/tab-separated-values";
		}else if(type.equals("wml")){
			return "text/vnd.wap.wml";
		}else if(type.equals("wmlc")){
			return "application/vnd.wap.wmlc";
		}else if(type.equals("wmls")){
			return "text/vnd.wap.wmlscript";
		}else if(type.equals("wmlsc")){
			return "application/vnd.wap.wmlscriptc";
		}else if(type.equals("etx")){
			return "text/x-setext";
		}else if(type.equals("sgm") || type.equals("sgml")){
			return "text/x-sgml";
		}else if(type.equals("talk") || type.equals("spc")){
			return "text/x-speech";
		}else if(type.equals("mpeg") || type.equals("mpg") || type.equals("mpe")){
			return "video/mpeg";
		}else if(type.equals("qt") || type.equals("mov")){
			return "video/quicktime";
		}else if(type.equals("viv") || type.equals("vivo")){
			return "video/vnd.vivo";
		}else if(type.equals("avi")){
			return "video/x-msvideo";
		}else if(type.equals("movie")){
			return "video/x-sgi-movie";
		}else if(type.equals("vts") || type.equals("vtts")){
			return "workbook/formulaone";
		}else if(type.equals("3dmf") || type.equals("3dm") || type.equals("qd3d") || type.equals("qd3")){
			return "x-world/x-3dmf";
		}else if(type.equals("wrl")){
			return "x-world/x-vrml";
		}else if(type.equals("ceb")){
			return "application/ceb";
		}else{
			return "";
		}
	}
	
}
