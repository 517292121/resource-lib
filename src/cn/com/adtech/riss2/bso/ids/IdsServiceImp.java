package cn.com.adtech.riss2.bso.ids;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.web.bind.ServletRequestUtils;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.TagsChecker;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.IndexCommonService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class IdsServiceImp extends IndexCommonService implements IIdsService {
	public int totalNumber = 0;
	public int successNumber = 0;
	public int failNumber = 0;
	private String returnMessage = "";
	
	//索引管理
    private ILuceneDaoService luceneDaoService = new LuceneDaoServiceImp();
	
	@Override
	public List<IdsInformationType> getIdsInformationTypeList(String idstName, String sort, String dir) {
		List<Object> param = new ArrayList<Object>();
		String hql = "select iit.idstId , iit.idstName , iit.idstIndex , iit.idstNumber from IdsInformationType iit ";
		if(CommonUtil.isNotNull(idstName)) {
			hql += "where iit.idstName = ? ";
			param.add(idstName);
		}
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir))
			hql += "order by iit." + sort + " " + dir;
		
		List<IdsInformationType> list = getHibernateTemplate().find(hql, param.toArray());
		return list;
	}

	@Override
	public List getIdsInformation(String idsiTitle) {
		List<Object> param = new ArrayList<Object>();
		String hql = "from IdsInformation ii ";
		if(idsiTitle != null) {
			hql += "where ii.idsiTitle = ? ";
			param.add(idsiTitle);
		}
		return this.getHibernateTemplate().find(hql, param.toArray());
	}

	@Override
	public List getIdsInformationList(Pagination pagination, Map<String, Object> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select ii.idsiId , ii.idsiIdstId , ii.idsInformationType.idstName , ii.sysManager.smanName , ii.idsiTitle , ii.idsiDate , ii.idsiHitCount ";
		StringBuilder fromHql = new StringBuilder("from IdsInformation ii where ii.idsiId>0 ");
		StringBuilder orderHql = new StringBuilder();
		// 按标题查询
		List idsTitleCondition = (List)conditions.get("idsTitleCondition");
		if(idsTitleCondition != null) {
			int len = idsTitleCondition.size();
			if(len > 0) {
				fromHql.append(" and ( ");
				for(int i = 0; i < len; i++) {
					fromHql.append("  ii.idsiTitle like ? ");
					if(i != len - 1) {
						fromHql.append(" or ");
					}
					param.add("%" + ((List)idsTitleCondition).get(i) + "%");
				}
				fromHql.append(" ) ");
			}
		}
		if(conditions.containsKey("groupId")){
			fromHql.append(" and ii.idsiIdstId=").append(conditions.get("groupId"));
		}
		
		// 添加排序方式
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("smanName".equals(sort))
				orderHql.append(" order by ii.sysManager.smanName ").append(dir);
			else if("idstName".equals(sort))
				orderHql.append(" order by ii.idsInformationType.idstName ").append(dir);
			else
				orderHql.append(" order by ii.").append(sort).append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getIdsInformationType(String idstName, Long idstIndex, String type) {
		StringBuffer fromHql = new StringBuffer("from IdsInformationType iit ");
		List<Object> param = new ArrayList<Object>();
		String orderHql = "order by iit.idstIndex ASC";
		boolean isWhere = true;
		if(idstName != null && idstName.trim().length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" iit.idstName = ? ");
			param.add(idstName);
		}
		if("1".equals(type)) {
			orderHql = " order by iit.idstIndex  DESC ";
		}
		if("2".equals(type)) {
			orderHql = " order by iit.idstIndex DESC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" iit.idstIndex < ? ");
			param.add(idstIndex);
		}
		if("3".equals(type)) {
			orderHql = " order by iit.idstIndex ASC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" iit.idstIndex > ? ");
			param.add(idstIndex);
		}
		return this.getHibernateTemplate().find(fromHql.toString() + orderHql, param.toArray());
	}

	/**
	 * 根据isWhere 生成的hql，开头部分是使用where 还是 and
	 * 
	 * @param whereHql
	 * @param isWhere
	 * @return
	 */
	protected boolean createWhereAnd(StringBuffer whereHql, boolean isWhere) {
		if(isWhere) {
			whereHql.append(" where ");
			isWhere = false;
		}else {
			whereHql.append(" and ");
		}
		return isWhere;
	}

	@Override
	public IdsInformation getIdsInfomarion(Long idsId) {
		IdsInformation idsinfo = (IdsInformation)getHibernateTemplate().get(IdsInformation.class, idsId);
		return idsinfo;
	}

	@Override
	public String getIdsInformationOptions(long selectid) {
		String hql = "from IdsInformationType";
		
		List<IdsInformationType> list = getHibernateTemplate().find(hql);
		
		
		if(list == null || list.size() < 1){
			return "<option value=''>请选择</option>";
		}
		IdsInformationType idsType = null;
		StringBuffer buffer = new StringBuffer();
		for(int i=0,s=list.size(); i<s; i++){
			idsType = list.get(i);
			buffer.append("<option value='"+idsType.getIdstId()+"'");
			if(idsType.getIdstId() == selectid){
				buffer.append(" selected='selected'");
			}
			buffer.append(">"+idsType.getIdstName()+"</option>\n");
		}
		String html = buffer.toString();
		idsType = null;
		list = null;
		
		return html;
	}
	
	/***************************以下是导入相关**************************/
	/**
	 * Excel文件有效性验证
	 */
	@Override
	public String checkExcel(File xlsFile) {
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);

			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int columns = rs.getColumns();
			if(columns < 3){
				return "Excel文件无效！";
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return "异常:" + e.getMessage();
		}
		return null;
	}
	
	
	/**
	 * Excle文件中的数据校验
	 */
	public String checkExcelData(File xlsFile, int beginRow){
		StringBuffer buffer = new StringBuffer(); //错误消息buffer
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			//将导入文件中的数据全部封装成对象整合到已有的分类集合中
			beginRow = beginRow - 1;//Excel行数下标是从0开始，因此要减去一位
			for(int i=beginRow; i<rows; i++){
				if(CommonUtil.isNotNull(rs.getCell(0, i).getContents().trim()) == false){
					buffer.append("<li>第").append(i + 1).append("行信息标题为空！</li>");
				}
				if(CommonUtil.isNotNull(rs.getCell(1, i).getContents().trim()) == false){
					buffer.append("<li>第").append(i + 1).append("行信息分类为空！</li>");
				}
				if(CommonUtil.isNotNull(rs.getCell(2, i).getContents().trim()) == false){
					buffer.append("<li>第").append(i + 1).append("行信息内容为空！</li>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "异常：" + e.getMessage();
		}
		returnMessage = buffer.toString();
		buffer = null;
		return returnMessage;
	}

	/**
	 * 批量导入
	 */
	@Override
	public String saveImportExcel(File xlsFile, HttpServletRequest request) {
		int beginRows = ServletRequestUtils.getIntParameter(request, "beginRow", 2); //起始行
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		try {
			InputStream is = new FileInputStream(xlsFile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			
			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			
			StringBuffer buffer = new StringBuffer(); //错误消息buffer
			int _importNumber = 0;
			IdsInformation ids = null;
			
			List<ResMainBean> tempList = new ArrayList<ResMainBean>();
            ResMainBean dataBean = null;
            
			//将导入文件中的数据全部封装成对象整合到已有的分类集合中
			beginRows = beginRows - 1; //Excel行数下标是从0开始，因此要减去一位
			for(int i=beginRows; i<rows; i++){
				ids = new IdsInformation();
				String title = rs.getCell(0, i).getContents().trim();
				String group = rs.getCell(1, i).getContents().trim();
				String content = rs.getCell(2, i).getContents().trim();
				IdsInformationType idtype =  getInfoGroupByName(group);
				
				if(CommonUtil.isNotNull(title) == false){
					buffer.append("<li>第").append(i + 1).append("行信息标题为空！</li>");
					continue;
				}
				if(CommonUtil.isNotNull(group) == false){
					buffer.append("<li>第").append(i + 1).append("行信息分类为空！</li>");
					continue;
				}
				if(CommonUtil.isNotNull(content) == false){
					buffer.append("<li>第").append(i + 1).append("行信息内容为空！</li>");
					continue;
				}
				
				if(idtype == null){
					idtype = new IdsInformationType();
					idtype.setIdstName(group);
					idtype.setIdstNumber(0L);
					save(idtype);
				}
				if(CommonUtil.isNotNull(content) && TagsChecker.check(content) == false){
					content = TagsChecker.fix(content);
				}
				try {
					ids.setIdsiTitle(title);
					ids.setIdsiIdstId(idtype.getIdstId());
					ids.setIdsiSmanId(sysManager.getSmanId());
					ids.setIdsiDate(new Date());
					ids.setIdsiHitCount(0);
					ids.setIdsiContent(content);
					
					String idsiImgUrl = null;
					if(CommonUtil.isNotNull(content)){
						//如果没有上传封面，再根据内容判断是否包含img 如果包含，剔出图片链接
						Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
						Matcher m = p.matcher(content);
						if(m.find()){
							idsiImgUrl = m.group(1);
						}
					}
					ids.setIdsiImgUrl(idsiImgUrl);
					
					//剔出内容中的所有文字
					 String idsiDescription="";
					 idsiDescription = content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
					 if(idsiDescription.length() > 300){
						 idsiDescription=idsiDescription.substring(0,300);
					 }
					 ids.setIdsiDescription(idsiDescription);
					 save(ids);
					 // 更新分类中信息数量
					 idtype.setIdstNumber(idtype.getIdstNumber() + 1);
					 save(idtype);
					 _importNumber++;
					 
					//添加到索引
                    dataBean = infoToDatabean(ids);
                    tempList.add(dataBean);
                    if(tempList.size() >= 100){
                    	this.luceneDaoService.addMultiDatabean(tempList);
                    	tempList = new ArrayList<ResMainBean>();
                    }
                    
					System.out.println("已完成：" + (i+1) + "/" + rows);
				} catch (Exception e) {
					delete(idtype);
					delete(ids);
					e.printStackTrace();
				}
			}
			
			//把剩下的加入索引
            if(tempList.size() > 0){
            	this.luceneDaoService.addMultiDatabean(tempList);
            }
            
			this.totalNumber = rows;
			this.successNumber = _importNumber;
			this.failNumber = rows - _importNumber;
			this.returnMessage = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "异常：" + e.getMessage();
		}
		
		return null;
	}
	
	public IdsInformationType getInfoGroupByName(String name){
		String hql = "from IdsInformationType where idstName = ";
		hql += "'"+name+"'";
		List<IdsInformationType> list = this.getHibernateTemplate().find(hql);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public void updateGroupNum(){
		String hql = "update IdsInformationType it set it.idstNumber=(select count(*) from IdsInformation info where info.idsiIdstId=it.idstId) ";
		this.getHibernateTemplate().bulkUpdate(hql);
	}
	
	public String getMessage(){
		return returnMessage;
	}
	
	public int getTotalNumber(){
		return this.totalNumber;
	}
	
	public int getSuccessNumber(){
		return this.successNumber;
	}
	
	public int getFailNumber(){
		return this.failNumber;
	}
	
	@Override
	public List<Object[]> getInfoList(Pagination p, Long classId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select info.idsiId,");
		sb.append("info.idsiTitle,");
		sb.append("info.idsiDescription,");
		sb.append("info.idsiDate,");
		sb.append("info.idsiImgUrl,");
		sb.append("info.idsiHitCount");
		sb.append(" from IdsInformation info");
		if(classId != null && classId > 0L){
			sb.append(" where info.idsiIdstId=").append(classId);
		}
		sb.append(" order by info.idsiDate DESC");
		
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = getPageHibernateTemplate().find(p, hql);
		CommonUtil.parserId(dataList);
		return dataList;
	}

	@Override
	public void updateInfoClassCache() {
		new Thread(){
			@Override
			public synchronized void run() {
				super.run();
				StringBuffer sb = new StringBuffer();
				sb.append("select idst.idstId,");
				sb.append("idst.idstName,");
				sb.append("idst.idstNumber");
				sb.append(" from IdsInformationType idst");
				sb.append(" order by idst.idstIndex ASC");
				String hql = sb.toString();
				sb = null;
				
				List<Object[]> dataList = getHibernateTemplate().find(hql);
				PlatformCacheFactory.instance().setInfoClassList(dataList);
			}	
		}.start();
	}

	@Override
	public Object[] getInfoNext(Long idsiId) {
		Pagination p = new Pagination();
		p.setPageResults(1);
		StringBuffer sb = new StringBuffer();
		sb.append("select info.idsiId, info.idsiTitle from IdsInformation info");
		sb.append(" where info.idsiId>").append(idsiId);
		sb.append(" order by info.idsiId ASC");
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = getHibernateTemplate().find(hql);
		if(dataList != null && dataList.size() > 0){
			Object[] info = dataList.get(0);
			info[0] = CommonUtil.idToString(info[0].toString());
			return info;
		}
		return null;
	}

	@Override
	public Object[] getInfoPre(Long idsiId) {
		Pagination p = new Pagination();
		p.setPageResults(1);
		StringBuffer sb = new StringBuffer();
		sb.append("select info.idsiId, info.idsiTitle from IdsInformation info");
		sb.append(" where info.idsiId<").append(idsiId);
		sb.append(" order by info.idsiId DESC");
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = getHibernateTemplate().find(hql);
		if(dataList != null && dataList.size() > 0){
			Object[] info = dataList.get(0);
			info[0] = CommonUtil.idToString(info[0].toString());
			return info;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getIdsInformationType(String lan) {
		if(StringUtils.isBlank(lan)) {
			return null;
		}
		StringBuilder sb = new StringBuilder("select new Map(idst.idstId as id");
		sb.append(",idst.idstName as name");
		sb.append(",idst.idstIndex as index");
		sb.append(",idst.idstNumber as number");
		sb.append(",0 as pid)");
		sb.append(" from IdsInformationType idst");
		sb.append(" where idst.idstLanguage = ?");
		
		String hql = sb.toString();
		sb = null;
		return this.getHibernateTemplate().find(hql, lan);
	}

	@Override
	public Long getCountByName(String name, Long id) {
		if(StringUtils.isBlank(name)) {
			return 0L;
		}
		String hql = "select count(id) from IdsInformationType idst where idst.idstName = ?";
		if(id != null && id > 0L) {
			hql += " and idst.idstId != "+id;
		}
		return this.getCountNumber(hql, name);
	}

	@Override
	public Long getInfoCountById(Long id) {
		if(id == null || id < 1) {
			return 0L;
		}
		String hql = "select count(*) from IdsInformation iinf where iinf.idsiIdstId = ?";
		return this.getCountNumber(hql, id);
	}
}
