package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.web.bind.ServletRequestUtils;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.bso.platform.PlatformBasicService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;

/**
 * 自定义分类操作
 * 
 * @author MaChao
 *
 */
public class DynamicTypeServiceImp extends CommonServiceImp implements IResDynamicTypeService {
	
	private String parseRdtyType(int rdtyType){
		if(rdtyType == 1){
			return "rsdaRdtyId";
		} else if (rdtyType == 2){
			return "rsdaRdtyTwo";
		} else if (rdtyType == 3){
			return "rsdaRdtyThree";
		}else{
			return "";
		}
	}

	/**
	 * 删除自定义分类
	 * @param rdtyId
	 * @return
	 */
	@Override
	public String deleteDynamicType(Long rdtyId) {
		if(rdtyId == null || rdtyId < 1){
			return "未知分类，删除失败！";
		}
		ResDynamicType dynamic = (ResDynamicType) get(ResDynamicType.class, rdtyId);
		if(dynamic == null){
			return "该分类不存在或已被删除！";
		}
		String childs = getAllChildIds(rdtyId);
		long count = getCountByIds(childs, dynamic.getRdtyType(), " rsda.rsdaId>0 ");
		if(count > 0){
			return "删除失败：该分类或子分类中已存在数据！";
		}
		
		String hql = "delete from ResDynamicType where rdtyId=? or rdtyPid=?";
		
		try {
			getHibernateTemplate().bulkUpdate(hql, new Long[]{rdtyId, rdtyId});
			return "";
		} catch (Exception e) {
			logger.error("自定义分类删除异常：" ,e);
			return "错误：" + e.getMessage();
		}
	}
	/**
	 * 根据指定的自定义分类ID获取所有子分类ID，包括本身
	 * @param rdtyId
	 * @return
	 */
	public String getAllChildIds(Long rdtyId) {
		if(rdtyId == null || rdtyId < 1){
			return "";
		}
		String sql = SqlBasicUnit.hierarchyIdsSql("res_dynamic_type", "rdty_id", "rdty_pid", rdtyId);
		List<Object> idlist = getSingleColoumList(sql, null);
		if(idlist == null || idlist.size() < 1){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		String ids = "";
		for(int i=0,s=idlist.size(); i<s; i++){
			buffer.append(idlist.get(i).toString());
			if(i<s-1){
				buffer.append(",");
			}
		}
		ids = buffer.toString();
		buffer = null;
		idlist = null;
		return ids;
	}
	/**
	 * 根据自定义分类ID字符串和分类类型获取资源总数
	 * @param childs 以逗号分类的ID字符串
	 * @param c 分类类型
	 * @param common 公共查询条件
	 * @return
	 */
	public Long getCountByIds(String childs, int c, String common){
		if(CommonUtil.isNotNull(childs)){
			String rdtyType = parseRdtyType(c);
			String hql = "select count(rsda.rsdaId) from ResSourceData rsda where "+common+" and rsda." + rdtyType + " in ("+childs+")";
			Long count = getCountNumber(hql);
			return count == null ? 0: count;
		}else{
			return 0L;
		}
	}

	/**
	 * 保存自定义分类
	 * @param rmstId
	 * @param rdtyPid
	 * @param rdtyName
	 * @param rdtyGrade
	 * @param rdtyIndex
	 * @param rdtyType
	 * @return
	 */
	@Override
	public boolean saveDynamicType(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName,
			long rdtyGrade, int rdtyIndex, int rdtyType, String rdtyCode) {
		if(rmstId == null || rmstId < 1){
			return false;
		}
		ResDynamicType dynamic = null;
		//如果页面无法确定分类的级数，则根据它的上级分类查找对应的级数
		if(rdtyGrade < 1){
			if(rdtyPid == null || rdtyPid < 1){
				rdtyGrade = 1;
			}else{
				dynamic = (ResDynamicType) get(ResDynamicType.class, rdtyPid);
				if(dynamic == null){
					rdtyGrade = 1;
				}else{
					rdtyGrade = dynamic.getRdtyGrade() + 1;
				}
			}
		}
		if(rdtyId < 1){
			rdtyId = null;
		}
		
		dynamic = new ResDynamicType();
		dynamic.setRdtyId(rdtyId);
		dynamic.setRdtyGrade(rdtyGrade);
		dynamic.setRdtyIndex(rdtyIndex == 0 ? null : rdtyIndex);
		dynamic.setRdtyName(rdtyName);
		dynamic.setRdtyPid(rdtyPid);
		dynamic.setRdtyType(rdtyType);
		dynamic.setRmstId(rmstId);
		dynamic.setRdtyCode(rdtyCode);
		try {
			save(dynamic);
			return true;
		} catch (Exception e) {
			logger.error("自定义分类保存异常：" ,e);
			return false;
		}
	}

	/**
	 * 保存自定义分类
	 * @param rmstId
	 * @param rdtyPid
	 * @param rdtyName
	 * @param rdtyZwName
	 * @param rdtyEnName
	 * @param rdtyGrade
	 * @param rdtyIndex
	 * @param rdtyType
	 * @return
	 */
	@Override
	public boolean saveDynamicType(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName,
			String rdtyZwName, String rdtyEnName,
			long rdtyGrade, int rdtyIndex, int rdtyType, String rdtyCode) {
		if(rmstId == null || rmstId < 1){
			return false;
		}
		ResDynamicType dynamic = null;
		//如果页面无法确定分类的级数，则根据它的上级分类查找对应的级数
		if(rdtyGrade < 1){
			if(rdtyPid == null || rdtyPid < 1){
				rdtyGrade = 1;
			}else{
				dynamic = (ResDynamicType) get(ResDynamicType.class, rdtyPid);
				if(dynamic == null){
					rdtyGrade = 1;
				}else{
					rdtyGrade = dynamic.getRdtyGrade() + 1;
				}
			}
		}
		if(rdtyId < 1){
			rdtyId = null;
		}
		
		dynamic = new ResDynamicType();
		dynamic.setRdtyId(rdtyId);
		dynamic.setRdtyGrade(rdtyGrade);
		dynamic.setRdtyIndex(rdtyIndex == 0 ? null : rdtyIndex);
		dynamic.setRdtyName(rdtyName);
		dynamic.setRdtyZwName(rdtyZwName);
		dynamic.setRdtyEnName(rdtyEnName);
		dynamic.setRdtyPid(rdtyPid);
		dynamic.setRdtyType(rdtyType);
		dynamic.setRmstId(rmstId);
		dynamic.setRdtyCode(rdtyCode);
		try {
			save(dynamic);
			return true;
		} catch (Exception e) {
			logger.error("自定义分类保存异常：" ,e);
			return false;
		}
	}
	
	
	
	/**
	 * 根据特色库ID以无限级数的方式获取所有Option
	 * @param rmstId 
	 * @param selectid
	 * @param rdtyType 自定义分类类型
	 * @return
	 */
	public String getDynamicOptions(Long rmstId, Long selectid, int rdtyType) {
		List<ResDynamicType> list = getDynamicTypeList(rmstId, rdtyType);
		if(list == null || list.size() < 1){
			return "";
		}
		buffer = new StringBuffer();
		String options = "<option value='0'>无</option>" + loopOptions(list, selectid, 0, null);
		return options;
	}
	
	private StringBuffer buffer = new StringBuffer();
	private String loopOptions(List<ResDynamicType> list, Long selectid, int grade, Long parentid){
		String tree = "";
		for(int i=0; i<grade; i++){
			tree += "─";
		}
		int now_grade = grade + 1;
		ResDynamicType dynamic = null;
		for(int i=0,s=list.size(); i<s; i++){
			dynamic = list.get(i);
			if(parentid == null || parentid < 1){
				if(dynamic.getRdtyPid() == null || dynamic.getRdtyPid() < 1){
					buffer.append("<option value='"+dynamic.getRdtyId()+"'");
					if(selectid.equals(dynamic.getRdtyId())){
						buffer.append(" selected=true ");
					}
					buffer.append(">├").append(tree).append(dynamic.getRdtyName()).append("</option>");
					loopOptions(list, selectid, now_grade, dynamic.getRdtyId());
				}
			}
			else if(parentid.equals(dynamic.getRdtyPid())){
				buffer.append("<option value='"+dynamic.getRdtyId()+"'");
				if(selectid.equals(dynamic.getRdtyId())){
					buffer.append(" selected=true ");
				}
				buffer.append(">├").append(tree).append(dynamic.getRdtyName()).append("</option>");
				loopOptions(list, selectid, now_grade, dynamic.getRdtyId());
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * 根据特色库ID和自定义分类类型获取所有的自定义分类
	 * @param rmstId  特色库ID
	 * @param rdtyType 自定义分类类型
	 * @return
	 */
	@Override
	public List<ResDynamicType> getDynamicTypeList(Long rmstId, int rdtyType) {
		String hql = "from ResDynamicType rdty where rdty.rdtyType=" + rdtyType + " and rdty.rmstId=? order by rdty.rdtyIndex ASC";
		List<ResDynamicType> list = getHibernateTemplate().find(hql,rmstId);
		return list;
	}

	
	
	/**
	 * 添加对分类号的唯一性验证
	 * @param rmstId
	 * @param rdtyType
	 * @param rdtyCode
	 * @return 如果有重复返回true，否则返回false
	 */
	public boolean checkGroupCode(Long rmstId, int rdtyType, String rdtyCode, Long selfId){
		String hql = "select count(rdtyId) from ResDynamicType where rmstId=? and rdtyCode=? and rdtyType=" + rdtyType;
		if(selfId != null && selfId > 0){
			hql += " and rdtyId!=" + selfId;
		}
		Object[] o = new Object[2];
		o[0] = rmstId;
		o[1] = rdtyCode;
		List<Long> list = getHibernateTemplate().find(hql,o);
		if(list == null || list.size() < 1){
			return false;
		}else{
			if(list.get(0) > 0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/******************************以下是自定义分类导入导出相关************************/
	
	private List<ResDynamicType> DynamicTypeCache = new ArrayList<ResDynamicType>();
	private ResDynamicType tempDynamic = null; 
	
	@Override
	public List<Object[]> getDynamicType(Long rmstId, int rdtyType) {
		tempList = new ArrayList<Object[]>();
//		String hql = "select rdty.rdtyId, rdty.rdtyPid, rdty.rdtyName, rdty.rdtyCode, rdty.rdtyGrade, rdty.rdtyIndex from ResDynamicType rdty where rdty.rdtyType=" + rdtyType + " and rdty.rmstId=? order by rdty.rdtyIndex ASC";
//		List<Object[]> list = getHibernateTemplate().find(hql,rmstId);
		List<ResDynamicType> groupList = getDynamicTypeList(rmstId, rdtyType);
		DynamicTypeCache = groupList;
		
		loopGrade(groupList, null);
		
		List<Object[]> list = tempList;
		tempList = null;
		return list;
	}
	
	private List<Object[]> tempList = new ArrayList<Object[]>();
	private Object[] dataObj = null;
	private String rdtyName = "";
	public void loopGrade(List<ResDynamicType> list, Long parentid){
		if(list == null || list.size() < 1){
			return;
		}
		ResDynamicType dynamic = null;
		for(int i=0,s=list.size(); i<s; i++){
			dynamic = list.get(i);
			if(parentid == null || parentid < 1){
				if(dynamic.getRdtyPid() == null || dynamic.getRdtyPid() < 1){
					dataObj = new Object[8];
					dataObj[0] = dynamic.getRdtyName(); //分类名称
					dataObj[1] = dynamic.getRdtyZwName(); //藏文名称
					dataObj[2] = dynamic.getRdtyEnName(); //英文名称
					dataObj[3] = dynamic.getRdtyCode() == null ? "" : dynamic.getRdtyCode(); //分类号
					dataObj[4] = ""; //分类id
					dataObj[5] = dynamic.getRdtyIndex() == null ? "" : dynamic.getRdtyIndex(); //排序号
					dataObj[6] = dynamic.getRdtyGrade() == null ? "" : dynamic.getRdtyGrade(); //级数
					dataObj[7] = dynamic.getRdtyId();
					tempList.add(dataObj);
					loopGrade(list, dynamic.getRdtyId());
				}
			}
			else if(parentid.equals(dynamic.getRdtyPid())){
				rdtyName = dynamic.getRdtyName();
				if(dynamic.getRdtyGrade() != null){
					for(int g = 0; g<dynamic.getRdtyGrade(); g++){
						rdtyName = "    " + rdtyName;
					}
				}
				
				String parentCode = getGroupCodeById(dynamic.getRdtyPid());
				
				dataObj = new Object[8];
				 
				dataObj[0] = rdtyName; //分类名称
				dataObj[1] = dynamic.getRdtyZwName(); //藏文名称
				dataObj[2] = dynamic.getRdtyEnName(); //英文名称
				dataObj[3] = dynamic.getRdtyCode() == null ? "" : dynamic.getRdtyCode(); //分类号
				dataObj[4] = parentCode == null ? "" : parentCode; //上级分类号
				dataObj[5] = dynamic.getRdtyIndex() == null ? "" : dynamic.getRdtyIndex(); //排序号
				dataObj[6] = dynamic.getRdtyGrade() == null ? "" : dynamic.getRdtyGrade(); //级数
				dataObj[7] = dynamic.getRdtyId();
				tempList.add(dataObj);
				loopGrade(list, dynamic.getRdtyId());
			}
		}
	}
	
	private String getGroupCodeById(Long rdtyId){
		if(rdtyId == null || rdtyId < 1){
			return "";
		}
		String code = "";
		for(int i=0,s=DynamicTypeCache.size(); i<s; i++){
			if(DynamicTypeCache.get(i).getRdtyId().equals(rdtyId)){
				code = DynamicTypeCache.get(i).getRdtyCode();
				break;
			}
		}
		return code;
	}

	/***************************以下是分类导入相关**************************/
	/**
	 * Excel文件有效性验证
	 * @param xlsFile
	 * @return
	 */
	@Override
	public String checkGroupExcel(File xlsFile) {
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);

			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int columns = rs.getColumns();
			if(columns < 4){
				return "Excel文件无效！";
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return "异常:" + e.getMessage();
		}
		return null;
	}
	
	
	private List<String> codeCacheTemp = null;
	/**
	 * Excle文件中的数据校验
	 * @param xlsFile
	 * @return
	 */
	public String checkGroupData(File xlsFile, int beginRow, Long rmstId, int rdtyType){
		StringBuffer buffer = new StringBuffer(); //错误消息buffer
		String code = ""; 
		codeCacheTemp = new LinkedList<String>();
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			
			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			
			//将导入文件中的数据全部封装成对象整合到已有的分类集合中
			beginRow = beginRow - 1; //Excel行数下标是从0开始，因此要减去一位
			for(int i=beginRow; i<rows; i++){
				code = rs.getCell(3, i).getContents().trim();
				if(CommonUtil.isNotNull(rs.getCell(0, i).getContents().trim()) == false){
					buffer.append("<li>第").append(i + 1).append("行分类名称为空！</li>");
				}
				if(CommonUtil.isNotNull(code) == false ){
					buffer.append("<li>第").append(i + 1).append("行“").append(rs.getCell(0, i).getContents().trim()).append("”分类号为空！</li>");
				}else{
					if(codeCacheTemp.contains(code)){
						buffer.append("<li>分类号“").append(code).append("”有重复！</li>");
					}else{
						codeCacheTemp.add(code);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "异常：" + e.getMessage();
		}
		String message = buffer.toString();
		buffer = null;
		codeCacheTemp = null;
		return message;
	}
	
	/**
	 * 批量导入分类
	 * @param xlsFile 上传的Excel文件
	 * @param rmstId 特色库ID
	 * @param rdtyType 分类类型
	 * @param coverType 分类覆盖类型
	 * @return
	 */
	@Override
	public String saveImportExcel(File xlsFile, HttpServletRequest request) {
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0); //资源类型ID
		int beginRows = ServletRequestUtils.getIntParameter(request, "beginRow", 2); //起始行
		int coverType = ServletRequestUtils.getIntParameter(request, "coverType", 1); //覆盖类型
		
		//初始化已有的分类
//		DynamicTypeCache = getDynamicTypeList(rmstId, rdtyType); 
		initDynamicCache(rmstId, rdtyType);
		
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			
			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			
			ResDynamicType dynamic = null;
			//将导入文件中的数据全部封装成对象整合到已有的分类集合中
			beginRows = beginRows - 1; //Excel行数下标是从0开始，因此要减去一位
			for(int i=beginRows; i<rows; i++){
				dynamic = new ResDynamicType();
				dynamic.setRmstId(rmstId);
				dynamic.setRdtyType(rdtyType);
				dynamic.setRdtyCount(0);
				dynamic.setRdtyName(rs.getCell(0, i).getContents().trim());
				dynamic.setRdtyZwName(rs.getCell(1, i).getContents().trim());
				dynamic.setRdtyEnName(rs.getCell(2, i).getContents().trim()); 
				try {
					dynamic.setRdtyIndex(Integer.parseInt(rs.getCell(5, i).getContents().trim()));
				} catch (Exception e) {
					dynamic.setRdtyIndex(null);
				}
				dynamic.setRdtyCode(rs.getCell(3, i).getContents().trim());
				dynamic.setParentCode(rs.getCell(4, i).getContents().trim());
				
//				DynamicTypeCache.add(dynamic);
				addDynamicType(dynamic, coverType);
			}
			loopGroup(DynamicTypeCache, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "异常：" + e.getMessage();
		}
		
		return null;
	}
	
	private void initDynamicCache(Long rmstId, int rdtyType){
		DynamicTypeCache = getDynamicTypeList(rmstId, rdtyType); 
		if(DynamicTypeCache == null || DynamicTypeCache.size() < 1){
			return;
		}
		ResDynamicType dynamic = null;
		for(int i=0,s=DynamicTypeCache.size(); i<s; i++){
			dynamic = DynamicTypeCache.get(i);
			if(dynamic.getRdtyPid() == null || dynamic.getRdtyPid() < 1){
				continue;
			}
			for(int j=0,js=DynamicTypeCache.size(); j<js; j++){
				if(dynamic.getRdtyPid().equals(DynamicTypeCache.get(j).getRdtyId())){
					dynamic.setParentCode(DynamicTypeCache.get(j).getRdtyCode());
					DynamicTypeCache.set(i, dynamic);
					break;
				}
			}
		}
	}

	private ResDynamicType dynamicType = null;
	private void addDynamicType(ResDynamicType dynamic, int coverType){
		//不覆盖重复
		if(coverType == 4){
			DynamicTypeCache.add(dynamic);
			return;
		}
		boolean isCover = false;
		for(int i=0,s=DynamicTypeCache.size(); i<s; i++){
			dynamicType = DynamicTypeCache.get(i);
			//将空设置成""便于比较
			if(CommonUtil.isNotNull(dynamicType.getParentCode()) == false){
				dynamicType.setParentCode("");
			}
			if(dynamicType.getParentCode().equals(dynamic.getParentCode())){
				//同级同分类名同分类号
				if(coverType == 1){
					if(dynamicType.getRdtyName().equals(dynamic.getRdtyName()) && dynamicType.getRdtyCode().equals(dynamic.getRdtyCode())){
						isCover = true;
						break;
					}
				}
				//同级同分类名
				if(coverType == 2){
					if(dynamicType.getRdtyName().equals(dynamic.getRdtyName())){
						isCover = true;
						break;
					}
				}
				//同级同分类号
				if(coverType == 3){
					if(dynamicType.getRdtyCode().equals(dynamic.getRdtyCode())){
						isCover = true;
						break;
					}
				}
			}
		}
		if(isCover == false){
			DynamicTypeCache.add(dynamic);
		}
	}
	
	
	private void loopGroup(List<ResDynamicType> list, String parentCode){
		ResDynamicType dynamic = null;
		for(int i=0,s=list.size(); i<s; i++){
			dynamic = list.get(i);
			if(CommonUtil.isNotNull(dynamic.getParentCode()) == false){
				dynamic.setParentCode("");
			}
			if(dynamic.getParentCode().equals(parentCode)){
				if(dynamic.getRdtyId() == null || dynamic.getRdtyId() < 1){
					getGroupByCode(parentCode);
					if(tempDynamic == null){
						dynamic.setRdtyPid(null);
						dynamic.setRdtyGrade(1L);
					}else{
						dynamic.setRdtyPid(tempDynamic.getRdtyId());
						dynamic.setRdtyGrade(tempDynamic.getRdtyGrade() + 1);
					}
					getHibernateTemplate().save(dynamic);
				}
				loopGroup(list, dynamic.getRdtyCode());
			}
		}
	}
	
	/**
	 * 根据分类号获取分类ID
	 * @return
	 */
	private void getGroupByCode(String code){
		tempDynamic = null;
		if(CommonUtil.isNotNull(code) == false){
			return;
		}
		for(int i=0,s=DynamicTypeCache.size(); i<s; i++){
			if(DynamicTypeCache.get(i).getRdtyCode() != null && DynamicTypeCache.get(i).getRdtyCode().equals(code)){
				tempDynamic = DynamicTypeCache.get(i);
				break;
			}
		}
	}

	/**
	 * 更新自定义分类的资源统计数
	 */
	@Override
	public void updateDynamicCount() {
		String getHql = "from ResDynamicType rdty order by rdty.rdtyIndex ASC";
		List<ResDynamicType> list = getHibernateTemplate().find(getHql);
		if(list == null || list.size() < 1){
			return;
		}
		String childids;
		ResDynamicType dynamic = null;
		long count = 0;
		String hql = "";
		String commonWhere = PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
		for(int i=0,s=list.size(); i<s; i++){
			dynamic = list.get(i);
			childids = getAllChildIds(dynamic.getRdtyId());
			count = getCountByIds(childids, dynamic.getRdtyType(), commonWhere);
			hql = "update ResDynamicType set rdtyCount=" + count +" where rdtyId=?";
			getHibernateTemplate().bulkUpdate(hql, dynamic.getRdtyId());
		}
	}
	
}
