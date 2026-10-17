package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

/**
 * 学科分类管理
 * @author MaChao
 *
 */
public class SubjectTypeServiceImp extends CommonServiceImp implements ISubjectTypeService {

	
	/**
	 * 根据ID获取学科对象
	 * @param rstyId
	 * @return
	 */
	@Override
	public ResSubjectType getSubjectById(Long rstyId) {
		ResSubjectType subject = (ResSubjectType) get(ResSubjectType.class, rstyId);
		return subject;
	}

	/**
	 * 获取所有学科
	 * @return
	 */
	@Override
	public List<ResSubjectType> getSubjectList() {
		String hql = "from ResSubjectType rst where rst.rstyId>0 order by rst.rstyPid ,rst.rstyIndex ASC";
		List<ResSubjectType> list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * 获取学科分类的下拉选项
	 * @param selectid 选择的选项ID
	 * @return
	 */
	@Override
	public String getSubjectOptions(Long selectid) {
		List<ResSubjectType> list = getSubjectList();
		if(list == null || list.size() < 1){
			return "";
		}
		buffer = new StringBuffer();
		String options = "<option value='0'>无</option>" + loopOptions(list, selectid, 0, null);
		return options;
	}

	
	private StringBuffer buffer = new StringBuffer();
	private String loopOptions(List<ResSubjectType> list, Long selectid, int grade, Long parentid){
		String tree = "";
		for(int i=0; i<grade; i++){
			tree += "─";
		}
		int now_grade = grade + 1;
		ResSubjectType subject = null;
		for(int i=0,s=list.size(); i<s; i++){
			subject = list.get(i);
			if(parentid == null || parentid < 1){
				if(subject.getRstyPid() == null || subject.getRstyPid() < 1){
					buffer.append("<option value='"+subject.getRstyId()+"'");
					if(selectid != null && selectid.equals(subject.getRstyId())){
						buffer.append(" selected=true ");
					}
					buffer.append(">├").append(tree).append(subject.getRstyName()).append("</option>");
					loopOptions(list, selectid, now_grade, subject.getRstyId());
				}
			}
			else if(parentid.equals(subject.getRstyPid())){
				buffer.append("<option value='"+subject.getRstyId()+"'");
				if(selectid != null && selectid.equals(subject.getRstyId())){
					buffer.append(" selected=true ");
				}
				buffer.append(">├").append(tree).append(subject.getRstyName()).append("</option>");
				loopOptions(list, selectid, now_grade, subject.getRstyId());
			}
		}
		
		return buffer.toString();
	}

	/**
	 * 保存学科分类
	 * @param rstyId
	 * @param rstyPid
	 * @param rstyName
	 * @param rstyIndex
	 * @param rstyCode
	 * @return
	 */
	@Override
	public boolean saveSubject(Long rstyId, Long rstyPid, String rstyName,
			Integer rstyIndex, String rstyCode) {
		ResSubjectType subject = null;
		Long rstyGrade;
		// 如果页面无法确定分类的级数，则根据它的上级分类查找对应的级数
		if (rstyPid == null || rstyPid < 1) {
			rstyGrade = 1L;
		} else {
			subject = (ResSubjectType) get(ResSubjectType.class, rstyPid);
			if (subject == null) {
				rstyGrade = 1L;
			} else {
				rstyGrade = subject.getRstyGrade() + 1;
			}
		}
		if(rstyId < 1){
			rstyId = null;
		}
		
		subject = new ResSubjectType();
		subject.setRstyCode(rstyCode);
		subject.setRstyGrade(rstyGrade);
		subject.setRstyId(rstyId);
		subject.setRstyIndex(rstyIndex);
		subject.setRstyName(rstyName);
		subject.setRstyPid(rstyPid);
		try {
			save(subject);
			return true;
		} catch (Exception e) {
			logger.error("自定义分类保存异常：" ,e);
			return false;
		}
	}

	/**
	 * 删除学科分类
	 * @param rstyId
	 * @return
	 */
	@Override
	public boolean deleteSubject(Long rstyId) {
		if(rstyId == null){
			return false;
		}
		String hql = "delete from ResSubjectType rst where rst.rstyId = ? or rst.rstyPid = ?";
		try {
			getHibernateTemplate().bulkUpdate(hql, new Object[]{rstyId,rstyId});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检验学科编号是否存在
	 * @param rstyCode
	 * @return
	 */
	@Override
	public boolean checkSubjectCode(String rstyCode, Long selfId) {
		String hql = "select count(rsty.rstyId) from ResSubjectType rsty where rsty.rstyCode='"+rstyCode+"' and rsty.rstyId !=" + selfId;
		try {
			String data = getSingleData(hql);
			Integer c = Integer.parseInt(data);
			return c > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
