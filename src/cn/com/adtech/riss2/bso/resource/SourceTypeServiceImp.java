package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResLiterType;

import com.jbetter.hibernate.IdGenerator;

public class SourceTypeServiceImp extends CommonServiceImp implements ISourceTypeService {
	public boolean saveSourceType(ResLiterType rlty) {
		try {
			if(null==rlty.getRltyId()||rlty.getRltyId() < 1){
				Serializable s = this.getHibernateTemplate().save(rlty);
				if(s != null) {
					long id = Long.parseLong(s.toString());
					boolean b = createMetadataElement(id);
					if(b) {
						return true;
					}else {
						deleteSourceType(id);
						return false;
					}
				}else {
					return false;
				}
			}else {
				this.getHibernateTemplate().saveOrUpdate(rlty);
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteSourceType(long typeId) {
		try {
			String sql = "delete from RES_LITER_TYPE where RLTY_ID=" + typeId;
			boolean b = execute(sql);
			return b;
		}catch(Exception e) {
			return false;
		}
	}
	/**
	 * 判断该类型是否被引用
	 * @param typeId
	 * @return
	 */
	public boolean getExit(long typeId){
		String count1 = "select count(*) from ResTempletMeta where rtmeRltyId = "+typeId;
		String count2 = "select count(*) from ResSourceData where rsdaRltyId = "+typeId;
		Long i = super.getCountNumber(count1);
		Long j = super.getCountNumber(count2);
		if(i>0 || j>0)
			return false;
		return true;
	}
	private boolean createMetadataElement(long rltyId) {
		String[] arr = new String[16];
		arr[0] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Title','题名',1,'','M',1,0)";
		arr[1] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Creator','创建者',1,'','R',2,0)";
		arr[2] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Subject','主题',1,'','R',3,0)";
		arr[3] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Description','描述',1,'','R',4,0)";
		arr[4] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Publisher','出版者',1,'','O',5,0)";
		arr[5] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Contributor','其他责任者',1,'','MA',6,0)";
		arr[6] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Date','日期',1,'','MA',7,0)";
		arr[7] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Type','类型',1,'','O',8,0)";
		arr[8] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Format','格式',1,'','R',9,0)";
		arr[9] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Identifier','标识符',1,'','MA',10,0)";
		arr[10] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Source','来源',1,'','O',11,0)";
		arr[11] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Language','语种',1,'','MA',12,0)";
		arr[12] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Relation','关联',1,'','O',13,0)";
		arr[13] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Coverage','时空范围',1,'','O',14,0)";
		arr[14] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Rights','权限',1,'','R',15,0)";
		arr[15] = "insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(" + IdGenerator.generateId() + "," + rltyId + ",'Keyword','关键词',1,'','R',16,0)";
		boolean b = executeUpdateMultiSql(arr);
		return b;
	}

	public String getSourceTypeName(long typeId) {
		String sql = "select RLTY_NAME from RES_LITER_TYPE where RLTY_ID=" + typeId;
		String name = getFirstData(sql);
		return name;
	}

	@Override
	public List<ResLiterType> getAllTypesByRltyType(Short type) {
		if(type>0){
			return getHibernateTemplate().find("from ResLiterType rlt where rlt.rltyType=?", type);
		}else{
			return getHibernateTemplate().find("from ResLiterType rlt");
		}
	}

	@Override
	public List<ResLiterType> getAllResTypes() {
		return getHibernateTemplate().find("from ResLiterType rlt");
	}
}