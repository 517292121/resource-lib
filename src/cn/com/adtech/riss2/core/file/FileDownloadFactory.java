package cn.com.adtech.riss2.core.file;

import java.util.HashSet;
import java.util.Set;


public class FileDownloadFactory {
	
	private static FileDownloadFactory instance = null;
	private Set<Long> keys = new HashSet();
	private FileDownloadFactory() {
 
	}
	public static FileDownloadFactory getInstance() {
		if (instance == null) {
			synchronized (FileDownloadFactory.class) {
				instance = new FileDownloadFactory();
			}
		}
		return instance;
	}
	
	
	public long getTime(){
		long time = System.currentTimeMillis();
		keys.add(time);
		return time;
	}
	
	public void removeTime(long time){
		keys.remove(time);
	}
	
	public boolean checkTime(long time){
		return keys.contains(time);
	}
}
