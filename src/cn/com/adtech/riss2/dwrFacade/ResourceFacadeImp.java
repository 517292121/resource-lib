package cn.com.adtech.riss2.dwrFacade;

import java.util.List;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class ResourceFacadeImp implements IResourceFacade {
	private IMetadataManagementService metadataManagementService;

	public IMetadataManagementService getMetadataManagementService() {
		return metadataManagementService;
	}

	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}

	@Override
	public String deleteResSubjectType(Long rstyId) {
		ResSubjectType rst = null;
		try {
			if(rstyId != null) {
				rst = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, rstyId);
				if(rst != null) {
					this.metadataManagementService.delete(rst);
				}
			}
		}catch(Exception e) {
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":" + rst.getRstyGrade() + "}";
	}

	@Override
	public String addResSubjectType(Long tempId, Long pId, String rstyName) {
		
		try {
			if(pId == null)
				throw new ErrorMessageException("学科分类父ID值不存在！");
			
			ResSubjectType rst = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, pId);
			ResSubjectType newRst = new ResSubjectType();
			Object[] rst2 = null;
			if(rst != null) {
				// 检验分类名称是否存在
				List list = this.metadataManagementService.getResSubjectTypeList(pId, null, rstyName, null, 0, null);
				if(list.size() > 0)
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + "}";
				list = this.metadataManagementService.getResSubjectTypeList(pId, null, null, rst.getRstyGrade() + 1, 0, "1");
				if(list.size() > 0) {
					rst2 = (Object[])list.get(0);
				}
				newRst.setRstyPid(pId);
				newRst.setRstyGrade(rst.getRstyGrade() + 1);
			}else {
				// 检验分类名称是否存在
				List list = this.metadataManagementService.getResSubjectTypeList(pId, null, rstyName, null, 0, null);
				if(list.size() > 0)
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + "}";
				list = this.metadataManagementService.getResSubjectTypeList(null, null, null, 1L, 0, "1");
				if(list.size() > 0) {
					rst2 = (Object[])list.get(0);
				}
				newRst.setRstyPid(null);
				newRst.setRstyGrade(1L);
			}
			if(rstyName.trim().length() > 20)
				rstyName = rstyName.substring(0, 20);
			newRst.setRstyName(rstyName);
			Integer tempIndex = null;
			try {
				tempIndex = Integer.parseInt((String)rst2[3]);
				tempIndex += 1;
			}catch(Exception e) {
				tempIndex = 1;
			}
			newRst.setRstyIndex(tempIndex);
			this.metadataManagementService.save(newRst);
			return "{\"rstyId\":\"" + newRst.getRstyId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"创建自定义分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateResSubjectType(Long rstyId, String rstyName) {
		try {
			if(rstyId == null)
				throw new ErrorMessageException("学科分类ID值不存在！");
			if(rstyName.trim().length() < 1)
				throw new ErrorMessageException("学科分类名称不能为空！");
			if(rstyName.trim().length() > 20)
				rstyName = rstyName.substring(0, 20);
			ResSubjectType rst = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, rstyId);
			if(rst == null)
				throw new ErrorMessageException("学科分类不存在！");
			// 校验分类名称是否存在
			ResSubjectType tempRST = this.metadataManagementService.getResSubjectTypeIndex(rst.getRstyPid(), rstyName, 0, null);
			if(tempRST != null && rst.getRstyId() != rstyId)
				return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + rstyId + ",\"rstyName\":\"" + rst.getRstyName() + "\"}";
			rst.setRstyName(rstyName);
			this.metadataManagementService.save(rst);
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新学科分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String handlerResSubjectTypeIndex(Long rstyId, String type) {
		try {
			if(rstyId == null)
				throw new ErrorMessageException("学科分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("学科分类排序操作类型不明！");
			ResSubjectType rst = (ResSubjectType)this.metadataManagementService.get(ResSubjectType.class, rstyId);
			if(rst == null)
				throw new ErrorMessageException("学科分类不存在！");
			// 排序号
			int tempIndex = rst.getRstyIndex();
			if("up".equals(type)) {
				ResSubjectType upRst = null;
				if(tempIndex > 1) {
					if(rst.getRstyPid() == 0)
						upRst = this.metadataManagementService.getResSubjectTypeIndex(0L, null, tempIndex, "2");
					else
						upRst = this.metadataManagementService.getResSubjectTypeIndex(rst.getRstyPid(), null, tempIndex, "2");
					if(upRst == null)
						throw new ErrorMessageException("上级分类不存在");
					// 更新分类排序：上级后移，当前分类前移；
					rst.setRstyIndex(upRst.getRstyIndex());
					this.metadataManagementService.save(rst);
					upRst.setRstyIndex(tempIndex);
					this.metadataManagementService.save(upRst);
				}
			}else {
				ResSubjectType downRst = null;
				ResSubjectType tempRst = null;
				downRst = this.metadataManagementService.getResSubjectTypeIndex(0L, null, 0, "1");
				if(downRst == null)
					throw new ErrorMessageException("分类不存在");
				if(tempIndex > 0 && tempIndex < downRst.getRstyIndex()) {
					if(rst.getRstyPid() == 0)
						tempRst = this.metadataManagementService.getResSubjectTypeIndex(0L, null, tempIndex, "3");
					else
						tempRst = this.metadataManagementService.getResSubjectTypeIndex(rst.getRstyPid(), null, tempIndex, "3");
					if(tempRst == null)
						throw new ErrorMessageException("下级分类不存在");
					// 更新分类排序；
					rst.setRstyIndex(tempRst.getRstyIndex());
					this.metadataManagementService.save(rst);
					tempRst.setRstyIndex(tempIndex);
					this.metadataManagementService.save(tempRst);
				}
			}
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作学科分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String addResdynamicType(Long tempId, Long pId, Long rmstId, String rdtyName, int rdtyType) {
		try {
			if(pId == null)
				throw new ErrorMessageException("自定义分类父ID值不存在！");
			if(rmstId == null)
				throw new ErrorMessageException("特色库ID不存在！");
			ResMetaStore rmst = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rmst == null)
				throw new ErrorMessageException("特色库不存在！");
			ResDynamicType rdt = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, pId);
			ResDynamicType newRdt = new ResDynamicType();
			Object[] rdt2 = null;
			if(rdt != null) {
				// 检验分类名称是否存在
				List list = this.metadataManagementService.getResDynamicTypeList(pId, rmstId, rdtyName, null, 0, null, rdtyType);
				if(list.size() > 0)
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + "}";
				list = this.metadataManagementService.getResDynamicTypeList(pId, rmstId, null, rdt.getRdtyGrade() + 1, 0, "1", rdtyType);
				if(list.size() > 0) {
					rdt2 = (Object[])list.get(0);
				}
				newRdt.setRdtyPid(pId);
				newRdt.setRdtyGrade(rdt.getRdtyGrade() + 1);
			}else {
				// 检验分类名称是否存在
				List list = this.metadataManagementService.getResDynamicTypeList(pId, rmstId, rdtyName, null, 0, null, rdtyType);
				if(list.size() > 0)
					return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + tempId + "}";
				list = this.metadataManagementService.getResDynamicTypeList(null, rmstId, null, 1L, 0, "1", rdtyType);
				if(list.size() > 0) {
					rdt2 = (Object[])list.get(0);
				}
				newRdt.setRdtyPid(null);
				newRdt.setRdtyGrade(1L);
			}
			if(rdtyName.trim().length() > 20)
				rdtyName = rdtyName.substring(0, 20);
			newRdt.setRdtyName(rdtyName);
			newRdt.setRmstId(rmstId);
			Integer tempIndex = null;
			try {
				tempIndex = Integer.parseInt((String)rdt2[3]);
				tempIndex += 1;
			}catch(Exception e) {
				tempIndex = 1;
			}
			newRdt.setRdtyIndex(tempIndex);
			newRdt.setRdtyType(rdtyType);
			this.metadataManagementService.save(newRdt);
			return "{\"rdtyId\":\"" + newRdt.getRdtyId() + "\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"创建自定义分类过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String deleteResDynamicType(Long rdtyId) {
		ResDynamicType rdt = null;
		try {
			if(rdtyId == null)
				throw new ErrorMessageException("自定义分类ID不存在！");
			rdt = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, rdtyId);
			if(rdt == null)
				throw new ErrorMessageException("该分类不存在！");
			this.metadataManagementService.delete(rdt);
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"该分类下已经存在数据项，不允许删除！\",\"isOk\":false}";
		}
		return "{\"isOk\":true,\"tempGrade\":" + rdt.getRdtyGrade() + "}";
	}

	@Override
	public String handlerResDynamicTypeIndex(Long rdtyId, String type, int rdtyType) {
		try {
			if(rdtyId == null)
				throw new ErrorMessageException("自定义分类ID值不存在！");
			if(type.trim().length() < 1)
				throw new ErrorMessageException("自定义分类排序操作类型不明！");
			ResDynamicType rdt = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, rdtyId);
			if(rdt == null)
				throw new ErrorMessageException("自定义分类不存在！");
			// 排序号
			Integer tempIndex = rdt.getRdtyIndex();
			if("up".equals(type)) {
				Object[] rdt2 = null;
				if(tempIndex > 1) {
					if(rdt.getRdtyGrade() == 1) {
						List list = this.metadataManagementService.getResDynamicTypeList(null, rdt.getRmstId(), null, 1L, tempIndex, "2", rdtyType);
						if(list.size() > 0) {
							rdt2 = (Object[])list.get(0);
						}
					}else {
						List list = this.metadataManagementService.getResDynamicTypeList(rdt.getRdtyPid(), rdt.getRmstId(), null, null,
								tempIndex, "2", rdtyType);
						if(list.size() > 0) {
							rdt2 = (Object[])list.get(0);
						}
					}
					if(rdt2 == null)
						throw new ErrorMessageException("上级分类不存在！");
					// 更新排序
					Integer index = Integer.parseInt((String)rdt2[3]);
					rdt.setRdtyIndex(index);
					this.metadataManagementService.save(rdt);
					Long newRdtyId = Long.parseLong((String)rdt2[0]);
					if(newRdtyId != null)
						this.metadataManagementService.updateResDynamicType(newRdtyId, tempIndex);
				}
			}else {
				Object[] rdt2 = null;
				if(rdt.getRdtyGrade() == 1) {
					List list = this.metadataManagementService.getResDynamicTypeList(null, rdt.getRmstId(), null, 1L, null, "1", rdtyType);
					if(list.size() > 0) {
						rdt2 = (Object[])list.get(0);
					}
				}else {
					List list = this.metadataManagementService.getResDynamicTypeList(rdt.getRdtyPid(), rdt.getRmstId(), null, rdt
							.getRdtyGrade(), null, "1", rdtyType);
					if(list.size() > 0) {
						rdt2 = (Object[])list.get(0);
					}
				}
				if(rdt2 == null)
					throw new ErrorMessageException("分类不存在！");
				Integer rdt2Index = Integer.parseInt((String)rdt2[3]);
				if(tempIndex > 0 && rdt2Index > 0 && tempIndex < rdt2Index) {
					Object[] rdt3 = null;
					if(rdt.getRdtyGrade() == 1) {
						List list = this.metadataManagementService.getResDynamicTypeList(null, rdt.getRmstId(), null, 1L, tempIndex, "3", rdtyType);
						if(list.size() > 0) {
							rdt3 = (Object[])list.get(0);
						}
					}else {
						List list = this.metadataManagementService.getResDynamicTypeList(rdt.getRdtyPid(), rdt.getRmstId(), null, rdt
								.getRdtyGrade(), tempIndex, "3", rdtyType);
						if(list.size() > 0) {
							rdt3 = (Object[])list.get(0);
						}
					}
					if(rdt3 == null)
						throw new ErrorMessageException("下级分类不存在！");
					// 更新排序
					Integer index_3 = Integer.parseInt((String)rdt3[3]);
					rdt.setRdtyIndex(index_3);
					this.metadataManagementService.save(rdt);
					Long newRdtyId = Long.parseLong((String)rdt3[0]);
					if(newRdtyId != null)
						this.metadataManagementService.updateResDynamicType(newRdtyId, tempIndex);
				}
			}
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"操作自定义分类排序过程中出现错误！\",\"isOk\":false}";
		}
	}

	@Override
	public String updateResDynamicType(Long rdtyId, String rdtyName, int rdtyType) {
		try {
			if(rdtyId == null)
				throw new ErrorMessageException("自定义分类ID值不存在！");
			if(rdtyName.trim().length() < 1)
				throw new ErrorMessageException("自定义分类名称不能为空！");
			if(rdtyName.trim().length() > 20)
				rdtyName = rdtyName.substring(0, 20);
			ResDynamicType rdt = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, rdtyId);
			if(rdt == null)
				throw new ErrorMessageException("自定义分类不存在！");
			// 检验分类名称是否存在
			List list = this.metadataManagementService.getResDynamicTypeList(rdt.getRdtyPid(), rdt.getRmstId(), rdtyName, null, 0, null, rdtyType);
			if(list.size() > 0) {
				if(list.size() > 0) {
					Object[] rdt2 = (Object[])list.get(0);
					Long tempRdtyId = Long.parseLong((String)rdt2[0]);
					if(!rdtyId.equals(tempRdtyId))
						return "{\"msg\":\"该分类名称已经存在！\",\"isOk\":false,\"tempId\":" + rdtyId + ",\"rstyName\":\"" + rdt.getRdtyName()
								+ "\"}";
				}
			}
			rdt.setRdtyName(rdtyName);
			this.metadataManagementService.save(rdt);
			return "{\"msg\":\"\",\"isOk\":true}";
		}catch(ErrorMessageException em) {
			em.printStackTrace();
			return "{\"msg\":\"" + em.getMessage() + "\",\"isOk\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"更新自定义分类过程中出现错误！\",\"isOk\":false}";
		}
	}
}
