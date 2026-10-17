package cn.com.adtech.riss2.core;

import java.util.List;

/**
 * ip相关操作
 * 
 * @author MaChao
 * @date 2011-3-11
 */
public class IpUtil {
	/**
	 * ip段中间的分割符
	 */
	final public static String IP_RANGE_SPLIT = "-";

	/**
	 * 验证ip合法性
	 * 
	 * @param ip
	 * @return
	 */
	public static Boolean isIp(String ip) {
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		return ip.matches(regex);
	}

	/**
	 * 将字符串形式的ip转换为Long型，以便计算，未做异常处理
	 * 
	 * @author MaChao
	 * @date 2011-3-11
	 * @param ip
	 * @return
	 */
	private static Long ipToLongValue(String ip) {
		Long ipValue = 0l;
		String ips[] = ip.split("\\.");
		ipValue = Long.parseLong(ips[0]) * 256 * 256 * 256;
		ipValue += Long.parseLong(ips[1]) * 256 * 256;
		ipValue += Long.parseLong(ips[2]) * 256;
		ipValue += Long.parseLong(ips[3]);
		return ipValue;
	}

	/**
	 * 判断目标ip是否在指定ip范围内
	 * 
	 * @param targetIp【目标ip】
	 * @param smallIp【较小ip】
	 * @param bigId【较大ip】
	 * @return
	 */
	public static Boolean isInIpRange(String targetIp, String smallIp, String bigId) {
		if(!isIp(targetIp) || !isIp(smallIp) || !isIp(bigId)) {
			return false;
		}
		if(ipToLongValue(targetIp) >= ipToLongValue(smallIp) && ipToLongValue(targetIp) <= ipToLongValue(bigId)) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 判断目标ip是否在指定ip范围内
	 * 
	 * @param targetIp【目标ip】
	 * @param ipRange【ip范围，形如：192.168.1.1-192.168.1.255或192.168.2.1】
	 * @return
	 */
	public static Boolean isInIpRange(String targetIp, String ipRange) {
		String ips[] = ipRange.split(IpUtil.IP_RANGE_SPLIT);
		if(ips.length < 2) {
			return targetIp.trim().equals(ips[0].trim()) ? true : false;
		}
		return isInIpRange(targetIp, ips[0], ips[1]);
	}

	/**
	 * 判断ip是否在ipRanges集合中，是则返回true,否则返回false
	 * 
	 * @author MaChao
	 * @date 2011-3-29
	 * @param ip
	 * @param ipRanges
	 * @return
	 */
	public static Boolean isInMyIp(String ip, List<String> ipRanges) {
		for(String ipRange : ipRanges) {
			if(IpUtil.isInIpRange(ip, ipRange)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 重置ip段，将值小的放在前面，大的放在后面，返回值形如：192.168.2.1-192.168.2.255
	 * 
	 * @author MaChao
	 * @date 2011-3-29
	 * @param beginIp
	 * @param endIp
	 * @return
	 */
	public static String resetIpRange(String beginIp, String endIp) {
		if(ipToLongValue(beginIp) > ipToLongValue(endIp)) {
			return endIp + IpUtil.IP_RANGE_SPLIT + beginIp;
		}else {
			return beginIp + IpUtil.IP_RANGE_SPLIT + endIp;
		}
	}
}
