package cn.com.adtech.riss2.dwrFacade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;
import cn.com.adtech.riss2.domain.sc.ScFaqType;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

public class ScFacadeImp implements IScFacade {
	private IScService scServices;

	private IIdsService idsServices;

	public IIdsService getIdsServices() {
		return idsServices;
	}

	public void setIdsServices(IIdsService idsServices) {
		this.idsServices = idsServices;
	}

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	@Override
	public String addScFaqType(Long tempId, Long pId, String scftName) {
		try {
			if(pId == null)
				throw new ErrorMessageException("FAQ库分类父ID值不存在！");
			ScFaqType sft = (ScFaqType)this.scServices.get(ScFaqType.class, pId);
			ScFaqType newSft = new ScFaqType();
			List list2 = null;
			// 检验分类名称是否存在
			List list = this.scServices.getScFaqTypeList(null, null, scftName, null, null, null);
			if(list.size() > 0)
				return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + ",\"rstyName\":\"" + scftName + "\"}";
			if(sft != null) {
				list2 = this.scServices.getScFaqTypeList(null, pId, null, null, "1", null);
				newSft.setScftParentId(sft.getScftId());
				newSft.setScftGrade(sft.getScftGrade() + 1);
			}else {
				list2 = this.scServices.getScFaqTypeList(null, null, null, 1L, "1", null);
				newSft.setScftParentId(null);
				newSft.setScftGrade(1L);
			}
			if(scftName.trim().length() > 20)
				scftName = scftName.substring(0, 20);
			newSft.setScftName(scftName);
			Long tempIndex = 1L;
			if(list2.size() > 0)
				tempIndex = ((ScFaqType)list2.get(0)).getScftIndex() + 1;
			newSft.setScftIndex(tempIndex);
			this.scServices.save(newSft);
			return "{\"rstyId\":\"" + newSft.getScftId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"新增分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String deleteScFaqType(Long scftId) {
		ScFaqType sft = null;
		try {
			if(scftId != null) {
				sft = (ScFaqType)this.scServices.get(ScFaqType.class, scftId);
				if(sft != null) {
					this.scServices.delete(sft);
				}
			}
		}catch(Exception e) {
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":" + sft.getScftGrade() + "}";
	}

	@Override
	public String handlerScFaqTypeIndex(Long scftId, String type) {
		try {
			if(scftId == null)
				throw new ErrorMessageException("分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("分类操作类型不明！");
			ScFaqType sft = (ScFaqType)this.scServices.get(ScFaqType.class, scftId);
			if(sft == null)
				throw new ErrorMessageException("分类不存在！");
			// 排序号
			Long tempIndex = sft.getScftIndex();
			if("up".equals(type)) {
				List upList = null;
				if(tempIndex > 1) {
					if(sft.getScftParentId() == null)
						upList = this.scServices.getScFaqTypeList(null, null, null, 1L, "2", tempIndex);
					else
						upList = this.scServices.getScFaqTypeList(null, sft.getScftParentId(), null, null, "2", tempIndex);
					if(upList.size() > 0) {
						ScFaqType tempSft = (ScFaqType)upList.get(0);
						// 更新分类排序
						sft.setScftIndex(tempSft.getScftIndex());
						this.scServices.save(sft);
						tempSft.setScftIndex(tempIndex);
						this.scServices.save(tempSft);
					}
				}
			}else {
				ScFaqType tempSft = null;
				List list = null;
				if(sft.getScftGrade() == 1) {
					list = this.scServices.getScFaqTypeList(null, null, null, 1L, "1", null);
				}else {
					list = this.scServices.getScFaqTypeList(null, sft.getScftParentId(), null, null, "1", null);
				}
				if(list.size() > 0)
					tempSft = (ScFaqType)list.get(0);
				if(tempSft == null)
					throw new ErrorMessageException("分类不存在！");
				Long temp2Index = tempSft.getScftIndex();
				if(tempIndex > 0 && temp2Index > 0 && tempIndex < temp2Index) {
					List list2 = null;
					ScFaqType temp2Sft = null;
					if(sft.getScftGrade() == 1) {
						list2 = this.scServices.getScFaqTypeList(null, null, null, 1L, "3", tempIndex);
					}else {
						list2 = this.scServices.getScFaqTypeList(null, sft.getScftParentId(), null, sft.getScftGrade(), "3", tempIndex);
					}
					if(list2.size() > 0)
						temp2Sft = (ScFaqType)list2.get(0);
					if(temp2Sft == null)
						throw new ErrorMessageException("下级分类不存在！");
					sft.setScftIndex(temp2Sft.getScftIndex());
					this.scServices.save(sft);
					temp2Sft.setScftIndex(tempIndex);
					this.scServices.save(temp2Sft);
				}
			}
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateScFaqType(Long scftId, String scftName) {
		try {
			if(scftId == null)
				throw new ErrorMessageException("分类ID值不存在！");
			if(scftName.trim().length() < 1)
				throw new ErrorMessageException("分类名称不能为空！");
			if(scftName.trim().length() > 20)
				scftName = scftName.substring(0, 20);
			ScFaqType sft = (ScFaqType)this.scServices.get(ScFaqType.class, scftId);
			if(sft == null)
				throw new ErrorMessageException("分类不存在！");
			// 检验分类名称是否存在
			List list = this.scServices.getScFaqTypeList(null, null, scftName, null, null, null);
			if(list.size() > 0) {
				ScFaqType temp = (ScFaqType)list.get(0);
				if(!temp.getScftId().equals(scftId))
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + scftId + ",\"rstyName\":\"" + scftName + "\"}";
			}
			sft.setScftName(scftName);
			this.scServices.save(sft);
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String addScIssueType(Long tempId, Long id, String scitName) {
		try {
			ScIssueType sit = new ScIssueType();
			// 检验分类名称是否存在
			List list = this.scServices.getScIssueType(scitName, null, null);
			if(list.size() > 0)
				return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + ",\"rstyName\":\"" + scitName + "\"}";
			List list2 = this.scServices.getScIssueType(null, null, "1");
			if(list2.size() > 0) {
				ScIssueType tempSit = (ScIssueType)list2.get(0);
				sit.setScitIndex(tempSit.getScitIndex() + 1);
			}else
				sit.setScitIndex(1L);
			if(scitName.trim().length() > 20)
				scitName = scitName.substring(0, 20);
			sit.setScitName(scitName);
			sit.setScitNumber(0L);
			this.scServices.save(sit);
			return "{\"rstyId\":\"" + sit.getScitId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"新增分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String deleteScIssueType(Long scitId) {
		ScIssueType sit = null;
		try {
			if(scitId != null) {
				sit = (ScIssueType)this.scServices.get(ScIssueType.class, scitId);
				if(sit != null)
					this.scServices.delete(sit);
			}
		}catch(Exception e) {
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":1}";
	}

	@Override
	public String handlerScIssueTypeIndex(Long scitId, String type) {
		try {
			if(scitId == null)
				throw new ErrorMessageException("分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("分类操作类型不明！");
			ScIssueType sit = (ScIssueType)this.scServices.get(ScIssueType.class, scitId);
			if(sit == null)
				throw new ErrorMessageException("分类不存在！");
			Long tempIndex = sit.getScitIndex();
			if("up".equals(type)) {
				if(tempIndex > 1) {
					List list = this.scServices.getScIssueType(null, tempIndex, "2");
					if(list.size() > 0) {
						ScIssueType tempSit = (ScIssueType)list.get(0);
						// 更新分类
						sit.setScitIndex(tempSit.getScitIndex());
						this.scServices.save(sit);
						tempSit.setScitIndex(tempIndex);
						this.scServices.save(tempSit);
					}
				}
			}else {
				ScIssueType tempSit = null;
				ScIssueType sit2 = null;
				List list = this.scServices.getScIssueType(null, null, "1");
				if(list.size() > 0) {
					tempSit = (ScIssueType)list.get(0);
				}
				Long temp2Index = tempSit.getScitIndex();
				if(tempIndex > 0 && temp2Index > 0 && tempIndex < temp2Index) {
					List temp_list = this.scServices.getScIssueType(null, tempIndex, "3");
					if(temp_list.size() > 0) {
						sit2 = (ScIssueType)temp_list.get(0);
					}
					if(sit2 == null)
						throw new ErrorMessageException("下级分类不存在！");
					sit.setScitIndex(sit2.getScitIndex());
					this.scServices.save(sit);
					sit2.setScitIndex(tempIndex);
					this.scServices.save(sit2);
				}
			}
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateScIssueType(Long scitId, String scitName) {
		try {
			if(scitId == null)
				throw new ErrorMessageException("分类ID不存在！");
			if(scitName.trim().length() < 1)
				throw new ErrorMessageException("分类名称不能为空！");
			if(scitName.trim().length() > 20)
				scitName = scitName.substring(0, 20);
			ScIssueType sit = (ScIssueType)this.scServices.get(ScIssueType.class, scitId);
			if(sit == null)
				throw new ErrorMessageException("分类不存在！");
			List list = this.scServices.getScIssueType(scitName, null, null);
			if(list.size() > 0) {
				ScIssueType tempSit = (ScIssueType)list.get(0);
				if(!tempSit.getScitId().equals(scitId))
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + scitId + ",\"rstyName\":\"" + sit.getScitName() + "\"}";
			}
			sit.setScitName(scitName);
			this.scServices.save(sit);
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String addScConsultationType(Long tempId, Long id, String scitName) {
		try {
			ScConsultationType sct = new ScConsultationType();
			// 检验分类名称是否存在
			List list = this.scServices.getScConsultationType(scitName, null, null);
			if(list.size() > 0)
				return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + ",\"rstyName\":\"" + scitName + "\"}";
			List list2 = this.scServices.getScConsultationType(null, null, "1");
			if(list2.size() > 0) {
				ScConsultationType tempSct = (ScConsultationType)list2.get(0);
				sct.setSctyIndex(tempSct.getSctyIndex() + 1);
			}else
				sct.setSctyIndex(1L);
			if(scitName.trim().length() > 20)
				scitName = scitName.substring(0, 20);
			sct.setSctyName(scitName);
			sct.setSctyNumber(0L);
			this.scServices.save(sct);
			return "{\"rstyId\":\"" + sct.getSctyId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"新增分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String deleteScConsultationType(Long scitId) {
		ScConsultationType sct = null;
		try {
			if(scitId != null) {
				sct = (ScConsultationType)this.scServices.get(ScConsultationType.class, scitId);
				if(sct != null)
					this.scServices.delete(sct);
			}
		}catch(Exception e) {
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":1}";
	}

	@Override
	public String handlerScConsultationTypeIndex(Long scitId, String type) {
		try {
			if(scitId == null)
				throw new ErrorMessageException("分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("分类操作类型不明！");
			ScConsultationType sct = (ScConsultationType)this.scServices.get(ScConsultationType.class, scitId);
			if(sct == null)
				throw new ErrorMessageException("分类不存在！");
			Long tempIndex = sct.getSctyIndex();
			if("up".equals(type)) {
				if(tempIndex > 1) {
					List list = this.scServices.getScConsultationType(null, tempIndex, "2");
					if(list.size() > 0) {
						ScConsultationType tempSct = (ScConsultationType)list.get(0);
						// 更新分类
						sct.setSctyIndex(tempSct.getSctyIndex());
						this.scServices.save(sct);
						tempSct.setSctyIndex(tempIndex);
						this.scServices.save(tempSct);
					}
				}
			}else {
				ScConsultationType tempSct = null;
				ScConsultationType sct2 = null;
				List list = this.scServices.getScConsultationType(null, null, "1");
				if(list.size() > 0) {
					tempSct = (ScConsultationType)list.get(0);
				}
				Long temp2Index = tempSct.getSctyIndex();
				if(tempIndex > 0 && temp2Index > 0 && tempIndex < temp2Index) {
					List temp_list = this.scServices.getScConsultationType(null, tempIndex, "3");
					if(temp_list.size() > 0) {
						sct2 = (ScConsultationType)temp_list.get(0);
					}
					if(sct2 == null)
						throw new ErrorMessageException("下级分类不存在！");
					sct.setSctyIndex(sct2.getSctyIndex());
					this.scServices.save(sct);
					sct2.setSctyIndex(tempIndex);
					this.scServices.save(sct2);
				}
			}
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateScConsultationType(Long scitId, String scitName) {
		try {
			if(scitId == null)
				throw new ErrorMessageException("分类ID不存在！");
			if(scitName.trim().length() < 1)
				throw new ErrorMessageException("分类名称不能为空！");
			if(scitName.trim().length() > 20)
				scitName = scitName.substring(0, 20);
			ScConsultationType sct = (ScConsultationType)this.scServices.get(ScConsultationType.class, scitId);
			if(sct == null)
				throw new ErrorMessageException("分类不存在！");
			List list = this.scServices.getScConsultationType(scitName, null, null);
			if(list.size() > 0) {
				ScConsultationType tempSct = (ScConsultationType)list.get(0);
				if(!tempSct.getSctyId().equals(scitId))
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + scitId + ",\"rstyName\":\"" + sct.getSctyName() + "\"}";
			}
			sct.setSctyName(scitName);
			this.scServices.save(sct);
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String addInformationType(Long tempId, Long id, String idstName) {
		try {
			IdsInformationType iit = new IdsInformationType();
			List list = this.idsServices.getIdsInformationType(idstName, null, null);
			if(list.size() > 0)
				return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + ",\"rstyName\":\"" + idstName + "\"}";
			List list2 = this.idsServices.getIdsInformationType(null, null, "1");
			if(list2.size() > 0) {
				IdsInformationType tempIit = (IdsInformationType)list2.get(0);
				if(tempIit.getIdstIndex() == null){
					iit.setIdstIndex(1L);
				}else{
					iit.setIdstIndex(tempIit.getIdstIndex() + 1);
				}
			}else
				iit.setIdstIndex(1L);
			if(idstName.trim().length() > 20)
				idstName = idstName.substring(0, 20);
			iit.setIdstName(idstName);
			iit.setIdstNumber(0L);
			this.scServices.save(iit);
			this.idsServices.updateInfoClassCache();
			return "{\"rstyId\":\"" + iit.getIdstId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"新增分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String deleteInformationType(Long idstId) {
		IdsInformationType iit = null;
		try {
			if(idstId != null) {
				iit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, idstId);
				if(iit != null) {
					this.idsServices.delete(iit);
					this.idsServices.updateInfoClassCache();
				}
			}
		}catch(Exception e) {
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":1}";
	}

	@Override
	public String handlerInformationTypeIndex(Long idstId, String type) {
		try {
			if(idstId == null)
				throw new ErrorMessageException("分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("分类操作类型不明！");
			IdsInformationType iit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, idstId);
			if(iit == null)
				throw new ErrorMessageException("分类不存在！");
			Long tempIndex = iit.getIdstIndex();
			if("up".equals(type)) {
				if(tempIndex > 1) {
					List list = this.idsServices.getIdsInformationType(null, tempIndex, "2");
					if(list.size() > 0) {
						IdsInformationType tempIit = (IdsInformationType)list.get(0);
						// 更新分类
						iit.setIdstIndex(tempIit.getIdstIndex());
						this.idsServices.save(iit);
						tempIit.setIdstIndex(tempIndex);
						this.idsServices.save(tempIit);
					}
				}
			}else {
				IdsInformationType tempIit = null;
				IdsInformationType iit2 = null;
				List list = this.idsServices.getIdsInformationType(null, null, "1");
				if(list.size() > 0)
					tempIit = (IdsInformationType)list.get(0);
				Long temp2Index = tempIit.getIdstIndex();
				if(tempIndex > 0 && temp2Index > 0 && tempIndex < temp2Index) {
					List temp_list = this.idsServices.getIdsInformationType(null, tempIndex, "3");
					if(temp_list.size() > 0)
						iit2 = (IdsInformationType)temp_list.get(0);
					if(iit2 == null)
						throw new ErrorMessageException("下级分类不存在！");
					iit.setIdstIndex(iit2.getIdstIndex());
					this.idsServices.save(iit);
					iit2.setIdstIndex(tempIndex);
					this.scServices.save(iit2);
				}
			}
			this.idsServices.updateInfoClassCache();
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateInformationType(Long idstId, String idstName) {
		try {
			if(idstId == null)
				throw new ErrorMessageException("分类ID不存在！");
			if(idstName.trim().length() < 1)
				throw new ErrorMessageException("分类名称不能为空！");
			if(idstName.trim().length() > 20)
				idstName = idstName.substring(0, 20);
			IdsInformationType iit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, idstId);
			if(iit == null)
				throw new ErrorMessageException("分类不存在！");
			List list = this.idsServices.getIdsInformationType(idstName, null, null);
			if(list.size() > 0) {
				IdsInformationType tempIit = (IdsInformationType)list.get(0);
				if(!tempIit.getIdstId().equals(idstId))
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + idstId + ",\"rstyName\":\"" + iit.getIdstName() + "\"}";
			}
			iit.setIdstName(idstName);
			this.idsServices.save(iit);
			
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String getIssueReplyList(int currentPage, Long scisId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否合法
//		SysUserinfo userinfo = null;
//		try {
//			userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
//			if(userinfo == null) {
//				response.sendRedirect(request.getContextPath() + "/index.htm");
//				return null;
//			}
//		}catch(Exception e) {
//			try {
//				response.sendRedirect(request.getContextPath() + "/illegal.htm");
//			}catch(IOException e1) {
//			}
//		}
		// 分页显示
		Pagination pagination = new Pagination();
		int pageSize = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		
		pagination.setCurrentPage(currentPage >= 0 ? currentPage : 0);
		pagination.setPageResults(pageSize);
		// 获取回复列表
		ScIssue scIssue = null;
		if(scisId != null && scisId > 0)
			scIssue = (ScIssue)this.scServices.get(ScIssue.class, scisId);
		if(scIssue != null) {
			List list = this.scServices.getScIssueReplyList(pagination, scisId, scIssue.getScisCloseScirId(), "1");
			if(list == null || list.size() == 0)
				return "{responseIsNull:false}";
			String result = CommonUtil.createJsonTable(list, new String[] {"scirId", "suinName", "scirReplyTime", "scirContent",
					"scirAttachmentName", "scirAttachmentUrl"}, pagination.getTotalResults() + "");
			return result;
		}
		return "{responseIsNull:false}";
	}
}
