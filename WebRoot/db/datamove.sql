--清空数据库中资源以及作业
delete from res_tag_info;
delete from res_source_remark;
delete from res_source_comt;
delete from res_source_browse_historey;
delete from res_loads_list;
delete from res_import_history;
delete from res_filesystem;
delete from res_extend_data;
delete from res_source_language;
delete from res_source_file;
delete from res_source_data;
delete from RES_SOURCE_SUBJECT;
delete from res_miss_devolve;
delete from res_mission_language;
delete from res_source_data_cache;
delete from res_mission_info;

--根据作业清空数据库中资源以及作业
delete from res_tag_info;
delete from res_source_remark;
delete from res_source_comt;
delete from res_source_browse_historey;
delete from res_loads_list where RLLI_RMIN_ID in ();
delete from res_import_history where RMHI_RMIN_ID in ();
delete from res_filesystem;
delete from res_extend_data where REDA_RSDA_ID in (select rsda_id from res_source_data where RSDA_RMIN_ID in ());
delete from res_source_language where rsla_rsda_id in (select rsda_id from res_source_data where RSDA_RMIN_ID in ());
delete from res_source_data where RSDA_RMIN_ID in ();
delete from RES_SOURCE_SUBJECT where RSSU_RMIN_ID in ();
delete from res_miss_devolve;
delete from res_mission_language where RMLA_RMIN_ID in (select rsda_id from res_source_data where RSDA_RMIN_ID in ());
delete from res_mission_info where RMIN_ID in ();

--设置所有资源及作业为通过状态
update res_source_data set rsda_status=3;
update res_source_language set rsla_status=3;
update res_mission_info set rmin_status=3;
update res_mission_language set rmla_status=3;

--根据导入记录完整删除所有资源--
delete from res_extend_data where reda_rsda_id in (select rsda_id from res_source_data where rsda_rihi_id=301303887551332);
delete from res_source_language where rsla_rsda_id in (select rsda_id from res_source_data where rsda_rihi_id=301303887551332);
delete from res_source_remark where rsre_rsda_id in (select rsda_id from res_source_data where rsda_rihi_id=301303887551332);
delete from res_source_data where  rsda_rihi_id=301303887551332;
delete from res_import_history where rmhi_id=301303887551332;


--清除所有论文信息
delete from psa_element_data;
delete from psa_logs;
delete from psa_papers_info;

--删除论文相关数据
delete from PSA_STUDENTS_IMPORT_HISTORY;
delete from PSA_IMPORT_HISTORY;
delete from PSA_DEPT_TEACHER;
delete from PSA_ELEMENT_DATA;
delete from PSA_PAPERS_INFO;
delete from PSA_LOGS;
delete from PSA_ERROR_INFO;
delete from PSA_DEGREE;
delete from PSA_STUDENTS;
delete from PSA_DEPARTMENTS;
delete from SYS_MANAGER WHERE SMAN_ID>1;




--清空论文管理系统所有功能点
delete from SYS_ROLE_FUNCTION where SRFU_SFUN_ID<80000 and SRFU_SFUN_ID>70000;
delete from SYS_FUNCTION where SFUN_ID>70000 and SFUN_ID<80000;
delete from SYS_CATALOG where SCAT_ID>700 AND SCAT_ID<800;




--清空数据库中从特色库到资源下所有数据
delete from SC_FAVORITES; --用户收藏
delete from res_tag_info;
delete from res_source_remark;
delete from res_source_comt;
delete from res_source_browse_historey;
delete from res_loads_list; --批量上传记录
delete from res_import_history;
delete from res_filesystem;
delete from res_extend_data;
delete from res_source_language;
delete from res_source_file;
delete from res_source_data;
delete from RES_SOURCE_SUBJECT;
delete from res_miss_devolve;
delete from res_source_data_cache;
delete from res_mission_language;
delete from res_mission_info;
delete from res_store_ip_deploy;
delete from res_dynamic_type;
delete from res_import_templet;
delete from RES_TEMPLET_META;
delete from RSS_USER_DATA; 
delete from res_meta_perm; --特色库的操作权限表
delete from res_store_liter;
delete from res_meta_store; 
delete from res_nav_cache; --资源导航缓存
delete from RES_NAV_DATE_SPAN_CACHE; --年代导航缓存
delete from RES_SOURCE_REPEAT; --查重记录
--2018.09新增删除
delete from DBMS_BAK_HISTORY;
delete from INDEX_BANNER;
delete from IS_INFO_SUBSCRIPTION;
delete from RES_INDEX_KEY;
delete from RES_LIBRARY_CLASSIFY;
delete from RES_TEXT_RESOURCE;
delete from RES_TEXT_LIBRARY;
delete from SC_CONSULTATION;
delete from SC_ISSUE;
delete from SC_ISSUE_REPLY;
delete from SYS_ACCESS_COUNTER;
delete from SYS_EMAIL_ACTIVATION;
delete from SYS_LINK;
delete from SYS_SENSITIVE_WORD;
delete from SYS_TOURIST_BROWSE_CACHE;
delete from SYS_USER_INFO where SUIN_ID > 1;

--删除指定的特色库及其所有相关的数据
delete from SC_FAVORITES where SCF_RES_SOURCE_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=); --用户收藏
delete from res_tag_info where RTIN_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_source_remark where RSRE_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_source_comt where RSCO_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_source_browse_historey where RSBH_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_loads_list where RLLI_RMIN_ID in (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID=); --批量上传记录
delete from res_import_history where RMHI_RMIN_ID in (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID=);
delete from res_extend_data where REDA_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_source_language where RSLA_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID=);
delete from res_source_data where RSDA_RMST_ID=;
delete from RES_SOURCE_SUBJECT where RSSU_RMIN_ID in (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID=); --删除主题
delete from res_miss_devolve where RMDE_RMIN_ID in (SELECT RMLA_ID FROM RES_MISSION_LANGUAGE WHERE RMLA_RMIN_ID IN (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID=));
delete from res_mission_language WHERE RMLA_RMIN_ID IN (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID=);
delete from res_mission_info where RMIN_RMST_ID=;
delete from res_store_ip_deploy WHERE RSID_RMST_ID=;
delete from res_dynamic_type WHERE RMST_ID=;
delete from res_import_templet WHERE RITE_RMST_ID=;
delete from RES_TEMPLET_META WHERE RTME_RSIN_ID=;
delete from res_meta_perm WHERE RMST_ID=; --特色库的操作权限表
delete from res_store_liter WHERE RMST_ID=;
delete from res_meta_store WHERE RMST_ID=; 
delete from res_nav_cache; --资源导航缓存
delete from RES_NAV_DATE_SPAN_CACHE; --年代导航缓存

--删除文本信息
delete from IDS_INFORMATION;
delete from IDS_INFORMATION_TYPE;

--删除除root外的管理员
DELETE FROM SYS_USER_ROLE WHERE SURO_ID>1;
DELETE FROM SYS_MANAGER WHERE SMAN_ID>1;

--同步作业中的资源总数
update RES_MISSION_INFO rmin set rmin.RMIN_RES_COUNT=(select count(rsda_id) from RES_SOURCE_DATA rsda where rsda.rsda_rmin_id=rmin.rmin_id);




