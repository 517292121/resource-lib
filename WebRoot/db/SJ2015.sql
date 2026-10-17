--同步信息资讯数量
update Ids_Information_Type it set it.idst_Number=(select count(*) from Ids_Information info where info.idsi_Idst_Id=it.idst_Id);

--删除指定功能点-功能表&权限表
delete from sys_role_function where srfu_sfun_id=60025;
delete from sys_function where sfun_id=60025;
--删除的功能点备份-方便恢复——会员登录统计
--INSERT INTO SYS_FUNCTION VALUES ('60025', '602', '会员登录统计', '系统管理_统计报表_会员登录统计', '/console/count/userLoginCount.htm?action=initView', '500', '60025', '1');
--insert into SYS_ROLE_FUNCTION values (1600251,60025,1);

--添加 用户操作统计 功能点
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60030,602,'用户操作统计','系统管理_统计报表_会员操作统计','/console/count/userAccess.htm',500,'60030',1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SROL_ID,SRFU_SFUN_ID) values(1600301,1,60030);

--添加记录日志
DROP TABLE SYS_ACCESS_COUNTER;
CREATE TABLE SYS_ACCESS_COUNTER (
"SACO_ID" NUMBER(19) NOT NULL ,
"SACO_SUIN_ID" NUMBER(19) NULL ,
"SACO_SUIN_NAME" VARCHAR2(128) NULL,
"SACO_TYPE" VARCHAR2(64) NULL,
"SACO_DATE" TIMESTAMP(6)  NOT NULL ,
"SACO_TIME" NUMBER(19,2) NULL,
"SACO_CONTENT" VARCHAR2(3000) NULL,
"SACO_IP" VARCHAR2(128) NULL 
);
ALTER TABLE SYS_ACCESS_COUNTER ADD CHECK ("SACO_ID" IS NOT NULL);
ALTER TABLE SYS_ACCESS_COUNTER ADD CHECK ("SACO_DATE" IS NOT NULL);
ALTER TABLE SYS_ACCESS_COUNTER ADD PRIMARY KEY ("SACO_ID");

--在系统参数设置 添加限制用户下载次数字段
INSERT INTO SYS_SETTINGS VALUES ('500', '允许用户每天下载资源的次数', 'resource_down_maxsize', '10', '1');

--添加用户下载量记数
alter table sys_user_info add(SUIN_DOWN_NUM number(5));

--为元素模板增加了2个字段  
--REPO_IS_SHOWLIST 控制是否是资源类别页面显示
--REPO_INDEX 序号 控制显示顺序
ALTER TABLE RES_ELEMENT_POOL ADD(REPO_IS_SHOWLIST NUMBER(2));
ALTER TABLE RES_ELEMENT_POOL ADD(REPO_INDEX NUMBER(5));

--创建资源主题表
create table RES_SOURCE_SUBJECT  (
   RSSU_ID              NUMBER(19,0)                    not null,
   RSSU_RMIN_ID         NUMBER(19,0),
   RSSU_NAME            VARCHAR2(256),
   RSSU_REMARK          VARCHAR2(1024),
   constraint PK_RES_RESOURCE_INFO primary key (RSSU_ID)
);

-- 添加特色库图片 背景色 字段
alter table RES_META_STORE add (RMST_LOGOBG varchar(30));

-- 为信息表 添加 封面和描述字段
alter table IDS_INFORMATION add (IDSI_DESCRIPTION VARCHAR2(1024));
alter table IDS_INFORMATION add (IDSI_IMG_URL VARCHAR2(512));

-- 创建保存资源首字母字段
alter table RES_SOURCE_DATA ADD RSDA_TITLE_EN VARCHAR2(16);


--将资源附件的描述扩展为3000
alter table RES_SOURCE_FILE modify RES_FILE_REMARK varchar2(3000);
alter table RES_SOURCE_FILE ADD RSFI_ACCESS_TYPE VARCHAR2(32); --访问权限

--添加 文献传递 字段（作者，题名，出版者，关键词，时间选择，页码选择，ISSN/ISBN，文献类型，描述，原文路径）
CREATE TABLE SC_DOCU_DELIVERY (
	"SDDE_ID" NUMBER(19) NOT NULL ,
	"SDDE_SUIN_ID" NUMBER(19) NULL ,
	"SDDE_SMAN_ID" NUMBER(19) NULL ,
	"SDDE_RSTY_ID" NUMBER(19) NOT NULL ,
	"SDDE_CREATION_TIME" TIMESTAMP(6)  NOT NULL ,
	"SDDE_STATUS" NUMBER(5) NOT NULL ,
	"SDDE_REPLY_CONTENT" CLOB NULL ,
	"SDDE_REPLY_TIME" TIMESTAMP(6)  NULL,
	"SDDE_DOCU_TYPE" VARCHAR2(256) NOT NULL,
	"SDDE_DOCU_TITLE" VARCHAR2(1024) NOT NULL,
	"SDDE_DOCU_CREATOR" VARCHAR2(256),
	"SDDE_DOCU_PUBLISHER" VARCHAR2(256),
	"SDDE_DOCU_KEYWORD" VARCHAR2(256),
	"SDDE_DOCU_ISBN" VARCHAR2(256),
	"SDDE_DOCU_DATE" VARCHAR2(64),
	"SDDE_DOCU_URL" VARCHAR2(512),
	"SDDE_DOCU_PAGE" VARCHAR2(64),
	"SDDE_CONTENT" CLOB NULL
);
ALTER TABLE SC_DOCU_DELIVERY ADD PRIMARY KEY ("SDDE_ID");

--添加 文献传递 功能点
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(406,401,'服务管理_文献传递','文献传递',500);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40061,406,'文献传递','用户管理_文献传递_文献传递','/console/data/docudelivery.htm?action=initDocuView',100,'40061',1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SROL_ID,SRFU_SFUN_ID) values(1400611,1,40061);


--添加作业中资源数量字段
ALTER TABLE RES_MISSION_INFO ADD RMIN_RES_COUNT NUMBER(9);

--同步作业中的资源总数
update RES_MISSION_INFO rmin set rmin.RMIN_RES_COUNT=(select count(rsda_id) from RES_SOURCE_DATA rsda where rsda.rsda_rmin_id=rmin.rmin_id);

--将RES_INDEX_KEY表作为热门检索词表：添加检索次数字段
ALTER TABLE RES_INDEX_KEY ADD RIKE_SEARCH_COUNT NUMBER(9);
