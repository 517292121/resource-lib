--添加资源导出功能点
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30065,306,'资源导出','资源管理_资源导出','/console/data/resource/exportResource.htm',500,'30065',1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SROL_ID,SRFU_SFUN_ID) values(130065,1,30065);
