package cn.com.adtech.riss2.core;

import java.util.Map;

import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;

public class CounselorOnline {

	public Map<Long, SysManager> onlineCounselors;
	
	public static CounselorOnline counselorOnline;
	
	public static CounselorOnline getInstance(){
		if(counselorOnline == null){
			counselorOnline = new CounselorOnline();
		}
		return counselorOnline;
	}

	public Map<Long, SysManager> getOnlineCounselors() {
		return onlineCounselors;
	}

	public void setOnlineCounselors(Map<Long, SysManager> onlineCounselors) {
		this.onlineCounselors = onlineCounselors;
	}
	
}
