package cn.com.unionlib.lucene.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class MapValueComparator implements Comparator<Map.Entry<String, Long>>  {
	
	@Override
	public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
		return o1.getValue().compareTo(o2.getValue());
	}
}
