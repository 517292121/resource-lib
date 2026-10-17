package cn.com.unionlib.lucene.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.domain.cache.Tree;

public class RecursiveUtil {

	/**
	 * 自定义分类树
	 * @author MaChao
	 * @date 2017-3-29
	 */
	public final static List<Tree> getTreeList(Map<String, Integer> map,List<Object[]> list,String rmstId) {		
		List<Tree> treeList = new ArrayList<Tree>();
		treeList = getrecursion(list,map, 0L,treeList,rmstId);
		
		return treeList;
	}
	
	/**
	 * 递归函数
	 * @author MaChao
	 * @date 2017-3-29
	 */
	private final static List<Tree> getrecursion(List<Object[]> list,
			Map<String, Integer> map,Long pid,List<Tree> treeList,String rmstId){
		//节点名
		String name = "";
		//当前父id
		Long pids = 0L;
		//节点聚类数
		Integer count = 0;	
		//当前节点id
		Long id = 0L;
		//判断是否需要生成节点
		if(map==null || map.size()<1){
			return treeList;
		}
		Tree tree = null;
		for(Object[] o : list){
			if(!o[3].toString().equals(rmstId)){
				continue;
			}
			id = Long.parseLong(o[0].toString());
			//获取当前父id
			if(o[1]!=null){
				pids = Long.parseLong(o[1].toString());
			}
			//获取名称
			name = o[2].toString();
			//获取节点聚类数
			count = map.get(name);
			//判断是否同级,且聚类数大于0
			if(count!=null && pids.equals(pid) && count>0){
				//生成树
				tree = new Tree();			
				tree.setName(name);
				tree.setId(id);
				tree.setPid(pid);
				tree.setCount(count);
				//添加进当前节点树
				treeList.add(tree);
				//子节点生成
				if(id>0L){
					getrecursion(list, map,id,treeList,rmstId);	
				}
			}
			continue;
		}
		return treeList;
	}	
}
