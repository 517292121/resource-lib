/*====================================
  有时候数据库导入导出过程中会将某些表的主键丢失
  以下是部分创建主键的脚本
=====================================*/

declare primaryKeyExistedCount number; 
begin 
	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('DBMS_BAK_HISTORY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table DBMS_BAK_HISTORY add constraint PK_DBMS_BAK_HISTORY primary key(DBMS_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('IDS_INFORMATION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table IDS_INFORMATION add constraint PK_IDS_INFORMATION primary key(IDSI_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('IDS_INFORMATION_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table IDS_INFORMATION_TYPE add constraint PK_IDS_INFORMATION_TYPE primary key(IDST_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('INDEX_BANNER') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table INDEX_BANNER add constraint PK_INDEX_BANNER primary key(IB_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('IS_IGNORE_RESSOURCE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table IS_IGNORE_RESSOURCE add constraint PK_IS_IGNORE_RESSOURCE primary key(IS_IGNORE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('IS_INFO_SUBSCRIPTION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table IS_INFO_SUBSCRIPTION add constraint PK_IS_INFO_SUBSCRIPTION primary key(IS_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_DYNAMIC_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_DYNAMIC_TYPE add constraint PK_RES_DYNAMIC_TYPE primary key(RDTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_ELEMENT_POOL') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_ELEMENT_POOL add constraint PK_RES_ELEMENT_POOL primary key(REPO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_EXTEND_DATA') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_EXTEND_DATA add constraint PK_RES_EXTEND_DATA primary key(REDA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_FILESYSTEM') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_FILESYSTEM add constraint PK_RES_FILESYSTEM primary key(RFIL_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_IMPORT_HISTORY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_IMPORT_HISTORY add constraint PK_RES_IMPORT_HISTORY primary key(RMHI_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_IMPORT_TEMPLET') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_IMPORT_TEMPLET add constraint PK_RES_IMPORT_TEMPLET primary key(RITE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_IMP_COLS') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_IMP_COLS add constraint PK_RES_IMP_COLS primary key(RICO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_INDEX_KEY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_INDEX_KEY add constraint PK_RES_INDEX_KEY primary key(RIKE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_LANGUAGE_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_LANGUAGE_TYPE add constraint PK_RES_LANGUAGE_TYPE primary key(RLTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_LITER_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_LITER_TYPE add constraint PK_RES_LITER_TYPE primary key(RLTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_LOADS_LIST') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_LOADS_LIST add constraint PK_RES_LOADS_LIST primary key(RLLI_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_META_PERM') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_META_PERM add constraint PK_RES_META_PERM primary key(RMPE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_META_STORE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_META_STORE add constraint PK_RES_META_STORE primary key(RMST_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_MIDDLE_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_MIDDLE_TYPE add constraint PK_RES_MIDDLE_TYPE primary key(BZTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_MISSION_INFO') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_MISSION_INFO add constraint PK_RES_MISSION_INFO primary key(RMIN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_MISSION_LANGUAGE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_MISSION_LANGUAGE add constraint PK_RES_MISSION_LANGUAGE primary key(RMLA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_MISS_DEVOLVE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_MISS_DEVOLVE add constraint PK_RES_MISS_DEVOLVE primary key(RMDE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_NAV_CACHE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_NAV_CACHE add constraint PK_RES_NAV_CACHE primary key(RNC_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_NAV_DATE_SPAN_CACHE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_NAV_DATE_SPAN_CACHE add constraint PK_RES_NAV_DATE_SPAN_CACHE primary key(RNDSC_POST_YEAR)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_BROWSE_HISTOREY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_BROWSE_HISTOREY add constraint PK_RES_SOURCE_BROWSE_HISTOREY primary key(RSBH_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_COMT') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_COMT add constraint PK_RES_SOURCE_COMT primary key(RSCO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_DATA') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_DATA add constraint PK_RES_SOURCE_DATA primary key(RSDA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_DATA_CACHE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_DATA_CACHE add constraint PK_RES_SOURCE_DATA_CACHE primary key(RSDC_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_FILE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_FILE add constraint PK_RES_SOURCE_FILE primary key(RSFI_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_LANGUAGE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_LANGUAGE add constraint PK_RES_SOURCE_LANGUAGE primary key(RSLA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_REMARK') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_REMARK add constraint PK_RES_SOURCE_REMARK primary key(RSRE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_REPEAT') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_REPEAT add constraint PK_RES_SOURCE_REPEAT primary key(RSRE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SOURCE_SUBJECT') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SOURCE_SUBJECT add constraint PK_RES_SOURCE_SUBJECT primary key(RSSU_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_STORE_IP_DEPLOY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_STORE_IP_DEPLOY add constraint PK_RES_STORE_IP_DEPLOY primary key(RSID_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_STORE_LITER') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_STORE_LITER add constraint PK_RES_STORE_LITER primary key(RSLI_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SUBJECT_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SUBJECT_TYPE add constraint PK_RES_SUBJECT_TYPE primary key(RSTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SYNONYMS_KEY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SYNONYMS_KEY add constraint PK_RES_SYNONYMS_KEY primary key(RSKE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_SYSTEM_REPEAT') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_SYSTEM_REPEAT add constraint PK_RES_SYSTEM_REPEAT primary key(RSYR_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_TAG_INFO') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_TAG_INFO add constraint PK_RES_TAG_INFO primary key(RTIN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_TEMPLATE_IMP') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_TEMPLATE_IMP add constraint PK_RES_TEMPLATE_IMP primary key(RTIM_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RES_TEMPLET_META') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RES_TEMPLET_META add constraint PK_RES_TEMPLET_META primary key(RTME_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('RSS_USER_DATA') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table RSS_USER_DATA add constraint PK_RSS_USER_DATA primary key(RSS_DATA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_CONSULTATION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_CONSULTATION add constraint PK_SC_CONSULTATION primary key(SCCO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_CONSULTATION_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_CONSULTATION_TYPE add constraint PK_SC_CONSULTATION_TYPE primary key(SCTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_DOCU_DELIVERY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_DOCU_DELIVERY add constraint PK_SC_DOCU_DELIVERY primary key(SDDE_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_FAQ_LIBRARY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_FAQ_LIBRARY add constraint PK_SC_FAQ_LIBRARY primary key(SCFL_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_FAQ_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_FAQ_TYPE add constraint PK_SC_FAQ_TYPE primary key(SCFT_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_FAVORITES') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_FAVORITES add constraint PK_SC_FAVORITES primary key(SCF_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_ISSUE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_ISSUE add constraint PK_SC_ISSUE primary key(SCIS_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_ISSUE_REPLY') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_ISSUE_REPLY add constraint PK_SC_ISSUE_REPLY primary key(SCIR_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SC_ISSUE_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SC_ISSUE_TYPE add constraint PK_SC_ISSUE_TYPE primary key(SCIT_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_ACCESS_COUNTER') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_ACCESS_COUNTER add constraint PK_SYS_ACCESS_COUNTER primary key(SACO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_CATALOG') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_CATALOG add constraint PK_SYS_CATALOG primary key(SCAT_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_COLLECTION_POSITION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_COLLECTION_POSITION add constraint PK_SYS_COLLECTION_POSITION primary key(SCP_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_EMAIL_ACTIVATION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_EMAIL_ACTIVATION add constraint PK_SYS_EMAIL_ACTIVATION primary key(SEAC_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_FORGET_PASSWORD') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_FORGET_PASSWORD add constraint PK_SYS_FORGET_PASSWORD primary key(SFPA_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_FUNCTION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_FUNCTION add constraint PK_SYS_FUNCTION primary key(SFUN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_LINK') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_LINK add constraint PK_SYS_LINK primary key(SLIN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_MANAGER') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_MANAGER add constraint PK_SYS_MANAGER primary key(SMAN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_ROLE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_ROLE add constraint PK_SYS_ROLE primary key(SROL_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_ROLE_FUNCTION') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_ROLE_FUNCTION add constraint PK_SYS_ROLE_FUNCTION primary key(SRFU_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_SENSITIVE_WORD') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_SENSITIVE_WORD add constraint PK_SYS_SENSITIVE_WORD primary key(SSW_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_SETTINGS') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_SETTINGS add constraint PK_SYS_SETTINGS primary key(SSET_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_STYLE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_STYLE add constraint PK_SYS_STYLE primary key(SSTY_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_TOURIST_BROWSE_CACHE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_TOURIST_BROWSE_CACHE add constraint PK_SYS_TOURIST_BROWSE_CACHE primary key(STBHC_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_USER_INFO') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_USER_INFO add constraint PK_SYS_USER_INFO primary key(SUIN_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_USER_ROLE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_USER_ROLE add constraint PK_SYS_USER_ROLE primary key(SURO_ID)';
	 end if; 

	 select count(1) into primaryKeyExistedCount from user_constraints t where t.table_name = upper('SYS_USER_TYPE') and t.constraint_type = 'P';
	 if primaryKeyExistedCount  = 0 then
	 execute immediate 
	 'alter table SYS_USER_TYPE add constraint PK_SYS_USER_TYPE primary key(SUTY_ID)';
	 end if; 
end;