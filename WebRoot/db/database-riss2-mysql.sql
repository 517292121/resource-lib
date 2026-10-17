SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;
CREATE DATABASE IF NOT EXISTS riss2_mlan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE riss2_mlan;

/*==============================================================*/
/* DBMS name: MySQL 8                           */
/* Created on:     2013-01-17 16:28:18                          */
/*==============================================================*/
/*==============================================================*/
/* Table: CMA_CLASSIFY_TYPE                                     */
/*==============================================================*/
create table CMA_CLASSIFY_TYPE  (
   CLTY_ID              NUMERIC(19)                     not null,
   CLTY_NAME            VARCHAR(256),
   constraint PK_CMA_CLASSIFY_TYPE primary key (CLTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: CMA_COMPANY_CLASSIFY                                  */
/*==============================================================*/
create table CMA_COMPANY_CLASSIFY  (
   CCCL_ID              NUMERIC(19)                     not null,
   CCCL_CLTY_ID         NUMERIC(19),
   CCCL_CCTY_ID         NUMERIC(19),
   CCCL_CCIN_ID         NUMERIC(19),
   constraint PK_CMA_COMPANY_CLASSIFY primary key (CCCL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: CMA_COMPANY_FILE                                      */
/*==============================================================*/
create table CMA_COMPANY_FILE  (
   CCFI_ID              NUMERIC(19)                     not null,
   CCFI_RMST_ID         NUMERIC(19),
   CCFI_FILENAME        VARCHAR(512),
   CCFI_REMARK          VARCHAR(3000),
   CCFI_FILESIZE        NUMERIC(19,0),
   CCFI_FORMAT          VARCHAR(32),
   CCFI_TYPE            NUMERIC(5,0),
   CCFI_LOCALPATH       VARCHAR(512),
   CCFI_URLPATH         VARCHAR(3000),
   CCFI_TOMAIN          NUMERIC(5,0),
   CCFI_FACEURL         VARCHAR(1024),
   CCFI_LOADTIME        DATETIME,
   constraint PK_CMA_COMPANY_FILE primary key (CCFI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: CMA_COMPANY_INFO                                      */
/*==============================================================*/
create table CMA_COMPANY_INFO  (
   CCIN_ID              NUMERIC(19)                     not null,
   CCIN_NAME            VARCHAR(256),
   CCIN_ADDRESS         VARCHAR(512),
   CCIN_LINKMAN         VARCHAR(32),
   CCIN_TEL             VARCHAR(32),
   CCIN_EMAIL           VARCHAR(128),
   CCIN_QQ              VARCHAR(32),
   CCIN_CODE            NUMERIC(19),
   CCIN_IMAGEURL        VARCHAR(1024),
   CCIN_REMARK          LONGTEXT,
   CCIN_CREATEDATE      DATETIME,
   CCIN_RCMD_MAIN       NUMERIC(1),
   constraint PK_CMA_COMPANY_INFO primary key (CCIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: CMA_COMPANY_TYPE                                      */
/*==============================================================*/
create table CMA_COMPANY_TYPE  (
   CCTY_ID              NUMERIC(19)                     not null,
   CCTY_CLTY_ID         NUMERIC(19),
   CCTY_NAME            VARCHAR(256),
   CCTY_PARENT          NUMERIC(19),
   CCTY_GRADE           NUMERIC(5,0),
   CCTY_CODE            VARCHAR(32),
   CCTY_ORDER           NUMERIC(5,0),
   constraint PK_CMA_COMPANY_TYPE primary key (CCTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: DBMS_BAK_HISTORY                                      */
/*==============================================================*/
create table DBMS_BAK_HISTORY  (
   DBMS_ID              NUMERIC(19)                     not null,
   DBMS_FILENAME        VARCHAR(128),
   DBMS_URLPATH         VARCHAR(1024),
   DBMS_BAKDATE         DATETIME,
   constraint PK_DBMS_BAK_HISTORY primary key (DBMS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: IDS_INFORMATION                                       */
/*==============================================================*/
create table IDS_INFORMATION  (
   IDSI_ID              NUMERIC(19,0)                   not null,
   IDSI_IDST_ID         NUMERIC(19,0)                   not null,
   IDSI_SMAN_ID         NUMERIC(19,0)                   not null,
   IDSI_TITLE           VARCHAR(256)                   not null,
   IDSI_CONTENT         LONGTEXT,
   IDSI_DATE            DATETIME                       not null,
   IDSI_HITCOUNT        NUMERIC(9,0)                    not null,
   constraint PK_IDS_INFORMATION primary key (IDSI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IIDSI_IDST_ID                                         */
/*==============================================================*/
create index IIDSI_IDST_ID on IDS_INFORMATION (
   IDSI_IDST_ID ASC
);

/*==============================================================*/
/* Index: IIDSI_SMAN_ID                                         */
/*==============================================================*/
create index IIDSI_SMAN_ID on IDS_INFORMATION (
   IDSI_SMAN_ID ASC
);

/*==============================================================*/
/* Index: IIDSI_DATE                                            */
/*==============================================================*/
create index IIDSI_DATE on IDS_INFORMATION (
   IDSI_DATE ASC
);

/*==============================================================*/
/* Table: IDS_INFORMATION_TYPE                                  */
/*==============================================================*/
create table IDS_INFORMATION_TYPE  (
   IDST_ID              NUMERIC(19,0)                   not null,
   IDST_NAME            VARCHAR(128)                   not null,
   IDST_INDEX           NUMERIC(5,0),
   IDST_NUMBER          NUMERIC(19,0)                   not null,
   constraint PK_IDS_INFORMATION_TYPE primary key (IDST_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IIDST_NAME                                            */
/*==============================================================*/
create unique index IIDST_NAME on IDS_INFORMATION_TYPE (
   IDST_NAME ASC
);

/*==============================================================*/
/* Index: IIDST_INDEX                                           */
/*==============================================================*/
create index IIDST_INDEX on IDS_INFORMATION_TYPE (
   IDST_INDEX ASC
);

/*==============================================================*/
/* Table: IS_IGNORE_RESSOURCE                                   */
/*==============================================================*/
create table IS_IGNORE_RESSOURCE  (
   IS_IGNORE_ID         NUMERIC(19,0)                   not null,
   IS_IGNORE_USER_ID    NUMERIC(19)                     not null,
   IS_IGNORE_RESOURCE_ID NUMERIC(19)                     not null,
   IS_IGNORE_DATE       DATETIME                       not null,
   constraint PK_IS_IGNORE_RESSOURCE primary key (IS_IGNORE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: IS_INFO_SUBSCRIPTION                                  */
/*==============================================================*/
create table IS_INFO_SUBSCRIPTION  (
   IS_ID                NUMERIC(19,0)                   not null,
   IS_USER_ID           NUMERIC(19)                     not null,
   IS_KEYWORD           VARCHAR(256)                   not null,
   IS_DATE              DATETIME                       not null,
   constraint PK_IS_INFO_SUBSCRIPTION primary key (IS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_DEGREE                                            */
/*==============================================================*/
create table PSA_DEGREE  (
   PDEG_ID              NUMERIC(19)                     not null,
   PDEG_NAME            VARCHAR(128),
   PDEG_CREATEDATE      DATETIME,
   constraint PK_PSA_DEGREE primary key (PDEG_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_DEPARTMENTS                                       */
/*==============================================================*/
create table PSA_DEPARTMENTS  (
   PDEP_ID              NUMERIC(19)                     not null,
   PDEP_NAME            VARCHAR(512)                   not null,
   PDEP_REMARK          VARCHAR(3000),
   PDEP_CREATEDATE      DATETIME,
   PDEP_PARENT          NUMERIC(19),
   PDEP_DEPTTYPE        NUMERIC(5,0),
   PDEP_INDEX           NUMERIC(5,0),
   PDEP_FOLDER          VARCHAR(32),
   constraint PK_PSA_DEPARTMENTS primary key (PDEP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_DEPT_TEACHER                                      */
/*==============================================================*/
create table PSA_DEPT_TEACHER  (
   PDTE_ID              NUMERIC(19)                     not null,
   PDTE_PDEP_ID         NUMERIC(19),
   PDTE_SMAN_ID         NUMERIC(19),
   constraint PK_PSA_DEPT_TEACHER primary key (PDTE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_ELEMENTS                                          */
/*==============================================================*/
create table PSA_ELEMENTS  (
   PELE_ID              NUMERIC(19)                     not null,
   PELE_REPO_ID         NUMERIC(19),
   constraint PK_PSA_ELEMENTS primary key (PELE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_ELEMENT_DATA                                      */
/*==============================================================*/
create table PSA_ELEMENT_DATA  (
   PEDA_ID              NUMERIC(19)                     not null,
   PEDA_PPIN_ID         NUMERIC(19),
   PEDA_REPO_ID         NUMERIC(19),
   PEDA_DATA            VARCHAR(3000),
   constraint PK_PSA_ELEMENT_DATA primary key (PEDA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_ERROR_INFO                                        */
/*==============================================================*/
create table PSA_ERROR_INFO  (
   PEIN_ID              NUMERIC(19)                     not null,
   PEIN_ERROR_DATA      VARCHAR(3000)                  not null,
   PEIN_AUTHOR          VARCHAR(128),
   PEIN_CREATEDATE      DATETIME,
   constraint PK_PSA_ERROR_INFO primary key (PEIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_IMPORT_HISTORY                                    */
/*==============================================================*/
create table PSA_IMPORT_HISTORY  (
   PIHI_ID              NUMERIC(19)                     not null,
   PIHI_AUTHOR          VARCHAR(50),
   PIHI_FILE_REALNAME   VARCHAR(128),
   PIHI_FILE_LOCALPATH  VARCHAR(128),
   PIHI_FILE_URL        VARCHAR(1024),
   PIHI_IMP_NUMBER      DECIMAL(38,0),
   PIHI_IMP_DATE        DATETIME,
   PIHI_TO_RTSS         NUMERIC(5,0),
   constraint PK_PSA_IMPORT_HISTORY primary key (PIHI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_IMPORT_TEMPLET                                    */
/*==============================================================*/
create table PSA_IMPORT_TEMPLET  (
   PITE_ID              NUMERIC(19)                     not null,
   PITE_COLUMN_KEY      VARCHAR(128),
   PITE_EXCEL_COLUMN    VARCHAR(32),
   constraint PK_PSA_IMPORT_TEMPLET primary key (PITE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_LOGS                                              */
/*==============================================================*/
create table PSA_LOGS  (
   PLOG_ID              NUMERIC(19)                     not null,
   PLOG_PPIN_ID         NUMERIC(19),
   PLOG_TYPE            NUMERIC(5,0),
   PLOG_CONTENT         VARCHAR(1024),
   PLOG_DATE            DATETIME,
   PLOG_IP              VARCHAR(64),
   PLOG_USERNAME        VARCHAR(64),
   PLOG_PSTU_ID         NUMERIC(19),
   constraint PK_PSA_LOGS primary key (PLOG_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_PAPERS_INFO                                       */
/*==============================================================*/
create table PSA_PAPERS_INFO  (
   PPIN_ID              NUMERIC(19)                     not null,
   PPIN_PSTU_ID         NUMERIC(19),
   PPIN_PDEG_ID         NUMERIC(19),
   PPIN_SMAN_ID         NUMERIC(19,0),
   PPIN_RSTY_ID         NUMERIC(19),
   PPIN_PDEP_ID         NUMERIC(19),
   PPIN_PROF_ID         NUMERIC(19),
   PPIN_PROTECTION_TYPE NUMERIC(5,0),
   PPIN_SUBJECT_ZH      VARCHAR(1024),
   PPIN_SUBJECT_EN      VARCHAR(1024),
   PPIN_TEACHER_ZH      VARCHAR(32),
   PPIN_TEACHER_EN      VARCHAR(128),
   PPIN_TECHER_COMPANY  VARCHAR(128),
   PPIN_KEYWORD_ZH      VARCHAR(512),
   PPIN_KEYWORD_EN      VARCHAR(512),
   PPIN_DESCRIPTION_ZH  VARCHAR(3400),
   PPIN_DESCRIPTION_EN  VARCHAR(3400),
   PPIN_PAGE_NUMBER     NUMERIC(5,0),
   PPIN_REFERENCE       LONGTEXT,
   PPIN_BUT_DATE        VARCHAR(32),
   PPIN_FORMAT          VARCHAR(32),
   PPIN_CREATEDATE      DATETIME,
   PPIN_PAPER_STATUS    NUMERIC(5,0),
   PPIN_AUDIT_DATE      DATETIME,
   PPIN_PAPER_REMARK    VARCHAR(3000),
   PPIN_REALNAME        VARCHAR(512),
   PPIN_LOCALPATH       VARCHAR(512),
   PPIN_FILEURL         VARCHAR(1024),
   PPIN_SWFURL          VARCHAR(1024),
   PPIN_RE_REALNAME     VARCHAR(512),
   PPIN_RE_LOCALPATH    VARCHAR(512),
   PPIN_RE_FILEURL      VARCHAR(1024),
   PPIN_RE_SWFURL       VARCHAR(1024),
   PPIN_LANGUAGE        VARCHAR(32),
   PPIN_ACCESS_NAME     VARCHAR(512),
   PPIN_URLPATH         VARCHAR(1024),
   PPIN_ACCESSPATH      VARCHAR(128),
   PPIN_VIEW_REALNAME   VARCHAR(128),
   PPIN_VIEW_LOCALPATH  VARCHAR(128),
   PPIN_SWF_PATH        VARCHAR(1024),
   PPIN_ISTO_RISS       NUMERIC(5,0),
   PPIN_RSDA_ID         NUMERIC(19),
   PPIN_PIHI_ID         NUMERIC(19),
   PPIN_LOOKNUMBER      CHAR(10),
   PPIN_USERTYPE        NUMERIC(5,0),
   PPIN_USERNAME        VARCHAR(128),
   PPIN_USERNUMBER      VARCHAR(128),
   constraint PK_PSA_PAPERS_INFO primary key (PPIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_STUDENTS                                          */
/*==============================================================*/
create table PSA_STUDENTS  (
   PSTU_ID              NUMERIC(19)                     not null,
   PSTU_PDEP_ID         NUMERIC(19),
   PSTU_LOGINNAME       VARCHAR(128),
   PSTU_LOGINPWD        VARCHAR(128),
   PSTU_ZH_NAME         VARCHAR(50),
   PSTU_EN_NAME         VARCHAR(50),
   PSTU_NUMBER          VARCHAR(32),
   PSTU_PHONE           VARCHAR(32),
   PSTU_EMAIL           VARCHAR(128),
   PSTU_STATU           NUMERIC(5,0),
   PSTU_CREATEDATE      DATETIME,
   PSTU_LASTLOGIN       DATETIME,
   PSTU_BIRTHDAY        VARCHAR(64),
   PSTU_SEX             VARCHAR(32),
   PSTU_QQ              VARCHAR(32),
   PPIN_PIHI_ID         NUMERIC(19),
   constraint PK_PSA_STUDENTS primary key (PSTU_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_STUDENTS_IMPORT_HISTORY                           */
/*==============================================================*/
create table PSA_STUDENTS_IMPORT_HISTORY  (
   PIHI_ID              NUMERIC(19)                     not null,
   PIHI_AUTHOR          VARCHAR(50),
   PIHI_FILE_REALNAME   VARCHAR(128),
   PIHI_FILE_LOCALPATH  VARCHAR(128),
   PIHI_FILE_URL        VARCHAR(1024),
   PIHI_IMP_NUMBER      DECIMAL(38,0),
   PIHI_IMP_DATE        DATETIME,
   constraint PK_PSA_STUDENTS_IMPORT_HISTORY primary key (PIHI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: PSA_STUDENTS_IMPORT_TEMPLET                           */
/*==============================================================*/
create table PSA_STUDENTS_IMPORT_TEMPLET  (
   PITE_ID              NUMERIC(19)                     not null,
   PITE_COLUMN_KEY      VARCHAR(128),
   PITE_EXCEL_COLUMN    VARCHAR(32),
   constraint PK_PSA_STUDENTS_IMPORT_TEMPLET primary key (PITE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_DATA_TEMP                                         */
/*==============================================================*/
create table RES_DATA_TEMP  (
   RDTE_ID              NUMERIC(19)                     not null,
   RDTE_RMST_ID         NUMERIC(19),
   RDTE_RLTY_ID         NUMERIC(19),
   RDTE_RSDA_ID         NUMERIC(19),
   RDTE_RSTY_ID         NUMERIC(19),
   RDTE_RDTY_ONE        NUMERIC(19),
   RDTE_RDTY_TWO        NUMERIC(19),
   RDTE_RDTY_THREE      NUMERIC(19),
   RDTE_TITLE           VARCHAR(3200),
   RDTE_DESCRIPTION     VARCHAR(3200),
   SpeciesName        VARCHAR(3200),
   GeographicalDistribution VARCHAR(3200),
   AltitudeDistribution VARCHAR(3200),
   FloweringHabit     VARCHAR(3200),
   FlowerColor        VARCHAR(3200),
   FruitColor         VARCHAR(3200),
   Florescence        VARCHAR(3200),
   FruitStage         VARCHAR(3200),
   FruitShape         VARCHAR(3200),
   Usage              VARCHAR(3200),
   Aroma              VARCHAR(3200),
   Resistance         VARCHAR(3200),
   PlantingArea       VARCHAR(3200),
   PlantHeight        VARCHAR(3200),
   Fertility          VARCHAR(3200),
   VaseLife           VARCHAR(3200),
   PetalCount         VARCHAR(3200),
   MaleParent         VARCHAR(3200),
   Location           VARCHAR(3200),
   FemaleParent       VARCHAR(3200),
   RDTE_BUILDDATE       DATETIME,
   RDTE_LOOKNUM         NUMERIC(9,0),
   RDTE_GRADE           NUMERIC(5,0),
   RDTE_ICON_URL        VARCHAR(3000),
   constraint PK_RES_DATA_TEMP primary key (RDTE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_DYNAMIC_TYPE                                      */
/*==============================================================*/
create table RES_DYNAMIC_TYPE  (
   RDTY_ID              NUMERIC(19)                     not null,
   RMST_ID              NUMERIC(19)                     not null,
   RDTY_PID             NUMERIC(19),
   RDTY_NAME            VARCHAR(64),
   RDTY_GRADE           NUMERIC(2,0),
   RDTY_INDEX           NUMERIC(9,0),
   RDTY_TYPE            NUMERIC(5,0),
   RDTY_CODE            VARCHAR(128),
   RDTY_COUNT           NUMERIC(9,0),
   constraint PK_RES_DYNAMIC_TYPE primary key (RDTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRDTY_RMST_ID                                         */
/*==============================================================*/
create index IRDTY_RMST_ID on RES_DYNAMIC_TYPE (
   RMST_ID ASC
);

/*==============================================================*/
/* Index: IRDTY_PID                                             */
/*==============================================================*/
create index IRDTY_PID on RES_DYNAMIC_TYPE (
   RDTY_PID ASC
);

/*==============================================================*/
/* Index: IRDTY_GRADE                                           */
/*==============================================================*/
create index IRDTY_GRADE on RES_DYNAMIC_TYPE (
   RDTY_GRADE ASC
);

/*==============================================================*/
/* Index: IRDTY_INDEX                                           */
/*==============================================================*/
create index IRDTY_INDEX on RES_DYNAMIC_TYPE (
   RDTY_INDEX ASC
);

/*==============================================================*/
/* Table: RES_ELEMENT_POOL                                      */
/*==============================================================*/
create table RES_ELEMENT_POOL  (
   REPO_ID              NUMERIC(19)                     not null,
   REPO_RLTY_ID         NUMERIC(19,0)                   not null,
   REPO_NAME            VARCHAR(64),
   REPO_TITLE           VARCHAR(128),
   REPO_TYPE            NUMERIC(5,0)                    not null,
   REPO_CODE            VARCHAR(512),
   REPO_RESTRICT        VARCHAR(32),
   REPO_SERIANUM        NUMERIC(9,0),
   REPO_PARENT          NUMERIC(19,0),
   REPO_SHOWMODE        NUMERIC(5,0),
   REPO_DEFAULTDATA     VARCHAR(3000),
   REPO_DATATYPE        NUMERIC(5,0),
   REPO_DATAOPTIONS     VARCHAR(3200),
   REPO_CALIS           NUMERIC(5,0),
   constraint PK_RES_ELEMENT_POOL primary key (REPO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 图书
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(101,1,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(102,1,'Alternative','交替题名',3,'','R',1,101);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(103,1,'Creator','创建者',1,'','O',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(104,1,'Subject','主题',1,'LCSH 国会图书馆主题词表,MESH 医学主题词表,CT 中国分类主题词表,DDC 杜威十进分类法,LCC 国会图书馆分类法,UDC 国际十进分类法,CLC 中国图书馆图书分类法,FDC 四库分类法','MA',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(105,1,'Description','描述',1,'','R',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(106,1,'Abstract','摘要',3,'','R',1,105);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(107,1,'Publisher','出版者',1,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(108,1,'Contributor','其他责任者',1,'','MA',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(109,1,'Date','日期',1,'Period,W3CDTF,ISO8601','MA',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(110,1,'Created','创建日期',3,'','MA',1,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(111,1,'Valid','生效日期',3,'','O',2,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(112,1,'Available','可获得日期',3,'','O',3,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(113,1,'Issued','发布日期',3,'','MA',4,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(114,1,'Modified','修改日期',3,'','O',5,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(115,1,'DateCopyrighted','版权日期',3,'','O',6,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(116,1,'DateCaptured','获取日期',3,'','O',7,109);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(117,1,'Type','类型',1,'DCMIType','O',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(118,1,'Format','格式',1,'','R',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(119,1,'Extent','范围',3,'','O',1,118);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(120,1,'Medium','媒体',3,'IMT','O',2,118);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(121,1,'Identifier','标识符',1,'URI,ISBN,DOI,SICI,ISSN,ISRC','MA',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(122,1,'BibliographicCitation','书目引文',3,'','O',1,121);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(123,1,'Source','来源',1,'URI','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(124,1,'Language','语种',1,'ISO639-2,RFC3066','MA',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(125,1,'Relation','关联',1,'URI','O',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(126,1,'IsVersionOf','版本继承',3,'','R',1,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(127,1,'HasVersion','版本关联',3,'','R',2,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(128,1,'IsReplacedBy','被替代',3,'','O',3,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(129,1,'Replaces','替代',3,'','O',4,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(130,1,'IsRequiredBy','被需求',3,'','O',5,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(131,1,'Requires','需求',3,'','R',6,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(132,1,'IsPartOf','部分于',3,'','R',7,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(133,1,'HasPart','部分为',3,'','O',8,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(134,1,'IsReferencedBy','被参照',3,'','O',9,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(135,1,'References','参照',3,'','O',10,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(136,1,'IsFormatOf','格式转换于',3,'','R',11,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(137,1,'HasFormat','格式转换为',3,'','R',12,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(138,1,'ConformsTo','遵循',3,'','O',13,125);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(139,1,'Coverage','时空范围',1,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(140,1,'Spatial','空间范围',3,'Point[DCMIPoint],ISO3166,Box[DCMIBox],TGN','MA',1,139);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(141,1,'Temporal','时间范围',3,'Period,W3CDTF','MA',2,139);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(142,1,'Rights','权限',1,'','R',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(143,1,'AccessRights','访问权限',3,'','R',1,142);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(144,1,'Audience','适用对象',2,'','O',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(145,1,'Edition','版本',2,'','R',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(146,1,'Location','馆藏位置',2,'','MA',3,0);

-- 期刊文献与论文
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(201,2,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(202,2,'Alternative','交替题名',3,'','R',1,201);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(203,2,'Creator','创建者',1,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(204,2,'Organization','机构',3,'','M',1,203);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(205,2,'Subject','主题',1,'LCSH 美国国会图书馆标题表,MeSH 医学主题词表,DDC 杜威十进分类法,LCC 美国国会图书馆分类法,UDC 国际十进分类法,CT 汉语主题词表,CLC 中图分类表,LASC 科图分类表','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(206,2,'Description','描述',1,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(207,2,'TableOfContents','目次',3,'','M',1,206);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(208,2,'Abstract','摘要',3,'','M',2,206);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(209,2,'Fund','资助',3,'','M',3,206);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(210,2,'Publisher','出版者',1,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(211,2,'Contributor','其他责任者',1,'','M',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(212,2,'Organization','机构',3,'','M',1,211);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(213,2,'Date','日期',1,'W3CDTF,DCMI Period','M',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(214,2,'DateAccepted','接受日期',3,'W3CDTF,DCMI Period','M',1,213);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(215,2,'Issued','发布日期',3,'W3CDTF,DCMI Period','M',2,213);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(216,2,'Type','类型',1,'','M',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(217,2,'Source','来源',1,'URI,DOI,ISSN 国际标准书号','M',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(218,2,'Format','格式',1,'IMT','M',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(219,2,'Extent','范围',3,'','M',1,218);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(220,2,'Medium','媒体',3,'IMT','M',2,218);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(221,2,'Identifier','标识符',1,'URI,DOI','M',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(222,2,'bibliographicCitation','文献出处',3,'SICI,DOI','M',1,221);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(223,2,'Language','语种',1,'ISO 639-2,RFC1766','R',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(224,2,'Relation','关联',1,'ISO 639-2,RFC1766','M',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(225,2,'IsPartOf','组成',3,'','M',1,224);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(226,2,'References','参照',3,'','MA',2,224);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(227,2,'IsTranslatedFrom','译自',3,'','O',3,224);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(228,2,'Rights','权限',1,'','M',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(229,2,'Coverage','时空范围',1,'','O',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(230,2,'Spatial','空间范围',3,'Point[DCMIPoint],ISO3166,Box[DCMIBox],TGN','MA',1,229);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(231,2,'Temporal','时间范围',3,'Period,W3CDTF','MA',2,229);

-- 学位论文
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(301,3,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(302,3,'Alternative','交替题名',3,'','R',1,301);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(303,3,'Creator','创建者',1,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(304,3,'Institution','培养单位',3,'','M',1,303);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(305,3,'Subject','主题',1,'LCSH 国会图书馆主题词表,MESH 医学主题词表,CT 中国分类主题词表,DDC 杜威十进分类法,LCC 国会图书馆分类法,UDC 国际十进分类法,CLC 中国图书馆图书分类法,FDC 四库分类法','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(306,3,'Description','描述',1,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(307,3,'TableOfContents','目次',3,'','M',1,306);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(308,3,'Abstract','文摘',3,'','M',2,306);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(309,3,'AchievementsList','成果目录',3,'','MA',3,306);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(310,3,'DescriptionRelation','相关文献附注',3,'','MA',4,306);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(311,3,'Fund','资助',3,'','MA',5,306);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(312,3,'Publisher','出版者',1,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(313,3,'Contributor','导师',1,'','M',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(314,3,'Organization','机构',3,'','M',1,313);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(315,3,'Date','日期',1,'W3CDTF','M',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(316,3,'DefenseDate','答辩日期',3,'W3CDTF','M',1,315);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(317,3,'DateSubmitted','提交日期',3,'W3CDTF','M',2,315);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(318,3,'Available','全文可获得日期',3,'W3CDTF','MA',3,315);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(319,3,'Type','类型',1,'','M',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(320,3,'Format','格式',1,'IMT','M',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(321,3,'Identifier','标识符',1,'URI','M',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(322,3,'Source','来源',1,'','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(323,3,'Language','语种',1,'ISO639-2','R',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(324,3,'Relation','关联',1,'URI','M',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(325,3,'HasPart','部分为',3,'','M',1,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(326,3,'HasVersion','版本关联',3,'','M',2,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(327,3,'References','参照',3,'','MA',3,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(328,3,'IsReferencedBy','被参照',3,'','MA',4,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(329,3,'Requires','需求',3,'','MA',5,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(330,3,'RelationAdditionResource','附加资源关联',3,'','MA',6,324);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(331,3,'Coverage','时空范围',1,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(332,3,'Spatial','空间范围',3,'Point[DCMIPoint],ISO3166,Box[DCMIBox],TGN','MA',1,331);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(333,3,'Temporal','时间范围',3,'Period,W3CDTF','MA',2,331);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(334,3,'Rights','权限',1,'','M',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(335,3,'RightsStatement','权限声明',3,'','MA',1,334);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(336,3,'SecuritylClassfication','保密级别',3,'','M',2,334);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(337,3,'Degree','学位',2,'GB6864-86 中华人民共和国学位名称与代码','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(338,3,'Discipline','学科专业',3,'学科/专业目录','M',1,337);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(339,3,'Grantor','学位授予单位',3,'','M',2,337);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(340,3,'Location','馆藏信息',2,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(341,3,'CallNumber','典藏号',3,'','M',1,340);

-- 古籍
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(401,4,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(402,4,'Parallel','并列题名',3,'','M',1,401);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(403,4,'RunningTitle','版心题名',3,'','M',2,401);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(404,4,'TitleNeifeng','内封题名',3,'','M',3,401);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(405,4,'TitleJuanduan','卷端题名',3,'','M',4,401);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(406,4,'Other','其它题名',3,'','MA',5,401);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(407,4,'Creator','创建者',1,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(408,4,'Role','责任方式',3,'','M',1,407);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(409,4,'AdditionsToAuthor','责任者说明',3,'','M',2,407);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(410,4,'Subject','主题',1,'汉语主题词表,四库类名','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(411,4,'Description','描述',1,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(412,4,'DescriptionAuthor','责任者附注',3,'','MA',1,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(413,4,'DescriptionRelation','相关资源附注',3,'','MA',2,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(414,4,'DescriptionSeries','丛编',3,'','MA',3,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(415,4,'TableOfContents','子目',3,'','MA',4,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(416,4,'Appendix','附录',3,'','MA',5,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(417,4,'Abstract','摘要',3,'','MA',6,411);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(418,4,'Publisher','出版者',1,'','M',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(419,4,'Printer','印刷者',3,'','MA',1,418);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(420,4,'PlacePublished','出版地',3,'','MA',2,418);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(421,4,'PlacePrinted','印刷地',3,'','MA',3,418);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(422,4,'Contributor','其他责任者',1,'','M',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(423,4,'Role','责任方式',3,'','M',1,422);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(424,4,'AdditionsToAuthor','责任者说明',3,'','M',2,422);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(425,4,'Date','日期',1,'W3CDTF','M',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(426,4,'Issued','出版日期',3,'年号纪元,公元纪年','M',1,425);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(427,4,'Printed','印刷日期',3,'年号纪元,公元纪年','M',2,425);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(428,4,'Type','类型',1,'','M',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(429,4,'Format','格式',1,'IMT','M',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(430,4,'Identifier','标识符',1,'URI','M',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(431,4,'Source','来源',1,'','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(432,4,'Language','语种',1,'','M',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(433,4,'Relation','关联',1,'URI','M',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(434,4,'RalationSeries','丛编',3,'URI','M',1,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(435,4,'RelationSubObject','子目',3,'URI','M',2,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(436,4,'RelationHekeshuming','合刻书名',3,'URI','M',3,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(437,4,'RelationHehanshuming','合函书名',3,'URI','M',4,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(438,4,'Appendix','附录',3,'','M',5,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(439,4,'RelationBibliographicCitation','书目文献',3,'','MA',6,433);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(440,4,'Coverage','时空范围',1,'年号纪元,公元纪年','M',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(441,4,'Spatial','地点',3,'','M',1,440);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(442,4,'Temporal','时间',3,'年号纪年,公元纪年','M',2,440);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(443,4,'Rights','权限',1,'','R',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(444,4,'Edition','版本类别',2,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(445,4,'AdditionsToEdition','版本说明',3,'','M',1,444);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(446,4,'PhysicalDescription','载体形态',2,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(447,4,'Binding','装订方式',3,'','M',1,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(448,4,'Number','数量',3,'','M',2,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(449,4,'Chart','图表',3,'','M',3,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(450,4,'Size','尺寸',3,'','R',4,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(451,4,'ParagraphStyle','行款版式',3,'','R',5,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(452,4,'Annex','附件',3,'','R',6,446);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(453,4,'Location','馆藏信息',2,'','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(454,4,'CallNumber','典藏号',3,'','M',1,453);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(455,4,'Provenance','收藏历史',2,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(456,4,'Availability','获得方式',3,'','R',1,455);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(457,4,'InscriptionAndSeal','题跋印记',3,'','MA',2,455);

-- 民国文献
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(501,5,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(502,5,'Alternative','交替题名',3,'','R',1,501);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(503,5,'Creator','创建者',1,'','O',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(504,5,'Subject','主题',1,'LCSH 国会图书馆主题词表,MESH 医学主题词表,CT 中国分类主题词表,DDC 杜威十进分类法,LCC 国会图书馆分类法,UDC 国际十进分类法,CLC 中国图书馆图书分类法,FDC 四库分类法','MA',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(505,5,'Description','描述',1,'','R',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(506,5,'Abstract','摘要',3,'','R',1,505);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(507,5,'Publisher','出版者',1,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(508,5,'Contributor','其他责任者',1,'','MA',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(509,5,'Date','日期',1,'Period,W3CDTF,ISO8601','MA',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(510,5,'Created','创建日期',3,'','MA',1,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(511,5,'Valid','生效日期',3,'','O',2,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(512,5,'Available','可获得日期',3,'','O',3,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(513,5,'Issued','发布日期',3,'','MA',4,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(514,5,'Modified','修改日期',3,'','O',5,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(515,5,'DateCopyrighted','版权日期',3,'','O',6,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(516,5,'DateCaptured','获取日期',3,'','O',7,509);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(517,5,'Type','类型',1,'DCMIType','O',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(518,5,'Format','格式',1,'','R',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(519,5,'Extent','范围',3,'','O',1,518);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(520,5,'Medium','媒体',3,'IMT','O',2,518);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(521,5,'Identifier','标识符',1,'URI,ISBN,DOI,SICI,ISSN,ISRC','MA',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(522,5,'BibliographicCitation','书目引文',3,'','O',1,521);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(523,5,'Source','来源',1,'URI','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(524,5,'Language','语种',1,'ISO639-2,RFC3066','MA',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(525,5,'Relation','关联',1,'URI','O',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(526,5,'IsVersionOf','版本继承',3,'','R',1,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(527,5,'HasVersion','版本关联',3,'','R',2,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(528,5,'IsReplacedBy','被替代',3,'','O',3,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(529,5,'Replaces','替代',3,'','O',4,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(530,5,'IsRequiredBy','被需求',3,'','O',5,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(531,5,'Requires','需求',3,'','R',6,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(532,5,'IsPartOf','部分于',3,'','R',7,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(533,5,'HasPart','部分为',3,'','O',8,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(534,5,'IsReferencedBy','被参照',3,'','O',9,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(535,5,'References','参照',3,'','O',10,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(536,5,'IsFormatOf','格式转换于',3,'','R',11,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(537,5,'HasFormat','格式转换为',3,'','R',12,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(538,5,'ConformsTo','遵循',3,'','O',13,525);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(539,5,'Coverage','时空范围',1,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(540,5,'Spatial','空间范围',3,'Point[DCMIPoint],ISO3166,Box[DCMIBox],TGN','MA',1,539);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(541,5,'Temporal','时间范围',3,'Period,W3CDTF','MA',2,539);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(542,5,'Rights','权限',1,'','R',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(543,5,'AccessRights','访问权限',3,'','R',1,542);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(544,5,'Audience','适用对象',2,'','O',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(545,5,'Edition','版本',2,'','R',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(546,5,'Location','馆藏位置',2,'','MA',3,0);

-- 音频
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(601,6,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(602,6,'Alternative','交替题名',3,'','M',1,601);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(603,6,'Creator','创建者',1,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(604,6,'Role','责任方式',3,'','M',1,603);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(605,6,'Subject','主题',1,'CT 汉语主题词表,CLC 中国图书馆分类法,LCSH 美国国会图书馆主题词表','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(606,6,'Description','描述',1,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(607,6,'Abstract','摘要',3,'','M',1,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(608,6,'TableOfContents','目次',3,'','M',2,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(609,6,'Genre','作品载体',3,'','M',3,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(610,6,'MusicInstrument','乐器说明',3,'','M',4,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(611,6,'Key','调性与调式',3,'','M',5,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(612,6,'Voice','声部',3,'','M',6,606);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(613,6,'Publisher','出版者',1,'','M',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(614,6,'Place','出版地',3,'','M',1,613);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(615,6,'Manufacturer','制作者',3,'','M',2,613);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(616,6,'Contributor','其他责任者',1,'','M',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(617,6,'Role','责任方式',3,'','M',1,616);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(618,6,'Date','日期',1,'W3CDTF','M',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(619,6,'Created','创建日期',3,'W3CDTF','M',1,618);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(620,6,'Issued','发布日期',3,'W3CDTF','M',2,618);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(621,6,'Available','可获得日期',3,'W3CDTF','M',3,618);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(622,6,'Type','类型',1,'DCMIType','M',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(623,6,'Format','格式',1,'IMT','M',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(624,6,'Extent','篇幅',3,'','M',1,623);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(625,6,'Medium','载体',3,'','M',2,623);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(626,6,'Technique','技术细节',3,'','M',3,623);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(627,6,'Identifier','标识符',1,'URI,DOI,ISBN 国际标准书号,ISRC 国际标准音像制品编码','M',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(628,6,'Source','来源',1,'URI,DOI,ISBN 国际标准书号,ISRC 国际标准音像制品编码','M',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(629,6,'Language','语种',1,'RFC3066,ISO639-2','M',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(630,6,'Relation','关联',1,'URI,DOI,ISBN 国际标准书号,ISRC 国际标准音像制品编码','M',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(631,6,'HasPart','包含',3,'','M',1,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(632,6,'IsPartOf','包含于',3,'','M',2,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(633,6,'References','参照',3,'','M',3,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(634,6,'IsReferencedBy','被参照',3,'','M',4,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(635,6,'HasVersion','其他版本',3,'','M',5,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(636,6,'IsVersionOf','原版本',3,'','M',6,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(637,6,'HasFormat','其他格式',3,'','M',7,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(638,6,'IsFormatOf','原格式',3,'','M',8,630);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(639,6,'Coverage','时空范围',1,'','M',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(640,6,'Temporal','时间范围',3,'Period,W3CDTF','M',1,639);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(641,6,'Spatial','空间范围',3,'Point,ISO3166','M',2,639);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(642,6,'Rights','权限',1,'','M',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(643,6,'Edition','版本',2,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(644,6,'Audience','受众',2,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(645,6,'Background','背景',2,'','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(646,6,'BackgroundTemporal','背景时间',3,'W3CDTF','M',1,645);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(647,6,'BackgroundSpatial','背景地点',3,'','M',2,645);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(648,6,'SourceMedium','源载体',2,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(649,6,'SourceStatement','乐谱说明',2,'','M',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(650,6,'Phrase','乐句',2,'','M',6,0);

-- 视频
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(701,7,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(702,7,'OriginalTitle','原文题名',3,'','M',1,701);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(703,7,'ChineseTitle','中文译名',3,'','M',2,701);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(704,7,'Creator','创建者',1,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(705,7,'Director','导演',3,'','M',1,704);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(706,7,'Screenwriter','编剧',3,'','M',2,704);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(707,7,'Producer','制片',3,'','M',3,704);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(708,7,'Photographer','摄影',3,'','M',4,704);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(709,7,'MainActor','主演',3,'','M',5,704);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(710,7,'Subject','主题',1,'','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(711,7,'Description','描述',1,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(712,7,'Abstract','内容简介',3,'','M',1,711);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(713,7,'TableOfContents','目次',3,'','M',2,711);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(714,7,'Publisher','出版者',1,'','M',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(715,7,'Country','出版国',3,'','M',1,714);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(716,7,'PictureCompany','出版公司',3,'','M',2,714);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(717,7,'Contributor','其他责任者',1,'','O',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(718,7,'Date','日期',1,'W3CDTF','M',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(719,7,'Created','创建日期',3,'W3CDTF','M',1,718);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(720,7,'Release','上映日期',3,'W3CDTF','M',2,718);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(721,7,'Type','类型',1,'','M',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(722,7,'Format','格式',1,'','M',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(723,7,'Identifier','标识符',1,'ISRC 国际标准音像制品编码','M',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(724,7,'Source','来源',1,'','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(725,7,'Language','语种',1,'','M',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(726,7,'DialogueLanguage','对白语种',3,'','M',1,725);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(727,7,'CaptionLanguage','字幕语种',3,'','M',2,725);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(728,7,'Relation','关联',1,'','M',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(729,7,'OriginalTitle','原著名称',3,'','M',1,728);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(730,7,'OriginalWriter','原作者',3,'','M',2,728);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(731,7,'Coverage','时空范围',1,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(732,7,'Rights','权限',1,'','O',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(733,7,'User','适用对象',2,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(734,7,'SerialTitle','系列题名',2,'','M',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(735,7,'PhysicalCharacteristic','物理特征',2,'','M',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(736,7,'Runtime','片长',3,'','M',1,735);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(737,7,'Color','色彩',3,'','M',2,735);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(738,7,'Size','大小',3,'','M',3,735);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(739,7,'Path','存放路径',3,'','M',4,735);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(740,7,'Rating','资源级别',2,'','M',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(741,7,'Management','记录管理信息',2,'','M',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(742,7,'RecordNumber','记录编号',3,'','M',1,741);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(743,7,'DateCreated','记录创建日期',3,'W3CDTF','M',2,741);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(744,7,'DiskNumber','备份盘盘号',2,'','M',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(745,7,'Note','其他信息',2,'','M',7,0);

-- 图像
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(801,8,'Title','题名',1,'','M',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(802,8,'Creator','创建者',1,'','O',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(803,8,'Subject','主题',1,'LCSH 国会图书馆主题词表,MESH 医学主题词表,CT 中国分类主题词表,DDC 杜威十进分类法,LCC 国会图书馆分类法,UDC 国际十进分类法,CLC 中国图书馆图书分类法,FDC 四库分类法','MA',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(804,8,'Description','描述',1,'','R',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(805,8,'Publisher','出版者',1,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(806,8,'Contributor','其他责任者',1,'','MA',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(807,8,'Date','日期',1,'Period,W3CDTF,ISO8601','MA',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(808,8,'Type','类型',1,'','O',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(809,8,'Format','格式',1,'','R',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(810,8,'Identifier','标识符',1,'URI,ISBN,DOI,SICI,ISSN,ISRC','MA',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(811,8,'Source','来源',1,'','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(812,8,'Language','语种',1,'ISO639-2,RFC3066','MA',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(813,8,'Relation','关联',1,'URI','O',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(814,8,'Coverage','时空范围',1,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(815,8,'Rights','权限',1,'','R',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(816,8,'Classification','图像分类',2,'','O',1,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(817,8,'Keyword','关键词',2,'','O',2,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(818,8,'PhysicalCharacteristic','物理特征',2,'','O',3,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(819,8,'Format','格式',3,'','O',1,818);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(820,8,'Size','大小',3,'','O',2,818);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(821,8,'Path','存放路径',3,'','O',3,818);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(822,8,'Textbook','配套使用教材',2,'','O',4,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(823,8,'TextbookTitle','教材名称',3,'','O',1,822);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(824,8,'LessonNumber','课目序号',3,'','O',2,822);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(825,8,'LessonTitle','课目名称',3,'','O',3,822);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(826,8,'Use','图像参考功用',2,'','O',5,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(827,8,'Management','记录管理信息',2,'','O',6,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(828,8,'RecordNumber','记录编号',3,'','O',1,827);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(829,8,'DateCreated','记录创建日期',3,'W3CDTF','O',2,827);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(830,8,'Make','生产者',2,'','O',7,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(831,8,'Model','型号',2,'','O',8,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(832,8,'XResolution/YResolution','X/Y 方向分辨率',2,'','O',9,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(833,8,'ResolutionUnit','分辨率单位',2,'','O',10,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(834,8,'Software','软件',2,'','O',11,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(835,8,'YCbCrPositioning','色相定位',2,'','O',12,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(836,8,'ExifOffset','Exif 信息位置',2,'','O',13,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(837,8,'ExposureTime','曝光时间',2,'','O',14,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(838,8,'Fnumber','光圈系数',2,'','O',15,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(839,8,'ExposureProgram','曝光程序',2,'','O',16,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(840,8,'(IsoSpeedRatings','感光度',2,'','O',17,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(841,8,'(ExifVersionExif','版本',2,'','O',18,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(842,8,'DateTimeDigitized','数字化时间',2,'','O',19,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(843,8,'ComponentsConfiguration','图像构造',2,'','O',20,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(844,8,'CompressedBitsPerPixel','压缩时每像素色彩位',2,'','O',21,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(845,8,'ExposureBiasValue','曝光补偿',2,'','O',22,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(846,8,'MaxApertureValue','最大光圈',2,'','O',23,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(847,8,'MeteringMode','测光方式',2,'','O',24,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(848,8,'Lightsource','光源',2,'','O',25,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(849,8,'Flash','闪光灯',2,'','O',26,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(850,8,'（FocalLength','焦距',2,'','O',27,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(851,8,'ColorSpace','色彩空间',2,'','O',28,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(852,8,'ExifImageWidth','图像宽度',2,'','O',29,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(853,8,'ExifImageLength','图像高度',2,'','O',30,0);
insert into RES_ELEMENT_POOL(REPO_ID,REPO_RLTY_ID,REPO_NAME,REPO_TITLE,REPO_TYPE,REPO_CODE,REPO_RESTRICT,REPO_SERIANUM,REPO_PARENT) values(854,8,'Note','其他信息',2,'','O',31,0);



-- 重置约束
update RES_ELEMENT_POOL set REPO_RESTRICT='MA' where REPO_NAME in ('Subject','Contributor','Date','Created','Issued','Identifier','Language','Spatial','Temporal','Location') and REPO_RESTRICT='M' and REPO_RLTY_ID in (2,3,4,6,7);
update RES_ELEMENT_POOL set REPO_RESTRICT='R' where REPO_NAME in ('Alternative','Description','Abstract','Format','IsVersionOf','HasVersion','Requires','IsPartOf','IsFormatOf','HasFormat','Rights','AccessRights','Edition') and REPO_RESTRICT='M' and REPO_RLTY_ID in (2,3,4,6,7);
update RES_ELEMENT_POOL set REPO_RESTRICT='O' where REPO_NAME<>'Title' and REPO_RESTRICT='M' and REPO_RLTY_ID in (2,3,4,6,7);

/*==============================================================*/
/* Index: IREPO_RLTY_ID                                         */
/*==============================================================*/
create index IREPO_RLTY_ID on RES_ELEMENT_POOL (
   REPO_RLTY_ID ASC
);

/*==============================================================*/
/* Index: IREPO_SERIANUM                                        */
/*==============================================================*/
create index IREPO_SERIANUM on RES_ELEMENT_POOL (
   REPO_SERIANUM ASC
);

/*==============================================================*/
/* Table: RES_EXTEND_DATA                                       */
/*==============================================================*/
create table RES_EXTEND_DATA  (
   REDA_ID              NUMERIC(19)                     not null,
   REDA_REPO_ID         NUMERIC(19),
   REDA_RSDA_ID         NUMERIC(19,0),
   REDA_DATA            VARCHAR(3200),
   REDA_LANGUAGE        NUMERIC(19),
   constraint PK_RES_EXTEND_DATA primary key (REDA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IREDA_REPO_ID                                         */
/*==============================================================*/
create index IREDA_REPO_ID on RES_EXTEND_DATA (
   REDA_REPO_ID ASC
);

/*==============================================================*/
/* Index: IREDA_RSDA_ID                                         */
/*==============================================================*/
create index IREDA_RSDA_ID on RES_EXTEND_DATA (
   REDA_RSDA_ID ASC
);

/*==============================================================*/
/* Index: IREDA_LANGUAGE                                        */
/*==============================================================*/
create index IREDA_LANGUAGE on RES_EXTEND_DATA (
   REDA_LANGUAGE ASC
);

/*==============================================================*/
/* Table: RES_FILESYSTEM                                        */
/*==============================================================*/
create table RES_FILESYSTEM  (
   RFIL_ID              NUMERIC(19,0)                   not null,
   RFIL_NAME            VARCHAR(256)                   not null,
   RFIL_ADDRESS         VARCHAR(512)                   not null,
   RFIL_FTP_HOST        VARCHAR(512),
   RFIL_FTP_USERNAME    VARCHAR(64),
   RFIL_FTP_PASSWORD    VARCHAR(64),
   RFIL_FPT_PATH        VARCHAR(512),
   RFIL_NUM             NUMERIC(5,0),
   constraint PK_RES_FILESYSTEM primary key (RFIL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_IMPORT_HISTORY                                    */
/*==============================================================*/
create table RES_IMPORT_HISTORY  (
   RMHI_ID              NUMERIC(19,0)                   not null,
   RMHI_RMIN_ID         NUMERIC(19),
   RMHI_AUTHOR          NUMERIC(19,0),
   RMHI_CHECK_MUST      NUMERIC(1,0),
   RMHI_CHECK_TYPE      NUMERIC(1,0),
   RMHI_FILEFOLDER      VARCHAR(128),
   RMHI_LOCAL_FILENAME  VARCHAR(512),
   RMHI_FILENAME        VARCHAR(512),
   RMHI_ADDRESS         VARCHAR(1024),
   RMHI_IMPORT_DATE     DATETIME,
   constraint PK_RES_IMPORT_HISTORY primary key (RMHI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_IMPORT_TEMPLET                                    */
/*==============================================================*/
create table RES_IMPORT_TEMPLET  (
   RITE_ID              NUMERIC(19,0)                   not null,
   RITE_RMST_ID         NUMERIC(19),
   RITE_RLTY_ID         NUMERIC(19),
   RITE_REPO_ID         NUMERIC(19,0),
   RITE_COLUMN          VARCHAR(32),
   RITE_IS_FORMAT       NUMERIC(1,0),
   RITE_REMARK          VARCHAR(512),
   constraint PK_RES_IMPORT_TEMPLET primary key (RITE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_IMP_COLS                                          */
/*==============================================================*/
create table RES_IMP_COLS  (
   RICO_ID              NUMERIC(19,0)                   not null,
   RICO_RTIM_ID         NUMERIC(19),
   RICO_EXCEL           NUMERIC(5,0),
   constraint PK_RES_IMP_COLS primary key (RICO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_INDEX_KEY                                         */
/*==============================================================*/
create table RES_INDEX_KEY  (
   RIKE_ID              NUMERIC(19)                     not null,
   RIKE_NAME            VARCHAR(128),
   RIKE_SEARCH_COUNT 	DECIMAL(38,0)(9),
   constraint PK_RES_INDEX_KEY primary key (RIKE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_LANGUAGE_TYPE                                     */
/*==============================================================*/
create table RES_LANGUAGE_TYPE  (
   RLTY_ID              NUMERIC(19)                     not null,
   RLTY_NAME            VARCHAR(64)                    not null,
   constraint PK_RES_LANGUAGE_TYPE primary key (RLTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into RES_LANGUAGE_TYPE(RLTY_ID,RLTY_NAME) values(1,'简体中文');
insert into RES_LANGUAGE_TYPE(RLTY_ID,RLTY_NAME) values(2,'繁体中文');
insert into RES_LANGUAGE_TYPE(RLTY_ID,RLTY_NAME) values(3,'英文');
insert into RES_LANGUAGE_TYPE(RLTY_ID,RLTY_NAME) values(4,'藏文');

/*==============================================================*/
/* Table: RES_LITER_TYPE                                        */
/*==============================================================*/
create table RES_LITER_TYPE  (
   RLTY_ID              NUMERIC(19)                     not null,
   RLTY_NAME            VARCHAR(128)                   not null,
   RLTY_TYPE            NUMERIC(5,0)                    not null,
   RLTY_CODE            VARCHAR(128),
   constraint PK_RES_LITER_TYPE primary key (RLTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(1,'图书',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(2,'期刊文献与论文',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(3,'学位论文',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(4,'古籍',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(5,'民国文献',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(6,'音频',3);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(7,'视频',4);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(8,'图像',2);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(9,'舆图',2);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(10,'网页',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(11,'手稿',1);
insert into RES_LITER_TYPE(RLTY_ID,RLTY_NAME,RLTY_TYPE) values(12,'收藏品',1);

/*==============================================================*/
/* Table: RES_LOADS_LIST                                        */
/*==============================================================*/
create table RES_LOADS_LIST  (
   RLLI_ID              NUMERIC(19)                     not null,
   RLLI_RMIN_ID         NUMERIC(19)                     not null,
   RLLI_AUTHOR          NUMERIC(19,0),
   RLLI_FILENAME        VARCHAR(64),
   RLLI_LOADDATE        DATETIME,
   constraint PK_RES_LOADS_LIST primary key (RLLI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_META_PERM                                         */
/*==============================================================*/
create table RES_META_PERM  (
   RMPE_ID              NUMERIC(19)                     not null,
   RMPE_ADMINID         NUMERIC(19,0),
   RMST_ID              DECIMAL(38,0)(19),
   RMPE_TYPE            NUMERIC(2,0)                    not null,
   RMPE_BUILDDATE       DATETIME,
   constraint PK_RES_META_PERM primary key (RMPE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRMPE_ADMINID                                         */
/*==============================================================*/
create index IRMPE_ADMINID on RES_META_PERM (
   RMPE_ADMINID ASC
);

/*==============================================================*/
/* Index: IRMPE_RMST_ID                                         */
/*==============================================================*/
create index IRMPE_RMST_ID on RES_META_PERM (
   RMST_ID ASC
);

/*==============================================================*/
/* Table: RES_META_STORE                                        */
/*==============================================================*/
create table RES_META_STORE  (
   RMST_ID              NUMERIC(19)                     not null,
   SMAN_ID              NUMERIC(19),
   RMST_RFIL_ID         NUMERIC(19,0),
   RMST_NAME            VARCHAR(128)                   not null,
   RMST_COMPANY         VARCHAR(128),
   RMST_REMARK          LONGTEXT,
   RMST_BUILDDATE       DATETIME,
   RMST_STATUS          NUMERIC(5),
   RMST_FOLDER          VARCHAR(128),
   RMST_CMTSTUS         NUMERIC(1,0),
   RMST_IS_UPLOAD_RESOURCE NUMERIC(1,0)                    not null,
   RMST_LOGO            VARCHAR(512),
   RMST_INDEX_SORT      NUMERIC(5,0),
   RMST_GROUP_ONE       VARCHAR(256),
   RMST_GROUP_TWO       VARCHAR(256),
   RMST_GROUP_THREE     VARCHAR(256),
   RMST_RESOURCE_AUDIT  NUMERIC(5,0),
   constraint PK_RES_META_STORE primary key (RMST_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRMST_NAME                                            */
/*==============================================================*/
create unique index IRMST_NAME on RES_META_STORE (
   RMST_NAME ASC
);

/*==============================================================*/
/* Index: IRMST_BUILDDATE                                       */
/*==============================================================*/
create index IRMST_BUILDDATE on RES_META_STORE (
   RMST_BUILDDATE ASC
);

/*==============================================================*/
/* Index: IRMST_STATUS                                          */
/*==============================================================*/
create index IRMST_STATUS on RES_META_STORE (
   RMST_STATUS ASC
);

/*==============================================================*/
/* Index: IRMST_CMTSTUS                                         */
/*==============================================================*/
create index IRMST_CMTSTUS on RES_META_STORE (
   RMST_CMTSTUS ASC
);

/*==============================================================*/
/* Table: RES_MIDDLE_TYPE                                       */
/*==============================================================*/
create table RES_MIDDLE_TYPE  (
   BZTY_ID              NUMERIC(19)                     not null,
   BZTY_NAME            VARCHAR(512)                   not null,
   BZTY_PARENT_ID       NUMERIC(19),
   BZTY_CODE            VARCHAR(64)                    not null,
   BZTY_LAYER           NUMERIC(5,0),
   BZTY_CUSTOM_CODE     VARCHAR(64),
   BZTY_PARSE_CODE      VARCHAR(256),
   constraint PK_RES_MIDDLE_TYPE primary key (BZTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000001,'A 马克思主义、列宁主义、毛泽东思想、邓小平理论',null,'A','01',1,'A');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000002,'A1 马克思、恩格斯著作',10000001,'A1','0101',2,'A1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000003,'A2 列宁著作',10000001,'A2','0102',2,'A2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000004,'A3 斯大林著作',10000001,'A3','0103',2,'A3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000005,'A4 毛泽东著作',10000001,'A4','0104',2,'A4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000006,'A49 邓小平著作',10000001,'A49','0105',2,'A49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000007,'A5 马克思、恩格斯、列宁、斯大林、毛泽东、邓小平著作汇编',10000001,'A5','0106',2,'A5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000008,'A7 马克思、恩格斯、列宁、斯大林、毛泽东、邓小平生平和传记',10000001,'A7','0107',2,'A7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(10000009,'A8 马克思主义、列宁主义、毛泽东思想、邓小平理论的学习和研究',10000001,'A8','0108',2,'A8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000001,'B 哲学、宗教',null,'B','02',1,'B');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000002,'B0 哲学理论',20000001,'B0','0201',2,'B0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000003,'B1 世界哲学',20000001,'B1','0202',2,'B1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000004,'B2 中国哲学',20000001,'B2','0203',2,'B2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000005,'B3 亚洲哲学',20000001,'B3','0204',2,'B3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000006,'B4 非洲哲学',20000001,'B4','0205',2,'B4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000007,'B5 欧洲哲学',20000001,'B5','0206',2,'B5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000008,'B6 大洋洲哲学',20000001,'B6','0207',2,'B6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000009,'B7 美洲哲学',20000001,'B7','0208',2,'B7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000010,'B80 思维科学',20000001,'B80','0209',2,'B80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000011,'B81 逻辑学（论理学）',20000001,'B81','0210',2,'B81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000012,'B82 伦理学（道德哲学）',20000001,'B82','0211',2,'B82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000013,'B83 美学',20000001,'B83','0212',2,'B83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000014,'B84 心理学',20000001,'B84','0213',2,'B84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(20000015,'B9 宗教',20000001,'B9','0214',2,'B9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000001,'C 社会科学总论',null,'C','03',1,'C');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000002,'C0 社会科学理论与方法论',30000001,'C0','0301',2,'C0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000003,'C1 社会科学现状及发展',30000001,'C1','0302',2,'C1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000004,'C2 社会科学机构、团体、会议',30000001,'C2','0303',2,'C2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000005,'C3 社会科学研究方法',30000001,'C3','0304',2,'C3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000006,'C4 社会科学教育与普及',30000001,'C4','0305',2,'C4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000007,'C5 社会科学丛书、文集、连续性出版物',30000001,'C5','0306',2,'C5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000008,'C6 社会科学参考工具书',30000001,'C6','0307',2,'C6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000009,'[C7] 社会科学文献检索工具书',30000001,'[C7]','0308',2,'C7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000010,'C8 统计学',30000001,'C8','0309',2,'C8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000011,'C91 社会学',30000001,'C91','0310',2,'C91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000012,'C92 人口学',30000001,'C92','0311',2,'C92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000013,'C93 管理学',30000001,'C93','0312',2,'C93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000014,'[C94] 系统科学',30000001,'[C94]','0313',2,'C94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000015,'C95 民族学',30000001,'C95','0314',2,'C95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000016,'C96 人才学',30000001,'C96','0315',2,'C96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(30000017,'C97 劳动科学',30000001,'C97','0316',2,'C97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000001,'D 政治、法律',null,'D','04',1,'D');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000002,'D0 政治理论',40000001,'D0','0401',2,'D0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000003,'D1 国际共产主义运动',40000001,'D1','0402',2,'D1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000004,'D2 中国共产党',40000001,'D2','0403',2,'D2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000005,'D33/37 各国共产党',40000001,'D33/37','0404',2,'D33/37,D33,D34,D35,D36,D37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000006,'D4 工人、农民、青年、妇女运动与组织',40000001,'D4','0405',2,'D4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000007,'D5 世界政治',40000001,'D5','0406',2,'D5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000008,'D6 中国政治',40000001,'D6','0407',2,'D6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000009,'D73/77 各国政治',40000001,'D73/77','0408',2,'D73/77,D73,D74,D75,D76,D77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000010,'D8 外交、国际关系',40000001,'D8','0409',2,'D8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000011,'D9 法律',40000001,'D9','0410',2,'D9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000012,'D90 法的理论(法学)',40000011,'D90','041001',3,'D90');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000013,'D901 立法理论',40000012,'D901','04100101',4,'D901');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000014,'D902 法制理论',40000012,'D902','04100102',4,'D902');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000015,'D903 法的起源与本质',40000012,'D903','04100103',4,'D903');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000016,'D904 法的历史类型',40000012,'D904','04100104',4,'D904');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000017,'D908 比较法学',40000012,'D908','04100105',4,'D908');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000018,'D909 法学史、法律思想史',40000012,'D909','04100106',4,'D909');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000019,'D909.9 法制史',40000012,'D909.9','04100107',4,'D9099');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000020,'D91 法学各部门',40000011,'D91','041002',3,'D91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000021,'D910 各国法律综合汇编',40000020,'D910','04100201',4,'D910');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000022,'D911 国家法、宪法',40000020,'D911','04100202',4,'D911');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000023,'D912.1 行政法',40000020,'D912.1','04100203',4,'D9121');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000024,'D912.2 财政法',40000020,'D912.2','04100204',4,'D9122');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000025,'D912.28 金融法',40000020,'D912.28','04100205',4,'D91228');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000026,'D912.29 经济法',40000020,'D912.29','04100206',4,'D91229');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000027,'D912.3 土地法',40000020,'D912.3','04100207',4,'D9123');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000028,'D912.4 农业经济管理法',40000020,'D912.4','04100208',4,'D9124');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000029,'D912.5 劳动法',40000020,'D912.5','04100209',4,'D9125');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000030,'D912.6 自然资源与环境保护法',40000020,'D912.6','04100210',4,'D9126');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000031,'D912.7 青少年法',40000020,'D912.7','04100211',4,'D9127');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000032,'[D912.8] 军法',40000020,'[D912.8]','04100212',4,'D9128');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000033,'D913 民法',40000020,'D913','04100213',4,'D913');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000034,'D914 刑法',40000020,'D914','04100214',4,'D914');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000035,'D915 诉讼法',40000020,'D915','04100215',4,'D915');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000036,'D916 司法制度',40000020,'D916','04100216',4,'D916');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000037,'D917 犯罪学',40000020,'D917','04100217',4,'D917');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000038,'D918 刑事侦查学(犯罪对策学、犯罪侦查学)',40000020,'D918','04100218',4,'D918');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000039,'D918.9 司法鉴定法',40000020,'D918.9','04100219',4,'D9189');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000040,'D919 法医学',40000020,'D919','04100220',4,'D919');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000041,'D92 中国法律',40000011,'D92','041003',3,'D92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000042,'D920.0 理论',40000041,'D920.0','04100301',4,'D9200');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000043,'D920.1 方针、政策及其阐述',40000041,'D920.1','04100302',4,'D9201');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000044,'D920.4 学习、研究',40000041,'D920.4','04100303',4,'D9204');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000045,'D920.5 解释、案例',40000041,'D920.5','04100304',4,'D9205');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000046,'D920.9 法律汇编',40000041,'D920.9','04100305',4,'D9209');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000047,'D921 国家法、宪法',40000041,'D921','04100306',4,'D921');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000048,'D922.1 行政法',40000041,'D922.1','04100307',4,'D9221');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000049,'D922.2 财政法',40000041,'D922.2','04100308',4,'D9222');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000050,'D922.28 金融法',40000041,'D922.28','04100309',4,'D92228');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000051,'D922.29 经济法',40000041,'D922.29','04100310',4,'D92229');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000052,'D922.3 土地法',40000041,'D922.3','04100311',4,'D9223');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000053,'D922.4 农业经济管理法',40000041,'D922.4','04100312',4,'D9224');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000054,'D922.5 劳动法',40000041,'D922.5','04100313',4,'D9225');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000055,'D922.6 自然资源与环境保护法',40000041,'D922.6','04100314',4,'D9226');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000056,'D923 民法',40000041,'D923','04100315',4,'D923');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000057,'D923.9 婚姻法',40000041,'D923.9','04100316',4,'D9239');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000058,'D923.99 商法（总论）',40000041,'D923.99','04100317',4,'D92399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000059,'D924 刑法',40000041,'D924','04100318',4,'D924');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000060,'D925 诉讼法',40000041,'D925','04100319',4,'D925');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000061,'D926 司法制度',40000041,'D926','04100320',4,'D926');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000062,'D927 地方法制',40000041,'D927','04100321',4,'D927');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000063,'D929 中国法制史',40000041,'D929','04100322',4,'D929');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000064,'D93/97 各国法律',40000011,'D93/97','041004',3,'D93/97,D93,D94,D95,D96,D97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000065,'D99 国际法',40000011,'D99','041005',3,'D99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000066,'D990 国际法理论',40000065,'D990','04100501',4,'D990');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000067,'D992 国家',40000065,'D992','04100502',4,'D992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000068,'D993 领土',40000065,'D993','04100503',4,'D993');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000069,'D993.7 外交领事法',40000065,'D993.7','04100504',4,'D9937');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000070,'D993.8 条约法',40000065,'D993.8','04100505',4,'D9938');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000071,'D993.9 国际组织法',40000065,'D993.9','04100506',4,'D9939');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000072,'D994 平时国际法',40000065,'D994','04100507',4,'D994');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000073,'D995 战时国际法（战争法）',40000065,'D995','04100508',4,'D995');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000074,'D995.9 中立',40000065,'D995.9','04100509',4,'D9959');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000075,'D996 国际经济法',40000065,'D996','04100510',4,'D996');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000076,'D996.9 国际环境保护法',40000065,'D996.9','04100511',4,'D9969');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000077,'D997 国际私法',40000065,'D997','04100512',4,'D997');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000078,'D997.9 国际刑法',40000065,'D997.9','04100513',4,'D9979');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000079,'D998 国籍法',40000065,'D998','04100514',4,'D998');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000080,'D999.1 外层空间法（宇宙法）',40000065,'D999.1','04100515',4,'D9991');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000081,'D999.2 核法',40000065,'D999.2','04100516',4,'D9992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000082,'DF 法律',40000001,'DF','0411',2,'DF');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000083,'DF0 法的理论(法学)',40000082,'DF0','041101',3,'DF0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000084,'DF01 立法理论',40000083,'DF01','04110101',4,'DF01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000085,'DF02 法制与民主',40000083,'DF02','04110102',4,'DF02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000086,'DF03 法的起源与本质',40000083,'DF03','04110103',4,'DF03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000087,'DF04 法的历史类型',40000083,'DF04','04110104',4,'DF04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000088,'DF07 比较法学',40000083,'DF07','04110105',4,'DF07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000089,'DF08 法学史、法律思想史',40000083,'DF08','04110106',4,'DF08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000090,'DF09 法制史',40000083,'DF09','04110107',4,'DF09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000091,'DF1 世界各国法律',40000082,'DF1','041102',3,'DF1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000092,'DF11 世界',40000091,'DF11','04110201',4,'DF11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000093,'DF12 中国',40000091,'DF12','04110202',4,'DF12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000094,'DF13/17 各国',40000091,'DF13/17','04110203',4,'DF13/17,DF13,DF14,DF15,DF16,DF17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000095,'DF2 国家法、宪法',40000082,'DF2','041103',3,'DF2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000096,'DF21 国家机构组织法',40000095,'DF21','04110301',4,'DF21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000097,'DF22 选举法',40000095,'DF22','04110302',4,'DF22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000098,'[DF27] 国籍法',40000095,'[DF27]','04110303',4,'DF27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000099,'DF28 地方自治法',40000095,'DF28','04110304',4,'DF28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000100,'DF29 特别行政区基本法',40000095,'DF29','04110305',4,'DF29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000101,'DF3 行政法',40000082,'DF3','041104',3,'DF3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000102,'DF31 行政管理法令',40000101,'DF31','04110401',4,'DF31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000103,'DF32 国防军事管理法令',40000101,'DF32','04110402',4,'DF32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000104,'DF33 外事管理法令',40000101,'DF33','04110403',4,'DF33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000105,'DF34 公安管理法令',40000101,'DF34','04110404',4,'DF34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000106,'DF35 侨务、民族事务管理法令',40000101,'DF35','04110405',4,'DF35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000107,'DF36 文教、卫生管理法令',40000101,'DF36','04110406',4,'DF36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000108,'DF37 科学技术管理法令',40000101,'DF37','04110407',4,'DF37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000109,'DF38 公用事业管理法令',40000101,'DF38','04110408',4,'DF38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000110,'DF391 民政和社会保障事业管理法令',40000101,'DF391','04110409',4,'DF391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000111,'DF393 青少年法',40000101,'DF393','04110410',4,'DF393');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000112,'DF399 其他法令',40000101,'DF399','04110411',4,'DF399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000113,'DF4 经济法、财政法',40000082,'DF4','041105',3,'DF4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000114,'DF41 经济法',40000113,'DF41','04110501',4,'DF41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000115,'DF43 财政法',40000113,'DF43','04110502',4,'DF43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000116,'DF438 金融法',40000113,'DF438','04110503',4,'DF438');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000117,'DF45 土地法',40000113,'DF45','04110504',4,'DF45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000118,'DF46 自然资源与环境保护法',40000113,'DF46','04110505',4,'DF46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000119,'DF47 劳动法',40000113,'DF47','04110506',4,'DF47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000120,'DF5 民法',40000082,'DF5','041106',3,'DF5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000121,'DF51 总则',40000120,'DF51','04110601',4,'DF51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000122,'DF52 分则',40000120,'DF52','04110602',4,'DF52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000123,'DF55 婚姻法',40000120,'DF55','04110603',4,'DF55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000124,'DF59 商法',40000120,'DF59','04110604',4,'DF59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000125,'DF6 刑法',40000082,'DF6','041107',3,'DF6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000126,'DF61 总则',40000125,'DF61','04110701',4,'DF61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000127,'DF62 分则',40000125,'DF62','04110702',4,'DF62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000128,'DF7 诉讼法',40000082,'DF7','041108',3,'DF7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000129,'DF71 诉讼制度',40000128,'DF71','04110801',4,'DF71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000130,'DF718 诉讼程序',40000128,'DF718','04110802',4,'DF718');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000131,'DF72 民事诉讼法',40000128,'DF72','04110803',4,'DF72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000132,'DF73 刑事诉讼法',40000128,'DF73','04110804',4,'DF73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000133,'DF74 行政诉讼法',40000128,'DF74','04110805',4,'DF74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000134,'DF75 仲裁法',40000128,'DF75','04110806',4,'DF75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000135,'{DF79} 司法鉴定',40000082,'{DF79}','041109',3,'DF79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000136,'DF792 犯罪学',40000082,'DF792','041110',3,'DF792');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000137,'DF792.1 犯罪原因',40000136,'DF792.1','04111001',4,'DF7921');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000138,'DF792.2 犯罪心理学',40000136,'DF792.2','04111002',4,'DF7922');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000139,'DF792.3 犯罪社会学',40000136,'DF792.3','04111003',4,'DF7923');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000140,'DF792.6 犯罪预防与治理',40000136,'DF792.6','04111004',4,'DF7926');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000141,'DF792.9 其他',40000136,'DF792.9','04111005',4,'DF7929');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000142,'DF793 刑事侦查学（犯罪对策学、犯罪侦查学）',40000082,'DF793','041111',3,'DF793');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000143,'DF793.1 犯罪同一认定',40000142,'DF793.1','04111101',4,'DF7931');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000144,'DF793.2 侦查技术与方法',40000142,'DF793.2','04111102',4,'DF7932');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000145,'{DF793.4} 痕迹学',40000142,'{DF793.4}','04111103',4,'DF7934');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000146,'DF793.5 现场勘查',40000142,'DF793.5','04111104',4,'DF7935');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000147,'DF793.7 预审学',40000142,'DF793.7','04111105',4,'DF7937');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000148,'DF794 司法鉴定学',40000082,'DF794','041112',3,'DF794');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000149,'DF794.1 痕迹学',40000148,'DF794.1','04111201',4,'DF7941');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000150,'DF794.2 文书检验',40000148,'DF794.2','04111202',4,'DF7942');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000151,'DF794.3 司法化学检验',40000148,'DF794.3','04111203',4,'DF7943');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000152,'DF794.5 司法会计学',40000148,'DF794.5','04111204',4,'DF7945');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000153,'DF795 法医学',40000082,'DF795','041113',3,'DF795');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000154,'DF795.1 法医基础科学',40000153,'DF795.1','04111301',4,'DF7951');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000155,'DF795.2 法医物证检验学',40000153,'DF795.2','04111302',4,'DF7952');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000156,'DF795.3 司法精神医学',40000153,'DF795.3','04111303',4,'DF7953');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000157,'DF795.4 法医鉴定学',40000153,'DF795.4','04111304',4,'DF7954');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000158,'DF795.5 妇婴法医学',40000153,'DF795.5','04111305',4,'DF7955');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000159,'DF795.6 法医人类学',40000153,'DF795.6','04111306',4,'DF7956');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000160,'DF8 司法制度',40000082,'DF8','041114',3,'DF8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000161,'DF81 司法行政',40000160,'DF81','04111401',4,'DF81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000162,'DF82 法院',40000160,'DF82','04111402',4,'DF82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000163,'DF83 检察院',40000160,'DF83','04111403',4,'DF83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000164,'DF84 司法监督',40000160,'DF84','04111404',4,'DF84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000165,'DF85 律师制度',40000160,'DF85','04111405',4,'DF85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000166,'DF86 公证制度',40000160,'DF86','04111406',4,'DF86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000167,'DF87 监狱制度与劳动改造制度',40000160,'DF87','04111407',4,'DF87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000168,'DF88 劳动教养制度',40000160,'DF88','04111408',4,'DF88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000169,'DF9 国际法',40000082,'DF9','041115',3,'DF9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000170,'DF90 国际法理论',40000169,'DF90','04111501',4,'DF90');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000171,'DF92 国家',40000169,'DF92','04111502',4,'DF92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000172,'DF93 领土',40000169,'DF93','04111503',4,'DF93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000173,'DF94 平时国际法',40000169,'DF94','04111504',4,'DF94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000174,'DF95 战时国际法(战争法)',40000169,'DF95','04111505',4,'DF95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000175,'DF959 中立',40000169,'DF959','04111506',4,'DF959');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000176,'DF96 国际经济法',40000169,'DF96','04111507',4,'DF96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000177,'DF969 国际环境保护法',40000169,'DF969','04111508',4,'DF969');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000178,'DF97 国际私法',40000169,'DF97','04111509',4,'DF97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000179,'DF979 国际刑法',40000169,'DF979','04111510',4,'DF979');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000180,'DF98 国籍法',40000169,'DF98','04111511',4,'DF98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000181,'DF991 外层空间法',40000169,'DF991','04111512',4,'DF991');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(40000182,'DF992 核法',40000169,'DF992','04111513',4,'DF992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000001,'E 军事',null,'E','05',1,'E');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000002,'E0 军事理论',50000001,'E0','0501',2,'E0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000003,'E1 世界军事',50000001,'E1','0502',2,'E1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000004,'E2 中国军事',50000001,'E2','0503',2,'E2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000005,'E3/7 各国军事',50000001,'E3/7','0504',2,'E3/7,E3,E4,E5,E6,E7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000006,'E8 战略学、战役学、战术学',50000001,'E8','0505',2,'E8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000007,'E9 军事技术',50000001,'E9','0506',2,'E9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(50000008,'E99 军事地形学、军事地理学',50000001,'E99','0507',2,'E99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000001,'F 经济',null,'F','06',1,'F');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000002,'F0 经济学',60000001,'F0','0601',2,'F0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000003,'F0-0 马克思主义政治经济学(总论)',60000002,'F0-0','060101',3,'F0-0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000004,'F01 经济学基本问题',60000002,'F01','060102',3,'F01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000005,'F02 前资本主义社会生产方式',60000002,'F02','060103',3,'F02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000006,'F03 资本主义社会生产方式',60000002,'F03','060104',3,'F03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000007,'F04 社会主义社会生产方式',60000002,'F04','060105',3,'F04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000008,'F05 共产主义社会生产方式',60000002,'F05','060106',3,'F05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000009,'F06 经济学分支科学',60000002,'F06','060107',3,'F06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000010,'F08 各科经济学',60000002,'F08','060108',3,'F08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000011,'F09 经济思想史',60000002,'F09','060109',3,'F09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000012,'F1 世界各国经济概况、经济史、经济地理',60000001,'F1','0602',2,'F1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000013,'F11 世界经济、国际经济关系',60000012,'F11','060201',3,'F11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000014,'F12 中国经济',60000012,'F12','060202',3,'F12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000015,'F13/17 各国经济',60000012,'F13/17','060203',3,'F13/17,F13,F14,F15,F16,F17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000016,'F2 经济计划与管理',60000001,'F2','0603',2,'F2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000017,'F20 国民经济管理',60000016,'F20','060301',3,'F20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000018,'F21 经济计划',60000016,'F21','060302',3,'F21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000019,'F22 经济计算、经济数学方法',60000016,'F22','060303',3,'F22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000020,'F23 会计',60000016,'F23','060304',3,'F23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000021,'F239 审计',60000016,'F239','060305',3,'F239');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000022,'F24 劳动经济',60000016,'F24','060306',3,'F24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000023,'F25 物资经济',60000016,'F25','060307',3,'F25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000024,'F27 企业经济',60000016,'F27','060308',3,'F27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000025,'F28 基本建设经济',60000016,'F28','060309',3,'F28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000026,'F29 城市与市政经济',60000016,'F29','060310',3,'F29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000027,'F3 农业经济',60000001,'F3','0604',2,'F3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000028,'F30 农业经济理论',60000027,'F30','060401',3,'F30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000029,'F31 世界农业经济',60000027,'F31','060402',3,'F31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000030,'F32 中国农业经济',60000027,'F32','060403',3,'F32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000031,'F33/37 各国农业经济',60000027,'F33/37','060404',3,'F33/37,F33,F34,F35,F36,F37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000032,'F4 工业经济',60000001,'F4','0605',2,'F4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000033,'F40 工业经济理论',60000032,'F40','060501',3,'F40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000034,'F41 世界工业经济',60000032,'F41','060502',3,'F41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000035,'F42 中国工业经济',60000032,'F42','060503',3,'F42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000036,'F43/47 各国工业经济',60000032,'F43/47','060504',3,'F43/47,F43,F44,F45,F46,F47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000037,'F49 信息产业经济(总论)',60000001,'F49','0606',2,'F49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000038,'F5 交通运输经济',60000001,'F5','0607',2,'F5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000039,'F50 交通运输经济理论',60000038,'F50','060701',3,'F50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000040,'F51 世界各国概况',60000038,'F51','060702',3,'F51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000041,'F53 铁路运输经济',60000038,'F53','060703',3,'F53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000042,'F54 陆路、公路运输经济',60000038,'F54','060704',3,'F54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000043,'F55 水路运输经济',60000038,'F55','060705',3,'F55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000044,'F56 航空运输经济',60000038,'F56','060706',3,'F56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000045,'F57 城市交通运输经济',60000038,'F57','060707',3,'F57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000046,'F59 旅游经济',60000001,'F59','0608',2,'F59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000047,'F590 旅游经济理论与方法',60000046,'F590','060801',3,'F590');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000048,'F591 世界旅游事业',60000046,'F591','060802',3,'F591');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000049,'F592 中国旅游事业',60000046,'F592','060803',3,'F592');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000050,'F593/597 各国旅游事业',60000046,'F593/597','060804',3,'F593/597,F593,F594,F595,F596,F597');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000051,'F6 邮电经济',60000001,'F6','0609',2,'F6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000052,'F60 邮电经济与理论',60000051,'F60','060901',3,'F60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000053,'F61 邮政',60000051,'F61','060902',3,'F61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000054,'F62 电信',60000051,'F62','060903',3,'F62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000055,'F63 世界各国邮电事业',60000051,'F63','060904',3,'F63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000056,'F7 贸易经济',60000001,'F7','0610',2,'F7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000057,'F71 国内贸易经济',60000056,'F71','061001',3,'F71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000058,'F72 中国国内贸易经济',60000056,'F72','061002',3,'F72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000059,'F73 世界各国国内贸易经济',60000056,'F73','061003',3,'F73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000060,'F74 国际贸易',60000056,'F74','061004',3,'F74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000061,'F75 各国对外贸易',60000056,'F75','061005',3,'F75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000062,'F76 商品学',60000056,'F76','061006',3,'F76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000063,'F8 财政、金融',60000001,'F8','0611',2,'F8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000064,'F81 财政、国家财政',60000063,'F81','061101',3,'F81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000065,'F82 货币',60000063,'F82','061102',3,'F82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000066,'F83 金融、银行',60000063,'F83','061103',3,'F83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(60000067,'F84 保险',60000063,'F84','061104',3,'F84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000001,'G 文化、科学、教育、体育',null,'G','07',1,'G');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000002,'G0 文化理论',70000001,'G0','0701',2,'G0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000003,'G02 文化哲学',70000002,'G02','070101',3,'G02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000004,'G03 文化的民族性',70000002,'G03','070102',3,'G03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000005,'G04 比较文化学',70000002,'G04','070103',3,'G04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000006,'G05 文化与其他学科的关系',70000002,'G05','070104',3,'G05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000007,'G07 文化地理学',70000002,'G07','070105',3,'G07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000008,'[G09] 文化史',70000002,'[G09]','070106',3,'G09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000009,'G1 世界各国文化与文化事业',70000001,'G1','0702',2,'G1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000010,'G11 世界',70000009,'G11','070201',3,'G11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000011,'G12 中国',70000009,'G12','070202',3,'G12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000012,'G13/17 各国',70000009,'G13/17','070203',3,'G13/17,G13,G14,G15,G16,G17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000013,'G2 信息与知识传播',70000001,'G2','0703',2,'G2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000014,'G20 信息与传播理论',70000013,'G20','070301',3,'G20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000015,'G21 新闻学、新闻事业',70000013,'G21','070302',3,'G21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000016,'G22 广播、电视事业',70000013,'G22','070303',3,'G22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000017,'G23 出版事业',70000013,'G23','070304',3,'G23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000018,'G24 群众文化事业',70000013,'G24','070305',3,'G24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000019,'G25 图书馆学、图书馆事业',70000013,'G25','070306',3,'G25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000020,'G26 博物馆学、博物馆事业',70000013,'G26','070307',3,'G26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000021,'G27 档案学、档案事业',70000013,'G27','070308',3,'G27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000022,'G3 科学、科学研究',70000001,'G3','0704',2,'G3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000023,'G30 科学研究理论',70000022,'G30','070401',3,'G30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000024,'G31 科学研究工作',70000022,'G31','070402',3,'G31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000025,'G32 世界各国科学研究事业',70000022,'G32','070403',3,'G32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000026,'G35 情报学、情报工作',70000022,'G35','070404',3,'G35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000027,'G4 教育',70000001,'G4','0705',2,'G4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000028,'G40 教育学',70000027,'G40','070501',3,'G40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000029,'G41 思想政治教育、德育',70000027,'G41','070502',3,'G41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000030,'G42 教学理论',70000027,'G42','070503',3,'G42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000031,'G43 电化教育',70000027,'G43','070504',3,'G43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000032,'G44 教育心理学',70000027,'G44','070505',3,'G44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000033,'G45 教师与学生',70000027,'G45','070506',3,'G45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000034,'G459 学校与家庭、学校与社会',70000027,'G459','070507',3,'G459');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000035,'G46 教育行政',70000027,'G46','070508',3,'G46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000036,'G47 学校管理',70000027,'G47','070509',3,'G47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000037,'G48 学校建筑和设备的管理',70000027,'G48','070510',3,'G48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000038,'{G5} 世界各国教育事业',70000027,'{G5}','070511',3,'G5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000039,'G51 世界教育事业',70000027,'G51','070512',3,'G51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000040,'G52 中国教育事业',70000027,'G52','070513',3,'G52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000041,'G53/57 各国教育事业',70000027,'G53/57','070514',3,'G53/57,G53,G54,G55,G56,G57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000042,'{G6} 各级教育',70000027,'{G6}','070515',3,'G6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000043,'G61 学前教育、幼儿教育',70000027,'G61','070516',3,'G61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000044,'G62 初等教育',70000027,'G62','070517',3,'G62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000045,'G63 中等教育',70000027,'G63','070518',3,'G63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000046,'G64 高等教育',70000027,'G64','070519',3,'G64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000047,'G65 师范教育',70000027,'G65','070520',3,'G65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000048,'{G7} 各类教育',70000027,'{G7}','070521',3,'G7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000049,'G71 职业技术教育',70000027,'G71','070522',3,'G71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000050,'G72 成人教育、业余教育',70000027,'G72','070523',3,'G72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000051,'G74 华侨教育、侨民教育',70000027,'G74','070524',3,'G74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000052,'G75 少数民族教育',70000027,'G75','070525',3,'G75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000053,'G76 特殊教育',70000027,'G76','070526',3,'G76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000054,'G77 社会教育',70000027,'G77','070527',3,'G77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000055,'G78 家庭教育',70000027,'G78','070528',3,'G78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000056,'G79 自学',70000027,'G79','070529',3,'G79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000057,'G8 体育',70000001,'G8','0706',2,'G8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000058,'G80 体育理论',70000057,'G80','070601',3,'G80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000059,'G81 世界各国体育事业',70000057,'G81','070602',3,'G81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000060,'G818 运动场地与设备',70000057,'G818','070603',3,'G818');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000061,'G819 体育运动技术（总论）',70000057,'G819','070604',3,'G819');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000062,'G82 田径运动',70000057,'G82','070605',3,'G82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000063,'G83 体操运动',70000057,'G83','070606',3,'G83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000064,'G84 球类运动',70000057,'G84','070607',3,'G84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000065,'G85 武术及民族形式体育',70000057,'G85','070608',3,'G85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000066,'G86 水上、冰上与雪上运动',70000057,'G86','070609',3,'G86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000067,'G87 其他体育运动',70000057,'G87','070610',3,'G87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(70000068,'G89 文体活动',70000057,'G89','070611',3,'G89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000001,'H 语言、文字',null,'H','08',1,'H');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000002,'H0 语言学',80000001,'H0','0801',2,'H0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000003,'H002 语言规划',80000002,'H002','080101',3,'H002');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000004,'H003 语言的分类',80000002,'H003','080102',3,'H003');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000005,'H004 语言的分布',80000002,'H004','080103',3,'H004');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000006,'H01 语音学',80000002,'H01','080104',3,'H01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000007,'H02 文字学',80000002,'H02','080105',3,'H02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000008,'H03 语义学、语用学、词汇学、词义学',80000002,'H03','080106',3,'H03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000009,'H04 语法学',80000002,'H04','080107',3,'H04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000010,'H05 写作学与修辞学',80000002,'H05','080108',3,'H05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000011,'H059 翻译学',80000002,'H059','080109',3,'H059');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000012,'H06 词典学',80000002,'H06','080110',3,'H06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000013,'H07 方言学',80000002,'H07','080111',3,'H07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000014,'H08 应用语言学',80000002,'H08','080112',3,'H08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000015,'H09 语文教学',80000002,'H09','080113',3,'H09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000016,'H1 汉语',80000001,'H1','0802',2,'H1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000017,'H11 语音',80000016,'H11','080201',3,'H11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000018,'H12 文字学',80000016,'H12','080202',3,'H12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000019,'H13 语义、词汇、词义（训诂学）',80000016,'H13','080203',3,'H13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000020,'H14 语法',80000016,'H14','080204',3,'H14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000021,'H15 写作、修辞',80000016,'H15','080205',3,'H15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000022,'H159 翻译',80000016,'H159','080206',3,'H159');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000023,'H16 字书、字典、词典',80000016,'H16','080207',3,'H16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000024,'H17 方言',80000016,'H17','080208',3,'H17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000025,'H19 汉语教学',80000016,'H19','080209',3,'H19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000026,'H2 中国少数民族语言',80000001,'H2','0803',2,'H2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000027,'H3 常用外国语',80000001,'H3','0804',2,'H3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000028,'H31 英语',80000027,'H31','080401',3,'H31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000029,'H311 语音',80000028,'H311','08040101',4,'H311');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000030,'H312 文字',80000028,'H312','08040102',4,'H312');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000031,'H313 语义、词汇、词义',80000028,'H313','08040103',4,'H313');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000032,'H314 语法',80000028,'H314','08040104',4,'H314');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000033,'H315 写作、修辞',80000028,'H315','08040105',4,'H315');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000034,'H315.9 翻译',80000028,'H315.9','08040106',4,'H3159');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000035,'H316 词典',80000028,'H316','08040107',4,'H316');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000036,'H317 方言',80000028,'H317','08040108',4,'H317');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000037,'H319 语文教学',80000028,'H319','08040109',4,'H319');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000038,'H32 法语',80000027,'H32','080402',3,'H32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000039,'H33 德语',80000027,'H33','080403',3,'H33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000040,'H34 西班牙语',80000027,'H34','080404',3,'H34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000041,'H35 俄语',80000027,'H35','080405',3,'H35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000042,'H36 日语',80000027,'H36','080406',3,'H36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000043,'H37 阿拉伯语',80000027,'H37','080407',3,'H37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000044,'H4 汉藏语系',80000001,'H4','0805',2,'H4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000045,'H41 壮侗语族（侗傣语族）',80000044,'H41','080501',3,'H41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000046,'H42 藏缅语族',80000044,'H42','080502',3,'H42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000047,'H43 苗瑶语族',80000044,'H43','080503',3,'H43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000048,'H44 越南语',80000044,'H44','080504',3,'H44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000049,'H5 阿尔泰语系（突厥-蒙古-通古斯语系）',80000001,'H5','0806',2,'H5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000050,'H51 突厥语族',80000049,'H51','080601',3,'H51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000051,'H53 蒙古语族',80000049,'H53','080602',3,'H53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000052,'H54 通古斯-满语族',80000049,'H54','080603',3,'H54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000053,'H55 朝鲜语',80000049,'H55','080604',3,'H55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000054,'H61 南亚语系（澳斯特罗-亚细亚语系）',80000001,'H61','0807',2,'H61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000055,'H611 扪达语族',80000054,'H611','080701',3,'H611');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000056,'H612 孟语',80000054,'H612','080702',3,'H612');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000057,'H613 高棉语（柬埔寨国语）',80000054,'H613','080703',3,'H613');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000058,'H614 占语',80000054,'H614','080704',3,'H614');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000059,'[H616] 老挝语',80000054,'[H616]','080705',3,'H616');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000060,'[H617] 泰语（暹罗）',80000054,'[H617]','080706',3,'H617');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000061,'[H618] 越南语',80000054,'[H618]','080707',3,'H618');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000062,'H62 南印语系（达罗毗荼语系、德拉维达语系）',80000001,'H62','0808',2,'H62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000063,'H621 泰卢固语',80000062,'H621','080801',3,'H621');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000064,'H622 泰米尔语',80000062,'H622','080802',3,'H622');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000065,'H623 马拉雅兰语',80000062,'H623','080803',3,'H623');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000066,'H624 卡那拉语',80000062,'H624','080804',3,'H624');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000067,'H625 库伊语',80000062,'H625','080805',3,'H625');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000068,'H626 贡德语',80000062,'H626','080806',3,'H626');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000069,'H627 布拉呼语',80000062,'H627','080807',3,'H627');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000070,'H628 图鲁语',80000062,'H628','080808',3,'H628');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000071,'H629 其他语言',80000062,'H629','080809',3,'H629');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000072,'H63 南岛语系（马来亚-玻里尼西亚语系）',80000001,'H63','0809',2,'H63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000073,'H631 印度尼西亚语族',80000072,'H631','080901',3,'H631');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000074,'H632 密克罗尼西亚语族',80000072,'H632','080902',3,'H632');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000075,'H633 美拉尼西亚语族',80000072,'H633','080903',3,'H633');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000076,'H634 玻里尼西亚语族',80000072,'H634','080904',3,'H634');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000077,'H635 巴布亚诸语言',80000072,'H635','080905',3,'H635');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000078,'H636 安达曼语',80000072,'H636','080906',3,'H636');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000079,'H64 东北亚诸语言',80000001,'H64','0810',2,'H64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000080,'H641 楚克奇语（罗拉维特兰语）',80000079,'H641','081001',3,'H641');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000081,'H642 内梅兰语（科里亚克语）',80000079,'H642','081002',3,'H642');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000082,'H643 伊杰耳缅语（堪察加语）',80000079,'H643','081003',3,'H643');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000083,'H644 开特语（叶尼塞-奥斯加克语）',80000079,'H644','081004',3,'H644');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000084,'H645 奥杜尔（犹卡吉尔语）',80000079,'H645','081005',3,'H645');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000085,'H646 尼夫赫语（吉里雅克语）',80000079,'H646','081006',3,'H646');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000086,'H647 阿伊努语（虾夷语）',80000079,'H647','081007',3,'H647');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000087,'H648 琉球语',80000079,'H648','081008',3,'H648');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000088,'H65 高加索语系（伊比利亚-高加索语系）',80000001,'H65','0811',2,'H65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000089,'H651 卡尔特维里语族（伊比利亚语族）',80000088,'H651','081101',3,'H651');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000090,'H652 阿布哈兹-阿第盖语族',80000088,'H652','081102',3,'H652');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000091,'H653 巴茨比-基斯金语族',80000088,'H653','081103',3,'H653');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000092,'H654 列兹金语族',80000088,'H654','081104',3,'H654');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000093,'H655 达格斯坦语族',80000088,'H655','081105',3,'H655');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000094,'H659 巴斯克语',80000088,'H659','081106',3,'H659');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000095,'H66 乌拉尔语系（芬兰-乌戈尔语系）',80000001,'H66','0812',2,'H66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000096,'H661 芬兰语族',80000095,'H661','081201',3,'H661');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000097,'H662 乌拉尔语族（乌戈尔语族）',80000095,'H662','081202',3,'H662');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000098,'H67 闪-含语系（阿非罗-亚细亚语系）',80000001,'H67','0813',2,'H67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000099,'H671 闪语族',80000098,'H671','081301',3,'H671');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000100,'H672 柏柏尔语族',80000098,'H672','081302',3,'H672');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000101,'H673 埃及-科普特语族',80000098,'H673','081303',3,'H673');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000102,'H674 库希特语族',80000098,'H674','081304',3,'H674');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000103,'H675 乍得语族',80000098,'H675','081305',3,'H675');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000104,'H7 印欧语系',80000001,'H7','0814',2,'H7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000105,'H71 印度语族',80000104,'H71','081401',3,'H71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000106,'H73 伊朗语族',80000104,'H73','081402',3,'H73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000107,'H74 斯拉夫语族',80000104,'H74','081403',3,'H74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000108,'H75 波罗的语族',80000104,'H75','081404',3,'H75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000109,'H76 日耳曼语族',80000104,'H76','081405',3,'H76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000110,'H77 罗马语族',80000104,'H77','081406',3,'H77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000111,'H78 凯尔特语族',80000104,'H78','081407',3,'H78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000112,'H791 希腊语族',80000104,'H791','081408',3,'H791');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000113,'H792 阿尔巴尼亚语族：阿尔巴尼亚语',80000104,'H792','081409',3,'H792');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000114,'H793 亚美尼亚语族：亚美尼亚语',80000104,'H793','081410',3,'H793');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000115,'H794 安纳托利亚语族',80000104,'H794','081411',3,'H794');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000116,'H795 吐火罗语族',80000104,'H795','081412',3,'H795');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000117,'H81 非洲诸语言',80000001,'H81','0815',2,'H81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000118,'H811 沙里-尼罗语系',80000117,'H811','081501',3,'H811');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000119,'H815 尼日尔-刚果语系',80000117,'H815','081502',3,'H815');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000120,'H824 布斯曼-霍登托（考伊散）语系',80000117,'H824','081503',3,'H824');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000121,'H83 美洲诸语言',80000001,'H83','0816',2,'H83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000122,'H831 那华特耳语、奎车语、卡克契阔耳语、曼姆语、犹卡特克语、刻克齐语 、奥托米语、乍泼特克语、密希特克语、托托那克语（墨西哥、危地马拉境内语言）',80000121,'H831','081601',3,'H831');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000123,'H832 图皮-瓜拉尼语系（巴拉圭、巴西西南部）',80000121,'H832','081602',3,'H832');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000124,'H833 革拉耳语（巴西亚马逊河流域）',80000121,'H833','081603',3,'H833');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000125,'H834 奎出瓦语（秘鲁、厄瓜多尔、玻利维亚）',80000121,'H834','081604',3,'H834');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000126,'H835 埃马拉语（秘鲁、玻利维亚）',80000121,'H835','081605',3,'H835');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000127,'H836 那瓦荷语（美国西南部）',80000121,'H836','081606',3,'H836');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000128,'H839 爱斯基摩-阿留申语系（北美洲极北部）',80000121,'H839','081607',3,'H839');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000129,'H84 大洋洲诸语言',80000001,'H84','0817',2,'H84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000130,'H9 国际辅助语',80000001,'H9','0818',2,'H9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000131,'H91 世界语',80000130,'H91','081801',3,'H91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000132,'H92 沃拉布克国际语',80000130,'H92','081802',3,'H92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000133,'H93 阿克奇顿道尔国际语',80000130,'H93','081803',3,'H93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000134,'H94 艾多国际语',80000130,'H94','081804',3,'H94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(80000135,'H95 国际语',80000130,'H95','081805',3,'H95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000001,'I 文学',null,'I','09',1,'I');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000002,'I0 文学理论',90000001,'I0','0901',2,'I0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000003,'I0-02 文学的哲学基础',90000002,'I0-02','090101',3,'I0-02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000004,'I0-03 文学的方法论',90000002,'I0-03','090102',3,'I0-03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000005,'I0-05 文学与其他科学的关系',90000002,'I0-05','090103',3,'I0-05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000006,'I01 文艺美学',90000002,'I01','090104',3,'I01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000007,'I02 文学理论的基本问题',90000002,'I02','090105',3,'I02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000008,'I021 文学的民族化、大众化',90000007,'I021','09010501',4,'I021');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000009,'I022 文学与现实生活',90000007,'I022','09010502',4,'I022');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000010,'I023 文学的普及与提高',90000007,'I023','09010503',4,'I023');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000011,'I024 内容与形式',90000007,'I024','09010504',4,'I024');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000012,'I025 典型与形象',90000007,'I025','09010505',4,'I025');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000013,'I026 文学遗产的批判继承',90000007,'I026','09010506',4,'I026');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000014,'I03 文艺工作者',90000002,'I03','090106',3,'I03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000015,'I04 文学创作论',90000002,'I04','090107',3,'I04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000016,'I041 题材、主题思想',90000015,'I041','09010701',4,'I041');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000017,'I042 人物形象',90000015,'I042','09010702',4,'I042');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000018,'I043 结构、情节',90000015,'I043','09010703',4,'I043');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000019,'I044 风格、技巧',90000015,'I044','09010704',4,'I044');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000020,'I045 文学语言',90000015,'I045','09010705',4,'I045');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000021,'I046 文学翻译',90000015,'I046','09010706',4,'I046');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000022,'I05 各体文学理论和创作方法',90000002,'I05','090108',3,'I05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000023,'I052 诗歌',90000022,'I052','09010801',4,'I052');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000024,'I053 戏剧文学',90000022,'I053','09010802',4,'I053');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000025,'I054 小说',90000022,'I054','09010803',4,'I054');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000026,'I055 报告文学、史传文学',90000022,'I055','09010804',4,'I055');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000027,'I056 散文、杂著',90000022,'I056','09010805',4,'I056');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000028,'I057 民间文学',90000022,'I057','09010806',4,'I057');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000029,'I058 儿童文学',90000022,'I058','09010807',4,'I058');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000030,'I059 民族文学',90000022,'I059','09010808',4,'I059');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000031,'I059.9 宗教文学',90000022,'I059.9','09010809',4,'I0599');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000032,'I06 文学评论、文学欣赏',90000002,'I06','090109',3,'I06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000033,'I1 世界文学',90000001,'I1','0902',2,'I1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000034,'I106 作品评论和研究',90000033,'I106','090201',3,'I106');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000035,'I106.2 诗歌',90000034,'I106.2','09020101',4,'I1062');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000036,'I106.3 戏剧文学',90000034,'I106.3','09020102',4,'I1063');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000037,'I106.4 小说',90000034,'I106.4','09020103',4,'I1064');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000038,'I106.5 报告文学',90000034,'I106.5','09020104',4,'I1065');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000039,'I106.6 散文、杂著',90000034,'I106.6','09020105',4,'I1066');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000040,'I106.7 民间文学',90000034,'I106.7','09020106',4,'I1067');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000041,'I106.8 儿童文学',90000034,'I106.8','09020107',4,'I1068');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000042,'I106.9 民族文学',90000034,'I106.9','09020108',4,'I1069');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000043,'I106.99 宗教文学',90000034,'I106.99','09020109',4,'I10699');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000044,'I109 文学史、文学思想史',90000033,'I109','090202',3,'I109');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000045,'I109.2 古代（公元前约3500年～公元476年）',90000044,'I109.2','09020201',4,'I1092');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000046,'I109.3 中世纪（476～1640年）',90000044,'I109.3','09020202',4,'I1093');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000047,'I109.4 近代（1640～1917年）',90000044,'I109.4','09020203',4,'I1094');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000048,'I109.5 现代（1917年～ ）',90000044,'I109.5','09020204',4,'I1095');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000049,'I109.9 文学流派及其研究',90000044,'I109.9','09020205',4,'I1099');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000050,'I11 作品集',90000033,'I11','090203',3,'I11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000051,'I12 诗歌集',90000050,'I12','09020301',4,'I12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000052,'I13 戏剧文学集',90000050,'I13','09020302',4,'I13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000053,'I14 小说集',90000050,'I14','09020303',4,'I14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000054,'I15 报告文学集',90000050,'I15','09020304',4,'I15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000055,'I16 散文集、杂著集',90000050,'I16','09020305',4,'I16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000056,'I17 民间文学集',90000050,'I17','09020306',4,'I17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000057,'I18 儿童文学集',90000050,'I18','09020307',4,'I18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000058,'I19 民族文学集',90000050,'I19','09020308',4,'I19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000059,'I199 宗教文学集',90000050,'I199','09020309',4,'I199');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000060,'I2 中国文学',90000001,'I2','0903',2,'I2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000061,'I200 方针政策及其阐述',90000060,'I200','090301',3,'I200');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000062,'I206 文学评论和研究',90000060,'I206','090302',3,'I206');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000063,'I206.09 文学批评史',90000062,'I206.09','09030201',4,'I20609');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000064,'I206.2 古代文学（ ～1840年）',90000062,'I206.2','09030202',4,'I2062');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000065,'I206.5 近代文学（1840～1919年）',90000062,'I206.5','09030203',4,'I2065');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000066,'I206.6 现代文学（1919～1949年）',90000062,'I206.6','09030204',4,'I2066');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000067,'I206.7 当代文学（1949年～ ）',90000062,'I206.7','09030205',4,'I2067');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000068,'I207 各体文学评论和研究',90000060,'I207','090303',3,'I207');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000069,'I207.2 诗歌、韵文',90000068,'I207.2','09030301',4,'I2072');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000070,'I207.3 戏剧文学',90000068,'I207.3','09030302',4,'I2073');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000071,'I207.39 曲艺',90000068,'I207.39','09030303',4,'I20739');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000072,'I207.4 小说',90000068,'I207.4','09030304',4,'I2074');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000073,'I207.5 报告文学',90000068,'I207.5','09030305',4,'I2075');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000074,'I207.6 散文、杂著',90000068,'I207.6','09030306',4,'I2076');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000075,'I207.7 民间文学',90000068,'I207.7','09030307',4,'I2077');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000076,'I207.8 儿童文学',90000068,'I207.8','09030308',4,'I2078');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000077,'I207.9 少数民族文学',90000068,'I207.9','09030309',4,'I2079');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000078,'I207.99 宗教文学',90000068,'I207.99','09030310',4,'I20799');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000079,'I209 文学史、文学思想史',90000060,'I209','090304',3,'I209');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000080,'I209.9 地方文学史',90000079,'I209.9','09030401',4,'I2099');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000081,'I21 作品集',90000060,'I21','090305',3,'I21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000082,'I210 鲁迅著作及研究',90000081,'I210','09030501',4,'I210');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000083,'I211 作品综合集',90000081,'I211','09030502',4,'I211');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000084,'I212/217 各时代作品集',90000081,'I212/217','09030503',4,'I212/217,I212,I213,I214,I215,I216,I217');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000085,'I218 地方作品综合集',90000081,'I218','09030504',4,'I218');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000086,'I219 群众文艺演唱材料',90000081,'I219','09030505',4,'I219');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000087,'I22 诗歌、韵文',90000060,'I22','090306',3,'I22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000088,'I222 古代至近代作品（ ～1919年）',90000087,'I222','09030601',4,'I222');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000089,'I226 现代作品（1919～1949年）',90000087,'I226','09030602',4,'I226');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000090,'I227 当代作品（1949年～ ）',90000087,'I227','09030603',4,'I227');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000091,'I23 戏剧文学',90000060,'I23','090307',3,'I23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000092,'I230 综合集',90000091,'I230','09030701',4,'I230');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000093,'I232 京剧',90000091,'I232','09030702',4,'I232');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000094,'I233 歌剧、歌舞剧',90000091,'I233','09030703',4,'I233');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000095,'I234 话剧',90000091,'I234','09030704',4,'I234');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000096,'I235 电影、电视、广播剧',90000091,'I235','09030705',4,'I235');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000097,'I236 地方剧',90000091,'I236','09030706',4,'I236');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000098,'I237 古代戏曲',90000091,'I237','09030707',4,'I237');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000099,'I238 其他剧种',90000091,'I238','09030708',4,'I238');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000100,'I239 曲艺',90000060,'I239','090308',3,'I239');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000101,'I239.1 弹词',90000100,'I239.1','09030801',4,'I2391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000102,'I239.2 鼓词',90000100,'I239.2','09030802',4,'I2392');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000103,'I239.3 河南坠子、道情、渔鼓',90000100,'I239.3','09030803',4,'I2393');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000104,'I239.4 琴书',90000100,'I239.4','09030804',4,'I2394');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000105,'I239.5 曲词',90000100,'I239.5','09030805',4,'I2395');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000106,'I239.6 快书、快板',90000100,'I239.6','09030806',4,'I2396');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000107,'I239.7 相声、独脚戏',90000100,'I239.7','09030807',4,'I2397');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000108,'I239.8 评书',90000100,'I239.8','09030808',4,'I2398');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000109,'I239.9 其他',90000100,'I239.9','09030809',4,'I2399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000110,'I24 小说',90000060,'I24','090309',3,'I24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000111,'I242 古代至近代作品（ ～1919年）',90000110,'I242','09030901',4,'I242');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000112,'I246 现代作品（1919～1949年）',90000110,'I246','09030902',4,'I246');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000113,'I247 当代作品（1949年～ ）',90000110,'I247','09030903',4,'I247');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000114,'I25 报告文学',90000060,'I25','090310',3,'I25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000115,'I251 回忆录',90000114,'I251','09031001',4,'I251');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000116,'I252 企业史、村史、家史',90000114,'I252','09031002',4,'I252');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000117,'I253 通讯、特写、专题报道',90000114,'I253','09031003',4,'I253');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000118,'I26 散文',90000060,'I26','090311',3,'I26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000119,'I262/265 古代至近代作品（ ～1919年）',90000118,'I262/265','09031101',4,'I262/265,I262,I263,I264,I265');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000120,'I266 现代作品（1919～1949年）',90000118,'I266','09031102',4,'I266');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000121,'I267 当代作品（1949年～ ）',90000118,'I267','09031103',4,'I267');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000122,'I269 杂著',90000060,'I269','090312',3,'I269');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000123,'I269.6 古代至现代作品（ ～1949年）',90000122,'I269.6','09031201',4,'I2696');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000124,'I269.7 当代作品（1949年～ ）',90000122,'I269.7','09031202',4,'I2697');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000125,'I27 民间文学',90000060,'I27','090313',3,'I27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000126,'I276 古代至现代作品（ ～1949年）',90000125,'I276','09031301',4,'I276');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000127,'I277 当代作品（1949年～ ）',90000125,'I277','09031302',4,'I277');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000128,'I28 儿童文学',90000060,'I28','090314',3,'I28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000129,'I286 古代至现代作品（ ～1949年）',90000128,'I286','09031401',4,'I286');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000130,'I287 当代作品（1949年～ ）',90000128,'I287','09031402',4,'I287');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000131,'I29 少数民族文学',90000060,'I29','090315',3,'I29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000132,'I299 宗教文学',90000060,'I299','090316',3,'I299');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(90000133,'I3/7 各国文学',90000001,'I3/7','0904',2,'I3/7,I3,I4,I5,I6,I7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000001,'J 艺术',null,'J','10',1,'J');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000002,'J0 艺术理论',100000001,'J0','1001',2,'J0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000003,'J0-02 艺术的哲学基础',100000002,'J0-02','100101',3,'J0-02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000004,'J0-03 艺术的方法论',100000002,'J0-03','100102',3,'J0-03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000005,'J0-05 艺术与其他科学的关系',100000002,'J0-05','100103',3,'J0-05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000006,'J01 艺术美学',100000002,'J01','100104',3,'J01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000007,'J02 艺术理论的基本问题',100000002,'J02','100105',3,'J02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000008,'J03 艺术工作者',100000002,'J03','100106',3,'J03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000009,'J04 艺术创作方法',100000002,'J04','100107',3,'J04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000010,'J05 艺术评论、欣赏',100000002,'J05','100108',3,'J05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000011,'J06 造型艺术理论',100000002,'J06','100109',3,'J06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000012,'J08 艺术技法',100000002,'J08','100110',3,'J08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000013,'J1 世界各国艺术概况',100000001,'J1','1002',2,'J1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000014,'J11 世界艺术',100000013,'J11','100201',3,'J11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000015,'J12 中国艺术',100000013,'J12','100202',3,'J12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000016,'J13/17 各国艺术',100000013,'J13/17','100203',3,'J13/17,J13,J14,J15,J16,J17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000017,'[J18] 美术考古',100000013,'[J18]','100204',3,'J18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000018,'J19 宗教艺术',100000013,'J19','100205',3,'J19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000019,'J2 绘画',100000001,'J2','1003',2,'J2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000020,'J20 绘画理论',100000019,'J20','100301',3,'J20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000021,'J21 绘画技法',100000019,'J21','100302',3,'J21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000022,'J22 中国绘画作品',100000019,'J22','100303',3,'J22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000023,'J23 各国绘画作品',100000019,'J23','100304',3,'J23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000024,'J29 书法、篆刻',100000001,'J29','1004',2,'J29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000025,'J292 中国书法、篆刻',100000024,'J292','100401',3,'J292');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000026,'J293 外文书法',100000024,'J293','100402',3,'J293');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000027,'J3 雕塑',100000001,'J3','1005',2,'J3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000028,'J30 雕塑理论',100000027,'J30','100501',3,'J30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000029,'J31 雕塑技法',100000027,'J31','100502',3,'J31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000030,'J32 中国雕塑作品',100000027,'J32','100503',3,'J32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000031,'J33 各国雕塑作品',100000027,'J33','100504',3,'J33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000032,'J4 摄影艺术',100000001,'J4','1006',2,'J4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000033,'J40 摄影艺术理论',100000032,'J40','100601',3,'J40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000034,'J41 各种摄影艺术',100000032,'J41','100602',3,'J41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000035,'J42 中国摄影艺术作品',100000032,'J42','100603',3,'J42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000036,'J43 世界各国摄影艺术',100000032,'J43','100604',3,'J43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000037,'J5 工艺美术',100000001,'J5','1007',2,'J5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000038,'J50 工艺美术理论',100000037,'J50','100701',3,'J50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000039,'J51 图案学',100000037,'J51','100702',3,'J51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000040,'J52 中国工艺美术',100000037,'J52','100703',3,'J52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000041,'J53 各国工艺美术',100000037,'J53','100704',3,'J53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000042,'[J59] 建筑艺术',100000001,'[J59]','1008',2,'J59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000043,'J6 音乐',100000001,'J6','1009',2,'J6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000044,'J60 音乐理论',100000043,'J60','100901',3,'J60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000045,'J61 音乐技术理论与方法',100000043,'J61','100902',3,'J61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000046,'J62 器乐理论与演奏法',100000043,'J62','100903',3,'J62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000047,'J63 民族器乐理论和演奏法',100000043,'J63','100904',3,'J63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000048,'J639 其他',100000043,'J639','100905',3,'J639');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000049,'J64 中国音乐作品',100000043,'J64','100906',3,'J64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000050,'J65 各国音乐作品',100000043,'J65','100907',3,'J65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000051,'J69 音乐事业',100000043,'J69','100908',3,'J69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000052,'J7 舞蹈',100000001,'J7','1010',2,'J7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000053,'J70 舞蹈理论',100000052,'J70','101001',3,'J70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000054,'J71 舞蹈技术和方法',100000052,'J71','101002',3,'J71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000055,'J72 中国舞蹈、舞剧',100000052,'J72','101003',3,'J72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000056,'J73 各国舞蹈、舞剧',100000052,'J73','101004',3,'J73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000057,'J79 舞蹈事业',100000052,'J79','101005',3,'J79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000058,'J8 戏剧艺术',100000001,'J8','1011',2,'J8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000059,'J80 戏剧艺术理论',100000058,'J80','101101',3,'J80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000060,'J81 舞台艺术',100000058,'J81','101102',3,'J81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000061,'J82 中国戏剧艺术',100000058,'J82','101103',3,'J82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000062,'J83 各国戏剧艺术',100000058,'J83','101104',3,'J83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000063,'J89 戏剧事业',100000058,'J89','101105',3,'J89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000064,'J9 电影、电视艺术',100000001,'J9','1012',2,'J9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000065,'J90 电影、电视艺术理论',100000064,'J90','101201',3,'J90');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000066,'J91 电影、电视艺术与技术',100000064,'J91','101202',3,'J91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000067,'J92 电影、电视分镜头',100000064,'J92','101203',3,'J92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000068,'J93 电影、电视拍摄艺术与技术',100000064,'J93','101204',3,'J93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000069,'J94 电影、电视企业组织与管理',100000064,'J94','101205',3,'J94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000070,'J95 各种电影、电视：按内容分',100000064,'J95','101206',3,'J95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000071,'J96 各种电影、电视：按表现形式和技术分',100000064,'J96','101207',3,'J96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000072,'J97 各种电影、电视：按题材分',100000064,'J97','101208',3,'J97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000073,'J98 幻灯',100000064,'J98','101209',3,'J98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(100000074,'J99 电影、电视事业',100000064,'J99','101210',3,'J99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000001,'K 历史、地理',null,'K','11',1,'K');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000002,'K0 史学理论',110000001,'K0','1101',2,'K0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000003,'K01 史学的哲学基础',110000002,'K01','110101',3,'K01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000004,'K02 社会发展理论',110000002,'K02','110102',3,'K02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000005,'K03 史学专论',110000002,'K03','110103',3,'K03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000006,'K04 年代学',110000002,'K04','110104',3,'K04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000007,'K05 史料学',110000002,'K05','110105',3,'K05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000008,'K06 历史研究',110000002,'K06','110106',3,'K06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000009,'K061 历史研究法',110000008,'K061','11010601',4,'K061');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000010,'K062 历史写法',110000008,'K062','11010602',4,'K062');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000011,'K063 历史学习法',110000008,'K063','11010603',4,'K063');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000012,'K09 史学史',110000002,'K09','110107',3,'K09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000013,'K091 世界',110000012,'K091','11010701',4,'K091');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000014,'K092 中国',110000012,'K092','11010702',4,'K092');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000015,'K093/097 各国',110000012,'K093/097','11010703',4,'K093/097,K93,K94,K95,K96,K97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000016,'K1 世界史',110000001,'K1','1102',2,'K1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000017,'K10 通史',110000016,'K10','110201',3,'K10');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000018,'K101 革命史',110000017,'K101','11020101',4,'K101');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000019,'K103 文化史',110000017,'K103','11020102',4,'K103');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000020,'K104 杂史、史钞',110000017,'K104','11020103',4,'K104');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000021,'K105 历史事件',110000017,'K105','11020104',4,'K105');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000022,'K106 史料',110000017,'K106','11020105',4,'K106');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000023,'K107 研究、考订、评论',110000017,'K107','11020106',4,'K107');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000024,'K108 年表',110000017,'K108','11020107',4,'K108');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000025,'K109 普及读物',110000017,'K109','11020108',4,'K109');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000026,'K11 上古史（公元前40世纪以前）',110000016,'K11','110202',3,'K11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000027,'K12 古代史（公元前40世纪～公元476年）',110000016,'K12','110203',3,'K12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000028,'K124 古代东方',110000027,'K124','11020301',4,'K124');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000029,'K125 古代希腊',110000027,'K125','11020302',4,'K125');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000030,'K126 古代罗马',110000027,'K126','11020303',4,'K126');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000031,'K13 中世纪史（476～1640年）',110000016,'K13','110204',3,'K13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000032,'K131 早期封建社会（5～10世纪）',110000031,'K131','11020401',4,'K131');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000033,'K132 中期封建社会（10～15世纪）',110000031,'K132','11020402',4,'K132');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000034,'K133 后期封建社会（15～17世纪上半叶）',110000031,'K133','11020403',4,'K133');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000035,'K134 拜占庭帝国（477～1453年）',110000031,'K134','11020404',4,'K134');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000036,'K135 阿拉伯帝国（632～1094年）',110000031,'K135','11020405',4,'K135');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000037,'K14 近代史（1640～1917年）',110000016,'K14','110205',3,'K14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000038,'K141 英国资产阶级革命至巴黎公社前夕（1640～1870年）',110000037,'K141','11020501',4,'K141');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000039,'K142 巴黎公社至苏联十月社会主义革命前夕（1871～1917年）',110000037,'K142','11020502',4,'K142');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000040,'K143 第一次世界大战（1914～1918年）',110000037,'K143','11020503',4,'K143');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000041,'K15 现代史（1917年～ ）',110000016,'K15','110206',3,'K15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000042,'K151 苏联十月社会主义革命至第二次世界大战前夕（1917～1939年）',110000041,'K151','11020601',4,'K151');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000043,'K152 第二次世界大战（1939～1945年）',110000041,'K152','11020602',4,'K152');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000044,'K153 第二次世界大战以后（1945年～ ）',110000041,'K153','11020603',4,'K153');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000045,'K18 民族史志',110000016,'K18','110207',3,'K18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000046,'K2 中国史',110000001,'K2','1103',2,'K2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000047,'K20 通史',110000046,'K20','110301',3,'K20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000048,'K201 革命史',110000047,'K201','11030101',4,'K201');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000049,'K203 文化史',110000047,'K203','11030102',4,'K203');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000050,'K204 古代史籍',110000047,'K204','11030103',4,'K204');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000051,'K205 历史事件',110000047,'K205','11030104',4,'K205');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000052,'K206 史料',110000047,'K206','11030105',4,'K206');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000053,'K207 研究、考订、评论',110000047,'K207','11030106',4,'K207');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000054,'K208 年表',110000047,'K208','11030107',4,'K208');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000055,'K209 普及读物',110000047,'K209','11030108',4,'K209');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000056,'K21 原始社会（约60万年前～4000多年前）',110000046,'K21','110302',3,'K21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000057,'K22 奴隶社会（约公元前21世纪～公元前475年）',110000046,'K22','110303',3,'K22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000058,'K221 三代（公元前21世纪～公元前770年）',110000057,'K221','11030301',4,'K221');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000059,'K225 春秋（公元前770年～公元前475年）',110000057,'K225','11030302',4,'K225');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000060,'K23 封建社会（公元前475～公元1840年）',110000046,'K23','110304',3,'K23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000061,'K231 战国（公元前475～公元前221年）',110000060,'K231','11030401',4,'K231');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000062,'K232 秦、汉（公元前221～公元220年）',110000060,'K232','11030402',4,'K232');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000063,'K235 三国、晋、南北朝（220～589年）',110000060,'K235','11030403',4,'K235');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000064,'K24 隋、唐至清前期（581～1840年）',110000060,'K24','11030404',4,'K24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000065,'K25 半殖民地、半封建社会（1840～1949年）',110000046,'K25','110305',3,'K25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000066,'K251 旧民主主义革命时期（1840～1919年）',110000065,'K251','11030501',4,'K251');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000067,'K26 新民主主义革命时期（1919～1949年）',110000065,'K26','11030502',4,'K26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000068,'K27 中华人民共和国时期（1949年～ ）',110000046,'K27','110306',3,'K27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000069,'K28 民族史志',110000046,'K28','110307',3,'K28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000070,'K280.0 各代民族总志',110000069,'K280.0','11030701',4,'K2800');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000071,'K280.1/.7 各省区民族总志',110000069,'K280.1/.7','11030702',4,'K2801/7,K2801,K2802,K2803,K2804,K2805,K2806,K2807');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000072,'K281/288 各民族史志',110000069,'K281/288','11030703',4,'K281/288,K281,K282,K283,K284,K285,K286,K287,K288');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000073,'K289 古代民族史志',110000069,'K289','11030704',4,'K289');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000074,'K29 地方史志',110000046,'K29','110308',3,'K29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000075,'K290 方志学',110000074,'K290','11030801',4,'K290');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000076,'K290.1/.7 各代总志',110000074,'K290.1/.7','11030802',4,'K2901/7,K2901,K2902,K2903,K2904,K2905,K2906,K2907');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000077,'K291/297 各省、市区史志',110000074,'K291/297','11030803',4,'K291/297,K291,K292,K293,K294,K295,K296,K297');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000078,'K3 亚洲史',110000001,'K3','1104',2,'K3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000079,'K300 通史',110000078,'K300','110401',3,'K300');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000080,'K301 上古史',110000078,'K301','110402',3,'K301');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000081,'K302 古代史',110000078,'K302','110403',3,'K302');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000082,'K303 中世纪史',110000078,'K303','110404',3,'K303');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000083,'K304 近代史',110000078,'K304','110405',3,'K304');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000084,'K305 现代史',110000078,'K305','110406',3,'K305');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000085,'K308 民族史志',110000078,'K308','110407',3,'K308');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000086,'K31 东亚',110000078,'K31','110408',3,'K31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000087,'K311 蒙古人民共和国',110000086,'K311','11040801',4,'K311');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000088,'K312 朝鲜',110000086,'K312','11040802',4,'K312');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000089,'K313 日本',110000086,'K313','11040803',4,'K313');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000090,'K33 东南亚',110000078,'K33','110409',3,'K33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000091,'K333 越南',110000090,'K333','11040901',4,'K333');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000092,'K334 老挝',110000090,'K334','11040902',4,'K334');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000093,'K335 柬埔寨',110000090,'K335','11040903',4,'K335');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000094,'K336 泰国（暹罗）',110000090,'K336','11040904',4,'K336');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000095,'K337 缅甸',110000090,'K337','11040905',4,'K337');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000096,'K338 马来西亚',110000090,'K338','11040906',4,'K338');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000097,'K339 新加坡',110000090,'K339','11040907',4,'K339');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000098,'K341 菲律宾',110000090,'K341','11040908',4,'K341');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000099,'K342 印度尼西亚',110000090,'K342','11040909',4,'K342');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000100,'K344 文莱',110000090,'K344','11040910',4,'K344');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000101,'K346 东帝汶',110000090,'K346','11040911',4,'K346');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000102,'K35 南亚',110000078,'K35','110410',3,'K35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000103,'K351 印度',110000102,'K351','11041001',4,'K351');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000104,'K353 巴基斯坦',110000102,'K353','11041002',4,'K353');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000105,'K354 孟加拉国',110000102,'K354','11041003',4,'K354');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000106,'K355 尼泊尔',110000102,'K355','11041004',4,'K355');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000107,'K356 锡金',110000102,'K356','11041005',4,'K356');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000108,'K357 不丹',110000102,'K357','11041006',4,'K357');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000109,'K358 斯里兰卡（锡兰）',110000102,'K358','11041007',4,'K358');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000110,'K359 马尔代夫',110000102,'K359','11041008',4,'K359');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000111,'K36 中亚',110000078,'K36','110411',3,'K36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000112,'K361 哈萨克斯坦',110000111,'K361','11041101',4,'K361');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000113,'K362 乌兹别克斯坦',110000111,'K362','11041102',4,'K362');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000114,'K363 土库曼斯坦',110000111,'K363','11041103',4,'K363');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000115,'K364 吉尔吉斯斯坦',110000111,'K364','11041104',4,'K364');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000116,'K365 塔吉克斯坦',110000111,'K365','11041105',4,'K365');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000117,'K367 格鲁吉亚',110000111,'K367','11041106',4,'K367');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000118,'K368 阿塞拜疆',110000111,'K368','11041107',4,'K368');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000119,'K369 亚美尼亚',110000111,'K369','11041108',4,'K369');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000120,'K37 西亚（西南亚）',110000078,'K37','110412',3,'K37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000121,'K372 阿富汗',110000120,'K372','11041201',4,'K372');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000122,'K373 伊朗（波斯）',110000120,'K373','11041202',4,'K373');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000123,'K374 土耳其',110000120,'K374','11041203',4,'K374');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000124,'K375 塞浦路斯',110000120,'K375','11041204',4,'K375');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000125,'K376 叙利亚',110000120,'K376','11041205',4,'K376');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000126,'K377 伊拉克',110000120,'K377','11041206',4,'K377');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000127,'K378 黎巴嫩',110000120,'K378','11041207',4,'K378');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000128,'K379 约旦',110000120,'K379','11041208',4,'K379');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000129,'K381 巴勒斯坦',110000120,'K381','11041209',4,'K381');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000130,'K382 以色列',110000120,'K382','11041210',4,'K382');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000131,'K383 科威特',110000120,'K383','11041211',4,'K383');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000132,'K384 沙特阿拉伯',110000120,'K384','11041212',4,'K384');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000133,'K385 卡塔尔',110000120,'K385','11041213',4,'K385');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000134,'K386 巴林',110000120,'K386','11041214',4,'K386');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000135,'K387 阿拉伯联合酋长国',110000120,'K387','11041215',4,'K387');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000136,'K388 阿曼',110000120,'K388','11041216',4,'K388');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000137,'K391 也门民主共和国（1970～1990年）',110000120,'K391','11041217',4,'K391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000138,'K392 阿拉伯也门共和国（南也门）（1962～1990年）',110000120,'K392','11041218',4,'K392');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000139,'K393 也门',110000120,'K393','11041219',4,'K393');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000140,'K4 非洲史',110000001,'K4','1105',2,'K4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000141,'K400 通史',110000140,'K400','110501',3,'K400');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000142,'K401 上古史',110000140,'K401','110502',3,'K401');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000143,'K402 古代史',110000140,'K402','110503',3,'K402');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000144,'K403 中世纪史',110000140,'K403','110504',3,'K403');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000145,'K404 近代史',110000140,'K404','110505',3,'K404');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000146,'K405 现代史',110000140,'K405','110506',3,'K405');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000147,'K408 民族史志',110000140,'K408','110507',3,'K408');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000148,'K41 北非',110000140,'K41','110508',3,'K41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000149,'K411 埃及',110000148,'K411','11050801',4,'K411');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000150,'K412 苏丹',110000148,'K412','11050802',4,'K412');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000151,'K413 利比亚',110000148,'K413','11050803',4,'K413');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000152,'K414 突尼斯',110000148,'K414','11050804',4,'K414');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000153,'K415 阿尔及利亚',110000148,'K415','11050805',4,'K415');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000154,'K416 摩洛哥',110000148,'K416','11050806',4,'K416');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000155,'K42 东非',110000140,'K42','110509',3,'K42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000156,'K421 埃塞俄比亚（阿比西尼亚）',110000155,'K421','11050901',4,'K421');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000157,'K422 索马里',110000155,'K422','11050902',4,'K422');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000158,'K423 吉布提',110000155,'K423','11050903',4,'K423');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000159,'K424 肯尼亚',110000155,'K424','11050904',4,'K424');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000160,'K425 坦桑尼亚',110000155,'K425','11050905',4,'K425');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000161,'K426 乌干达',110000155,'K426','11050906',4,'K426');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000162,'K427 卢旺达',110000155,'K427','11050907',4,'K427');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000163,'K428 布隆迪',110000155,'K428','11050908',4,'K428');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000164,'K43 西非',110000140,'K43','110510',3,'K43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000165,'K431 毛里塔尼亚',110000164,'K431','11051001',4,'K431');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000166,'K432 西撒哈拉',110000164,'K432','11051002',4,'K432');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000167,'K434 塞内加尔',110000164,'K434','11051003',4,'K434');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000168,'K435 冈比亚',110000164,'K435','11051004',4,'K435');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000169,'K436 尼日尔',110000164,'K436','11051005',4,'K436');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000170,'K437 尼日利亚',110000164,'K437','11051006',4,'K437');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000171,'K438 喀麦隆',110000164,'K438','11051007',4,'K438');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000172,'K439 赤道几内亚',110000164,'K439','11051008',4,'K439');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000173,'K441 圣多美和普林西比',110000164,'K441','11051009',4,'K441');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000174,'K442 布基纳法索（上沃尔特）',110000164,'K442','11051010',4,'K442');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000175,'K443 贝宁（达荷美）',110000164,'K443','11051011',4,'K443');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000176,'K444 多哥',110000164,'K444','11051012',4,'K444');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000177,'K445 加纳',110000164,'K445','11051013',4,'K445');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000178,'K446 科特迪瓦（象牙海岸）',110000164,'K446','11051014',4,'K446');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000179,'K447 利比里亚',110000164,'K447','11051015',4,'K447');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000180,'K448 马里',110000164,'K448','11051016',4,'K448');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000181,'K449 塞拉利昂',110000164,'K449','11051017',4,'K449');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000182,'K451 几内亚',110000164,'K451','11051018',4,'K451');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000183,'K452 几内亚比绍',110000164,'K452','11051019',4,'K452');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000184,'K453 佛得角',110000164,'K453','11051020',4,'K453');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000185,'K46 中非',110000140,'K46','110511',3,'K46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000186,'K461 乍得',110000185,'K461','11051101',4,'K461');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000187,'K462 中非共和国',110000185,'K462','11051102',4,'K462');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000188,'K463 扎伊尔',110000185,'K463','11051103',4,'K463');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000189,'K464 刚果',110000185,'K464','11051104',4,'K464');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000190,'K465 加蓬',110000185,'K465','11051105',4,'K465');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000191,'K47 南非',110000140,'K47','110512',3,'K47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000192,'K471 莫桑比克（莫三鼻给）',110000191,'K471','11051201',4,'K471');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000193,'K472 马拉维',110000191,'K472','11051202',4,'K472');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000194,'K473 赞比亚',110000191,'K473','11051203',4,'K473');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000195,'K474 安哥拉',110000191,'K474','11051204',4,'K474');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000196,'K475 津巴布韦（南罗得西亚）',110000191,'K475','11051205',4,'K475');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000197,'K476 博茨瓦纳',110000191,'K476','11051206',4,'K476');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000198,'K477 纳米比亚（西南非洲）',110000191,'K477','11051207',4,'K477');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000199,'K478 南非（阿扎尼亚）',110000191,'K478','11051208',4,'K478');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000200,'K479 斯威士兰（恩格尼尼）',110000191,'K479','11051209',4,'K479');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000201,'K481 莱索托（巴苏托兰）',110000191,'K481','11051210',4,'K481');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000202,'K482 马达加斯加（马拉加萨共和国、马尔加什）',110000191,'K482','11051211',4,'K482');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000203,'K484 毛里求斯',110000191,'K484','11051212',4,'K484');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000204,'K5 欧洲史',110000001,'K5','1106',2,'K5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000205,'K500 通史',110000204,'K500','110601',3,'K500');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000206,'K501 上古史',110000204,'K501','110602',3,'K501');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000207,'K502 古代史',110000204,'K502','110603',3,'K502');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000208,'K503 中世纪史',110000204,'K503','110604',3,'K503');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000209,'K504 近代史',110000204,'K504','110605',3,'K504');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000210,'K505 现代史',110000204,'K505','110606',3,'K505');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000211,'K508 民族史志',110000204,'K508','110607',3,'K508');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000212,'K51 东欧、中欧',110000204,'K51','110608',3,'K51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000213,'[K511.2] 俄罗斯',110000212,'[K511.2]','11060801',4,'K5112');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000214,'K511.3 乌克兰',110000212,'K511.3','11060802',4,'K5113');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000215,'K511.4 白俄罗斯',110000212,'K511.4','11060803',4,'K5114');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000216,'K511.5 摩尔达维亚',110000212,'K511.5','11060804',4,'K5115');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000217,'K511.6 爱沙尼亚',110000212,'K511.6','11060805',4,'K5116');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000218,'K511.7 沙脱维亚',110000212,'K511.7','11060806',4,'K5117');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000219,'K511.8 立陶宛',110000212,'K511.8','11060807',4,'K5118');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000220,'K512 俄罗斯及苏联',110000212,'K512','11060808',4,'K512');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000221,'K513 波兰',110000212,'K513','11060809',4,'K513');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000222,'K514 捷克斯洛伐克（ ～1992年）',110000212,'K514','11060810',4,'K514');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000223,'K515 匈牙利',110000212,'K515','11060811',4,'K515');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000224,'K516 德国',110000212,'K516','11060812',4,'K516');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000225,'K517 德意志民主共和国（1945～1990年）',110000212,'K517','11060813',4,'K517');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000226,'K518 德意志联邦共和国（1945～1990年）',110000212,'K518','11060814',4,'K518');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000227,'K519 卢森堡',110000212,'K519','11060815',4,'K519');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000228,'K521 奥地利',110000212,'K521','11060816',4,'K521');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000229,'k522 瑞士',110000212,'k522','11060817',4,'k522');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000230,'K523 列支敦士登',110000212,'K523','11060818',4,'K523');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000231,'K524 捷克',110000212,'K524','11060819',4,'K524');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000232,'K525 斯洛伐克',110000212,'K525','11060820',4,'K525');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000233,'K53 北欧',110000204,'K53','110609',3,'K53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000234,'K531 芬兰',110000233,'K531','11060901',4,'K531');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000235,'K532 瑞典',110000233,'K532','11060902',4,'K532');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000236,'K533 挪威',110000233,'K533','11060903',4,'K533');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000237,'K534 丹麦',110000233,'K534','11060904',4,'K534');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000238,'K535 冰岛',110000233,'K535','11060905',4,'K535');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000239,'K54 南欧（东南欧、西南欧）',110000204,'K54','110610',3,'K54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000240,'K541 阿尔巴尼亚',110000239,'K541','11061001',4,'K541');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000241,'K542 罗马尼亚',110000239,'K542','11061002',4,'K542');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000242,'K543 南斯拉夫',110000239,'K543','11061003',4,'K543');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000243,'K544 保加利亚',110000239,'K544','11061004',4,'K544');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000244,'K545 希腊',110000239,'K545','11061005',4,'K545');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000245,'K546 意大利',110000239,'K546','11061006',4,'K546');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000246,'K547 梵蒂冈',110000239,'K547','11061007',4,'K547');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000247,'K548 圣马力诺',110000239,'K548','11061008',4,'K548');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000248,'K549 马耳他',110000239,'K549','11061009',4,'K549');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000249,'K551 西班牙',110000239,'K551','11061010',4,'K551');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000250,'K552 葡萄牙',110000239,'K552','11061011',4,'K552');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000251,'K553 安道尔',110000239,'K553','11061012',4,'K553');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000252,'[K555.1] 南斯拉夫联邦（塞-黑联邦）',110000239,'[K555.1]','11061013',4,'K5551');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000253,'K555.3 克罗地亚',110000239,'K555.3','11061014',4,'K5553');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000254,'K555.4 斯洛文尼亚',110000239,'K555.4','11061015',4,'K5554');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000255,'K555.5 波斯尼亚-黑塞哥维那（波黑）',110000239,'K555.5','11061016',4,'K5555');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000256,'K555.6 马其顿',110000239,'K555.6','11061017',4,'K5556');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000257,'K56 西欧',110000204,'K56','110611',3,'K56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000258,'K561 英国',110000257,'K561','11061101',4,'K561');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000259,'K562 爱尔兰',110000257,'K562','11061102',4,'K562');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000260,'K563 荷兰',110000257,'K563','11061103',4,'K563');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000261,'K564 比利时',110000257,'K564','11061104',4,'K564');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000262,'K565 法国',110000257,'K565','11061105',4,'K565');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000263,'K566 摩纳哥',110000257,'K566','11061106',4,'K566');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000264,'K6 大洋洲史',110000001,'K6','1107',2,'K6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000265,'K611 澳大利亚',110000264,'K611','11070101',4,'K611');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000266,'K612 新西兰',110000264,'K612','11070102',4,'K612');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000267,'K613 巴布亚新几内亚',110000264,'K613','11070103',4,'K613');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000268,'K638 西萨摩亚',110000264,'K638','11070104',4,'K638');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000269,'K639 汤加',110000264,'K639','11070105',4,'K639');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000270,'K641 库克群岛',110000264,'K641','11070106',4,'K641');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000271,'{K645} 库克群岛',110000264,'{K645}','11070107',4,'K645');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000272,'K646 图瓦卢',110000264,'K646','11070108',4,'K646');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000273,'{K647} 基里巴斯',110000264,'{K647}','11070109',4,'K647');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000274,'K655 瑙鲁',110000264,'K655','11070110',4,'K655');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000275,'K656 基里巴斯',110000264,'K656','11070111',4,'K656');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000276,'K661 斐济',110000264,'K661','11070112',4,'K661');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000277,'K7 美洲史',110000001,'K7','1108',2,'K7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000278,'K700 通史',110000277,'K700','110801',3,'K700');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000279,'K702 古代史（ ～1492年）',110000277,'K702','110802',3,'K702');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000280,'K703 殖民地时期（1492～1774年）',110000277,'K703','110803',3,'K703');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000281,'K704 美国独立战争至第二次世界大战时期（1775～1945年）',110000277,'K704','110804',3,'K704');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000282,'K705 第二次世界大战以后（1945年～ ）',110000277,'K705','110805',3,'K705');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000283,'K708 民族史志',110000277,'K708','110806',3,'K708');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000284,'K71 北美洲',110000277,'K71','110807',3,'K71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000285,'K711 加拿大',110000284,'K711','11080701',4,'K711');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000286,'K712 美国',110000284,'K712','11080702',4,'K712');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000287,'K73 拉丁美洲',110000277,'K73','110808',3,'K73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000288,'K731 墨西哥',110000287,'K731','11080801',4,'K731');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000289,'K741 危地马拉',110000287,'K741','11080802',4,'K741');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000290,'K742 洪都拉斯',110000287,'K742','11080803',4,'K742');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000291,'K743 伯利兹',110000287,'K743','11080804',4,'K743');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000292,'K744 萨尔瓦多',110000287,'K744','11080805',4,'K744');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000293,'K745 尼加拉瓜',110000287,'K745','11080806',4,'K745');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000294,'K746 哥斯达黎加',110000287,'K746','11080807',4,'K746');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000295,'K747 巴拿马',110000287,'K747','11080808',4,'K747');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000296,'K75 西印度群岛',110000277,'K75','110809',3,'K75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000297,'K751 古巴',110000296,'K751','11080901',4,'K751');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000298,'K752 海地',110000296,'K752','11080902',4,'K752');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000299,'K753 多米尼加',110000296,'K753','11080903',4,'K753');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000300,'K754 牙买加',110000296,'K754','11080904',4,'K754');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000301,'K755 波多黎各',110000296,'K755','11080905',4,'K755');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000302,'K762 巴巴多斯',110000296,'K762','11080906',4,'K762');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000303,'K766 圣卢西亚',110000296,'K766','11080907',4,'K766');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000304,'K767 特立尼达和多巴哥',110000296,'K767','11080908',4,'K767');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000305,'K768 巴哈马',110000296,'K768','11080909',4,'K768');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000306,'K769.1 格林纳达',110000296,'K769.1','11080910',4,'K7691');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000307,'K769.2 多米尼加联邦',110000296,'K769.2','11080911',4,'K7692');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000308,'K769.3 圣文森特和格林纳丁斯',110000296,'K769.3','11080912',4,'K7693');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000309,'K77 南美洲',110000277,'K77','110810',3,'K77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000310,'K771 圭亚那',110000309,'K771','11081001',4,'K771');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000311,'{K773} 圭亚那',110000309,'{K773}','11081002',4,'K773');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000312,'K774 委内瑞拉',110000309,'K774','11081003',4,'K774');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000313,'K775 哥伦比亚',110000309,'K775','11081004',4,'K775');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000314,'K776 厄瓜多尔',110000309,'K776','11081005',4,'K776');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000315,'K777 巴西',110000309,'K777','11081006',4,'K777');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000316,'K778 秘鲁',110000309,'K778','11081007',4,'K778');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000317,'K779 玻利维亚',110000309,'K779','11081008',4,'K779');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000318,'K781 巴拉圭',110000309,'K781','11081009',4,'K781');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000319,'K782 乌拉圭',110000309,'K782','11081010',4,'K782');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000320,'K783 阿根廷',110000309,'K783','11081011',4,'K783');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000321,'K784 智利',110000309,'K784','11081012',4,'K784');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000322,'K81 传记',110000001,'K81','1109',2,'K81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000323,'K810 传记研究与编写',110000322,'K810','110901',3,'K810');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000324,'K810.1 传记写作法',110000323,'K810.1','11090101',4,'K8101');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000325,'K810.2 谱系法',110000323,'K810.2','11090102',4,'K8102');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000326,'K811 世界人物传记',110000322,'K811','110902',3,'K811');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000327,'K811-6 参考工具书',110000326,'K811-6','11090201',4,'K811-6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000328,'K812 人物总传：按时代分',110000326,'K812','11090202',4,'K812');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000329,'[K813] 人物总传：按地区分',110000326,'[K813]','11090203',4,'K813');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000330,'K815 人物总传：按学科分',110000326,'K815','11090204',4,'K815');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000331,'K816 氏族谱系',110000326,'K816','11090205',4,'K816');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000332,'K82 中国人物传记',110000322,'K82','110903',3,'K82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000333,'K82-6 参考工具书',110000332,'K82-6','11090301',4,'K82-6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000334,'K820 人物总传：按时代分',110000332,'K820','11090302',4,'K820');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000335,'K820.8 人物总传：按地区分',110000332,'K820.8','11090303',4,'K8208');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000336,'K820.9 氏族谱系',110000332,'K820.9','11090304',4,'K8209');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000337,'K825 人物传记：按学科分',110000332,'K825','11090305',4,'K825');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000338,'K833/837 各国人物传记',110000322,'K833/837','110904',3,'K833/837,K833,K834,K835,K836,K837');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000339,'K85 文物考古',110000001,'K85','1110',2,'K85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000340,'[K852] 古文献学',110000339,'[K852]','111001',3,'K852');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000341,'K853 纹章学',110000339,'K853','111002',3,'K853');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000342,'K854 考古方法',110000339,'K854','111003',3,'K854');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000343,'K854.1 调查发掘方法',110000342,'K854.1','11100301',4,'K8541');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000344,'K854.2 古物鉴定',110000342,'K854.2','11100302',4,'K8542');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000345,'[K854.3] 古物保管和修复',110000342,'[K854.3]','11100303',4,'K8543');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000346,'K86 世界文物考古',110000339,'K86','111004',3,'K86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000347,'K87 中国文物考古',110000339,'K87','111005',3,'K87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000348,'K871 各代文物考古',110000347,'K871','11100501',4,'K871');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000349,'K872 地区文物考古',110000347,'K872','11100502',4,'K872');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000350,'K873 出土文物图录',110000347,'K873','11100503',4,'K873');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000351,'K874 民族文物考古',110000347,'K874','11100504',4,'K874');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000352,'K875 各种用品器物',110000347,'K875','11100505',4,'K875');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000353,'K876 各种材料器物',110000347,'K876','11100506',4,'K876');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000354,'K877 古书契',110000347,'K877','11100507',4,'K877');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000355,'K878 遗址',110000347,'K878','11100508',4,'K878');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000356,'K879 美术考古',110000347,'K879','11100509',4,'K879');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000357,'K883/887 各国文物考古',110000339,'K883/887','111006',3,'K883/887,K883,K884,K885,K886,K887');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000358,'K89 风俗习惯',110000001,'K89','1111',2,'K89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000359,'K890 民俗学',110000358,'K890','111101',3,'K890');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000360,'K891 世界风俗习惯',110000358,'K891','111102',3,'K891');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000361,'K892 中国风俗习惯',110000358,'K892','111103',3,'K892');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000362,'K893/897 各国风俗习惯',110000358,'K893/897','111104',3,'K893/897,K893,K894,K895,K896,K897');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000363,'K9 地理',110000001,'K9','1112',2,'K9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000364,'K90 地理学',110000363,'K90','111201',3,'K90');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000365,'K901 人文地理学',110000364,'K901','11120101',4,'K901');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000366,'[K902] 经济地理学',110000364,'[K902]','11120102',4,'K902');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000367,'[K903] 自然地理学',110000364,'[K903]','11120103',4,'K903');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000368,'K909 应用地理学',110000364,'K909','11120104',4,'K909');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000369,'K91 世界地理',110000363,'K91','111202',3,'K91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000370,'K912 政治区划',110000369,'K912','11120201',4,'K912');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000371,'K915 城市、村落',110000369,'K915','11120202',4,'K915');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000372,'K916 历史地理',110000369,'K916','11120203',4,'K916');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000373,'K917 名胜古迹',110000369,'K917','11120204',4,'K917');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000374,'K918 专类地理',110000369,'K918','11120205',4,'K918');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000375,'K919 旅行、游记',110000369,'K919','11120206',4,'K919');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000376,'K92 中国地理',110000363,'K92','111203',3,'K92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000377,'K921/927 区域地理、地理志',110000376,'K921/927','11120301',4,'K921/927,K921,K922,K923,K924,K925,K926,K927');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000378,'K928 专类地理',110000376,'K928','11120302',4,'K928');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000379,'K93/97 各国地理',110000363,'K93/97','111204',3,'K93/97,K93,K94,K95,K96,K97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000380,'K99 地图',110000363,'K99','111205',3,'K99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000381,'K991 世界',110000380,'K991','11120501',4,'K991');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000382,'K992 中国',110000380,'K992','11120502',4,'K992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(110000383,'K993/997 各国',110000380,'K993/997','11120503',4,'K993/997,K993,K994,K995,K996,K997');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000001,'N 自然科学总论',null,'N','12',1,'N');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000002,'N0 自然科学理论与方法论',120000001,'N0','1201',2,'N0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000003,'N1 自然科学现状及发展',120000001,'N1','1202',2,'N1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000004,'N2 自然科学机构、团体、会议',120000001,'N2','1203',2,'N2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000005,'N3 自然科学研究方法',120000001,'N3','1204',2,'N3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000006,'N4 自然科学教育与普及',120000001,'N4','1205',2,'N4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000007,'N5 自然科学丛书、文集、连续性出版物',120000001,'N5','1206',2,'N5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000008,'N6 自然科学参考工具书',120000001,'N6','1207',2,'N6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000009,'[N7] 自然科学文献检索工具',120000001,'[N7]','1208',2,'N7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000010,'N8 自然科学调查、考察',120000001,'N8','1209',2,'N8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000011,'N91 自然研究、自然历史',120000001,'N91','1210',2,'N91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000012,'N93 非线性科学',120000001,'N93','1211',2,'N93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000013,'N94 系统科学',120000001,'N94','1212',2,'N94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(120000014,'[N99] 情报学、情报工作',120000001,'[N99]','1213',2,'N99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000001,'O 数理科学和化学',null,'O','13',1,'O');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000002,'O1 数学',130000001,'O1','1301',2,'O1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000003,'O1-0 数学理论',130000002,'O1-0','130101',3,'O1-0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000004,'O1-8 计算工具',130000002,'O1-8','130102',3,'O1-8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000005,'O11 古典数学',130000002,'O11','130103',3,'O11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000006,'O119 中国数学',130000002,'O119','130104',3,'O119');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000007,'O12 初等数学',130000002,'O12','130105',3,'O12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000008,'O13 高等数学',130000002,'O13','130106',3,'O13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000009,'O14 数理逻辑、数学基础',130000002,'O14','130107',3,'O14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000010,'O15 代数、数论、组合理论',130000002,'O15','130108',3,'O15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000011,'O17 数学分析',130000002,'O17','130109',3,'O17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000012,'O18 几何、拓扑',130000002,'O18','130110',3,'O18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000013,'O19 动力系统理论',130000002,'O19','130111',3,'O19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000014,'O21 概率论与数理统计',130000002,'O21','130112',3,'O21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000015,'O22 运筹学',130000002,'O22','130113',3,'O22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000016,'O23 控制论、信息论（数学理论）',130000002,'O23','130114',3,'O23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000017,'O24 计算数学',130000002,'O24','130115',3,'O24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000018,'O29 应用数学',130000002,'O29','130116',3,'O29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000019,'O3 力学',130000001,'O3','1302',2,'O3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000020,'O31 理论力学（一般力学）',130000019,'O31','130201',3,'O31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000021,'O32 振动理论',130000019,'O32','130202',3,'O32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000022,'O33 连续介质力学（变形体力学）',130000019,'O33','130203',3,'O33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000023,'O34 固体力学',130000019,'O34','130204',3,'O34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000024,'O35 流体力学',130000019,'O35','130205',3,'O35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000025,'O369 物理力学',130000019,'O369','130206',3,'O369');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000026,'O37 流变学',130000019,'O37','130207',3,'O37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000027,'O38 爆炸力学',130000019,'O38','130208',3,'O38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000028,'O39 应用力学',130000019,'O39','130209',3,'O39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000029,'O4 物理学',130000001,'O4','1303',2,'O4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000030,'O4-1 物理学现状与发展',130000029,'O4-1','130301',3,'O4-1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000031,'O41 理论物理学',130000029,'O41','130302',3,'O41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000032,'O42 声学',130000029,'O42','130303',3,'O42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000033,'O43 光学',130000029,'O43','130304',3,'O43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000034,'O44 电磁学、电动力学',130000029,'O44','130305',3,'O44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000035,'O45 无线电物理学',130000029,'O45','130306',3,'O45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000036,'O46 真空电子学（电子物理学）',130000029,'O46','130307',3,'O46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000037,'O469 凝聚态物理学',130000029,'O469','130308',3,'O469');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000038,'O47 半导体物理学',130000029,'O47','130309',3,'O47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000039,'O48 固体物理学',130000029,'O48','130310',3,'O48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000040,'O51 低温物理学',130000029,'O51','130311',3,'O51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000041,'O52 高压与高温物理学',130000029,'O52','130312',3,'O52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000042,'O53 等离子体物理学',130000029,'O53','130313',3,'O53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000043,'O55 热学与物质分子运动论',130000029,'O55','130314',3,'O55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000044,'O56 分子物理学、原子物理学',130000029,'O56','130315',3,'O56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000045,'O57 原子核物理学、高能物理学',130000029,'O57','130316',3,'O57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000046,'O59 应用物理学',130000029,'O59','130317',3,'O59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000047,'O6 化学',130000001,'O6','1304',2,'O6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000048,'O6-0 化学原理和方法',130000047,'O6-0','130401',3,'O6-0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000049,'O6-1 化学现状与发展',130000047,'O6-1','130402',3,'O6-1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000050,'O6-3 化学实验（实验化学）',130000047,'O6-3','130403',3,'O6-3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000051,'O61 无机化学',130000047,'O61','130404',3,'O61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000052,'O62 有机化学',130000047,'O62','130405',3,'O62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000053,'O63 高分子化学（高聚物）',130000047,'O63','130406',3,'O63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000054,'O64 物理化学（理论化学）、化学物理学',130000047,'O64','130407',3,'O64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000055,'O65 分析化学',130000047,'O65','130408',3,'O65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000056,'O69 应用化学',130000047,'O69','130409',3,'O69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000057,'O7 晶体学',130000001,'O7','1305',2,'O7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000058,'O71 几何晶体学',130000057,'O71','130501',3,'O71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000059,'O72 X射线晶体学',130000057,'O72','130502',3,'O72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000060,'O73 晶体物理',130000057,'O73','130503',3,'O73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000061,'O74 晶体化学',130000057,'O74','130504',3,'O74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000062,'O75 非晶态和类晶态',130000057,'O75','130505',3,'O75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000063,'O76 晶体结构',130000057,'O76','130506',3,'O76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000064,'O77 晶体缺陷',130000057,'O77','130507',3,'O77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000065,'O78 晶体生长',130000057,'O78','130508',3,'O78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000066,'O79 晶体物理化学过程',130000057,'O79','130509',3,'O79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(130000067,'O799 应用晶体学',130000057,'O799','130510',3,'O799');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000001,'P 天文学、地球科学',null,'P','14',1,'P');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000002,'P1 天文学',140000001,'P1','1401',2,'P1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000003,'P2 测绘学',140000001,'P2','1402',2,'P2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000004,'P3 地球物理学',140000001,'P3','1403',2,'P3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000005,'P4 大气科学（气象学）',140000001,'P4','1404',2,'P4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000006,'P5 地质学',140000001,'P5','1405',2,'P5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000007,'P7 海洋学',140000001,'P7','1406',2,'P7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(140000008,'P9 自然地理学',140000001,'P9','1407',2,'P9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000001,'Q 生物科学',null,'Q','15',1,'Q');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000002,'Q1 普通生物学',150000001,'Q1','1501',2,'Q1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000003,'Q2 细胞生物学',150000001,'Q2','1502',2,'Q2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000004,'Q3 遗传学',150000001,'Q3','1503',2,'Q3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000005,'Q4 生理学',150000001,'Q4','1504',2,'Q4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000006,'Q5 生物化学',150000001,'Q5','1505',2,'Q5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000007,'Q6 生物物理学',150000001,'Q6','1506',2,'Q6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000008,'Q7 分子生物学',150000001,'Q7','1507',2,'Q7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000009,'Q81 生物工程学（生物技术）',150000001,'Q81','1508',2,'Q81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000010,'[Q89] 环境生物学',150000001,'[Q89]','1509',2,'Q89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000011,'Q91 古生物学',150000001,'Q91','1510',2,'Q91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000012,'Q93 微生物学',150000001,'Q93','1511',2,'Q93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000013,'Q94 植物学',150000001,'Q94','1512',2,'Q94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000014,'Q95 动物学',150000001,'Q95','1513',2,'Q95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000015,'Q96 昆虫学',150000001,'Q96','1514',2,'Q96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(150000016,'Q98 人类学',150000001,'Q98','1515',2,'Q98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000001,'R 医药、卫生',null,'R','16',1,'R');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000002,'R1 预防医学、卫生学',160000001,'R1','1601',2,'R1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000003,'R2 中国医学',160000001,'R2','1602',2,'R2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000004,'R3 基础医学',160000001,'R3','1603',2,'R3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000005,'R4 临床医学',160000001,'R4','1604',2,'R4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000006,'R5 内科学',160000001,'R5','1605',2,'R5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000007,'R6 外科学',160000001,'R6','1606',2,'R6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000008,'R71 妇产科学',160000001,'R71','1607',2,'R71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000009,'R72 儿科学',160000001,'R72','1608',2,'R72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000010,'R73 肿瘤学',160000001,'R73','1609',2,'R73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000011,'R74 神经病学与精神病学',160000001,'R74','1610',2,'R74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000012,'R75 皮肤病学与性病学',160000001,'R75','1611',2,'R75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000013,'R76 耳鼻咽喉科学',160000001,'R76','1612',2,'R76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000014,'R77 眼科学',160000001,'R77','1613',2,'R77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000015,'R78 口腔科学',160000001,'R78','1614',2,'R78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000016,'R79 外国民族医学',160000001,'R79','1615',2,'R79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000017,'R8 特种医学',160000001,'R8','1616',2,'R8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(160000018,'R9 药学',160000001,'R9','1617',2,'R9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000001,'S 农业科学',null,'S','17',1,'S');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000002,'S1 农业基础科学',170000001,'S1','1701',2,'S1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000003,'S2 农业工程',170000001,'S2','1702',2,'S2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000004,'S3 农学（农艺学）',170000001,'S3','1703',2,'S3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000005,'S4 植物保护',170000001,'S4','1704',2,'S4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000006,'S5 农作物',170000001,'S5','1705',2,'S5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000007,'S6 园艺',170000001,'S6','1706',2,'S6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000008,'S7 林业',170000001,'S7','1707',2,'S7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000009,'S8 畜牧、动物医学、狩猎、蚕、蜂',170000001,'S8','1708',2,'S8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(170000010,'S9 水产、渔业',170000001,'S9','1709',2,'S9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000001,'T 工业技术',null,'T','18',1,'T');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000002,'T-0 工业技术理论',180000001,'T-0','18010101',4,'T-0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000003,'T-1 工业技术现状与发展',180000001,'T-1','18010102',4,'T-1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000004,'T-2 机构、团体、会议',180000001,'T-2','18010103',4,'T-2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000005,'T-6 参考工具书',180000001,'T-6','18010104',4,'T-6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000006,'[T-9] 工业经济',180000001,'[T-9]','18010105',4,'T-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000007,'TB 一般工业技术',180000001,'TB','1802',2,'TB');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000008,'TB1 工程基础科学',180000007,'TB1','180201',3,'TB1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000009,'TB11 工程数学',180000008,'TB11','18020101',4,'TB11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000010,'TB12 工程力学',180000008,'TB12','18020102',4,'TB12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000011,'TB13 工程物理学',180000008,'TB13','18020103',4,'TB13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000012,'[TB14] 工程化学',180000008,'[TB14]','18020104',4,'TB14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000013,'TB15 工程天文学',180000008,'TB15','18020105',4,'TB15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000014,'[TB16] 工程地质学',180000008,'[TB16]','18020106',4,'TB16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000015,'TB17 工程仿生学',180000008,'TB17','18020107',4,'TB17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000016,'TB18 人体工程学',180000008,'TB18','18020108',4,'TB18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000017,'TB2 工程设计与测绘',180000007,'TB2','180202',3,'TB2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000018,'TB21 工程设计',180000017,'TB21','18020201',4,'TB21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000019,'TB22 工程测量',180000017,'TB22','18020202',4,'TB22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000020,'TB23 工程制图',180000017,'TB23','18020203',4,'TB23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000021,'TB24 工程模拟',180000017,'TB24','18020204',4,'TB24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000022,'TB3 工程材料学',180000007,'TB3','180203',3,'TB3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000023,'TB30 工程材料一般性问题',180000022,'TB30','18020301',4,'TB30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000024,'[TB31] 金属材料',180000022,'[TB31]','18020302',4,'TB31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000025,'TB32 非金属材料',180000022,'TB32','18020303',4,'TB32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000026,'TB33 复合材料',180000022,'TB33','18020304',4,'TB33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000027,'TB34 功能材料',180000022,'TB34','18020305',4,'TB34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000028,'TB35 耐高温材料、耐低温材料',180000022,'TB35','18020306',4,'TB35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000029,'TB37 耐腐蚀材料',180000022,'TB37','18020307',4,'TB37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000030,'TB381 智能材料',180000022,'TB381','18020308',4,'TB381');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000031,'TB383 特种结构材料',180000022,'TB383','18020309',4,'TB383');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000032,'TB39 其他材料',180000022,'TB39','18020310',4,'TB39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000033,'TB4 工业通用技术与设备',180000007,'TB4','180204',3,'TB4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000034,'TB41 爆破技术',180000033,'TB41','18020401',4,'TB41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000035,'TB42 密封技术',180000033,'TB42','18020402',4,'TB42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000036,'TB43 薄膜技术',180000033,'TB43','18020403',4,'TB43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000037,'TB44 粉末技术',180000033,'TB44','18020404',4,'TB44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000038,'TB47 工业设计',180000033,'TB47','18020405',4,'TB47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000039,'TB48 包装工程',180000033,'TB48','18020406',4,'TB48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000040,'TB49 工厂、车间',180000033,'TB49','18020407',4,'TB49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000041,'TB5 声学工程',180000007,'TB5','180205',3,'TB5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000042,'TB51 声学仪器',180000041,'TB51','18020501',4,'TB51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000043,'TB52 声学测量',180000041,'TB52','18020502',4,'TB52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000044,'TB53 振动、噪声及其控制',180000041,'TB53','18020503',4,'TB53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000045,'TB54 电声工程',180000041,'TB54','18020504',4,'TB54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000046,'TB55 超声工程',180000041,'TB55','18020505',4,'TB55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000047,'TB56 水声工程',180000041,'TB56','18020506',4,'TB56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000048,'TB57 光声工程',180000041,'TB57','18020507',4,'TB57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000049,'TB6 制冷工程',180000007,'TB6','180206',3,'TB6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000050,'TB61 制冷理论',180000049,'TB61','18020601',4,'TB61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000051,'TB64 制冷材料',180000049,'TB64','18020602',4,'TB64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000052,'TB65 制冷机械和设备',180000049,'TB65','18020603',4,'TB65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000053,'TB66 制冷技术',180000049,'TB66','18020604',4,'TB66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000054,'TB69 制冷应用',180000049,'TB69','18020605',4,'TB69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000055,'TB7 真空技术',180000007,'TB7','180207',3,'TB7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000056,'TB71 真空技术基础理论',180000055,'TB71','18020701',4,'TB71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000057,'TB74 真空材料',180000055,'TB74','18020702',4,'TB74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000058,'TB75 真空获得技术及设备',180000055,'TB75','18020703',4,'TB75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000059,'TB77 真空测试及仪器',180000055,'TB77','18020704',4,'TB77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000060,'TB79 真空技术的应用',180000055,'TB79','18020705',4,'TB79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000061,'TB8 摄影技术',180000007,'TB8','180208',3,'TB8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000062,'TB81 摄影理论',180000061,'TB81','18020801',4,'TB81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000063,'TB82 拍摄技术',180000061,'TB82','18020802',4,'TB82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000064,'TB84 感光材料',180000061,'TB84','18020803',4,'TB84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000065,'TB85 摄影机具与设备',180000061,'TB85','18020804',4,'TB85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000066,'TB86 各种摄影技术',180000061,'TB86','18020805',4,'TB86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000067,'TB88 洗印技术',180000061,'TB88','18020806',4,'TB88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000068,'TB89 摄影技术的应用',180000061,'TB89','18020807',4,'TB89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000069,'TB9 计量学',180000007,'TB9','180209',3,'TB9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000070,'TB91 计量单位与单位制',180000069,'TB91','18020901',4,'TB91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000071,'TB92 几何量计量',180000069,'TB92','18020902',4,'TB92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000072,'TB93 力学计量',180000069,'TB93','18020903',4,'TB93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000073,'TB94 热学计量',180000069,'TB94','18020904',4,'TB94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000074,'TB95 声学计量',180000069,'TB95','18020905',4,'TB95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000075,'TB96 光学计量',180000069,'TB96','18020906',4,'TB96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000076,'TB97 电磁学与无线电计量',180000069,'TB97','18020907',4,'TB97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000077,'TB98 电离辐射和放射性计量',180000069,'TB98','18020908',4,'TB98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000078,'TB99 物理化学计量',180000069,'TB99','18020909',4,'TB99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000079,'TD 矿业工程',180000001,'TD','1803',2,'TD');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000080,'[TD-9] 矿山经济',180000079,'[TD-9]','18030101',4,'TD-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000081,'TD1 矿山地质与测量',180000079,'TD1','180302',3,'TD1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000082,'[TD11] 矿床学',180000081,'[TD11]','18030201',4,'TD11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000083,'[TD12] 水文地质学与工程地质学',180000081,'[TD12]','18030202',4,'TD12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000084,'[TD15] 普查与勘探',180000081,'[TD15]','18030203',4,'TD15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000085,'TD163 矿井地质',180000081,'TD163','18030204',4,'TD163');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000086,'TD164 露天矿地质',180000081,'TD164','18030205',4,'TD164');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000087,'TD166 生产地质勘探',180000081,'TD166','18030206',4,'TD166');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000088,'TD167 矿山环境地质',180000081,'TD167','18030207',4,'TD167');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000089,'TD17 矿山测量与制图',180000081,'TD17','18030208',4,'TD17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000090,'TD2 矿山设计与建设',180000079,'TD2','180303',3,'TD2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000091,'TD21 矿山设计',180000090,'TD21','18030301',4,'TD21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000092,'TD22 矿山地面建设',180000090,'TD22','18030302',4,'TD22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000093,'TD23 凿岩爆破工程',180000090,'TD23','18030303',4,'TD23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000094,'TD26 井巷工程',180000090,'TD26','18030304',4,'TD26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000095,'TD3 矿山压力与支护',180000079,'TD3','180304',3,'TD3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000096,'TD31 矿山压力理论',180000095,'TD31','18030401',4,'TD31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000097,'TD32 矿山压力与岩层移动',180000095,'TD32','18030402',4,'TD32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000098,'TD35 矿井支护与设备',180000095,'TD35','18030403',4,'TD35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000099,'TD4 矿山机械',180000079,'TD4','180305',3,'TD4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000100,'TD40 一般性问题',180000099,'TD40','18030501',4,'TD40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000101,'[TD41] 勘探机械、钻孔机',180000099,'[TD41]','18030502',4,'TD41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000102,'TD42 采掘机械',180000099,'TD42','18030503',4,'TD42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000103,'TD43 水力采矿机械化设备',180000099,'TD43','18030504',4,'TD43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000104,'TD44 矿山固定机械设备',180000099,'TD44','18030505',4,'TD44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000105,'TD45 选矿机械',180000099,'TD45','18030506',4,'TD45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000106,'TD5 矿山运输与设备',180000079,'TD5','180306',3,'TD5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000107,'TD50 一般性问题',180000106,'TD50','18030601',4,'TD50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000108,'TD52 井下运输与设备',180000106,'TD52','18030602',4,'TD52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000109,'TD53 矿井提升',180000106,'TD53','18030603',4,'TD53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000110,'TD54 井口设备',180000106,'TD54','18030604',4,'TD54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000111,'TD55 斜井运输',180000106,'TD55','18030605',4,'TD55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000112,'TD56 地面运输',180000106,'TD56','18030606',4,'TD56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000113,'TD57 露天矿运输',180000106,'TD57','18030607',4,'TD57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000114,'TD58 矿外运输',180000106,'TD58','18030608',4,'TD58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000115,'TD6 矿山电工',180000079,'TD6','180307',3,'TD6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000116,'TD60 一般性问题',180000115,'TD60','18030701',4,'TD60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000117,'TD61 矿山输电与配电',180000115,'TD61','18030702',4,'TD61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000118,'TD62 矿山电气照明及设备',180000115,'TD62','18030703',4,'TD62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000119,'TD63 矿山机械的电力装备与自动化',180000115,'TD63','18030704',4,'TD63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000120,'TD64 矿山电机车的电力装备',180000115,'TD64','18030705',4,'TD64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000121,'TD65 矿山信号与通信',180000115,'TD65','18030706',4,'TD65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000122,'TD67 矿山生产自动化技术',180000115,'TD67','18030707',4,'TD67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000123,'TD68 矿山电气安全设备',180000115,'TD68','18030708',4,'TD68');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000124,'TD7 矿山安全与劳动保护',180000079,'TD7','180308',3,'TD7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000125,'TD71 矿井大气',180000124,'TD71','18030801',4,'TD71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000126,'TD72 矿井通风',180000124,'TD72','18030802',4,'TD72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000127,'[TD73] 岩石沉陷及安全措施',180000124,'[TD73]','18030803',4,'TD73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000128,'TD74 矿山排水与堵水',180000124,'TD74','18030804',4,'TD74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000129,'TD75 矿山防火',180000124,'TD75','18030805',4,'TD75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000130,'TD76 矿山安全监测系统',180000124,'TD76','18030806',4,'TD76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000131,'TD77 矿山事故及救护',180000124,'TD77','18030807',4,'TD77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000132,'TD78 矿山卫生',180000124,'TD78','18030808',4,'TD78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000133,'TD79 劳动安全',180000124,'TD79','18030809',4,'TD79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000134,'TD8 矿山开采',180000079,'TD8','180309',3,'TD8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000135,'TD80 一般性问题',180000134,'TD80','18030901',4,'TD80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000136,'TD82 煤矿开采',180000134,'TD82','18030902',4,'TD82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000137,'TD83 油页岩开采',180000134,'TD83','18030903',4,'TD83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000138,'TD84 煤及油页岩地下气化',180000134,'TD84','18030904',4,'TD84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000139,'TD85 金属矿开采',180000134,'TD85','18030905',4,'TD85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000140,'TD87 非金属矿开采',180000134,'TD87','18030906',4,'TD87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000141,'TD88 矿区复田',180000134,'TD88','18030907',4,'TD88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000142,'TD9 选矿',180000079,'TD9','180310',3,'TD9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000143,'TD91 选矿理论',180000142,'TD91','18031001',4,'TD91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000144,'TD92 选矿流程与方法',180000142,'TD92','18031002',4,'TD92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000145,'TD94 选煤',180000142,'TD94','18031003',4,'TD94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000146,'TD95 金属矿选矿',180000142,'TD95','18031004',4,'TD95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000147,'TD97 非金属矿选矿',180000142,'TD97','18031005',4,'TD97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000148,'TD98 矿产资源的综合利用',180000079,'TD98','180311',3,'TD98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000149,'TD981 黑色金属矿产',180000148,'TD981','18031101',4,'TD981');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000150,'TD982 有色及贵重金属矿产',180000148,'TD982','18031102',4,'TD982');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000151,'TD983 稀有、分散、放射矿产',180000148,'TD983','18031103',4,'TD983');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000152,'TD984 燃料矿产',180000148,'TD984','18031104',4,'TD984');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000153,'TD985 非金属矿产',180000148,'TD985','18031105',4,'TD985');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000154,'TD989 其他',180000148,'TD989','18031106',4,'TD989');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000155,'TE 石油、天然气工业',180000001,'TE','1804',2,'TE');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000156,'[TE-9] 石油、天然气工业经济',180000155,'[TE-9]','18040101',4,'TE-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000157,'TE0 能源与节能',180000155,'TE0','180402',3,'TE0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000158,'TE01 能源计算',180000157,'TE01','18040201',4,'TE01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000159,'TE02 能源调查',180000157,'TE02','18040202',4,'TE02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000160,'TE08 节能',180000157,'TE08','18040203',4,'TE08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000161,'TE09 能源综合利用',180000157,'TE09','18040204',4,'TE09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000162,'TE1 石油、天然气地质与勘探',180000155,'TE1','180403',3,'TE1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000163,'TE11 油气田勘探组织与管理',180000162,'TE11','18040301',4,'TE11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000164,'[TE12] 石油、天然气地质',180000162,'[TE12]','18040302',4,'TE12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000165,'[TE13] 石油、天然气调查与勘探',180000162,'[TE13]','18040303',4,'TE13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000166,'TE14 油矿地质',180000162,'TE14','18040304',4,'TE14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000167,'TE15 油气田测量和储量计算',180000162,'TE15','18040305',4,'TE15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000168,'TE17 油气田区域分布',180000162,'TE17','18040306',4,'TE17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000169,'TE19 新技术在石油、天然气地质与勘探中的应用',180000162,'TE19','18040307',4,'TE19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000170,'TE2 钻井工程',180000155,'TE2','180404',3,'TE2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000171,'TE21 钻井理论',180000170,'TE21','18040401',4,'TE21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000172,'TE22 钻井设计',180000170,'TE22','18040402',4,'TE22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000173,'TE24 钻井工艺',180000170,'TE24','18040403',4,'TE24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000174,'TE25 洗井、固井、完井、油层损害与预防',180000170,'TE25','18040404',4,'TE25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000175,'TE26 井身质量及固井质量检查',180000170,'TE26','18040405',4,'TE26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000176,'TE27 中途测试及试油',180000170,'TE27','18040406',4,'TE27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000177,'TE28 钻井安全生产与复杂情况处理',180000170,'TE28','18040407',4,'TE28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000178,'[TE29] 钻井综合技术经济指标分析',180000170,'[TE29]','18040408',4,'TE29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000179,'TE3 油气田开发与开采',180000155,'TE3','180405',3,'TE3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000180,'TE31 基础理论',180000179,'TE31','18040501',4,'TE31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000181,'TE32 油气田开发设计与计算',180000179,'TE32','18040502',4,'TE32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000182,'TE33 油气田动态分析',180000179,'TE33','18040503',4,'TE33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000183,'TE34 油田开发(油藏工程)',180000179,'TE34','18040504',4,'TE34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000184,'TE35 采油工程',180000179,'TE35','18040505',4,'TE35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000185,'TE37 气田开发与开采',180000179,'TE37','18040506',4,'TE37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000186,'TE38 油气田开发和开采安全技术',180000179,'TE38','18040507',4,'TE38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000187,'TE39 油田应用化学',180000179,'TE39','18040508',4,'TE39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000188,'TE4 油气田建设工程',180000155,'TE4','180406',3,'TE4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000189,'TE41 工厂设计、规划与布局',180000188,'TE41','18040601',4,'TE41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000190,'TE42 设备与安装、施工',180000188,'TE42','18040602',4,'TE42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000191,'TE43 力能供应',180000188,'TE43','18040603',4,'TE43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000192,'TE44 供暖与照明设备',180000188,'TE44','18040604',4,'TE44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000193,'TE45 给水、排水',180000188,'TE45','18040605',4,'TE45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000194,'TE46 交通与通信',180000188,'TE46','18040606',4,'TE46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000195,'TE48 生产技术安全与卫生',180000188,'TE48','18040607',4,'TE48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000196,'TE49 其他',180000188,'TE49','18040608',4,'TE49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000197,'TE5 海上油气田勘探与开发',180000155,'TE5','180407',3,'TE5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000198,'[TE51] 海上油气田地质与勘探',180000197,'[TE51]','18040701',4,'TE51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000199,'TE52 海上油气田钻井工程',180000197,'TE52','18040702',4,'TE52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000200,'TE53 海上油气田开采技术',180000197,'TE53','18040703',4,'TE53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000201,'TE54 海上油气田建设工程',180000197,'TE54','18040704',4,'TE54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000202,'TE58 海上油气田勘探与开发安全技术',180000197,'TE58','18040705',4,'TE58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000203,'TE6 石油、天然气加工工业',180000155,'TE6','180408',3,'TE6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000204,'TE62 石油炼制',180000203,'TE62','18040801',4,'TE62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000205,'TE64 天然气加工',180000203,'TE64','18040802',4,'TE64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000206,'TE65 石油化学工业',180000203,'TE65','18040803',4,'TE65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000207,'TE66 人造石油',180000203,'TE66','18040804',4,'TE66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000208,'TE68 油气加工厂',180000203,'TE68','18040805',4,'TE68');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000209,'TE8 石油、天然气储存与运输',180000155,'TE8','180409',3,'TE8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000210,'TE81 油气储运过程中油气性质及组分测定',180000209,'TE81','18040901',4,'TE81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000211,'TE82 油气储存',180000209,'TE82','18040902',4,'TE82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000212,'TE83 油气输送与运输',180000209,'TE83','18040903',4,'TE83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000213,'TE85 油气储存损耗及预防措施',180000209,'TE85','18040904',4,'TE85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000214,'TE86 矿场油气集输与处理',180000209,'TE86','18040905',4,'TE86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000215,'TE88 油气储运安全技术',180000209,'TE88','18040906',4,'TE88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000216,'TE89 其他',180000209,'TE89','18040907',4,'TE89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000217,'TE9 石油机械设备与自动化',180000155,'TE9','180410',3,'TE9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000218,'[TE91] 地质勘探机械设备',180000217,'[TE91]','18041001',4,'TE91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000219,'TE92 钻井机械设备',180000217,'TE92','18041002',4,'TE92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000220,'TE93 油气开采机械设备',180000217,'TE93','18041003',4,'TE93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000221,'TE94 油气田工程建设机械设备',180000217,'TE94','18041004',4,'TE94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000222,'TE95 海上油气田开发开采机械设备',180000217,'TE95','18041005',4,'TE95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000223,'TE96 油气加工厂机械设备',180000217,'TE96','18041006',4,'TE96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000224,'TE97 油气储运机械设备',180000217,'TE97','18041007',4,'TE97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000225,'TE98 机械设备的腐蚀与防护',180000217,'TE98','18041008',4,'TE98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000226,'[TE99] 石油、天然气工业环境保护与综合利用',180000155,'[TE99]','180411',3,'TE99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000227,'[TE991] 石油、天然气工业环境污染与防治',180000226,'[TE991]','18041101',4,'TE991');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000228,'[TE992] 石油、天然气工业三废处理与综合利用',180000226,'[TE992]','18041102',4,'TE992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000229,'TF 冶金工业',180000001,'TF','1805',2,'TF');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000230,'[TF-9] 冶金工业经济',180000229,'[TF-9]','18050101',4,'TF-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000231,'TF0 一般性问题',180000229,'TF0','180502',3,'TF0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000232,'TF01 冶金原理',180000231,'TF01','18050201',4,'TF01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000233,'TF02 冶炼计算',180000231,'TF02','18050202',4,'TF02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000234,'TF03 冶炼试验与分析',180000231,'TF03','18050203',4,'TF03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000235,'TF04 冶炼原料及矿石预处理',180000231,'TF04','18050204',4,'TF04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000236,'TF05 冶金燃料与燃烧',180000231,'TF05','18050205',4,'TF05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000237,'TF06 冶金炉',180000231,'TF06','18050206',4,'TF06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000238,'TF08 冶金工厂',180000231,'TF08','18050207',4,'TF08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000239,'[TF09] 冶金工业废物处理与综合利用',180000231,'[TF09]','18050208',4,'TF09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000240,'TF1 冶金技术',180000229,'TF1','180503',3,'TF1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000241,'TF11 提炼冶金（化学冶金）',180000240,'TF11','18050301',4,'TF11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000242,'TF12 粉末冶金（金属陶瓷工艺）',180000240,'TF12','18050302',4,'TF12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000243,'TF13 真空冶金',180000240,'TF13','18050303',4,'TF13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000244,'TF14 电渣重熔',180000240,'TF14','18050304',4,'TF14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000245,'TF15 原子能冶金',180000240,'TF15','18050305',4,'TF15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000246,'TF16 纤维冶金',180000240,'TF16','18050306',4,'TF16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000247,'TF17 卤素冶金',180000240,'TF17','18050307',4,'TF17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000248,'TF18 微生物冶金',180000240,'TF18','18050308',4,'TF18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000249,'TF19 其他冶金技术',180000240,'TF19','18050309',4,'TF19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000250,'TF3 冶金机械、冶金生产自动化',180000229,'TF3','180504',3,'TF3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000251,'TF30 一般性问题',180000250,'TF30','18050401',4,'TF30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000252,'TF31 钢铁冶炼机械与生产自动化',180000250,'TF31','18050402',4,'TF31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000253,'TF32 炼铁机械与生产自动化',180000250,'TF32','18050403',4,'TF32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000254,'TF33 铁合金冶炼机械与生产自动化',180000250,'TF33','18050404',4,'TF33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000255,'TF34 炼钢机械与生产自动化',180000250,'TF34','18050405',4,'TF34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000256,'TF35 有色冶金机械与生产自动化',180000250,'TF35','18050406',4,'TF35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000257,'TF37 粉末冶金机械与生产自动化',180000250,'TF37','18050407',4,'TF37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000258,'TF4 钢铁冶炼（黑色金属冶炼）（总论）',180000229,'TF4','180505',3,'TF4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000259,'TF5 炼铁',180000229,'TF5','180506',3,'TF5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000260,'TF51 理论与计算',180000259,'TF51','18050601',4,'TF51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000261,'TF52 原材料',180000259,'TF52','18050602',4,'TF52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000262,'TF53 高炉熔冶过程',180000259,'TF53','18050603',4,'TF53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000263,'TF54 高炉操作',180000259,'TF54','18050604',4,'TF54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000264,'TF55 铁矿石直接还原',180000259,'TF55','18050605',4,'TF55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000265,'TF56 其他炼铁法',180000259,'TF56','18050606',4,'TF56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000266,'TF57 炼铁炉（高炉）',180000259,'TF57','18050607',4,'TF57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000267,'TF58 炼铁厂',180000259,'TF58','18050608',4,'TF58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000268,'TF59 炼铁产品',180000259,'TF59','18050609',4,'TF59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000269,'TF6 铁合金冶炼',180000229,'TF6','180507',3,'TF6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000270,'TF61 理论和计算',180000269,'TF61','18050701',4,'TF61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000271,'TF62 原材料',180000269,'TF62','18050702',4,'TF62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000272,'TF63 冶炼方法',180000269,'TF63','18050703',4,'TF63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000273,'TF64 各种铁合金冶炼',180000269,'TF64','18050704',4,'TF64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000274,'TF7 炼钢',180000229,'TF7','180508',3,'TF7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000275,'TF70 一般性问题',180000274,'TF70','18050801',4,'TF70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000276,'TF71 转炉炼钢',180000274,'TF71','18050802',4,'TF71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000277,'TF72 氧气转炉炼钢',180000274,'TF72','18050803',4,'TF72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000278,'TF73 平炉炼钢',180000274,'TF73','18050804',4,'TF73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000279,'TF741 电炉炼钢',180000274,'TF741','18050805',4,'TF741');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000280,'TF742 混合炼钢',180000274,'TF742','18050806',4,'TF742');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000281,'[TF743] 真空炼钢',180000274,'[TF743]','18050807',4,'TF743');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000282,'[TF744] 钢的电渣重熔',180000274,'[TF744]','18050808',4,'TF744');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000283,'TF746 其他炼钢法',180000274,'TF746','18050809',4,'TF746');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000284,'TF747 早期炼钢法',180000274,'TF747','18050810',4,'TF747');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000285,'TF748 炼钢炉',180000274,'TF748','18050811',4,'TF748');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000286,'TF758 炼钢厂',180000274,'TF758','18050812',4,'TF758');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000287,'TF76 各种钢的冶炼',180000274,'TF76','18050813',4,'TF76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000288,'TF769 钢液二次精炼和炉外处理',180000274,'TF769','18050814',4,'TF769');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000289,'TF77 铸锭',180000274,'TF77','18050815',4,'TF77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000290,'TF79 其他黑色金属冶炼',180000229,'TF79','180509',3,'TF79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000291,'TF791 炼铬',180000290,'TF791','18050901',4,'TF791');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000292,'TF792 炼锰',180000290,'TF792','18050902',4,'TF792');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000293,'TF8 有色金属冶炼',180000229,'TF8','180510',3,'TF8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000294,'TF80 一般性问题',180000293,'TF80','18051001',4,'TF80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000295,'TF81 重金属冶炼',180000293,'TF81','18051002',4,'TF81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000296,'TF82 轻金属冶炼',180000293,'TF82','18051003',4,'TF82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000297,'TF83 贵金属及铂族金属冶炼',180000293,'TF83','18051004',4,'TF83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000298,'TF84 稀有金属冶炼',180000293,'TF84','18051005',4,'TF84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000299,'[TF88] 放射性元素冶炼',180000293,'[TF88]','18051006',4,'TF88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000300,'[TF89] 半导体元素冶炼',180000293,'[TF89]','18051007',4,'TF89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000301,'TG 金属学与金属工艺',180000001,'TG','1806',2,'TG');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000302,'TG1 金属学与热处理',180000301,'TG1','180601',3,'TG1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000303,'TG11 金属学（物理冶金）',180000302,'TG11','18060101',4,'TG11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000304,'TG13 合金学与各种性质合金',180000302,'TG13','18060102',4,'TG13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000305,'TG14 金属材料',180000302,'TG14','18060103',4,'TG14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000306,'TG15 热处理',180000302,'TG15','18060104',4,'TG15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000307,'TG17 金属腐蚀与保护、金属表面处理',180000302,'TG17','18060105',4,'TG17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000308,'TG2 铸造',180000301,'TG2','180602',3,'TG2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000309,'TG21 铸造理论',180000308,'TG21','18060201',4,'TG21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000310,'TG22 铸造原材料及配制',180000308,'TG22','18060202',4,'TG22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000311,'TG23 铸造机械设备',180000308,'TG23','18060203',4,'TG23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000312,'TG24 铸造工艺',180000308,'TG24','18060204',4,'TG24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000313,'TG249 特种铸造',180000308,'TG249','18060205',4,'TG249');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000314,'TG25 铸铁件铸造',180000308,'TG25','18060206',4,'TG25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000315,'TG26 钢件铸造',180000308,'TG26','18060207',4,'TG26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000316,'TG27 合金铸造',180000308,'TG27','18060208',4,'TG27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000317,'TG28 铸造车间（厂）',180000308,'TG28','18060209',4,'TG28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000318,'TG29 有色金属铸造',180000308,'TG29','18060210',4,'TG29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000319,'TG3 金属压力加工',180000301,'TG3','180603',3,'TG3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000320,'TG30 一般性问题',180000319,'TG30','18060301',4,'TG30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000321,'TG31 锻造、锻压与锻工',180000319,'TG31','18060302',4,'TG31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000322,'TG33 轧制',180000319,'TG33','18060303',4,'TG33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000323,'TG35 拉制、拉拔',180000319,'TG35','18060304',4,'TG35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000324,'TG37 挤压',180000319,'TG37','18060305',4,'TG37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000325,'TG38 冷冲压（钣金加工）',180000319,'TG38','18060306',4,'TG38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000326,'TG39 高能成型',180000319,'TG39','18060307',4,'TG39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000327,'TG4 焊接、金属切割及金属粘接',180000301,'TG4','180604',3,'TG4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000328,'TG40 焊接一般性问题',180000327,'TG40','18060401',4,'TG40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000329,'TG42 焊接材料',180000327,'TG42','18060402',4,'TG42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000330,'TG43 焊接设备',180000327,'TG43','18060403',4,'TG43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000331,'TG44 焊接工艺',180000327,'TG44','18060404',4,'TG44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000332,'TG47 焊接的应用',180000327,'TG47','18060405',4,'TG47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000333,'TG48 金属切割及设备',180000327,'TG48','18060406',4,'TG48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000334,'TG49 粘接、胶接',180000327,'TG49','18060407',4,'TG49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000335,'TG5 金属切削加工及机床',180000301,'TG5','180605',3,'TG5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000336,'TG50 一般性问题',180000335,'TG50','18060501',4,'TG50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000337,'TG51 车削加工及车床（旋床）',180000335,'TG51','18060502',4,'TG51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000338,'TG52 钻削加工及钻床',180000335,'TG52','18060503',4,'TG52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000339,'TG53 镗削加工及镗床',180000335,'TG53','18060504',4,'TG53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000340,'TG54 铣削加工及铣床',180000335,'TG54','18060505',4,'TG54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000341,'TG55 刨削加工、刨床与插床（立刨）',180000335,'TG55','18060506',4,'TG55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000342,'TG56 锯削加工与锯床、锉床',180000335,'TG56','18060507',4,'TG56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000343,'TG57 拉削加工与拉床',180000335,'TG57','18060508',4,'TG57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000344,'TG58 磨削加工与磨床',180000335,'TG58','18060509',4,'TG58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000345,'TG61 齿轮加工及齿轮机床',180000335,'TG61','18060510',4,'TG61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000346,'TG62 螺纹加工及螺纹加工机床',180000335,'TG62','18060511',4,'TG62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000347,'TG63 刻线加工及刻线机',180000335,'TG63','18060512',4,'TG63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000348,'TG64 仪表加工及仪表机床',180000335,'TG64','18060513',4,'TG64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000349,'TG65 组合机床及其加工',180000335,'TG65','18060514',4,'TG65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000350,'TG659 程序控制机床、数控机床及其加工',180000335,'TG659','18060515',4,'TG659');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000351,'TG66 特种加工机床及其加工',180000335,'TG66','18060516',4,'TG66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000352,'TG68 机床加工生产自动化',180000335,'TG68','18060517',4,'TG68');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000353,'TG7 刀具、磨料、磨具、夹具、模具和手工具',180000301,'TG7','180606',3,'TG7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000354,'TG70 一般性问题',180000353,'TG70','18060601',4,'TG70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000355,'TG71 刀具',180000353,'TG71','18060602',4,'TG71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000356,'TG73 磨料',180000353,'TG73','18060603',4,'TG73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000357,'TG74 磨具、研具',180000353,'TG74','18060604',4,'TG74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000358,'TG75 夹具',180000353,'TG75','18060605',4,'TG75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000359,'TG76 模具',180000353,'TG76','18060606',4,'TG76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000360,'[TG78] 手工具',180000353,'[TG78]','18060607',4,'TG78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000361,'TG8 公差与技术测量及机械量仪',180000301,'TG8','180607',3,'TG8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000362,'TG80 一般性问题',180000361,'TG80','18060701',4,'TG80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000363,'TG81 长度测量及其量仪',180000361,'TG81','18060702',4,'TG81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000364,'TG82 角度测量及其量仪',180000361,'TG82','18060703',4,'TG82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000365,'TG83 形位偏差测量及其量仪',180000361,'TG83','18060704',4,'TG83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000366,'TG84 表面光洁度(表面粗糙度)的测量及其量仪',180000361,'TG84','18060705',4,'TG84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000367,'TG85 螺纹测量及其量仪',180000361,'TG85','18060706',4,'TG85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000368,'TG86 齿轮测量及其量仪',180000361,'TG86','18060707',4,'TG86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000369,'TG87 自动量仪',180000361,'TG87','18060708',4,'TG87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000370,'[TG88] 光学量仪',180000361,'[TG88]','18060709',4,'TG88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000371,'TG9 钳工工艺与装配工艺',180000301,'TG9','180608',3,'TG9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000372,'TG91 基本理论',180000371,'TG91','18060801',4,'TG91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000373,'TG93 钳工工作法及其装备',180000371,'TG93','18060802',4,'TG93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000374,'TG95 机器装配、机器安装法',180000371,'TG95','18060803',4,'TG95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000375,'TG96 包装技术与产品标识',180000371,'TG96','18060804',4,'TG96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000376,'TH 机械、仪表工业',180000001,'TH','1807',2,'TH');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000377,'[TH-9] 机械、仪表工业经济',180000376,'[TH-9]','18070101',4,'TH-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000378,'TH11 机械学（机械设计基础理论）',180000376,'TH11','180702',3,'TH11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000379,'TH111 机械原理',180000378,'TH111','18070201',4,'TH111');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000380,'TH112 机构学',180000378,'TH112','18070202',4,'TH112');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000381,'TH113 机械动力学',180000378,'TH113','18070203',4,'TH113');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000382,'TH114 机械强度',180000378,'TH114','18070204',4,'TH114');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000383,'TH115 机械精确度',180000378,'TH115','18070205',4,'TH115');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000384,'TH117 机械摩擦、磨损与润滑',180000378,'TH117','18070206',4,'TH117');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000385,'TH12 机械设计、计算与制图',180000376,'TH12','180703',3,'TH12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000386,'TH121 标准、规格',180000385,'TH121','18070301',4,'TH121');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000387,'TH122 机械设计',180000385,'TH122','18070302',4,'TH122');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000388,'TH123 机械计算',180000385,'TH123','18070303',4,'TH123');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000389,'[TH124] 机械公差、配合与技术测量',180000385,'[TH124]','18070304',4,'TH124');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000390,'TH126 机械制图',180000385,'TH126','18070305',4,'TH126');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000391,'TH128 机械模型',180000385,'TH128','18070306',4,'TH128');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000392,'TH13 机械零件及传动装置',180000376,'TH13','180704',3,'TH13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000393,'TH131 联接及联接零件',180000392,'TH131','18070401',4,'TH131');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000394,'TH132 机械传动机构',180000392,'TH132','18070402',4,'TH132');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000395,'TH133 转动机件',180000392,'TH133','18070403',4,'TH133');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000396,'TH134 控制机件',180000392,'TH134','18070404',4,'TH134');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000397,'TH135 弹簧',180000392,'TH135','18070405',4,'TH135');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000398,'TH136 其他机械元件',180000392,'TH136','18070406',4,'TH136');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000399,'TH137 液压传动',180000392,'TH137','18070407',4,'TH137');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000400,'TH138 气压传动',180000392,'TH138','18070408',4,'TH138');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000401,'TH139 其他传动',180000392,'TH139','18070409',4,'TH139');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000402,'TH14 机械制造用材料',180000376,'TH14','180705',3,'TH14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000403,'TH140 一般性问题',180000402,'TH140','18070501',4,'TH140');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000404,'TH142 金属材料',180000402,'TH142','18070502',4,'TH142');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000405,'TH145 非金属材料',180000402,'TH145','18070503',4,'TH145');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000406,'TH16 机械制造工艺',180000376,'TH16','180706',3,'TH16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000407,'TH161 机械加工精度理论',180000406,'TH161','18070601',4,'TH161');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000408,'TH162 工艺设计',180000406,'TH162','18070602',4,'TH162');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000409,'TH163 成组工艺',180000406,'TH163','18070603',4,'TH163');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000410,'TH164 计算机辅助机械制造',180000406,'TH164','18070604',4,'TH164');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000411,'TH165 柔性制造系统及柔性制造单元',180000406,'TH165','18070605',4,'TH165');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000412,'TH166 计算机集成制造',180000406,'TH166','18070606',4,'TH166');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000413,'TH17 机械运行与维修',180000376,'TH17','180707',3,'TH17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000414,'TH18 机械工厂（车间）',180000376,'TH18','180708',3,'TH18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000415,'TH181 规划与设计',180000414,'TH181','18070801',4,'TH181');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000416,'TH182 设备安装',180000414,'TH182','18070802',4,'TH182');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000417,'TH183 力能供应',180000414,'TH183','18070803',4,'TH183');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000418,'TH184 空调与照明',180000414,'TH184','18070804',4,'TH184');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000419,'TH185 给水与排水',180000414,'TH185','18070805',4,'TH185');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000420,'TH186 生产技术管理',180000414,'TH186','18070806',4,'TH186');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000421,'TH187 贮运',180000414,'TH187','18070807',4,'TH187');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000422,'TH188 生产技术安全与卫生',180000414,'TH188','18070808',4,'TH188');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000423,'TH2 起重机械与运输机械',180000376,'TH2','180709',3,'TH2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000424,'TH21 起重机械',180000423,'TH21','18070901',4,'TH21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000425,'TH22 运输机械',180000423,'TH22','18070902',4,'TH22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000426,'TH24 装卸机械',180000423,'TH24','18070903',4,'TH24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000427,'TH3 泵',180000376,'TH3','180710',3,'TH3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000428,'TH31 叶片式泵',180000427,'TH31','18071001',4,'TH31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000429,'TH32 容积泵',180000427,'TH32','18071002',4,'TH32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000430,'TH33 内燃泵',180000427,'TH33','18071003',4,'TH33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000431,'TH34 水锤泵',180000427,'TH34','18071004',4,'TH34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000432,'TH35 电磁泵（液体金属泵）',180000427,'TH35','18071005',4,'TH35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000433,'[TH36] 真空泵',180000427,'[TH36]','18071006',4,'TH36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000434,'TH38 各种用途泵',180000427,'TH38','18071007',4,'TH38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000435,'TH4 气体压缩与输送机械',180000376,'TH4','180711',3,'TH4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000436,'TH41 压缩空气工程',180000435,'TH41','18071101',4,'TH41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000437,'[TH42] 风扇',180000435,'[TH42]','18071102',4,'TH42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000438,'TH43 通风机',180000435,'TH43','18071103',4,'TH43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000439,'TH44 鼓风机',180000435,'TH44','18071104',4,'TH44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000440,'TH45 压缩机、压气机',180000435,'TH45','18071105',4,'TH45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000441,'TH47 其他气动工具',180000435,'TH47','18071106',4,'TH47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000442,'TH48 各种用途气体压缩输送机械',180000435,'TH48','18071107',4,'TH48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000443,'TH49 压力容器',180000435,'TH49','18071108',4,'TH49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000444,'TH6 专用机械与设备',180000376,'TH6','180712',3,'TH6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000445,'TH69 其他专用机械与设备',180000444,'TH69','18071201',4,'TH69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000446,'TH7 仪器、仪表',180000376,'TH7','180713',3,'TH7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000447,'TH70 一般性问题',180000446,'TH70','18071301',4,'TH70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000448,'TH71 计量仪器',180000446,'TH71','18071302',4,'TH71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000449,'TH72 坐标器、计算机具、计数器',180000446,'TH72','18071303',4,'TH72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000450,'TH73 物理学与力学一般仪器',180000446,'TH73','18071304',4,'TH73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000451,'TH74 光学仪器',180000446,'TH74','18071305',4,'TH74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000452,'TH75 天文仪器',180000446,'TH75','18071306',4,'TH75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000453,'TH76 地球科学仪器',180000446,'TH76','18071307',4,'TH76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000454,'TH77 医药卫生器械',180000446,'TH77','18071308',4,'TH77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000455,'TH79 生物科学与农林科学仪器',180000446,'TH79','18071309',4,'TH79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000456,'TH81 热工量的测量仪表',180000446,'TH81','18071310',4,'TH81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000457,'TH82 力学量测量仪表',180000446,'TH82','18071311',4,'TH82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000458,'TH83 成分分析仪器',180000446,'TH83','18071312',4,'TH83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000459,'{TH84} 质谱仪',180000446,'{TH84}','18071313',4,'TH84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000460,'TH841 波谱仪',180000446,'TH841','18071314',4,'TH841');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000461,'TH842 能谱仪',180000446,'TH842','18071315',4,'TH842');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000462,'TH843 质谱仪',180000446,'TH843','18071316',4,'TH843');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000463,'TH85 显示仪表',180000446,'TH85','18071317',4,'TH85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000464,'TH86 工业自动化仪表',180000446,'TH86','18071318',4,'TH86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000465,'TH87 材料试验机与试验仪器',180000446,'TH87','18071319',4,'TH87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000466,'TH89 其他仪器仪表',180000446,'TH89','18071320',4,'TH89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000467,'TJ 武器工业',180000001,'TJ','1808',2,'TJ');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000468,'[TJ-9] 武器工业经济',180000467,'[TJ-9]','18080101',4,'TJ-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000469,'TJ0 一般性问题',180000467,'TJ0','180802',3,'TJ0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000470,'TJ01 理论与试验',180000469,'TJ01','18080201',4,'TJ01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000471,'TJ02 设计、计算、制图',180000469,'TJ02','18080202',4,'TJ02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000472,'TJ03 结构',180000469,'TJ03','18080203',4,'TJ03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000473,'TJ04 材料',180000469,'TJ04','18080204',4,'TJ04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000474,'TJ05 制造工艺及设备',180000469,'TJ05','18080205',4,'TJ05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000475,'TJ06 测试技术及设施',180000469,'TJ06','18080206',4,'TJ06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000476,'[TJ07] 保养与维修',180000469,'[TJ07]','18080207',4,'TJ07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000477,'TJ08 制造厂',180000469,'TJ08','18080208',4,'TJ08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000478,'TJ089 储运、销毁',180000469,'TJ089','18080209',4,'TJ089');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000479,'TJ2 枪械',180000467,'TJ2','180803',3,'TJ2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000480,'TJ20 一般性问题',180000479,'TJ20','18080301',4,'TJ20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000481,'TJ21 手枪、转轮枪',180000479,'TJ21','18080302',4,'TJ21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000482,'TJ22 步枪、马枪',180000479,'TJ22','18080303',4,'TJ22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000483,'TJ23 冲锋枪',180000479,'TJ23','18080304',4,'TJ23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000484,'TJ24 轻、重机枪与两用机枪',180000479,'TJ24','18080305',4,'TJ24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000485,'TJ25 高射机枪、大口径机枪',180000479,'TJ25','18080306',4,'TJ25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000486,'TJ26 坦克机枪、舰用机枪、航空机枪',180000479,'TJ26','18080307',4,'TJ26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000487,'TJ27 特种用途与特殊性能枪',180000479,'TJ27','18080308',4,'TJ27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000488,'TJ279 其他枪械',180000479,'TJ279','18080309',4,'TJ279');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000489,'TJ28 冷兵器',180000479,'TJ28','18080310',4,'TJ28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000490,'TJ29 榴弹发射器、枪榴弹发射器',180000479,'TJ29','18080311',4,'TJ29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000491,'TJ3 火炮',180000467,'TJ3','180804',3,'TJ3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000492,'TJ30 一般性问题',180000491,'TJ30','18080401',4,'TJ30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000493,'TJ31 迫击炮',180000491,'TJ31','18080402',4,'TJ31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000494,'TJ32 无座力炮',180000491,'TJ32','18080403',4,'TJ32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000495,'TJ33 榴弹炮',180000491,'TJ33','18080404',4,'TJ33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000496,'TJ34 加农炮',180000491,'TJ34','18080405',4,'TJ34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000497,'TJ35 高射炮、高射机关炮',180000491,'TJ35','18080406',4,'TJ35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000498,'TJ36 超级炮',180000491,'TJ36','18080407',4,'TJ36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000499,'TJ37 反坦克炮',180000491,'TJ37','18080408',4,'TJ37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000500,'TJ38 坦克炮',180000491,'TJ38','18080409',4,'TJ38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000501,'TJ391 舰炮',180000491,'TJ391','18080410',4,'TJ391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000502,'TJ392 航空炮',180000491,'TJ392','18080411',4,'TJ392');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000503,'TJ393 火箭炮',180000491,'TJ393','18080412',4,'TJ393');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000504,'TJ394 海岸炮',180000491,'TJ394','18080413',4,'TJ394');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000505,'TJ395 铁道炮',180000491,'TJ395','18080414',4,'TJ395');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000506,'TJ396 原子炮',180000491,'TJ396','18080415',4,'TJ396');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000507,'TJ399 其他',180000491,'TJ399','18080416',4,'TJ399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000508,'TJ4 弹药、引信、火工品',180000467,'TJ4','180805',3,'TJ4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000509,'TJ41 弹药',180000508,'TJ41','18080501',4,'TJ41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000510,'TJ43 引信',180000508,'TJ43','18080502',4,'TJ43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000511,'TJ45 火工品',180000508,'TJ45','18080503',4,'TJ45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000512,'TJ5 爆破器材、烟火器材、火炸药',180000467,'TJ5','180806',3,'TJ5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000513,'TJ51 爆破器材',180000512,'TJ51','18080601',4,'TJ51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000514,'TJ53 烟火器材',180000512,'TJ53','18080602',4,'TJ53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000515,'[TJ55] 火炸药',180000512,'[TJ55]','18080603',4,'TJ55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000516,'TJ6 水中兵器',180000467,'TJ6','180807',3,'TJ6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000517,'TJ61 水雷',180000516,'TJ61','18080701',4,'TJ61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000518,'TJ63 鱼雷及其发射装置',180000516,'TJ63','18080702',4,'TJ63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000519,'TJ65 深水炸弹及发射装置',180000516,'TJ65','18080703',4,'TJ65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000520,'TJ67 反潜武器',180000516,'TJ67','18080704',4,'TJ67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000521,'TJ7 火箭、导弹',180000467,'TJ7','180808',3,'TJ7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000522,'TJ71 火箭筒、火箭炮、火箭弹',180000521,'TJ71','18080801',4,'TJ71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000523,'TJ76 导弹',180000521,'TJ76','18080802',4,'TJ76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000524,'TJ8 战车、战舰、战机、航天武器',180000467,'TJ8','180809',3,'TJ8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000525,'TJ81 战车',180000524,'TJ81','18080901',4,'TJ81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000526,'[TJ83] 战舰',180000524,'[TJ83]','18080902',4,'TJ83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000527,'[TJ85] 战机',180000524,'[TJ85]','18080903',4,'TJ85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000528,'TJ86 航天武器',180000524,'TJ86','18080904',4,'TJ86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000529,'TJ9 核武器与其他特种武器及其防护设备',180000467,'TJ9','180810',3,'TJ9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000530,'TJ91 核武器及防护设备',180000529,'TJ91','18081001',4,'TJ91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000531,'TJ92 化学（毒物）武器及防护设备',180000529,'TJ92','18081002',4,'TJ92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000532,'TJ93 生物武器及防护设备',180000529,'TJ93','18081003',4,'TJ93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000533,'TJ95 激光武器及防护设备',180000529,'TJ95','18081004',4,'TJ95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000534,'TJ96 声学武器与防护武器',180000529,'TJ96','18081005',4,'TJ96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000535,'TJ97 等离子武器与防护武器',180000529,'TJ97','18081006',4,'TJ97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000536,'TJ99 其他特种武器与防护武器',180000529,'TJ99','18081007',4,'TJ99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000537,'TK 能源与动力工程',180000001,'TK','1809',2,'TK');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000538,'[TK-9] 能源与动力工业经济',180000537,'[TK-9]','18090101',4,'TK-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000539,'TK0 一般性问题',180000537,'TK0','180902',3,'TK0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000540,'TK01 能源',180000539,'TK01','18090201',4,'TK01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000541,'TK02 蓄能技术',180000539,'TK02','18090202',4,'TK02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000542,'TK05 动力机械',180000539,'TK05','18090203',4,'TK05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000543,'TK08 动力厂',180000539,'TK08','18090204',4,'TK08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000544,'[TK09] 三废处理与综合利用',180000539,'[TK09]','18090205',4,'TK09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000545,'TK1 热力工程、热机',180000537,'TK1','180903',3,'TK1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000546,'TK11 热能',180000545,'TK11','18090301',4,'TK11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000547,'TK12 热力工程理论',180000545,'TK12','18090302',4,'TK12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000548,'TK14 气体透平（涡轮机）',180000545,'TK14','18090303',4,'TK14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000549,'TK16 燃料与燃烧',180000545,'TK16','18090304',4,'TK16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000550,'TK17 工业用热工设备',180000545,'TK17','18090305',4,'TK17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000551,'TK2 蒸汽动力工程',180000537,'TK2','180904',3,'TK2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000552,'TK21 蒸汽理论',180000551,'TK21','18090401',4,'TK21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000553,'TK22 蒸汽锅炉',180000551,'TK22','18090402',4,'TK22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000554,'TK24 蒸汽机',180000551,'TK24','18090403',4,'TK24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000555,'TK26 蒸汽轮机（蒸汽透平、汽轮机）',180000551,'TK26','18090404',4,'TK26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000556,'TK28 蒸汽动力工厂（车间）',180000551,'TK28','18090405',4,'TK28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000557,'TK3 热工量测和热工自动控制',180000537,'TK3','180905',3,'TK3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000558,'TK31 量测技术及仪表',180000557,'TK31','18090501',4,'TK31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000559,'TK32 热工自动控制',180000557,'TK32','18090502',4,'TK32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000560,'TK36 安装、调整',180000557,'TK36','18090503',4,'TK36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000561,'TK37 运行',180000557,'TK37','18090504',4,'TK37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000562,'TK38 检修、维护',180000557,'TK38','18090505',4,'TK38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000563,'TK39 热工量测和热工自动控制的应用',180000557,'TK39','18090506',4,'TK39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000564,'TK4 内燃机',180000537,'TK4','180906',3,'TK4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000565,'TK40 一般性问题',180000564,'TK40','18090601',4,'TK40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000566,'TK41 汽油机',180000564,'TK41','18090602',4,'TK41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000567,'TK42 柴油机',180000564,'TK42','18090603',4,'TK42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000568,'TK43 气体燃料内燃机',180000564,'TK43','18090604',4,'TK43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000569,'TK44 复合式发动机',180000564,'TK44','18090605',4,'TK44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000570,'TK45 旋转活塞式内燃机',180000564,'TK45','18090606',4,'TK45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000571,'TK46 其他燃料的内燃机',180000564,'TK46','18090607',4,'TK46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000572,'TK47 燃气轮机（燃气透平）',180000564,'TK47','18090608',4,'TK47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000573,'[TK48] 喷气推进器',180000564,'[TK48]','18090609',4,'TK48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000574,'[TK49] 火箭发动机',180000564,'[TK49]','18090610',4,'TK49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000575,'TK5 特殊热能及其机械',180000537,'TK5','180907',3,'TK5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000576,'TK51 太阳能技术',180000575,'TK51','18090701',4,'TK51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000577,'TK52 地下热能、地下热能机械',180000575,'TK52','18090702',4,'TK52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000578,'TK6 生物能及其利用',180000537,'TK6','180908',3,'TK6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000579,'TK7 水能、水力机械',180000537,'TK7','180909',3,'TK7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000580,'TK71 水能',180000579,'TK71','18090901',4,'TK71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000581,'TK72 水力机械理论',180000579,'TK72','18090902',4,'TK72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000582,'TK73 水力原动机、水轮机',180000579,'TK73','18090903',4,'TK73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000583,'TK79 水能的利用',180000579,'TK79','18090904',4,'TK79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000584,'TK8 风能、风力机械',180000537,'TK8','180910',3,'TK8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000585,'TK81 风能',180000584,'TK81','18091001',4,'TK81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000586,'TK82 风能的贮存',180000584,'TK82','18091002',4,'TK82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000587,'TK83 风力机械和设备',180000584,'TK83','18091003',4,'TK83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000588,'TK89 风能的利用',180000584,'TK89','18091004',4,'TK89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000589,'TK91 氢能及其利用',180000537,'TK91','180911',3,'TK91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000590,'TL 原子能技术',180000001,'TL','1810',2,'TL');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000591,'[TL-9] 原子能技术经济',180000590,'[TL-9]','18100101',4,'TL-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000592,'TL1 基础理论',180000590,'TL1','181002',3,'TL1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000593,'[TL11] 原子能物理',180000592,'[TL11]','18100201',4,'TL11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000594,'[TL12] 放射性化学',180000592,'[TL12]','18100202',4,'TL12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000595,'[TL13] 辐射化学',180000592,'[TL13]','18100203',4,'TL13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000596,'TL2 核燃料及其生产',180000590,'TL2','181003',3,'TL2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000597,'TL21 铀燃料的生产',180000596,'TL21','18100301',4,'TL21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000598,'TL22 钍燃料的生产',180000596,'TL22','18100302',4,'TL22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000599,'TL24 乏燃料后处理',180000596,'TL24','18100303',4,'TL24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000600,'TL25 铀和其他稳定同位素的分离',180000596,'TL25','18100304',4,'TL25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000601,'TL27 核燃料的分析',180000596,'TL27','18100305',4,'TL27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000602,'TL28 核燃料生产用辅助物料及其分析',180000596,'TL28','18100306',4,'TL28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000603,'TL291 热核燃料的生产',180000596,'TL291','18100307',4,'TL291');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000604,'TL292 热室及其设备',180000596,'TL292','18100308',4,'TL292');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000605,'TL3 核反应堆工程',180000590,'TL3','181004',3,'TL3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000606,'TL31 反应堆基础理论',180000605,'TL31','18100401',4,'TL31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000607,'TL32 反应堆物理及其设计、计算',180000605,'TL32','18100402',4,'TL32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000608,'TL33 反应堆热工水力学及其设计、计算',180000605,'TL33','18100403',4,'TL33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000609,'TL34 反应堆材料及其性能',180000605,'TL34','18100404',4,'TL34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000610,'TL35 反应堆部件及其设计、制造',180000605,'TL35','18100405',4,'TL35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000611,'TL36 反应堆安全与控制',180000605,'TL36','18100406',4,'TL36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000612,'TL37 反应堆设计、建造、安装、实验与测量',180000605,'TL37','18100407',4,'TL37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000613,'TL38 反应堆运行与维修',180000605,'TL38','18100408',4,'TL38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000614,'TL4 各种核反应堆、核电厂',180000590,'TL4','181005',3,'TL4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000615,'TL41 核反应堆：按用途分',180000614,'TL41','18100501',4,'TL41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000616,'TL42 核反应堆：按冷却剂分',180000614,'TL42','18100502',4,'TL42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000617,'TL43 核反应堆：按中子能谱分',180000614,'TL43','18100503',4,'TL43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000618,'TL44 核反应堆：按燃料分',180000614,'TL44','18100504',4,'TL44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000619,'TL45 核反应堆：按结构分',180000614,'TL45','18100505',4,'TL45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000620,'TL46 裂变、聚变混合反应堆',180000614,'TL46','18100506',4,'TL46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000621,'[TL48] 核电厂（核电站）',180000614,'[TL48]','18100507',4,'TL48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000622,'TL5 加速器',180000590,'TL5','181006',3,'TL5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000623,'TL50 一般性问题',180000622,'TL50','18100601',4,'TL50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000624,'TL51 高压倍加器',180000622,'TL51','18100602',4,'TL51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000625,'TL52 静电加速器、串列式静电加速器',180000622,'TL52','18100603',4,'TL52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000626,'TL53 直线加速器',180000622,'TL53','18100604',4,'TL53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000627,'TL54 循环加速器',180000622,'TL54','18100605',4,'TL54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000628,'TL55 电子束聚变加速器',180000622,'TL55','18100606',4,'TL55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000629,'TL56 重离子加速器',180000622,'TL56','18100607',4,'TL56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000630,'TL57 粒子工厂',180000622,'TL57','18100608',4,'TL57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000631,'TL58 粒子束聚变加速器',180000622,'TL58','18100609',4,'TL58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000632,'TL593 交变梯度强聚焦加速器',180000622,'TL593','18100610',4,'TL593');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000633,'TL594 储存环（对头碰）',180000622,'TL594','18100611',4,'TL594');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000634,'TL6 受控热核反应（聚变反应理论及实验装置）',180000590,'TL6','181007',3,'TL6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000635,'TL61 理论',180000634,'TL61','18100701',4,'TL61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000636,'TL62 聚变工程技术',180000634,'TL62','18100702',4,'TL62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000637,'TL63 热核装置',180000634,'TL63','18100703',4,'TL63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000638,'TL64 热核反应堆（聚变堆）',180000634,'TL64','18100704',4,'TL64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000639,'TL65 等离子体诊断（测量）',180000634,'TL65','18100705',4,'TL65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000640,'TL67 实验技术与设备',180000634,'TL67','18100706',4,'TL67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000641,'TL69 热核反应堆安全与环境',180000634,'TL69','18100707',4,'TL69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000642,'TL7 辐射防护',180000590,'TL7','181008',3,'TL7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000643,'[TL71] 防护理论',180000642,'[TL71]','18100801',4,'TL71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000644,'[TL72] 辐射剂量学',180000642,'[TL72]','18100802',4,'TL72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000645,'TL73 辐射事故',180000642,'TL73','18100803',4,'TL73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000646,'TL75 核设施和铀矿山的辐射监测防护和卫生',180000642,'TL75','18100804',4,'TL75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000647,'TL76 核试验的防护',180000642,'TL76','18100805',4,'TL76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000648,'TL77 辐射源的防护',180000642,'TL77','18100806',4,'TL77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000649,'TL8 粒子探测技术、辐射探测技术与核仪器仪表',180000590,'TL8','181009',3,'TL8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000650,'TL81 辐射探测技术和仪器仪表',180000649,'TL81','18100901',4,'TL81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000651,'TL82 核电子学仪器',180000649,'TL82','18100902',4,'TL82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000652,'TL84 放射性计量学与计量技术',180000649,'TL84','18100903',4,'TL84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000653,'TL91 核爆炸',180000590,'TL91','181010',3,'TL91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000654,'TL92 放射性同位素的生产与制备',180000590,'TL92','181011',3,'TL92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000655,'TL92+1 放射性同位素生产方式',180000654,'TL92+1','18101101',4,'TL921');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000656,'TL92+2 放射性同位素的分离提取',180000654,'TL92+2','18101102',4,'TL922');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000657,'TL92+3 标记化合物的制备',180000654,'TL92+3','18101103',4,'TL923');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000658,'TL929 辐射源',180000590,'TL929','181012',3,'TL929');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000659,'TL93 放射性物质的包装、运输和贮存',180000590,'TL93','181013',3,'TL93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000660,'TL93+1 包装方法和设备',180000659,'TL93+1','18101301',4,'TL931');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000661,'TL93+2 运输方式和设备',180000659,'TL93+2','18101302',4,'TL932');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000662,'TL93+3 贮存',180000659,'TL93+3','18101303',4,'TL933');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000663,'TL94 放射性废物管理及综合利用',180000590,'TL94','181014',3,'TL94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000664,'TL941 放射性废物及其处理',180000663,'TL941','18101401',4,'TL941');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000665,'TL942 放射性废物的处置',180000663,'TL942','18101402',4,'TL942');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000666,'TL943 核设施退役',180000663,'TL943','18101403',4,'TL943');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000667,'TL944 核设施和设备的去污',180000663,'TL944','18101404',4,'TL944');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000668,'[TL949] 放射性废物的综合利用',180000663,'[TL949]','18101405',4,'TL949');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000669,'TL99 原子能技术的应用',180000590,'TL99','181015',3,'TL99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000670,'TM 电工技术',180000001,'TM','1811',2,'TM');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000671,'[TM-9] 电工技术经济',180000670,'[TM-9]','18110101',4,'TM-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000672,'TM0 一般性问题',180000670,'TM0','181102',3,'TM0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000673,'TM02 电工设计、制图',180000672,'TM02','18110201',4,'TM02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000674,'TM05 电工安装技术',180000672,'TM05','18110202',4,'TM05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000675,'TM07 电工保养、维修',180000672,'TM07','18110203',4,'TM07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000676,'TM08 电工安全',180000672,'TM08','18110204',4,'TM08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000677,'TM1 电工基础理论',180000670,'TM1','181103',3,'TM1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000678,'TM11 电工单位、电工计算',180000677,'TM11','18110301',4,'TM11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000679,'[TM12] 电学、磁学',180000677,'[TM12]','18110302',4,'TM12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000680,'TM13 电路理论',180000677,'TM13','18110303',4,'TM13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000681,'TM14 磁路',180000677,'TM14','18110304',4,'TM14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000682,'TM15 电磁场理论的应用',180000677,'TM15','18110305',4,'TM15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000683,'TM2 电工材料',180000670,'TM2','181104',3,'TM2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000684,'TM20 一般性问题',180000683,'TM20','18110401',4,'TM20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000685,'TM21 绝缘材料、电介质及其制品',180000683,'TM21','18110402',4,'TM21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000686,'TM22 强性介质和压电介质',180000683,'TM22','18110403',4,'TM22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000687,'[TM23] 半导体材料',180000683,'[TM23]','18110404',4,'TM23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000688,'TM24 导电材料及其制品',180000683,'TM24','18110405',4,'TM24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000689,'TM25 微波吸收材料',180000683,'TM25','18110406',4,'TM25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000690,'TM26 超导体、超导体材料',180000683,'TM26','18110407',4,'TM26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000691,'TM27 磁性材料、铁氧体',180000683,'TM27','18110408',4,'TM27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000692,'TM28 电工陶瓷材料',180000683,'TM28','18110409',4,'TM28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000693,'TM3 电机',180000670,'TM3','181105',3,'TM3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000694,'TM30 一般性问题',180000693,'TM30','18110501',4,'TM30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000695,'TM31 发电机、大型发电机组（总论）',180000693,'TM31','18110502',4,'TM31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000696,'TM32 电动机（总论）',180000693,'TM32','18110503',4,'TM32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000697,'TM33 直流电机',180000693,'TM33','18110504',4,'TM33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000698,'TM34 交流电机',180000693,'TM34','18110505',4,'TM34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000699,'TM35 特殊电机',180000693,'TM35','18110506',4,'TM35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000700,'TM36 无接点电机',180000693,'TM36','18110507',4,'TM36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000701,'TM37 超导体电机',180000693,'TM37','18110508',4,'TM37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000702,'TM38 微电机',180000693,'TM38','18110509',4,'TM38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000703,'TM4 变压器、变流器及电抗器',180000670,'TM4','181106',3,'TM4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000704,'TM40 一般性问题',180000703,'TM40','18110601',4,'TM40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000705,'TM41 电力变压器',180000703,'TM41','18110602',4,'TM41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000706,'TM42 变压器：按作用性能分',180000703,'TM42','18110603',4,'TM42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000707,'TM43 变压器：按频率分',180000703,'TM43','18110604',4,'TM43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000708,'TM44 稳定器',180000703,'TM44','18110605',4,'TM44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000709,'TM45 互感器',180000703,'TM45','18110606',4,'TM45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000710,'TM46 变流器',180000703,'TM46','18110607',4,'TM46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000711,'TM47 电抗器',180000703,'TM47','18110608',4,'TM47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000712,'TM5 电器',180000670,'TM5','181107',3,'TM5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000713,'TM50 一般性问题',180000712,'TM50','18110701',4,'TM50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000714,'TM51 高压电器（总论）',180000712,'TM51','18110702',4,'TM51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000715,'TM52 低压电器（总论）',180000712,'TM52','18110703',4,'TM52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000716,'TM53 电容器',180000712,'TM53','18110704',4,'TM53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000717,'TM54 电阻器、电位器',180000712,'TM54','18110705',4,'TM54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000718,'TM55 电感器、线圈、扼流圈',180000712,'TM55','18110706',4,'TM55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000719,'TM56 开关电器、断路器',180000712,'TM56','18110707',4,'TM56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000720,'TM57 控制器、接触器、起动器、电磁铁',180000712,'TM57','18110708',4,'TM57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000721,'TM58 继电器',180000712,'TM58','18110709',4,'TM58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000722,'TM59 成套电器',180000712,'TM59','18110710',4,'TM59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000723,'TM6 发电、发电厂',180000670,'TM6','181108',3,'TM6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000724,'TM60 电能学',180000723,'TM60','18110801',4,'TM60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000725,'TM61 各种发电',180000723,'TM61','18110802',4,'TM61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000726,'TM62 发电厂',180000723,'TM62','18110803',4,'TM62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000727,'TM63 变电所',180000723,'TM63','18110804',4,'TM63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000728,'TM64 配电设备和电气接线',180000723,'TM64','18110805',4,'TM64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000729,'TM7 输配电工程、电力网及电力系统',180000670,'TM7','181109',3,'TM7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000730,'TM71 理论与分析',180000729,'TM71','18110901',4,'TM71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000731,'TM72 输配电技术',180000729,'TM72','18110902',4,'TM72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000732,'TM73 电力系统的调度、管理、通信',180000729,'TM73','18110903',4,'TM73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000733,'TM74 电力系统的模拟与计算',180000729,'TM74','18110904',4,'TM74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000734,'TM75 线路及杆塔',180000729,'TM75','18110905',4,'TM75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000735,'TM76 电力系统的自动化',180000729,'TM76','18110906',4,'TM76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000736,'TM77 电力系统继电保护',180000729,'TM77','18110907',4,'TM77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000737,'TM8 高电压技术',180000670,'TM8','181110',3,'TM8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000738,'TM81 高压安全',180000737,'TM81','18111001',4,'TM81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000739,'TM83 高电压试验设备及测量技术',180000737,'TM83','18111002',4,'TM83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000740,'TM85 高电压绝缘技术',180000737,'TM85','18111003',4,'TM85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000741,'TM86 过电压及其防护',180000737,'TM86','18111004',4,'TM86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000742,'TM89 高电压及大电流技术的应用',180000737,'TM89','18111005',4,'TM89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000743,'TM91 独立电源技术（直接发电）',180000670,'TM91','181111',3,'TM91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000744,'TM910 一般性问题',180000743,'TM910','18111101',4,'TM910');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000745,'TM911 化学电源、电池、燃料电池',180000743,'TM911','18111102',4,'TM911');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000746,'TM912 蓄电池',180000743,'TM912','18111103',4,'TM912');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000747,'TM913 温差电池、温差发电器',180000743,'TM913','18111104',4,'TM913');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000748,'TM914 光电池',180000743,'TM914','18111105',4,'TM914');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000749,'TM915 热离子、热电子换能器',180000743,'TM915','18111106',4,'TM915');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000750,'TM916 磁流体发电',180000743,'TM916','18111107',4,'TM916');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000751,'TM917 电流体发电、电气体发电、超导体发电',180000743,'TM917','18111108',4,'TM917');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000752,'TM918 核能换能器',180000743,'TM918','18111109',4,'TM918');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000753,'TM919 其他独立电源',180000743,'TM919','18111110',4,'TM919');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000754,'TM92 电气化、电能应用',180000670,'TM92','181112',3,'TM92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000755,'TM921 电力拖动（电气传动）',180000754,'TM921','18111201',4,'TM921');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000756,'TM922 电力牵引',180000754,'TM922','18111202',4,'TM922');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000757,'TM923 电气照明',180000754,'TM923','18111203',4,'TM923');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000758,'TM924 电热',180000754,'TM924','18111204',4,'TM924');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000759,'TM925 家用电器及其他电器设备',180000754,'TM925','18111205',4,'TM925');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000760,'[TM926] 农村电气化',180000754,'[TM926]','18111206',4,'TM926');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000761,'TM93 电气测量技术及仪器',180000670,'TM93','181113',3,'TM93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000762,'TM930 一般性问题',180000761,'TM930','18111301',4,'TM930');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000763,'TM931 微波测量及仪表',180000761,'TM931','18111302',4,'TM931');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000764,'TM932 数字式测量及仪表',180000761,'TM932','18111303',4,'TM932');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000765,'TM933 电数量的测量及仪表',180000761,'TM933','18111304',4,'TM933');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000766,'TM934 集中参数、分布参数的测量及仪表',180000761,'TM934','18111305',4,'TM934');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000767,'TM935 频率、波形参数的测量及仪表',180000761,'TM935','18111306',4,'TM935');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000768,'TM936 磁数量测量及仪器',180000761,'TM936','18111307',4,'TM936');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000769,'TM937 电磁场强度（信号强度）测量及仪表',180000761,'TM937','18111308',4,'TM937');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000770,'TM938 复用、较量、记录和模拟测试装置',180000761,'TM938','18111309',4,'TM938');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000771,'TN 无线电电子学、电信技术',180000001,'TN','1812',2,'TN');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000772,'[TN-9] 电子工业经济',180000771,'[TN-9]','18120101',4,'TN-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000773,'TN0 一般性问题',180000771,'TN0','181202',3,'TN0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000774,'TN01 基础理论',180000773,'TN01','18120201',4,'TN01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000775,'TN02 设计、制图',180000773,'TN02','18120202',4,'TN02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000776,'TN03 结构',180000773,'TN03','18120203',4,'TN03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000777,'TN04 材料',180000773,'TN04','18120204',4,'TN04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000778,'TN05 制造工艺及设备',180000773,'TN05','18120205',4,'TN05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000779,'TN06 测试技术及设备',180000773,'TN06','18120206',4,'TN06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000780,'TN07 无线电产品的维修、保养',180000773,'TN07','18120207',4,'TN07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000781,'TN08 无线电工厂',180000773,'TN08','18120208',4,'TN08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000782,'TN1 真空电子技术',180000771,'TN1','181203',3,'TN1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000783,'TN10 一般性问题',180000782,'TN10','18120301',4,'TN10');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000784,'TN11 电子管',180000782,'TN11','18120302',4,'TN11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000785,'TN12 微波电子管',180000782,'TN12','18120303',4,'TN12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000786,'TN13 气体放电器件、离子管',180000782,'TN13','18120304',4,'TN13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000787,'TN14 电子束器件、X射线管、阴极射线管',180000782,'TN14','18120305',4,'TN14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000788,'TN15 光电器件、光电管',180000782,'TN15','18120306',4,'TN15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000789,'TN16 电子光学仪器',180000782,'TN16','18120307',4,'TN16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000790,'TN2 光电子技术、激光技术',180000771,'TN2','181204',3,'TN2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000791,'TN20 一般性问题',180000790,'TN20','18120401',4,'TN20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000792,'TN21 红外技术及仪器',180000790,'TN21','18120402',4,'TN21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000793,'TN22 夜视技术、夜视仪',180000790,'TN22','18120403',4,'TN22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000794,'TN23 紫外技术及仪器',180000790,'TN23','18120404',4,'TN23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000795,'TN24 激光技术、微波激射技术',180000790,'TN24','18120405',4,'TN24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000796,'TN25 波导光学与集成光学',180000790,'TN25','18120406',4,'TN25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000797,'[TN26] 全息术',180000790,'[TN26]','18120407',4,'TN26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000798,'TN27 显示技术',180000790,'TN27','18120408',4,'TN27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000799,'TN29 光电子技术的应用',180000790,'TN29','18120409',4,'TN29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000800,'TN3 半导体技术',180000771,'TN3','181205',3,'TN3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000801,'TN30 一般性问题',180000800,'TN30','18120501',4,'TN30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000802,'TN31 半导体二极管',180000800,'TN31','18120502',4,'TN31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000803,'TN32 半导体三极管（晶体管）',180000800,'TN32','18120503',4,'TN32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000804,'TN335 PNPN四层结构器件',180000800,'TN335','18120504',4,'TN335');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000805,'TN34 晶闸管（可控硅）',180000800,'TN34','18120505',4,'TN34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000806,'TN35 半导体整流器',180000800,'TN35','18120506',4,'TN35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000807,'TN36 半导体光电器件',180000800,'TN36','18120507',4,'TN36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000808,'TN37 半导体热电器件、热敏电阻',180000800,'TN37','18120508',4,'TN37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000809,'TN382 霍尔器件、光磁电探测器件',180000800,'TN382','18120509',4,'TN382');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000810,'TN383 发光器件',180000800,'TN383','18120510',4,'TN383');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000811,'TN384 铁电及压电器件',180000800,'TN384','18120511',4,'TN384');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000812,'TN385 微波半导体器件',180000800,'TN385','18120512',4,'TN385');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000813,'TN386 场效应器件',180000800,'TN386','18120513',4,'TN386');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000814,'TN387 体效应器件',180000800,'TN387','18120514',4,'TN387');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000815,'TN389 其他器件',180000800,'TN389','18120515',4,'TN389');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000816,'TN4 微电子学、集成电路（IC）',180000771,'TN4','181206',3,'TN4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000817,'TN40 一般性问题',180000816,'TN40','18120601',4,'TN40');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000818,'TN41 印刷电路',180000816,'TN41','18120602',4,'TN41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000819,'TN42 微模组件',180000816,'TN42','18120603',4,'TN42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000820,'TN43 半导体集成电路（固体电路）',180000816,'TN43','18120604',4,'TN43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000821,'TN44 膜集成电路',180000816,'TN44','18120605',4,'TN44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000822,'TN45 混合集成电路',180000816,'TN45','18120606',4,'TN45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000823,'TN46 中规模集成电路',180000816,'TN46','18120607',4,'TN46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000824,'TN47 大规模集成电路、超大规模集成电路',180000816,'TN47','18120608',4,'TN47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000825,'TN48 真空集成电路',180000816,'TN48','18120609',4,'TN48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000826,'TN491 光学集成电路（集成光路）',180000816,'TN491','18120610',4,'TN491');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000827,'TN492 专用集成电路',180000816,'TN492','18120611',4,'TN492');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000828,'TN495 功能块（分子电路）',180000816,'TN495','18120612',4,'TN495');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000829,'TN6 电子元件、组件',180000771,'TN6','181207',3,'TN6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000830,'TN60 一般性问题',180000829,'TN60','18120701',4,'TN60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000831,'TN61 微波元件、微波铁氧体元件',180000829,'TN61','18120702',4,'TN61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000832,'TN62 微波传输控制元件',180000829,'TN62','18120703',4,'TN62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000833,'TN63 微波过渡元件',180000829,'TN63','18120704',4,'TN63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000834,'TN64 电声器件',180000829,'TN64','18120705',4,'TN64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000835,'TN65 声光器件',180000829,'TN65','18120706',4,'TN65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000836,'TN7 基本电子电路',180000771,'TN7','181208',3,'TN7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000837,'TN70 一般性问题',180000836,'TN70','18120801',4,'TN70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000838,'TN710 电子电路',180000836,'TN710','18120802',4,'TN710');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000839,'TN711 网络',180000836,'TN711','18120803',4,'TN711');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000840,'TN712 变能器',180000836,'TN712','18120804',4,'TN712');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000841,'TN713 滤波技术、滤波器',180000836,'TN713','18120805',4,'TN713');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000842,'TN715 均衡器、衰减器（衰耗器）',180000836,'TN715','18120806',4,'TN715');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000843,'TN72 放大技术、放大器',180000836,'TN72','18120807',4,'TN72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000844,'TN73 功率合成器',180000836,'TN73','18120808',4,'TN73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000845,'TN74 频率合成技术、频率合成器',180000836,'TN74','18120809',4,'TN74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000846,'TN75 振荡技术、振荡器',180000836,'TN75','18120810',4,'TN75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000847,'TN76 调制技术与调制器、解调技术与解调器',180000836,'TN76','18120811',4,'TN76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000848,'TN77 倍频器、分频器、变频器',180000836,'TN77','18120812',4,'TN77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000849,'TN78 脉冲技术、脉冲电路',180000836,'TN78','18120813',4,'TN78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000850,'TN79 数字电路',180000836,'TN79','18120814',4,'TN79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000851,'TN8 无线电设备、电信设备',180000771,'TN8','181209',3,'TN8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000852,'TN80 一般性问题',180000851,'TN80','18120901',4,'TN80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000853,'TN81 馈线设备（传输线和波导）',180000851,'TN81','18120902',4,'TN81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000854,'TN82 天线',180000851,'TN82','18120903',4,'TN82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000855,'TN83 发送设备、发射机',180000851,'TN83','18120904',4,'TN83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000856,'TN85 接收设备、无线电收音机',180000851,'TN85','18120905',4,'TN85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000857,'TN86 电源',180000851,'TN86','18120906',4,'TN86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000858,'TN87 终端设备',180000851,'TN87','18120907',4,'TN87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000859,'TN91 通信',180000771,'TN91','181210',3,'TN91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000860,'TN911 通信理论',180000859,'TN911','18121001',4,'TN911');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000861,'TN912 电声技术和语音信号处理',180000859,'TN912','18121002',4,'TN912');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000862,'TN913 有线通信、通信线路工程',180000859,'TN913','18121003',4,'TN913');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000863,'TN914 通信系统（传输系统）',180000859,'TN914','18121004',4,'TN914');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000864,'TN915 通信网',180000859,'TN915','18121005',4,'TN915');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000865,'TN916 电话',180000859,'TN916','18121006',4,'TN916');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000866,'TN917 电报、传真',180000859,'TN917','18121007',4,'TN917');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000867,'TN918 通信保密与通信安全',180000859,'TN918','18121008',4,'TN918');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000868,'TN919 数据通信',180000859,'TN919','18121009',4,'TN919');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000869,'TN919.8 图像通信、多媒体通信',180000859,'TN919.8','18121010',4,'TN9198');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000870,'TN92 无线通信',180000771,'TN92','181211',3,'TN92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000871,'[TN921] 无线电话',180000870,'[TN921]','18121101',4,'TN921');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000872,'[TN922] 无线电报',180000870,'[TN922]','18121102',4,'TN922');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000873,'TN923 无线电和有线电通信联接系统',180000870,'TN923','18121103',4,'TN923');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000874,'TN924 无线电台',180000870,'TN924','18121104',4,'TN924');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000875,'TN925 无线电中继通信、微波通信',180000870,'TN925','18121105',4,'TN925');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000876,'TN926 散射通信',180000870,'TN926','18121106',4,'TN926');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000877,'TN927 卫星通信和宇宙通信',180000870,'TN927','18121107',4,'TN927');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000878,'TN928 波导通信、毫米波通信',180000870,'TN928','18121108',4,'TN928');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000879,'TN929.1 光波通信、激光通信',180000870,'TN929.1','18121109',4,'TN9291');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000880,'TN929.3 水下通信（声纳通信）',180000870,'TN929.3','18121110',4,'TN9293');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000881,'TN929.4 地下通信、岩层通信',180000870,'TN929.4','18121111',4,'TN9294');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000882,'TN929.5 移动通信',180000870,'TN929.5','18121112',4,'TN9295');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000883,'TN929.6 中微子束通信',180000870,'TN929.6','18121113',4,'TN9296');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000884,'TN93 广播',180000771,'TN93','181212',3,'TN93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000885,'TN931 广播中心、广播电台',180000884,'TN931','18121201',4,'TN931');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000886,'TN932 广播站',180000884,'TN932','18121202',4,'TN932');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000887,'TN933 有线广播',180000884,'TN933','18121203',4,'TN933');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000888,'TN934 无线广播',180000884,'TN934','18121204',4,'TN934');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000889,'TN935 立体声广播',180000884,'TN935','18121205',4,'TN935');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000890,'TN937 超短波广播',180000884,'TN937','18121206',4,'TN937');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000891,'TN938 卫星广播',180000884,'TN938','18121207',4,'TN938');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000892,'TN939.1 节目传送与分配',180000884,'TN939.1','18121208',4,'TN9391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000893,'TN94 电视',180000771,'TN94','181213',3,'TN94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000894,'TN941 电视信号理论',180000893,'TN941','18121301',4,'TN941');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000895,'TN942 电视光学',180000893,'TN942','18121302',4,'TN942');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000896,'TN943 电视信号的传输',180000893,'TN943','18121303',4,'TN943');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000897,'TN944 电视扫描系统',180000893,'TN944','18121304',4,'TN944');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000898,'TN945 电视同步系统',180000893,'TN945','18121305',4,'TN945');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000899,'TN946 录像系统、放像系统',180000893,'TN946','18121306',4,'TN946');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000900,'TN947 电视偏转和聚焦系统',180000893,'TN947','18121307',4,'TN947');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000901,'TN948 电视中心、电视设备',180000893,'TN948','18121308',4,'TN948');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000902,'TN949.1 电视：按体制分',180000893,'TN949.1','18121309',4,'TN9491');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000903,'TN949.2 电视：按功能、用途分',180000893,'TN949.2','18121310',4,'TN9492');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000904,'TN949.5 制式交换与国际节目交换',180000893,'TN949.5','18121311',4,'TN9495');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000905,'TN949.6 电视测量及测量仪器',180000893,'TN949.6','18121312',4,'TN9496');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000906,'TN949.7 电视接收机的维修',180000893,'TN949.7','18121313',4,'TN9497');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000907,'TN949.8 电视传真',180000893,'TN949.8','18121314',4,'TN9498');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000908,'TN95 雷达',180000771,'TN95','181214',3,'TN95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000909,'TN951 雷达原理',180000908,'TN951','18121401',4,'TN951');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000910,'TN952 雷达电子电路装置',180000908,'TN952','18121402',4,'TN952');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000911,'TN953 雷达跟踪系统',180000908,'TN953','18121403',4,'TN953');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000912,'TN954 雷达监控与保护系统',180000908,'TN954','18121404',4,'TN954');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000913,'TN955 雷达系统模拟',180000908,'TN955','18121405',4,'TN955');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000914,'TN956 雷达的可靠性',180000908,'TN956','18121406',4,'TN956');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000915,'TN957 雷达设备、雷达站',180000908,'TN957','18121407',4,'TN957');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000916,'TN958 雷达：按体制分',180000908,'TN958','18121408',4,'TN958');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000917,'TN959 雷达：按用途分',180000908,'TN959','18121409',4,'TN959');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000918,'TN959.7 雷达：按使用地点分',180000908,'TN959.7','18121410',4,'TN9597');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000919,'TN96 无线电导航',180000771,'TN96','181215',3,'TN96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000920,'TN961 无线电导航原理',180000919,'TN961','18121501',4,'TN961');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000921,'TN962 导航电子电路装置',180000919,'TN962','18121502',4,'TN962');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000922,'TN964 导航的伺服系统和控制系统',180000919,'TN964','18121503',4,'TN964');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000923,'TN965 导航设备、导航台',180000919,'TN965','18121504',4,'TN965');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000924,'TN966 各种体制的导航系统',180000919,'TN966','18121505',4,'TN966');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000925,'TN97 电子对抗（干扰及抗干扰）',180000771,'TN97','181216',3,'TN97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000926,'TN971 侦察问题',180000925,'TN971','18121601',4,'TN971');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000927,'TN972 干扰',180000925,'TN972','18121602',4,'TN972');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000928,'TN973 反侦察、反干扰',180000925,'TN973','18121603',4,'TN973');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000929,'TN974 雷达电子对抗',180000925,'TN974','18121604',4,'TN974');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000930,'TN975 通信电子对抗',180000925,'TN975','18121605',4,'TN975');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000931,'TN976 红外电子对抗',180000925,'TN976','18121606',4,'TN976');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000932,'TN977 激光电子对抗',180000925,'TN977','18121607',4,'TN977');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000933,'TN978 通信干扰设备',180000925,'TN978','18121608',4,'TN978');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000934,'[TN98] 无线电、电信测量技术及仪器',180000771,'[TN98]','181217',3,'TN98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000935,'TN99 无线电电子学的应用',180000771,'TN99','181218',3,'TN99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000936,'TP 自动化技术、计算机技术',180000001,'TP','1813',2,'TP');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000937,'[TP-9] 自动化技术经济',180000936,'[TP-9]','18130101',4,'TP-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000938,'TP1 自动化基础理论',180000936,'TP1','181302',3,'TP1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000939,'TP11 自动化系统理论',180000938,'TP11','18130201',4,'TP11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000940,'TP13 自动控制理论',180000938,'TP13','18130202',4,'TP13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000941,'TP14 自动信息理论',180000938,'TP14','18130203',4,'TP14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000942,'TP15 自动模拟理论（自动仿真理论）',180000938,'TP15','18130204',4,'TP15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000943,'TP17 开关电路理论',180000938,'TP17','18130205',4,'TP17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000944,'TP18 人工智能理论',180000938,'TP18','18130206',4,'TP18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000945,'TP2 自动化技术及设备',180000936,'TP2','181303',3,'TP2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000946,'TP20 一般性问题',180000945,'TP20','18130301',4,'TP20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000947,'TP21 自动化元件、部件',180000945,'TP21','18130302',4,'TP21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000948,'TP23 自动化装置与设备',180000945,'TP23','18130303',4,'TP23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000949,'TP24 机器人技术',180000945,'TP24','18130304',4,'TP24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000950,'TP27 自动化系统',180000945,'TP27','18130305',4,'TP27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000951,'TP29 自动化技术在各方面的应用',180000945,'TP29','18130306',4,'TP29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000952,'TP3 计算技术、计算机技术',180000936,'TP3','181304',3,'TP3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000953,'TP3-05 计算机与其他学科的关系',180000952,'TP3-05','1813040101',5,'TP3-05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000954,'TP30 一般性问题',180000952,'TP30','18130402',4,'TP30');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000955,'TP301 理论、方法',180000954,'TP301','1813040201',5,'TP301');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000956,'TP302 设计与性能分析',180000954,'TP302','1813040202',5,'TP302');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000957,'TP303 总体结构、系统结构',180000954,'TP303','1813040203',5,'TP303');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000958,'TP304 材料',180000954,'TP304','1813040204',5,'TP304');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000959,'TP305 制造、装配、改装',180000954,'TP305','1813040205',5,'TP305');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000960,'TP306 调整、测试、校验',180000954,'TP306','1813040206',5,'TP306');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000961,'TP307 检修、维护',180000954,'TP307','1813040207',5,'TP307');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000962,'TP308 机房',180000954,'TP308','1813040208',5,'TP308');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000963,'TP309 安全保密',180000954,'TP309','1813040209',5,'TP309');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000964,'TP31 计算机软件',180000952,'TP31','18130403',4,'TP31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000965,'TP311 程序设计、软件工程',180000964,'TP311','1813040301',5,'TP311');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000966,'TP312 程序语言、算法语言',180000964,'TP312','1813040302',5,'TP312');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000967,'TP313 汇编程序',180000964,'TP313','1813040303',5,'TP313');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000968,'TP314 编译程序、解释程序',180000964,'TP314','1813040304',5,'TP314');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000969,'TP315 管理程序、管理系统',180000964,'TP315','1813040305',5,'TP315');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000970,'TP316 操作系统',180000964,'TP316','1813040306',5,'TP316');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000971,'TP317 程序包（应用软件）',180000964,'TP317','1813040307',5,'TP317');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000972,'TP319 专用应用软件',180000964,'TP319','1813040308',5,'TP319');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000973,'TP32 一般计算器和计算机',180000952,'TP32','18130404',4,'TP32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000974,'TP321 非电子计算机',180000973,'TP321','1813040401',5,'TP321');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000975,'TP322 分析计算机（穿孔卡片计算机）',180000973,'TP322','1813040402',5,'TP322');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000976,'TP323 电子计算器',180000973,'TP323','1813040403',5,'TP323');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000977,'TP33 电子数字计算机（不连续作用电子计算机）',180000952,'TP33','18130405',4,'TP33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000978,'TP331 基本电路',180000977,'TP331','1813040501',5,'TP331');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000979,'TP332 运算器和控制器(CPU)',180000977,'TP332','1813040502',5,'TP332');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000980,'TP333 存贮器',180000977,'TP333','1813040503',5,'TP333');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000981,'TP334 外部设备',180000977,'TP334','1813040504',5,'TP334');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000982,'TP335 信息转换及其设备',180000977,'TP335','1813040505',5,'TP335');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000983,'TP336 总线、通道',180000977,'TP336','1813040506',5,'TP336');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000984,'TP337 仿真器',180000977,'TP337','1813040507',5,'TP337');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000985,'TP338 各种电子数字计算机',180000977,'TP338','1813040508',5,'TP338');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000986,'TP34 电子模拟计算机(连续作用电子计算机)',180000952,'TP34','18130406',4,'TP34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000987,'TP342 运算放大器和控制器',180000986,'TP342','1813040601',5,'TP342');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000988,'TP343 存贮器',180000986,'TP343','1813040602',5,'TP343');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000989,'TP344 输入器、输出器',180000986,'TP344','1813040603',5,'TP344');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000990,'TP346 函数发生器',180000986,'TP346','1813040604',5,'TP346');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000991,'TP347 延时器',180000986,'TP347','1813040605',5,'TP347');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000992,'TP348 各种电子模拟计算机',180000986,'TP348','1813040606',5,'TP348');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000993,'TP35 混合电子计算机',180000952,'TP35','18130407',4,'TP35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000994,'TP352 数字-模拟计算机',180000993,'TP352','1813040701',5,'TP352');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000995,'TP353 模拟-数字计算机',180000993,'TP353','1813040702',5,'TP353');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000996,'TP36 微型计算机',180000952,'TP36','18130408',4,'TP36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000997,'TP368 各种微型计算机',180000996,'TP368','1813040801',5,'TP368');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000998,'TP37 多媒体技术与多媒体计算机',180000952,'TP37','18130409',4,'TP37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180000999,'TP38 其他计算机',180000952,'TP38','18130410',4,'TP38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001000,'TP381 激光计算机',180000999,'TP381','1813041001',5,'TP381');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001001,'TP382 射流计算机',180000999,'TP382','1813041002',5,'TP382');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001002,'TP383 超导计算机',180000999,'TP383','1813041003',5,'TP383');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001003,'TP384 分子计算机',180000999,'TP384','1813041004',5,'TP384');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001004,'TP387 第五代计算机',180000999,'TP387','1813041005',5,'TP387');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001005,'TP389.1 人工神经网络计算机',180000999,'TP389.1','1813041006',5,'TP3891');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001006,'TP39 计算机的应用',180000952,'TP39','18130411',4,'TP39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001007,'TP391 信息处理(信息加工)',180001006,'TP391','1813041101',5,'TP391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001008,'TP392 各种专用数据库',180001006,'TP392','1813041102',5,'TP392');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001009,'TP393 计算机网络',180001006,'TP393','1813041103',5,'TP393');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001010,'TP399 在其他方面的应用',180001006,'TP399','1813041104',5,'TP399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001011,'TP6 射流技术（流控技术）',180000936,'TP6','181305',3,'TP6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001012,'TP60 一般性问题',180001011,'TP60','18130501',4,'TP60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001013,'TP61 射流元件',180001011,'TP61','18130502',4,'TP61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001014,'TP62 射流附件',180001011,'TP62','18130503',4,'TP62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001015,'TP63 检测发信装置',180001011,'TP63','18130504',4,'TP63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001016,'TP64 执行机构',180001011,'TP64','18130505',4,'TP64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001017,'TP65 动力源',180001011,'TP65','18130506',4,'TP65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001018,'TP66 射流控制线路',180001011,'TP66','18130507',4,'TP66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001019,'TP67 射流自动控制系统',180001011,'TP67','18130508',4,'TP67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001020,'TP69 射流技术的应用',180001011,'TP69','18130509',4,'TP69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001021,'TP7 遥感技术',180000936,'TP7','181306',3,'TP7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001022,'TP70 一般性问题',180001021,'TP70','18130601',4,'TP70');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001023,'TP72 遥感方式',180001021,'TP72','18130602',4,'TP72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001024,'TP73 探测仪器及系统',180001021,'TP73','18130603',4,'TP73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001025,'TP75 遥感图像的解译、识别与处理',180001021,'TP75','18130604',4,'TP75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001026,'TP79 遥感技术的应用',180001021,'TP79','18130605',4,'TP79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001027,'TP8 远动技术',180000936,'TP8','181307',3,'TP8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001028,'TP80 一般性问题',180001027,'TP80','18130701',4,'TP80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001029,'[TP81] 远动元件、部件',180001027,'[TP81]','18130702',4,'TP81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001030,'TP83 远动化装置',180001027,'TP83','18130703',4,'TP83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001031,'TP84 远程信道',180001027,'TP84','18130704',4,'TP84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001032,'TP87 远动化系统',180001027,'TP87','18130705',4,'TP87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001033,'TP89 远动技术在各方面的应用',180001027,'TP89','18130706',4,'TP89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001034,'TQ 化学工业',180000001,'TQ','1814',2,'TQ');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001035,'[TQ-9] 化学工业经济',180001034,'[TQ-9]','18140101',4,'TQ-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001036,'TQ0 一般性问题',180001034,'TQ0','181402',3,'TQ0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001037,'TQ01 基础理论',180001036,'TQ01','18140201',4,'TQ01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001038,'TQ02 化工过程（物理过程及物理化学过程）',180001036,'TQ02','18140202',4,'TQ02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001039,'TQ03 化学反应过程',180001036,'TQ03','18140203',4,'TQ03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001040,'TQ04 化工原料、辅助物料',180001036,'TQ04','18140204',4,'TQ04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001041,'TQ05 化工机械与仪器、设备',180001036,'TQ05','18140205',4,'TQ05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001042,'TQ06 化工生产过程、产品最后处理及包装',180001036,'TQ06','18140206',4,'TQ06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001043,'TQ07 化工产品与副产品',180001036,'TQ07','18140207',4,'TQ07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001044,'TQ08 化工厂',180001036,'TQ08','18140208',4,'TQ08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001045,'[TQ09] 化学工业废物的处理与综合利用',180001036,'[TQ09]','18140209',4,'TQ09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001046,'TQ11 基本无机化学工业',180001034,'TQ11','181403',3,'TQ11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001047,'TQ110 一般性问题',180001046,'TQ110','18140301',4,'TQ110');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001048,'TQ111 无机酸类生产',180001046,'TQ111','18140302',4,'TQ111');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001049,'TQ113 氨和铵盐工业',180001046,'TQ113','18140303',4,'TQ113');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001050,'TQ114 氯碱工业',180001046,'TQ114','18140304',4,'TQ114');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001051,'TQ115 无机盐工业',180001046,'TQ115','18140305',4,'TQ115');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001052,'TQ116 工业气体',180001046,'TQ116','18140306',4,'TQ116');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001053,'TQ117 特种气体',180001046,'TQ117','18140307',4,'TQ117');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001054,'TQ118 无机过酸及过酸盐',180001046,'TQ118','18140308',4,'TQ118');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001055,'TQ12 非金属元素及其无机化合物化学工业',180001034,'TQ12','181404',3,'TQ12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001056,'TQ122 氢',180001055,'TQ122','18140401',4,'TQ122');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001057,'TQ123 氧',180001055,'TQ123','18140402',4,'TQ123');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001058,'TQ124 卤素及其化合物',180001055,'TQ124','18140403',4,'TQ124');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001059,'TQ125 第Ⅵ族非金属元素及其无机化合物',180001055,'TQ125','18140404',4,'TQ125');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001060,'TQ126 第Ⅴ族非金属元素及其无机化合物',180001055,'TQ126','18140405',4,'TQ126');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001061,'TQ127 第Ⅳ族非金属元素及其无机化合物',180001055,'TQ127','18140406',4,'TQ127');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001062,'TQ128 第Ⅲ族非金属元素（硼）及其无机化合物',180001055,'TQ128','18140407',4,'TQ128');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001063,'TQ129 多种非金属元素组成的无机化合物',180001055,'TQ129','18140408',4,'TQ129');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001064,'TQ13 金属元素的无机化合物化学工业',180001034,'TQ13','181405',3,'TQ13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001065,'TQ131 第Ⅰ族金属元素的无机化合物',180001064,'TQ131','18140501',4,'TQ131');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001066,'TQ132 第Ⅱ族金属元素的无机化合物',180001064,'TQ132','18140502',4,'TQ132');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001067,'TQ133 第Ⅲ族金属元素的无机化合物',180001064,'TQ133','18140503',4,'TQ133');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001068,'TQ134 第Ⅳ族金属元素的无机化合物',180001064,'TQ134','18140504',4,'TQ134');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001069,'TQ135 第Ⅴ族金属元素的无机化合物',180001064,'TQ135','18140505',4,'TQ135');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001070,'TQ136 第Ⅵ族金属元素的无机化合物',180001064,'TQ136','18140506',4,'TQ136');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001071,'TQ137 第Ⅶ族金属元素的无机化合物',180001064,'TQ137','18140507',4,'TQ137');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001072,'TQ138 第Ⅷ族金属元素的无机化合物',180001064,'TQ138','18140508',4,'TQ138');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001073,'TQ139.1 杂多元酸及其盐类的生产',180001064,'TQ139.1','18140509',4,'TQ1391');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001074,'TQ139.2 其他复杂的无机化合物的生产',180001064,'TQ139.2','18140510',4,'TQ1392');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001075,'TQ15 电化学工业',180001034,'TQ15','181406',3,'TQ15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001076,'TQ150 一般性问题',180001075,'TQ150','18140601',4,'TQ150');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001077,'TQ151 电解工业',180001075,'TQ151','18140602',4,'TQ151');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001078,'[TQ152] 化学电源',180001075,'[TQ152]','18140603',4,'TQ152');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001079,'TQ153 电镀工业',180001075,'TQ153','18140604',4,'TQ153');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001080,'TQ16 电热工业、高温制品工业',180001034,'TQ16','181407',3,'TQ16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001081,'TQ161 碳化钙（电石）的生产',180001080,'TQ161','18140701',4,'TQ161');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001082,'TQ162 氰胺钙的生产',180001080,'TQ162','18140702',4,'TQ162');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001083,'TQ163 人造超硬度材料的生产',180001080,'TQ163','18140703',4,'TQ163');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001084,'TQ164 人造宝石、合成宝石的生产',180001080,'TQ164','18140704',4,'TQ164');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001085,'TQ165 人造石墨',180001080,'TQ165','18140705',4,'TQ165');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001086,'TQ17 硅酸盐工业',180001034,'TQ17','181408',3,'TQ17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001087,'TQ170 一般性问题',180001086,'TQ170','18140801',4,'TQ170');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001088,'TQ171 玻璃工业',180001086,'TQ171','18140802',4,'TQ171');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001089,'TQ172 水泥工业',180001086,'TQ172','18140803',4,'TQ172');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001090,'TQ173 搪瓷（珐琅、搪玻璃、衬玻璃）工业',180001086,'TQ173','18140804',4,'TQ173');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001091,'TQ174 陶瓷工业',180001086,'TQ174','18140805',4,'TQ174');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001092,'TQ175 耐火材料工业',180001086,'TQ175','18140806',4,'TQ175');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001093,'TQ176 石棉工业',180001086,'TQ176','18140807',4,'TQ176');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001094,'TQ177 人造石及其他胶凝材料',180001086,'TQ177','18140808',4,'TQ177');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001095,'[TQ178] 砼（混凝土）及砼制品',180001086,'[TQ178]','18140809',4,'TQ178');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001096,'[TQ179] 砖、瓦、砌块',180001086,'[TQ179]','18140810',4,'TQ179');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001097,'TQ2 基本有机化学工业',180001034,'TQ2','181409',3,'TQ2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001098,'TQ20 一般性问题',180001097,'TQ20','18140901',4,'TQ20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001099,'TQ21 各类有机化合物的生产（总论）',180001097,'TQ21','18140902',4,'TQ21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001100,'TQ22 脂肪族化合物（无环化合物）的生产',180001097,'TQ22','18140903',4,'TQ22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001101,'TQ23 碳环化合物、脂环族化合物的生产',180001097,'TQ23','18140904',4,'TQ23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001102,'TQ24 芳香族化合物的生产',180001097,'TQ24','18140905',4,'TQ24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001103,'TQ25 杂环化合物的生产',180001097,'TQ25','18140906',4,'TQ25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001104,'TQ26 元素有机化合物的生产',180001097,'TQ26','18140907',4,'TQ26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001105,'TQ27 含同位素有机化合物的生产',180001097,'TQ27','18140908',4,'TQ27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001106,'TQ28 天然有机化合物的生产',180001097,'TQ28','18140909',4,'TQ28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001107,'TQ31 高分子化合物工业（高聚物工业）',180001034,'TQ31','181410',3,'TQ31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001108,'TQ311 基础理论',180001107,'TQ311','18141001',4,'TQ311');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001109,'TQ314 原理与辅助材料',180001107,'TQ314','18141002',4,'TQ314');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001110,'TQ315 机械与设备',180001107,'TQ315','18141003',4,'TQ315');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001111,'TQ316 生产过程',180001107,'TQ316','18141004',4,'TQ316');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001112,'TQ317 高分子化合物产品',180001107,'TQ317','18141005',4,'TQ317');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001113,'TQ318 高分子化合物工厂',180001107,'TQ318','18141006',4,'TQ318');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001114,'[TQ319] 三废处理与综合利用',180001107,'[TQ319]','18141007',4,'TQ319');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001115,'TQ32 合成树脂与塑料工业',180001034,'TQ32','181411',3,'TQ32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001116,'TQ320 一般性问题',180001115,'TQ320','18141101',4,'TQ320');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001117,'TQ321 天然高分子树脂与塑料',180001115,'TQ321','18141102',4,'TQ321');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001118,'TQ322 合成树脂及塑料',180001115,'TQ322','18141103',4,'TQ322');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001119,'TQ323 缩聚类树脂及塑料',180001115,'TQ323','18141104',4,'TQ323');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001120,'TQ324 特种塑料',180001115,'TQ324','18141105',4,'TQ324');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001121,'TQ325 聚合类树脂及塑料',180001115,'TQ325','18141106',4,'TQ325');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001122,'TQ327 增强塑料、填充塑料',180001115,'TQ327','18141107',4,'TQ327');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001123,'TQ328 泡沫塑料',180001115,'TQ328','18141108',4,'TQ328');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001124,'TQ33 橡胶工业',180001034,'TQ33','181412',3,'TQ33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001125,'TQ330 一般性问题',180001124,'TQ330','18141201',4,'TQ330');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001126,'TQ331 胶乳',180001124,'TQ331','18141202',4,'TQ331');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001127,'TQ332 天然橡胶',180001124,'TQ332','18141203',4,'TQ332');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001128,'TQ333 合成橡胶',180001124,'TQ333','18141204',4,'TQ333');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001129,'TQ334 热塑性弹性体',180001124,'TQ334','18141205',4,'TQ334');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001130,'TQ335 再生橡胶',180001124,'TQ335','18141206',4,'TQ335');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001131,'TQ336 橡胶制品',180001124,'TQ336','18141207',4,'TQ336');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001132,'TQ337 胶乳制品',180001124,'TQ337','18141208',4,'TQ337');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001133,'TQ338 胶布及胶布制品',180001124,'TQ338','18141209',4,'TQ338');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001134,'TQ339 橡胶胶粘剂',180001124,'TQ339','18141210',4,'TQ339');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001135,'TQ34 化学纤维工业',180001034,'TQ34','181413',3,'TQ34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001136,'TQ340 一般性问题',180001135,'TQ340','18141301',4,'TQ340');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001137,'TQ341 再生纤维',180001135,'TQ341','18141302',4,'TQ341');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001138,'TQ342 合成纤维',180001135,'TQ342','18141303',4,'TQ342');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001139,'TQ343 无机纤维',180001135,'TQ343','18141304',4,'TQ343');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001140,'TQ35 纤维素质的化学加工工业',180001034,'TQ35','181414',3,'TQ35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001141,'TQ351 木材化学加工工业',180001140,'TQ351','18141401',4,'TQ351');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001142,'TQ352 纤维素化学加工工业',180001140,'TQ352','18141402',4,'TQ352');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001143,'TQ353 植物纤维水解工业',180001140,'TQ353','18141403',4,'TQ353');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001144,'TQ41 溶剂与增塑剂的生产',180001034,'TQ41','181415',3,'TQ41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001145,'TQ410 一般性问题',180001144,'TQ410','18141501',4,'TQ410');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001146,'TQ413 溶剂',180001144,'TQ413','18141502',4,'TQ413');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001147,'TQ414 增塑剂',180001144,'TQ414','18141503',4,'TQ414');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001148,'TQ415 有毒溶剂',180001144,'TQ415','18141504',4,'TQ415');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001149,'TQ42 试剂与纯化学品的生产',180001034,'TQ42','181416',3,'TQ42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001150,'TQ420 一般性问题',180001149,'TQ420','18141601',4,'TQ420');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001151,'TQ421 试剂',180001149,'TQ421','18141602',4,'TQ421');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001152,'TQ422 光化学物质',180001149,'TQ422','18141603',4,'TQ422');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001153,'TQ423 表面活性剂',180001149,'TQ423','18141604',4,'TQ423');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001154,'TQ424 吸附剂',180001149,'TQ424','18141605',4,'TQ424');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001155,'TQ425 离子交换剂',180001149,'TQ425','18141606',4,'TQ425');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001156,'TQ426 催化剂（触媒）',180001149,'TQ426','18141607',4,'TQ426');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001157,'TQ427 胶体与半胶体物质',180001149,'TQ427','18141608',4,'TQ427');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001158,'TQ43 胶粘剂工业',180001034,'TQ43','181417',3,'TQ43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001159,'TQ430 一般性问题',180001158,'TQ430','18141701',4,'TQ430');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001160,'TQ431 动物胶',180001158,'TQ431','18141702',4,'TQ431');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001161,'TQ432 植物胶粘剂',180001158,'TQ432','18141703',4,'TQ432');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001162,'TQ433 合成胶粘剂',180001158,'TQ433','18141704',4,'TQ433');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001163,'TQ436 各种性能胶粘剂',180001158,'TQ436','18141705',4,'TQ436');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001164,'TQ437 各种用途的胶粘剂',180001158,'TQ437','18141706',4,'TQ437');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001165,'TQ44 化学肥料工业',180001034,'TQ44','181418',3,'TQ44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001166,'TQ440 一般性问题',180001165,'TQ440','18141801',4,'TQ440');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001167,'TQ441 氮肥',180001165,'TQ441','18141802',4,'TQ441');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001168,'TQ442 磷肥',180001165,'TQ442','18141803',4,'TQ442');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001169,'TQ443 钾肥',180001165,'TQ443','18141804',4,'TQ443');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001170,'TQ444 复合肥料（混合肥料）',180001165,'TQ444','18141805',4,'TQ444');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001171,'TQ445 其他无机肥料',180001165,'TQ445','18141806',4,'TQ445');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001172,'TQ446 细菌肥料',180001165,'TQ446','18141807',4,'TQ446');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001173,'TQ447 专用肥料',180001165,'TQ447','18141808',4,'TQ447');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001174,'TQ449 其他化学肥料',180001165,'TQ449','18141809',4,'TQ449');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001175,'TQ45 农药工业',180001034,'TQ45','181419',3,'TQ45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001176,'TQ450 一般性问题',180001175,'TQ450','18141901',4,'TQ450');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001177,'TQ451 土农药',180001175,'TQ451','18141902',4,'TQ451');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001178,'TQ452 植物生长调节剂',180001175,'TQ452','18141903',4,'TQ452');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001179,'TQ453 杀虫剂',180001175,'TQ453','18141904',4,'TQ453');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001180,'TQ454 杀螨剂',180001175,'TQ454','18141905',4,'TQ454');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001181,'TQ455 杀菌剂',180001175,'TQ455','18141906',4,'TQ455');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001182,'TQ456 杀鼠剂',180001175,'TQ456','18141907',4,'TQ456');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001183,'TQ457 除草剂',180001175,'TQ457','18141908',4,'TQ457');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001184,'TQ458 微生物农药',180001175,'TQ458','18141909',4,'TQ458');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001185,'TQ459 其他农药',180001175,'TQ459','18141910',4,'TQ459');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001186,'TQ46 制药化学工业',180001034,'TQ46','181420',3,'TQ46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001187,'TQ460 一般性问题',180001186,'TQ460','18142001',4,'TQ460');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001188,'TQ461 中草药制剂的生产',180001186,'TQ461','18142002',4,'TQ461');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001189,'TQ462 无机化合物药物的生产',180001186,'TQ462','18142003',4,'TQ462');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001190,'TQ463 有机化合物药物的生产',180001186,'TQ463','18142004',4,'TQ463');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001191,'TQ464 生物制品药物的生产',180001186,'TQ464','18142005',4,'TQ464');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001192,'TQ465 抗菌素制造',180001186,'TQ465','18142006',4,'TQ465');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001193,'TQ466 维生素制造',180001186,'TQ466','18142007',4,'TQ466');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001194,'TQ467 激素制造',180001186,'TQ467','18142008',4,'TQ467');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001195,'TQ468 磺胺类药物制造',180001186,'TQ468','18142009',4,'TQ468');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001196,'[TQ469] 各种药剂的制备',180001186,'[TQ469]','18142010',4,'TQ469');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001197,'TQ51 燃料化学工业(总论)',180001034,'TQ51','181421',3,'TQ51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001198,'TQ511 基础理论',180001197,'TQ511','18142101',4,'TQ511');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001199,'TQ514 原料',180001197,'TQ514','18142102',4,'TQ514');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001200,'TQ515 机械与设备',180001197,'TQ515','18142103',4,'TQ515');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001201,'TQ517 燃料种类及性质',180001197,'TQ517','18142104',4,'TQ517');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001202,'TQ519 燃料化学加工的综合利用',180001197,'TQ519','18142105',4,'TQ519');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001203,'TQ52 炼焦化学工业',180001034,'TQ52','181422',3,'TQ52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001204,'TQ520 一般性问题',180001203,'TQ520','18142201',4,'TQ520');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001205,'TQ521 土法炼焦',180001203,'TQ521','18142202',4,'TQ521');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001206,'TQ522 煤的高温干馏',180001203,'TQ522','18142203',4,'TQ522');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001207,'TQ523 煤的低温干馏、中温干馏',180001203,'TQ523','18142204',4,'TQ523');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001208,'TQ524 其他来源的焦油及其处理',180001203,'TQ524','18142205',4,'TQ524');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001209,'TQ529 煤炭液化',180001203,'TQ529','18142206',4,'TQ529');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001210,'TQ53 煤化学及煤的加工利用',180001034,'TQ53','181423',3,'TQ53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001211,'TQ530 煤化学基础理论',180001210,'TQ530','18142301',4,'TQ530');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001212,'TQ531 煤的性质与测定',180001210,'TQ531','18142302',4,'TQ531');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001213,'TQ533 煤的分析与检验',180001210,'TQ533','18142303',4,'TQ533');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001214,'TQ534 煤的燃烧',180001210,'TQ534','18142304',4,'TQ534');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001215,'TQ536 煤的加工利用',180001210,'TQ536','18142305',4,'TQ536');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001216,'TQ54 煤炭气化工业',180001034,'TQ54','181424',3,'TQ54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001217,'TQ541 气化理论（可燃气体、瓦斯）',180001216,'TQ541','18142401',4,'TQ541');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001218,'TQ542 煤气的种类和性质',180001216,'TQ542','18142402',4,'TQ542');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001219,'TQ544 气化原料与辅助物料',180001216,'TQ544','18142403',4,'TQ544');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001220,'TQ545 气化设备',180001216,'TQ545','18142404',4,'TQ545');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001221,'TQ546 气化工艺',180001216,'TQ546','18142405',4,'TQ546');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001222,'TQ547 煤气的分析、鉴定和储运',180001216,'TQ547','18142406',4,'TQ547');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001223,'TQ548 煤气厂（煤气站、煤气车间）',180001216,'TQ548','18142407',4,'TQ548');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001224,'[TQ549] 城市煤气供应',180001216,'[TQ549]','18142408',4,'TQ549');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001225,'TQ55 燃料照明工业',180001034,'TQ55','181425',3,'TQ55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001226,'TQ56 爆炸物工业、火柴工业',180001034,'TQ56','181426',3,'TQ56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001227,'TQ560 一般性的问题。',180001226,'TQ560','18142601',4,'TQ560');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001228,'TQ561 土法制火药及炸药',180001226,'TQ561','18142602',4,'TQ561');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001229,'TQ562 发射药',180001226,'TQ562','18142603',4,'TQ562');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001230,'TQ563 起爆药',180001226,'TQ563','18142604',4,'TQ563');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001231,'TQ564 猛性炸药',180001226,'TQ564','18142605',4,'TQ564');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001232,'TQ565 点火、点爆材料',180001226,'TQ565','18142606',4,'TQ565');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001233,'TQ567 火工术、焰火、爆竹',180001226,'TQ567','18142607',4,'TQ567');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001234,'TQ568 火柴工业',180001226,'TQ568','18142608',4,'TQ568');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001235,'TQ569 灭火器与灭火用剂的生产',180001226,'TQ569','18142609',4,'TQ569');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001236,'TQ57 感光材料工业',180001034,'TQ57','181427',3,'TQ57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001237,'TQ571 感光理论',180001236,'TQ571','18142701',4,'TQ571');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001238,'TQ572 原材料及辅助物料',180001236,'TQ572','18142702',4,'TQ572');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001239,'TQ573 机械与设备',180001236,'TQ573','18142703',4,'TQ573');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001240,'TQ574 生产工艺',180001236,'TQ574','18142704',4,'TQ574');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001241,'TQ575 感光乳剂合成',180001236,'TQ575','18142705',4,'TQ575');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001242,'TQ576 支持体',180001236,'TQ576','18142706',4,'TQ576');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001243,'TQ577 感光材料产品、产品分析及鉴定',180001236,'TQ577','18142707',4,'TQ577');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001244,'TQ578 感光材料制造厂',180001236,'TQ578','18142708',4,'TQ578');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001245,'[TQ579] 三废处理与综合利用',180001236,'[TQ579]','18142709',4,'TQ579');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001246,'TQ58 磁性记录材料工业',180001034,'TQ58','181428',3,'TQ58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001247,'TQ581 磁记录技术理论',180001246,'TQ581','18142801',4,'TQ581');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001248,'TQ584 原材料及辅助物料',180001246,'TQ584','18142802',4,'TQ584');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001249,'TQ585 机械与设备',180001246,'TQ585','18142803',4,'TQ585');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001250,'TQ586 生产工艺',180001246,'TQ586','18142804',4,'TQ586');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001251,'TQ587 产品',180001246,'TQ587','18142805',4,'TQ587');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001252,'TQ588 磁记录材料厂',180001246,'TQ588','18142806',4,'TQ588');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001253,'[TQ589] 三废处理与综合利用',180001246,'[TQ589]','18142807',4,'TQ589');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001254,'TQ59 光学记录材料工业',180001034,'TQ59','181429',3,'TQ59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001255,'TQ591 光学记录技术理论',180001254,'TQ591','18142901',4,'TQ591');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001256,'TQ594 原材料及辅助物料',180001254,'TQ594','18142902',4,'TQ594');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001257,'TQ595 机械与设备',180001254,'TQ595','18142903',4,'TQ595');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001258,'TQ596 生产工艺',180001254,'TQ596','18142904',4,'TQ596');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001259,'TQ597 产品',180001254,'TQ597','18142905',4,'TQ597');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001260,'TQ598 光学记录材料厂',180001254,'TQ598','18142906',4,'TQ598');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001261,'[TQ599] 三废处理与综合利用',180001254,'[TQ599]','18142907',4,'TQ599');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001262,'TQ61 染料及中间体工业',180001034,'TQ61','181430',3,'TQ61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001263,'TQ610 一般性问题',180001262,'TQ610','18143001',4,'TQ610');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001264,'TQ611 天然染料',180001262,'TQ611','18143002',4,'TQ611');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001265,'TQ612 中间体产品',180001262,'TQ612','18143003',4,'TQ612');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001266,'TQ613 各种结构的合成染料',180001262,'TQ613','18143004',4,'TQ613');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001267,'TQ615 各种性能的合成染料',180001262,'TQ615','18143005',4,'TQ615');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001268,'TQ617 各种用途的合成染料',180001262,'TQ617','18143006',4,'TQ617');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001269,'TQ619 染料应用',180001262,'TQ619','18143007',4,'TQ619');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001270,'TQ62 颜料工业',180001034,'TQ62','181431',3,'TQ62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001271,'TQ620 一般性问题',180001270,'TQ620','18143101',4,'TQ620');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001272,'TQ621 消色颜料',180001270,'TQ621','18143102',4,'TQ621');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001273,'TQ622 彩色颜料',180001270,'TQ622','18143103',4,'TQ622');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001274,'TQ623 体质颜料',180001270,'TQ623','18143104',4,'TQ623');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001275,'TQ624 金属颜料',180001270,'TQ624','18143105',4,'TQ624');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001276,'[TQ625] 有机颜料、色淀',180001270,'[TQ625]','18143106',4,'TQ625');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001277,'TQ626 金属元素发光体颜料',180001270,'TQ626','18143107',4,'TQ626');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001278,'TQ628 专用颜料',180001270,'TQ628','18143108',4,'TQ628');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001279,'TQ629 其他',180001270,'TQ629','18143109',4,'TQ629');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001280,'TQ63 涂料工业',180001034,'TQ63','181432',3,'TQ63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001281,'TQ630 一般性问题',180001280,'TQ630','18143201',4,'TQ630');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001282,'TQ631 油基漆',180001280,'TQ631','18143202',4,'TQ631');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001283,'TQ633 天然树脂漆、合成树脂漆',180001280,'TQ633','18143203',4,'TQ633');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001284,'TQ634 硝基漆',180001280,'TQ634','18143204',4,'TQ634');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001285,'TQ635 腻子',180001280,'TQ635','18143205',4,'TQ635');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001286,'TQ636 稀料',180001280,'TQ636','18143206',4,'TQ636');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001287,'TQ637 专用漆料',180001280,'TQ637','18143207',4,'TQ637');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001288,'TQ638 其他',180001280,'TQ638','18143208',4,'TQ638');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001289,'TQ639 涂料的施工',180001280,'TQ639','18143209',4,'TQ639');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001290,'TQ64 油脂和蜡的化学加工工业、肥皂工业',180001034,'TQ64','181433',3,'TQ64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001291,'TQ641 基础理论',180001290,'TQ641','18143301',4,'TQ641');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001292,'TQ642 原料',180001290,'TQ642','18143302',4,'TQ642');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001293,'TQ643 机械与设备',180001290,'TQ643','18143303',4,'TQ643');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001294,'TQ644 生产工艺',180001290,'TQ644','18143304',4,'TQ644');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001295,'TQ645 油脂工业产品及副产品',180001290,'TQ645','18143305',4,'TQ645');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001296,'TQ646 油料和油脂分析及检验',180001290,'TQ646','18143306',4,'TQ646');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001297,'TQ647 油脂工厂',180001290,'TQ647','18143307',4,'TQ647');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001298,'TQ648 肥皂工业',180001290,'TQ648','18143308',4,'TQ648');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001299,'TQ649 合成洗涤剂工业',180001290,'TQ649','18143309',4,'TQ649');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001300,'TQ65 香料及化妆品工业',180001034,'TQ65','181434',3,'TQ65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001301,'TQ651 香料化学',180001300,'TQ651','18143401',4,'TQ651');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001302,'TQ652 香料及化妆品的药理',180001300,'TQ652','18143402',4,'TQ652');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001303,'TQ653 香料毒理',180001300,'TQ653','18143403',4,'TQ653');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001304,'TQ654 天然香料',180001300,'TQ654','18143404',4,'TQ654');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001305,'TQ655 合成香料',180001300,'TQ655','18143405',4,'TQ655');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001306,'TQ656 各种用途香料',180001300,'TQ656','18143406',4,'TQ656');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001307,'TQ657 香精',180001300,'TQ657','18143407',4,'TQ657');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001308,'TQ658 化妆品',180001300,'TQ658','18143408',4,'TQ658');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001309,'TQ9 其他化学工业',180001034,'TQ9','181435',3,'TQ9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001310,'TQ91 农产物化学加工工业',180001309,'TQ91','18143501',4,'TQ91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001311,'TQ92 发酵工业',180001309,'TQ92','18143502',4,'TQ92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001312,'TQ93 蛋白质（朊）化学加工工业',180001309,'TQ93','18143503',4,'TQ93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001313,'TQ94 鞣料工业',180001309,'TQ94','18143504',4,'TQ94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001314,'TQ95 海洋化学工业',180001309,'TQ95','18143505',4,'TQ95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001315,'TS 轻工业、手工业',180000001,'TS','1815',2,'TS');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001316,'[TS-9] 轻工业、手工业经济',180001315,'[TS-9]','18150101',4,'TS-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001317,'TS0 一般性问题',180001315,'TS0','181502',3,'TS0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001318,'TS01 基础理论',180001317,'TS01','18150201',4,'TS01');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001319,'TS02 设计、计算、制图',180001317,'TS02','18150202',4,'TS02');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001320,'TS03 原材料及辅助物料',180001317,'TS03','18150203',4,'TS03');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001321,'TS04 机械与设备',180001317,'TS04','18150204',4,'TS04');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001322,'TS05 生产工艺',180001317,'TS05','18150205',4,'TS05');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001323,'TS06 产品及副产品',180001317,'TS06','18150206',4,'TS06');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001324,'TS07 产品标准与检验',180001317,'TS07','18150207',4,'TS07');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001325,'TS08 轻工业、手工业工厂',180001317,'TS08','18150208',4,'TS08');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001326,'TS09 包装装潢技术',180001317,'TS09','18150209',4,'TS09');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001327,'TS1 纺织工业、染整工业',180001315,'TS1','181503',3,'TS1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001328,'TS10 一般性问题',180001327,'TS10','18150301',4,'TS10');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001329,'TS11 棉纺织',180001327,'TS11','18150302',4,'TS11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001330,'TS12 麻纺织',180001327,'TS12','18150303',4,'TS12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001331,'TS13 毛纺织',180001327,'TS13','18150304',4,'TS13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001332,'TS14 丝纺织',180001327,'TS14','18150305',4,'TS14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001333,'TS15 化学纤维纺织',180001327,'TS15','18150306',4,'TS15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001334,'TS17 非织造布',180001327,'TS17','18150307',4,'TS17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001335,'TS18 针织',180001327,'TS18','18150308',4,'TS18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001336,'TS19 染整工业',180001327,'TS19','18150309',4,'TS19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001337,'TS2 食品工业',180001315,'TS2','181504',3,'TS2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001338,'TS20 一般性问题',180001337,'TS20','18150401',4,'TS20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001339,'TS21 粮食加工工业',180001337,'TS21','18150402',4,'TS21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001340,'TS22 食用油脂加工工业',180001337,'TS22','18150403',4,'TS22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001341,'TS23 淀粉工业',180001337,'TS23','18150404',4,'TS23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001342,'TS24 制糖工业',180001337,'TS24','18150405',4,'TS24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001343,'TS251 屠宰及肉类加工工业',180001337,'TS251','18150406',4,'TS251');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001344,'TS252 乳品加工工业',180001337,'TS252','18150407',4,'TS252');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001345,'TS253 蛋品加工工业',180001337,'TS253','18150408',4,'TS253');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001346,'TS254 水产加工工业',180001337,'TS254','18150409',4,'TS254');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001347,'TS255 水果、蔬菜、坚果加工工业',180001337,'TS255','18150410',4,'TS255');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001348,'TS26 酿造工业',180001337,'TS26','18150411',4,'TS26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001349,'TS27 饮料冷食制造工业',180001337,'TS27','18150412',4,'TS27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001350,'TS29 罐头工业',180001337,'TS29','18150413',4,'TS29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001351,'TS3 制盐工业',180001315,'TS3','181505',3,'TS3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001352,'TS31 制盐基础科学',180001351,'TS31','18150501',4,'TS31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001353,'TS32 盐业资源',180001351,'TS32','18150502',4,'TS32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001354,'TS33 盐业机械',180001351,'TS33','18150503',4,'TS33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001355,'TS34 海盐生产技术',180001351,'TS34','18150504',4,'TS34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001356,'TS35 其他矿盐生产技术',180001351,'TS35','18150505',4,'TS35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001357,'TS36 原盐加工技术',180001351,'TS36','18150506',4,'TS36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001358,'TS37 产品标准与检验',180001351,'TS37','18150507',4,'TS37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001359,'TS38 盐场、盐矿及盐化工厂',180001351,'TS38','18150508',4,'TS38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001360,'TS39 盐业副产品加工及利用',180001351,'TS39','18150509',4,'TS39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001361,'TS4 烟草工业',180001315,'TS4','181506',3,'TS4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001362,'TS41 基础科学',180001361,'TS41','18150601',4,'TS41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001363,'TS42 原料',180001361,'TS42','18150602',4,'TS42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001364,'TS43 机械与设备',180001361,'TS43','18150603',4,'TS43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001365,'TS44 烟草初加工',180001361,'TS44','18150604',4,'TS44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001366,'TS45 烟草加工工艺及制品',180001361,'TS45','18150605',4,'TS45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001367,'TS47 产品标准与检验',180001361,'TS47','18150606',4,'TS47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001368,'TS48 烟草加工厂',180001361,'TS48','18150607',4,'TS48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001369,'TS49 烟草工业副产品加工与利用',180001361,'TS49','18150608',4,'TS49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001370,'TS5 皮革工业',180001315,'TS5','181507',3,'TS5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001371,'TS51 皮革学',180001370,'TS51','18150701',4,'TS51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001372,'TS52 原料及辅助物料',180001370,'TS52','18150702',4,'TS52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001373,'TS53 加工机械与设备',180001370,'TS53','18150703',4,'TS53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001374,'TS54 制革工艺',180001370,'TS54','18150704',4,'TS54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001375,'TS55 毛皮工艺',180001370,'TS55','18150705',4,'TS55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001376,'TS56 皮革产品',180001370,'TS56','18150706',4,'TS56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001377,'TS57 产品标准与检验',180001370,'TS57','18150707',4,'TS57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001378,'TS58 皮革工厂',180001370,'TS58','18150708',4,'TS58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001379,'TS59 毛皮副产品加工及利用',180001370,'TS59','18150709',4,'TS59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001380,'TS6 木材加工工业、家具制造工业',180001315,'TS6','181508',3,'TS6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001381,'TS61 理论',180001380,'TS61','18150801',4,'TS61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001382,'TS62 原材料与辅料',180001380,'TS62','18150802',4,'TS62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001383,'TS63 结构、部件',180001380,'TS63','18150803',4,'TS63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001384,'TS64 加工机具与设备',180001380,'TS64','18150804',4,'TS64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001385,'TS65 加工工艺',180001380,'TS65','18150805',4,'TS65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001386,'TS66 各种制品',180001380,'TS66','18150806',4,'TS66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001387,'TS67 木材产品标准与检验',180001380,'TS67','18150807',4,'TS67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001388,'TS68 木材加工厂',180001380,'TS68','18150808',4,'TS68');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001389,'TS69 木材副产品加工及利用',180001380,'TS69','18150809',4,'TS69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001390,'TS7 造纸工业',180001315,'TS7','181509',3,'TS7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001391,'TS71 基础理论',180001390,'TS71','18150901',4,'TS71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001392,'TS72 原料及辅助物料',180001390,'TS72','18150902',4,'TS72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001393,'TS73 机械与设备',180001390,'TS73','18150903',4,'TS73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001394,'TS74 制浆工艺',180001390,'TS74','18150904',4,'TS74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001395,'TS75 造纸工艺',180001390,'TS75','18150905',4,'TS75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001396,'TS76 产品',180001390,'TS76','18150906',4,'TS76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001397,'TS77 产品标准与检验',180001390,'TS77','18150907',4,'TS77');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001398,'TS78 造纸厂',180001390,'TS78','18150908',4,'TS78');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001399,'TS79 造纸副产品加工与利用',180001390,'TS79','18150909',4,'TS79');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001400,'TS8 印刷工业',180001315,'TS8','181510',3,'TS8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001401,'TS80 一般性问题',180001400,'TS80','18151001',4,'TS80');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001402,'TS81 凸版印刷',180001400,'TS81','18151002',4,'TS81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001403,'TS82 平版印刷',180001400,'TS82','18151003',4,'TS82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001404,'TS83 凹版印刷',180001400,'TS83','18151004',4,'TS83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001405,'[TS84] 孔版印刷',180001400,'[TS84]','18151005',4,'TS84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001406,'TS85 特种印刷',180001400,'TS85','18151006',4,'TS85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001407,'TS87 其他印刷',180001400,'TS87','18151007',4,'TS87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001408,'TS88 装订技术、装帧技术',180001400,'TS88','18151008',4,'TS88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001409,'TS89 印刷技术的应用',180001400,'TS89','18151009',4,'TS89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001410,'TS91 五金制品工业',180001315,'TS91','181511',3,'TS91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001411,'TS911 基础理论',180001410,'TS911','18151101',4,'TS911');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001412,'TS912 原材料及辅料',180001410,'TS912','18151102',4,'TS912');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001413,'TS913 加工工艺及设备',180001410,'TS913','18151103',4,'TS913');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001414,'TS914 五金制品',180001410,'TS914','18151104',4,'TS914');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001415,'TS916 金属编结及制品',180001410,'TS916','18151105',4,'TS916');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001416,'TS917 产品标准检验',180001410,'TS917','18151106',4,'TS917');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001417,'TS918 五金工厂',180001410,'TS918','18151107',4,'TS918');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001418,'TS93 工艺美术制品工业',180001315,'TS93','181512',3,'TS93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001419,'TS932 雕塑工艺品',180001418,'TS932','18151201',4,'TS932');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001420,'TS933 石料美术制品',180001418,'TS933','18151202',4,'TS933');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001421,'TS934 金属工艺美术制品',180001418,'TS934','18151203',4,'TS934');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001422,'TS935 刺绣、编结、制毯',180001418,'TS935','18151204',4,'TS935');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001423,'[TS936] 陶瓷、玻璃工艺美术制品',180001418,'[TS936]','18151205',4,'TS936');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001424,'TS938 民间工艺美术制品',180001418,'TS938','18151206',4,'TS938');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001425,'TS939 其他工艺美术制品',180001418,'TS939','18151207',4,'TS939');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001426,'TS94 服装工业、制鞋工业',180001315,'TS94','181513',3,'TS94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001427,'TS941 服装工业',180001426,'TS941','18151301',4,'TS941');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001428,'TS942 服装表演、服装展示',180001426,'TS942','18151302',4,'TS942');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001429,'TS943 制鞋工业',180001426,'TS943','18151303',4,'TS943');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001430,'TS95 其他轻工业、手工业',180001315,'TS95','181514',3,'TS95');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001431,'TS951 文教用品制造工业',180001430,'TS951','18151401',4,'TS951');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001432,'TS952 体育器具制造工业',180001430,'TS952','18151402',4,'TS952');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001433,'TS953 乐器制造工业',180001430,'TS953','18151403',4,'TS953');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001434,'TS954 放音器、录音片',180001430,'TS954','18151404',4,'TS954');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001435,'TS955 舞台道具、装饰用品、事物模特制造工业',180001430,'TS955','18151405',4,'TS955');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001436,'TS956 灯具制造',180001430,'TS956','18151406',4,'TS956');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001437,'TS958 玩具制造工业',180001430,'TS958','18151407',4,'TS958');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001438,'TS959.1 毛发、羽毛加工及制品',180001430,'TS959.1','18151408',4,'TS9591');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001439,'TS959.2 竹、藤、棕、草等加工及制品',180001430,'TS959.2','18151409',4,'TS9592');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001440,'TS959.3 油漆工艺',180001430,'TS959.3','18151410',4,'TS9593');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001441,'TS959.4 纸料工',180001430,'TS959.4','18151411',4,'TS9594');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001442,'TS959.5 制扇、制伞',180001430,'TS959.5','18151412',4,'TS9595');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001443,'TS959.6 眼镜及其制造',180001430,'TS959.6','18151413',4,'TS9596');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001444,'TS959.7 制镜',180001430,'TS959.7','18151414',4,'TS9597');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001445,'TS959.9 其他',180001430,'TS959.9','18151415',4,'TS9599');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001446,'TS97 生活服务技术',180001315,'TS97','181515',3,'TS97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001447,'TS971 美食学',180001446,'TS971','18151501',4,'TS971');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001448,'TS972 饮食调制技术及设备',180001446,'TS972','18151502',4,'TS972');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001449,'TS973 洗染、缝补',180001446,'TS973','18151503',4,'TS973');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001450,'TS974 美容、沐浴、清洁',180001446,'TS974','18151504',4,'TS974');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001451,'TS975 居住管理',180001446,'TS975','18151505',4,'TS975');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001452,'TS976 家庭管理、家庭生活、家庭服务',180001446,'TS976','18151506',4,'TS976');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001453,'TU 建筑科学',180000001,'TU','1816',2,'TU');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001454,'TU-0 建筑理论',180001453,'TU-0','18160101',4,'TU-0');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001455,'TU-8 建筑艺术',180001453,'TU-8','18160102',4,'TU-8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001456,'[TU-9] 建筑经济',180001453,'[TU-9]','18160103',4,'TU-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001457,'TU1 建筑基础科学',180001453,'TU1','181602',3,'TU1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001458,'TU11 建筑物理学',180001457,'TU11','18160201',4,'TU11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001459,'TU12 数学在建筑中的应用',180001457,'TU12','18160202',4,'TU12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001460,'[TU13] 力学在建筑中的应用',180001457,'[TU13]','18160203',4,'TU13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001461,'TU14 气象学在建筑中的应用',180001457,'TU14','18160204',4,'TU14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001462,'TU17 电子计算机在建筑中的应用',180001457,'TU17','18160205',4,'TU17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001463,'TU18 其他科学技术在建筑中的应用',180001457,'TU18','18160206',4,'TU18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001464,'TU19 建筑勘测',180001453,'TU19','181603',3,'TU19');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001465,'TU191 建筑勘探原理与组织',180001464,'TU191','18160301',4,'TU191');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001466,'TU192 取样、试验、参数',180001464,'TU192','18160302',4,'TU192');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001467,'[TU193] 钻进技术',180001464,'[TU193]','18160303',4,'TU193');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001468,'TU194 钻进设计、钻进布置',180001464,'TU194','18160304',4,'TU194');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001469,'TU195 勘探技术',180001464,'TU195','18160305',4,'TU195');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001470,'TU196 观测',180001464,'TU196','18160306',4,'TU196');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001471,'[TU197] 勘探仪器与设备',180001464,'[TU197]','18160307',4,'TU197');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001472,'TU198 建筑工程测量及制图',180001464,'TU198','18160308',4,'TU198');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001473,'TU2 建筑设计',180001453,'TU2','181604',3,'TU2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001474,'TU20 一般性问题',180001473,'TU20','18160401',4,'TU20');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001475,'TU22 房屋细部构造设计',180001473,'TU22','18160402',4,'TU22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001476,'TU24 民用建筑',180001473,'TU24','18160403',4,'TU24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001477,'TU26 农业建筑',180001473,'TU26','18160404',4,'TU26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001478,'TU27 工业建筑',180001473,'TU27','18160405',4,'TU27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001479,'[TU28] 地下建筑',180001473,'[TU28]','18160406',4,'TU28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001480,'TU289 水下建筑',180001473,'TU289','18160407',4,'TU289');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001481,'TU29 其他建筑',180001473,'TU29','18160408',4,'TU29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001482,'TU3 建筑结构',180001453,'TU3','181605',3,'TU3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001483,'TU31 结构理论、计算',180001482,'TU31','18160501',4,'TU31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001484,'TU32 杆件系统结构',180001482,'TU32','18160502',4,'TU32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001485,'TU33 薄壳结构',180001482,'TU33','18160503',4,'TU33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001486,'TU34 实体结构',180001482,'TU34','18160504',4,'TU34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001487,'TU35 特种结构',180001482,'TU35','18160505',4,'TU35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001488,'TU36 土、砖、石、竹、木结构',180001482,'TU36','18160506',4,'TU36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001489,'TU37 混凝土结构、钢筋混凝土结构',180001482,'TU37','18160507',4,'TU37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001490,'TU38 非金属结构',180001482,'TU38','18160508',4,'TU38');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001491,'TU39 金属结构',180001482,'TU39','18160509',4,'TU39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001492,'TU398 组合结构',180001482,'TU398','18160510',4,'TU398');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001493,'TU399 其他结构',180001482,'TU399','18160511',4,'TU399');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001494,'TU4 土力学、地基基础工程',180001453,'TU4','181606',3,'TU4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001495,'TU41 土工试验',180001494,'TU41','18160601',4,'TU41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001496,'[TU42] 工程地质学、水文地质学',180001494,'[TU42]','18160602',4,'TU42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001497,'TU43 土力学',180001494,'TU43','18160603',4,'TU43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001498,'TU44 各类型土与地基',180001494,'TU44','18160604',4,'TU44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001499,'TU45 岩石（岩体）力学及岩石测试',180001494,'TU45','18160605',4,'TU45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001500,'TU46 地下水与基础',180001494,'TU46','18160606',4,'TU46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001501,'TU47 地基基础',180001494,'TU47','18160607',4,'TU47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001502,'TU48 薄壳基础',180001494,'TU48','18160608',4,'TU48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001503,'TU5 建筑材料',180001453,'TU5','181607',3,'TU5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001504,'TU50 一般性问题',180001503,'TU50','18160701',4,'TU50');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001505,'TU51 金属材料',180001503,'TU51','18160702',4,'TU51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001506,'TU52 非金属材料',180001503,'TU52','18160703',4,'TU52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001507,'TU53 有机材料、建筑化工材料',180001503,'TU53','18160704',4,'TU53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001508,'TU54 耐高温材料（耐火材料）、防火材料',180001503,'TU54','18160705',4,'TU54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001509,'TU55 隔热材料、隔（吸）声材料',180001503,'TU55','18160706',4,'TU55');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001510,'TU56 建筑涂料、装饰材料',180001503,'TU56','18160707',4,'TU56');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001511,'TU57 防水、防潮材料，嵌缝、密封材料',180001503,'TU57','18160708',4,'TU57');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001512,'TU58 粘结料',180001503,'TU58','18160709',4,'TU58');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001513,'TU59 其他特种材料',180001503,'TU59','18160710',4,'TU59');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001514,'TU6 建筑施工机械和设备',180001453,'TU6','181608',3,'TU6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001515,'TU60 一般性问题',180001514,'TU60','18160801',4,'TU60');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001516,'[TU61] 起重运输机械',180001514,'[TU61]','18160802',4,'TU61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001517,'TU62 土工机械、挖掘机械',180001514,'TU62','18160803',4,'TU62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001518,'TU63 石方机械和设备',180001514,'TU63','18160804',4,'TU63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001519,'TU64 混凝土机械与设备',180001514,'TU64','18160805',4,'TU64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001520,'TU65 砖瓦砌筑机具、粉刷机具和装修机具',180001514,'TU65','18160806',4,'TU65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001521,'TU66 压实机械',180001514,'TU66','18160807',4,'TU66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001522,'TU67 桩工机械',180001514,'TU67','18160808',4,'TU67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001523,'[TU681] 挖泥船、卸泥船、运泥船',180001514,'[TU681]','18160809',4,'TU681');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001524,'[TU687] 管道铺设机械',180001514,'[TU687]','18160810',4,'TU687');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001525,'[TU688] 金属加工工具',180001514,'[TU688]','18160811',4,'TU688');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001526,'TU689 机器人在建筑施工中的应用',180001514,'TU689','18160812',4,'TU689');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001527,'TU69 其他建筑机械和工具',180001514,'TU69','18160813',4,'TU69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001528,'TU7 建筑施工',180001453,'TU7','181609',3,'TU7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001529,'TU71 施工管理',180001528,'TU71','18160901',4,'TU71');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001530,'TU72 施工组织与计划',180001528,'TU72','18160902',4,'TU72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001531,'TU73 施工设备',180001528,'TU73','18160903',4,'TU73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001532,'TU74 施工技术',180001528,'TU74','18160904',4,'TU74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001533,'TU75 各项工程与工种',180001528,'TU75','18160905',4,'TU75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001534,'TU8 房屋建筑设备',180001453,'TU8','181610',3,'TU8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001535,'TU81 管道设备',180001534,'TU81','18161001',4,'TU81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001536,'TU82 房屋卫生技术设备',180001534,'TU82','18161002',4,'TU82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001537,'TU83 空气调节、采暖、通风及其设备',180001534,'TU83','18161003',4,'TU83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001538,'[TU84] 煤气设备',180001534,'[TU84]','18161004',4,'TU84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001539,'TU85 机电设备',180001534,'TU85','18161005',4,'TU85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001540,'TU86 降温与保温（隔热）设备',180001534,'TU86','18161006',4,'TU86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001541,'TU87 消声、隔声设备',180001534,'TU87','18161007',4,'TU87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001542,'[TU88] 照明设备',180001534,'[TU88]','18161008',4,'TU88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001543,'TU89 安全设备',180001534,'TU89','18161009',4,'TU89');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001544,'TU9 地下建筑',180001453,'TU9','181611',3,'TU9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001545,'TU91 地下建筑理论、勘测与计算',180001544,'TU91','18161101',4,'TU91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001546,'TU92 地下建筑设计',180001544,'TU92','18161102',4,'TU92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001547,'TU93 地下建筑结构',180001544,'TU93','18161103',4,'TU93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001548,'TU94 地下建筑施工、施工机械与设备',180001544,'TU94','18161104',4,'TU94');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001549,'TU96 地下建筑设备',180001544,'TU96','18161105',4,'TU96');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001550,'TU97 高层建筑',180001453,'TU97','181612',3,'TU97');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001551,'TU971 高层建筑理论',180001550,'TU971','18161201',4,'TU971');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001552,'TU972 高层建筑设计',180001550,'TU972','18161202',4,'TU972');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001553,'TU973 高层建筑结构',180001550,'TU973','18161203',4,'TU973');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001554,'TU974 高层建筑施工、施工机械与设备',180001550,'TU974','18161204',4,'TU974');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001555,'TU976 高层建筑设备',180001550,'TU976','18161205',4,'TU976');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001556,'TU978 高层建筑养护、维修、改建',180001550,'TU978','18161206',4,'TU978');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001557,'TU98 区域规划、城乡规划',180001453,'TU98','181613',3,'TU98');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001558,'TU981 规划理论与方法',180001557,'TU981','18161301',4,'TU981');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001559,'TU982 区域规划',180001557,'TU982','18161302',4,'TU982');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001560,'TU984 城市规划',180001557,'TU984','18161303',4,'TU984');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001561,'TU985 绿化规划',180001557,'TU985','18161304',4,'TU985');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001562,'TU986 园林规划与建设',180001557,'TU986','18161305',4,'TU986');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001563,'TU99 市政工程',180001453,'TU99','181614',3,'TU99');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001564,'TU990.3 管线工程',180001563,'TU990.3','18161401',4,'TU9903');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001565,'TU991 给水工程（上水道工程）',180001563,'TU991','18161402',4,'TU991');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001566,'TU992 排水工程（沟渠工程、下水道工程）',180001563,'TU992','18161403',4,'TU992');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001567,'TU993 公共卫生工程',180001563,'TU993','18161404',4,'TU993');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001568,'TU994 城市供电和通信',180001563,'TU994','18161405',4,'TU994');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001569,'TU995 城市集中供热',180001563,'TU995','18161406',4,'TU995');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001570,'TU996 城市燃气供应',180001563,'TU996','18161407',4,'TU996');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001571,'[TU997] 城市道路、桥梁工程',180001563,'[TU997]','18161408',4,'TU997');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001572,'TU998 其他市政工程及公用设备',180001563,'TU998','18161409',4,'TU998');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001573,'TV 水利工程',180000001,'TV','1817',2,'TV');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001574,'[TV-9] 水利经济',180001573,'[TV-9]','18170101',4,'TV-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001575,'TV1 水利工程基础科学',180001573,'TV1','181702',3,'TV1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001576,'[TV11] 水文学',180001575,'[TV11]','18170201',4,'TV11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001577,'TV12 工程水文学',180001575,'TV12','18170202',4,'TV12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001578,'TV13 水力学',180001575,'TV13','18170203',4,'TV13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001579,'TV14 泥沙动力学、河流动力学',180001575,'TV14','18170204',4,'TV14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001580,'TV15 世界各国河流泥沙',180001575,'TV15','18170205',4,'TV15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001581,'[TV16] 土力学',180001575,'[TV16]','18170206',4,'TV16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001582,'TV21 水资源调查与水利规划',180001573,'TV21','181703',3,'TV21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001583,'TV211 水利调查',180001582,'TV211','18170301',4,'TV211');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001584,'TV212 水利规划、水电规划',180001582,'TV212','18170302',4,'TV212');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001585,'TV213 水资源开发',180001582,'TV213','18170303',4,'TV213');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001586,'TV214 水利计算',180001582,'TV214','18170304',4,'TV214');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001587,'TV22 水工勘测、水工设计',180001573,'TV22','181704',3,'TV22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001588,'TV221 水工勘测',180001587,'TV221','18170401',4,'TV221');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001589,'TV222 水工设计',180001587,'TV222','18170402',4,'TV222');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001590,'TV223 地基基础及其加固',180001587,'TV223','18170403',4,'TV223');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001591,'TV3 水工结构',180001573,'TV3','181705',3,'TV3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001592,'TV31 结构理论和计算',180001591,'TV31','18170501',4,'TV31');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001593,'TV32 结构试验',180001591,'TV32','18170502',4,'TV32');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001594,'TV33 混凝土结构和加筋混凝土结构',180001591,'TV33','18170503',4,'TV33');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001595,'TV34 金属结构',180001591,'TV34','18170504',4,'TV34');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001596,'TV35 非金属结构',180001591,'TV35','18170505',4,'TV35');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001597,'TV36 水下结构',180001591,'TV36','18170506',4,'TV36');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001598,'TV37 拱和薄壳结构',180001591,'TV37','18170507',4,'TV37');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001599,'TV39 其他',180001591,'TV39','18170508',4,'TV39');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001600,'TV4 水工材料',180001573,'TV4','181706',3,'TV4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001601,'TV41 水工材料试验',180001600,'TV41','18170601',4,'TV41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001602,'TV42 水泥、集料（骨料）、掺加料和外加剂',180001600,'TV42','18170602',4,'TV42');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001603,'TV43 水工混凝土和砂浆',180001600,'TV43','18170603',4,'TV43');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001604,'TV44 防渗材料和止水材料',180001600,'TV44','18170604',4,'TV44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001605,'TV45 气硬性胶结材料',180001600,'TV45','18170605',4,'TV45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001606,'TV46 环氧树脂胶结材料',180001600,'TV46','18170606',4,'TV46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001607,'TV47 防腐材料',180001600,'TV47','18170607',4,'TV47');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001608,'TV48 金属材料',180001600,'TV48','18170608',4,'TV48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001609,'TV49 其他',180001600,'TV49','18170609',4,'TV49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001610,'TV5 水利工程施工',180001573,'TV5','181707',3,'TV5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001611,'TV51 施工计划和管理',180001610,'TV51','18170701',4,'TV51');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001612,'TV52 施工技术',180001610,'TV52','18170702',4,'TV52');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001613,'TV53 施工机械与设备',180001610,'TV53','18170703',4,'TV53');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001614,'TV54 各种工程、工种',180001610,'TV54','18170704',4,'TV54');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001615,'TV6 水利枢纽、水工建筑物',180001573,'TV6','181708',3,'TV6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001616,'TV61 水利枢纽工程',180001615,'TV61','18170801',4,'TV61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001617,'TV62 水库工程',180001615,'TV62','18170802',4,'TV62');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001618,'TV63 世界各国水利枢纽与水库',180001615,'TV63','18170803',4,'TV63');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001619,'TV64 挡水坝',180001615,'TV64','18170804',4,'TV64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001620,'TV65 泄水建筑物',180001615,'TV65','18170805',4,'TV65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001621,'TV66 水闸',180001615,'TV66','18170806',4,'TV66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001622,'TV67 取水、引水工程',180001615,'TV67','18170807',4,'TV67');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001623,'TV68 调水工程',180001615,'TV68','18170808',4,'TV68');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001624,'[TV691] 过航建筑物',180001615,'[TV691]','18170809',4,'TV691');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001625,'TV697 水库管理',180001615,'TV697','18170810',4,'TV697');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001626,'TV698 水工建筑物管理',180001615,'TV698','18170811',4,'TV698');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001627,'TV7 水能利用、水电站工程',180001573,'TV7','181709',3,'TV7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001628,'TV72 水能勘测与设计',180001627,'TV72','18170901',4,'TV72');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001629,'TV73 水电站建筑与设备',180001627,'TV73','18170902',4,'TV73');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001630,'TV74 各种水电站',180001627,'TV74','18170903',4,'TV74');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001631,'TV75 世界各国水力发电工程',180001627,'TV75','18170904',4,'TV75');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001632,'TV76 中小河道水能利用',180001627,'TV76','18170905',4,'TV76');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001633,'TV8 治河工程与防洪工程',180001573,'TV8','181710',3,'TV8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001634,'TV81 河工学',180001633,'TV81','18171001',4,'TV81');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001635,'TV82 治河勘测及规划',180001633,'TV82','18171002',4,'TV82');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001636,'TV83 河工试验',180001633,'TV83','18171003',4,'TV83');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001637,'[TV84] 河工材料',180001633,'[TV84]','18171004',4,'TV84');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001638,'TV85 治河方法（河道整治）',180001633,'TV85','18171005',4,'TV85');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001639,'TV86 整治建筑',180001633,'TV86','18171006',4,'TV86');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001640,'TV87 防洪工程',180001633,'TV87','18171007',4,'TV87');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001641,'TV88 世界各国河流治理',180001633,'TV88','18171008',4,'TV88');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001642,'[TV91] 运渠(运河、渠道)工程',180001573,'[TV91]','181711',3,'TV91');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001643,'[TV92] 港湾工程',180001573,'[TV92]','181712',3,'TV92');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(180001644,'[TV93] 农田水利工程',180001573,'[TV93]','181713',3,'TV93');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000001,'U 交通运输',null,'U','19',1,'U');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000002,'[U-9] 交通运输经济',190000001,'[U-9]','190101',3,'U-9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000003,'U1 综合运输',190000001,'U1','1902',2,'U1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000004,'U11 综合运输体制与结构',190000003,'U11','190201',3,'U11');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000005,'U12 城市交通运输',190000003,'U12','190202',3,'U12');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000006,'U13 乡村交通运输',190000003,'U13','190203',3,'U13');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000007,'U14 长途运输',190000003,'U14','190204',3,'U14');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000008,'[U15] 联运',190000003,'[U15]','190205',3,'U15');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000009,'U16 工商业运输',190000003,'U16','190206',3,'U16');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000010,'U169 集装箱运输',190000003,'U169','190207',3,'U169');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000011,'U17 管道运输',190000003,'U17','190208',3,'U17');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000012,'U18 索道运输',190000003,'U18','190209',3,'U18');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000013,'U2 铁路运输',190000001,'U2','1903',2,'U2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000014,'U21 铁路线路工程',190000013,'U21','190301',3,'U21');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000015,'U22 电气化铁路',190000013,'U22','190302',3,'U22');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000016,'U23 特种铁路',190000013,'U23','190303',3,'U23');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000017,'[U24] 铁路桥涵工程',190000013,'[U24]','190304',3,'U24');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000018,'[U25] 铁路隧道工程',190000013,'[U25]','190305',3,'U25');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000019,'U26 机车工程',190000013,'U26','190306',3,'U26');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000020,'U27 车辆工程',190000013,'U27','190307',3,'U27');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000021,'U28 铁路通信、信号',190000013,'U28','190308',3,'U28');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000022,'U29 铁路运输管理工程',190000013,'U29','190309',3,'U29');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000023,'U4 公路运输',190000001,'U4','1904',2,'U4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000024,'U41 道路工程',190000023,'U41','190401',3,'U41');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000025,'U44 桥涵工程',190000023,'U44','190402',3,'U44');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000026,'U45 隧道工程',190000023,'U45','190403',3,'U45');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000027,'U46 汽车工程',190000023,'U46','190404',3,'U46');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000028,'U48 其他道路运输工具',190000023,'U48','190405',3,'U48');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000029,'U49 交通工程与公路运输技术管理',190000023,'U49','190406',3,'U49');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000030,'U6 水路运输',190000001,'U6','1905',2,'U6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000031,'U61 航道工程',190000030,'U61','190501',3,'U61');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000032,'U64 通航建筑物与助航设备',190000030,'U64','190502',3,'U64');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000033,'U65 港口工程',190000030,'U65','190503',3,'U65');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000034,'U66 船舶工程',190000030,'U66','190504',3,'U66');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000035,'U69 水路运输技术管理',190000030,'U69','190505',3,'U69');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(190000036,'[U8] 航空运输',190000001,'[U8]','1906',2,'U8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(200000001,'V 航空、航天',null,'V','20',1,'V');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(200000002,'V1 航空、航天技术的研究与探索',200000001,'V1','2001',2,'V1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(200000003,'V2 航空',200000001,'V2','2002',2,'V2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(200000004,'V4 航天（宇宙航行）',200000001,'V4','2003',2,'V4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(200000005,'[V7] 航空、航天医学',200000001,'[V7]','2004',2,'V7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000001,'X 环境科学、安全科学',null,'X','21',1,'X');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000002,'X1 环境科学基础理论',210000001,'X1','2101',2,'X1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000003,'X2 社会与环境',210000001,'X2','2102',2,'X2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000004,'X3 环境保护管理',210000001,'X3','2103',2,'X3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000005,'X4 灾害及其防治',210000001,'X4','2104',2,'X4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000006,'X5 环境污染及其防治',210000001,'X5','2105',2,'X5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000007,'X7 废物处理与综合利用',210000001,'X7','2106',2,'X7');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000008,'X8 环境质量评价与环境监测',210000001,'X8','2107',2,'X8');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(210000009,'X9 安全科学',210000001,'X9','2108',2,'X9');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000001,'Z 综合性图书',null,'Z','22',1,'Z');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000002,'Z1 丛书',220000001,'Z1','2201',2,'Z1');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000003,'Z2 百科全书、类书',220000001,'Z2','2202',2,'Z2');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000004,'Z3 辞典',220000001,'Z3','2203',2,'Z3');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000005,'Z4 论文集、全集、选集、杂著',220000001,'Z4','2204',2,'Z4');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000006,'Z5 年鉴、年刊',220000001,'Z5','2205',2,'Z5');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000007,'Z6 期刊、连续性出版物',220000001,'Z6','2206',2,'Z6');
insert into RES_MIDDLE_TYPE(BZTY_ID,BZTY_NAME,BZTY_PARENT_ID,BZTY_CODE,BZTY_CUSTOM_CODE,BZTY_LAYER,BZTY_PARSE_CODE) values(220000008,'Z8 图书目录、文摘、索引',220000001,'Z8','2207',2,'Z8');

/*==============================================================*/
/* Index: IBZTY_PARENT_ID                                       */
/*==============================================================*/
create index IBZTY_PARENT_ID on RES_MIDDLE_TYPE (
   BZTY_PARENT_ID ASC
);

/*==============================================================*/
/* Index: IBZTY_LAYER                                           */
/*==============================================================*/
create index IBZTY_LAYER on RES_MIDDLE_TYPE (
   BZTY_LAYER ASC
);

/*==============================================================*/
/* Index: IBZTY_CUSTOM_CODE                                     */
/*==============================================================*/
create index IBZTY_CUSTOM_CODE on RES_MIDDLE_TYPE (
   BZTY_CUSTOM_CODE ASC
);

/*==============================================================*/
/* Table: RES_MISSION_INFO                                      */
/*==============================================================*/
create table RES_MISSION_INFO  (
   RMIN_ID              NUMERIC(19)                     not null,
   RMIN_RMST_ID         NUMERIC(19),
   RMIN_AUTHOR          NUMERIC(19,0),
   RMIN_USER            NUMERIC(19,0),
   RMIN_LANGUAGE        NUMERIC(19),
   RMIN_NAMEREMARK      VARCHAR(128),
   RMIN_MISSNAME        VARCHAR(128)                   not null,
   RMIN_REMARK          VARCHAR(1024),
   RMIN_BUILDDATE       DATETIME,
   RMIN_ENDDATE         DATETIME,
   RMIN_STATUS          NUMERIC(5),
   RMIN_RLTY_ID         NUMERIC(19),
   RMIN_CHECKUSER       NUMERIC(19,0),
   constraint PK_RES_MISSION_INFO primary key (RMIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRMIN_RMST_ID                                         */
/*==============================================================*/
create index IRMIN_RMST_ID on RES_MISSION_INFO (
   RMIN_RMST_ID ASC
);

/*==============================================================*/
/* Index: IRMIN_AUTHOR                                          */
/*==============================================================*/
create index IRMIN_AUTHOR on RES_MISSION_INFO (
   RMIN_AUTHOR ASC
);

/*==============================================================*/
/* Index: IRMIN_USER                                            */
/*==============================================================*/
create index IRMIN_USER on RES_MISSION_INFO (
   RMIN_USER ASC
);

/*==============================================================*/
/* Index: IRMIN_STATUS                                          */
/*==============================================================*/
create index IRMIN_STATUS on RES_MISSION_INFO (
   RMIN_STATUS ASC
);

/*==============================================================*/
/* Table: RES_MISSION_LANGUAGE                                  */
/*==============================================================*/
create table RES_MISSION_LANGUAGE  (
   RMLA_ID              NUMERIC(19)                     not null,
   RMLA_RMIN_ID         NUMERIC(19),
   RMLA_RLTY_ID         NUMERIC(19),
   RMLA_STATUS          NUMERIC(5,0),
   RMLA_PROCESSOR       NUMERIC(19),
   RMLA_ASSESSOR        NUMERIC(19),
   RMLA_PROCTIME        DATETIME,
   RMLA_ASSETIME        DATETIME,
   RMLA_ISFIRST         NUMERIC(1,0),
   constraint PK_RES_MISSION_LANGUAGE primary key (RMLA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRMLA_RMIN_ID                                         */
/*==============================================================*/
create index IRMLA_RMIN_ID on RES_MISSION_LANGUAGE (
   RMLA_RMIN_ID ASC
);

/*==============================================================*/
/* Index: IRMLA_RLTY_ID                                         */
/*==============================================================*/
create index IRMLA_RLTY_ID on RES_MISSION_LANGUAGE (
   RMLA_RLTY_ID ASC
);

/*==============================================================*/
/* Index: IRMLA_STATUS                                          */
/*==============================================================*/
create index IRMLA_STATUS on RES_MISSION_LANGUAGE (
   RMLA_STATUS ASC
);

/*==============================================================*/
/* Index: IRMLA_PROCESSOR                                       */
/*==============================================================*/
create index IRMLA_PROCESSOR on RES_MISSION_LANGUAGE (
   RMLA_PROCESSOR ASC
);

/*==============================================================*/
/* Index: IRMLA_ASSESSOR                                        */
/*==============================================================*/
create index IRMLA_ASSESSOR on RES_MISSION_LANGUAGE (
   RMLA_ASSESSOR ASC
);

/*==============================================================*/
/* Table: RES_MISS_DEVOLVE                                      */
/*==============================================================*/
create table RES_MISS_DEVOLVE  (
   RMDE_ID              NUMERIC(19)                     not null,
   RMDE_PREUID          NUMERIC(19,0),
   RMDE_AFTUID          NUMERIC(19,0),
   RMDE_RMIN_ID         NUMERIC(19,0),
   RMDE_MISSIONTYPE     NUMERIC(5,0),
   RMDE_MISSDATE        DATETIME,
   RMDE_DEVODATE        DATETIME,
   RMDE_REMARK          VARCHAR(1024),
   RMDE_STATUS          NUMERIC(2,0),
   RMDE_MIND            VARCHAR(1024),
   RMDE_AUDIDATE        DATETIME,
   constraint PK_RES_MISS_DEVOLVE primary key (RMDE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_NAV_CACHE                                         */
/*==============================================================*/
create table RES_NAV_CACHE  (
   RNC_ID               NUMERIC(19,0)                   not null,
   RNC_RECORD_ID        NUMERIC(19,0)                   not null,
   RNC_COUNT            NUMERIC(9,0)                    not null,
   RNC_TYPE             NUMERIC(5,0)                    not null,
   RNC_NAME             VARCHAR(256)                   not null,
   RNC_PARENT_ID        NUMERIC(19,0),
   RNC_DISPLAY_STATUS   NUMERIC(1,0)                    not null,
   constraint PK_RES_NAV_CACHE primary key (RNC_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_NAV_DATE_SPAN_CACHE                               */
/*==============================================================*/
create table RES_NAV_DATE_SPAN_CACHE  (
   RNDSC_ID             NUMERIC(19,0)                   not null,
   RNDSC_PRE_YEAR       VARCHAR(4),
   RNDSC_POST_YEAR      VARCHAR(4),
   RNDSC_IS_INCLUD_PRE_YEAR NUMERIC(1,0),
   RNDSC_IS_INCLUD_POST_YEAR NUMERIC(1,0),
   constraint PK_RES_NAV_DATE_SPAN_CACHE primary key (RNDSC_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into RES_NAV_DATE_SPAN_CACHE(RNDSC_ID,RNDSC_POST_YEAR,RNDSC_IS_INCLUD_POST_YEAR) values(600000,'1998',0);
insert into RES_NAV_DATE_SPAN_CACHE values(500000,'1998','2001',1,0);
insert into RES_NAV_DATE_SPAN_CACHE values(400000,'2001','2004',1,0);
insert into RES_NAV_DATE_SPAN_CACHE values(300000,'2004','2007',1,0);
insert into RES_NAV_DATE_SPAN_CACHE values(200000,'2007','2010',1,0);
insert into RES_NAV_DATE_SPAN_CACHE values(100000,'2010','2013',1,0);

/*==============================================================*/
/* Table: RES_SOURCE_BROWSE_HISTOREY                            */
/*==============================================================*/
create table RES_SOURCE_BROWSE_HISTOREY  (
   RSBH_ID              NUMERIC(19,0)                   not null,
   RSBH_RSDA_ID         NUMERIC(19,0),
   RSBH_SUIN_ID         NUMERIC(19,0),
   RSBH_DATE            DATETIME,
   constraint PK_RES_SOURCE_BROWSE_HISTOREY primary key (RSBH_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRSBH_RSDA_ID                                         */
/*==============================================================*/
create index IRSBH_RSDA_ID on RES_SOURCE_BROWSE_HISTOREY (
   RSBH_RSDA_ID ASC
);

/*==============================================================*/
/* Index: IRSBH_SUIN_ID                                         */
/*==============================================================*/
create index IRSBH_SUIN_ID on RES_SOURCE_BROWSE_HISTOREY (
   RSBH_SUIN_ID ASC
);

/*==============================================================*/
/* Index: IRSBH_DATE                                            */
/*==============================================================*/
create index IRSBH_DATE on RES_SOURCE_BROWSE_HISTOREY (
   RSBH_DATE ASC
);

/*==============================================================*/
/* Table: RES_SOURCE_COMT                                       */
/*==============================================================*/
create table RES_SOURCE_COMT  (
   RSCO_ID              NUMERIC(19)                     not null,
   RSCO_SUIN_ID         NUMERIC(19),
   RSCO_RSDA_ID         NUMERIC(19),
   RSCO_REFID           NUMERIC(19),
   RSCO_CONTENT         VARCHAR(1024),
   RSCO_GRADE           NUMERIC(5,0),
   RSCO_DATE            DATETIME,
   RSCO_STATUS          NUMERIC(1,0),
   constraint PK_RES_SOURCE_COMT primary key (RSCO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRSCO_SUIN_ID                                         */
/*==============================================================*/
create index IRSCO_SUIN_ID on RES_SOURCE_COMT (
   RSCO_SUIN_ID ASC
);

/*==============================================================*/
/* Index: IRSCO_RSDA_ID                                         */
/*==============================================================*/
create index IRSCO_RSDA_ID on RES_SOURCE_COMT (
   RSCO_RSDA_ID ASC
);

/*==============================================================*/
/* Index: IRSCO_DATE                                            */
/*==============================================================*/
create index IRSCO_DATE on RES_SOURCE_COMT (
   RSCO_DATE ASC
);

/*==============================================================*/
/* Index: IRSCO_STATUS                                          */
/*==============================================================*/
create index IRSCO_STATUS on RES_SOURCE_COMT (
   RSCO_STATUS ASC
);

/*==============================================================*/
/* Table: RES_SOURCE_DATA                                       */
/*==============================================================*/
create table RES_SOURCE_DATA  (
   RSDA_ID              NUMERIC(19)                     not null,
   RSDA_RMIN_ID         NUMERIC(19),
   RSDA_RMST_ID         NUMERIC(19),
   RSDA_RLTY_ID         NUMERIC(19),
   RSDA_RSTY_ID         NUMERIC(19),
   RSDA_SMAN_ID         NUMERIC(19,0),
   RSDA_RDTY_ID         NUMERIC(19),
   RSDA_MIDDLENAME      VARCHAR(64),
   RSDA_BUILDDATE       DATETIME,
   RSDA_STATUS          NUMERIC(1,0),
   RSDA_GRADE           NUMERIC(2,0),
   RSDA_CMTSTUS         NUMERIC(1,0),
   RSDA_RCMD            NUMERIC(1,0),
   RSDA_ISUSE           NUMERIC(1),
   RSDA_LOOKNUM         NUMERIC(9,0),
   RSDA_AUDITING        VARCHAR(512),
   RSDA_TITLE           VARCHAR(3200),
   RSDA_SUBJECT         VARCHAR(3200),
   RSDA_DESCRIPTION     VARCHAR(3200),
   RSDA_SOURCE          VARCHAR(3200),
   RSDA_LANGUAGE        VARCHAR(3200),
   RSDA_RELATION        VARCHAR(3200),
   RSDA_COVERAGE        VARCHAR(3200),
   RSDA_CREATOR         VARCHAR(3200),
   RSDA_PUBLISHER       VARCHAR(3200),
   RSDA_CONTRIBUTOR     VARCHAR(3200),
   RSDA_RIGHTS          VARCHAR(3200),
   RSDA_DATE            VARCHAR(3200),
   RSDA_TYPE            VARCHAR(3200),
   RSDA_FORMAT          VARCHAR(3200),
   RSDA_IDENTIFIER      VARCHAR(3200),
   RSDA_VIEWFILE_NAME   VARCHAR(256),
   RSDA_VIEWFILE_LOCAL_NAME VARCHAR(256),
   RSDA_VIEWFILE_URL    VARCHAR(1024),
   RSDA_VIEWFILE_SWF_URL VARCHAR(1024),
   RSDA_FILE_NAME       VARCHAR(256),
   RSDA_FILE_TYPE       VARCHAR(32),
   RSDA_FILE_LOCAL_NAME VARCHAR(256),
   RSDA_FILE_URL        VARCHAR(1024),
   RSDA_FILE_SWF_URL    VARCHAR(1024),
   RSDA_BIGIMG_URL      VARCHAR(1024),
   RSDA_BIGIMG_NAME     VARCHAR(256),
   RSDA_BIGIMG_LOCAL_NAME VARCHAR(256),
   RSDA_AUTHORTYPE      NUMERIC(5,0)                    not null,
   RSDA_RIHI_ID         NUMERIC(19,0),
   RSDA_USER_ID         NUMERIC(19,0),
   RSDA_RMST_STATUS     NUMERIC(5,0),
   RSDA_RMIN_STATUS     NUMERIC(5,0),
   RSDA_RDTY_TWO        NUMERIC(19),
   RSDA_RDTY_THREE      NUMERIC(19),
   RSDA_FILE_SIZE       NUMERIC(19,0),
   constraint PK_RES_SOURCE_DATA primary key (RSDA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRSDA_RMIN_ID                                         */
/*==============================================================*/
create index IRSDA_RMIN_ID on RES_SOURCE_DATA (
   RSDA_RMIN_ID ASC
);

/*==============================================================*/
/* Index: IRSDA_RMST_ID                                         */
/*==============================================================*/
create index IRSDA_RMST_ID on RES_SOURCE_DATA (
   RSDA_RMST_ID ASC
);

/*==============================================================*/
/* Index: IRSDA_RLTY_ID                                         */
/*==============================================================*/
create index IRSDA_RLTY_ID on RES_SOURCE_DATA (
   RSDA_RLTY_ID ASC
);

/*==============================================================*/
/* Index: IRSDA_RSTY_ID                                         */
/*==============================================================*/
create index IRSDA_RSTY_ID on RES_SOURCE_DATA (
   RSDA_RSTY_ID ASC
);

/*==============================================================*/
/* Index: IRSDA_RDTY_ID                                         */
/*==============================================================*/
create index IRSDA_RDTY_ID on RES_SOURCE_DATA (
   RSDA_RDTY_ID ASC
);

/*==============================================================*/
/* Index: IRSDA_TITLE                                           */
/*==============================================================*/
create index IRSDA_TITLE on RES_SOURCE_DATA (
   RSDA_TITLE ASC
);

/*==============================================================*/
/* Index: IRSDA_BUILDDATE                                       */
/*==============================================================*/
create index IRSDA_BUILDDATE on RES_SOURCE_DATA (
   RSDA_BUILDDATE ASC
);

/*==============================================================*/
/* Index: IRSDA_STATUS                                          */
/*==============================================================*/
create index IRSDA_STATUS on RES_SOURCE_DATA (
   RSDA_STATUS ASC
);

/*==============================================================*/
/* Index: IRSDA_RCMD                                            */
/*==============================================================*/
create index IRSDA_RCMD on RES_SOURCE_DATA (
   RSDA_RCMD ASC
);

/*==============================================================*/
/* Index: IRSDA_ISUSE                                           */
/*==============================================================*/
create index IRSDA_ISUSE on RES_SOURCE_DATA (
   RSDA_ISUSE ASC
);

/*==============================================================*/
/* Table: RES_SOURCE_DATA_CACHE                                 */
/*==============================================================*/
create table RES_SOURCE_DATA_CACHE  (
   RSDC_ID              NUMERIC(19,0)                   not null,
   RSDC_TITLE           VARCHAR(3000),
   RSDC_CREATOR         VARCHAR(3000),
   RSDC_PUBLISHER       VARCHAR(3000),
   RSDC_DESCRIPTION     VARCHAR(3000),
   RSDC_BUILD_DATE      DATETIME,
   RSDC_LOOK_NUM        NUMERIC(9,0),
   RSDC_RCMD            NUMERIC(1,0),
   RSDC_REVIEW_NUM      NUMERIC(5,0),
   RSDC_RLTY_ID         NUMERIC(19,0),
   RSDC_RLTY_NAME       VARCHAR(128),
   RSDC_RLTY_TYPE       NUMERIC(5,0),
   RSDC_ICON_TYPE       VARCHAR(50),
   RSDC_BIGIMG_URL      VARCHAR(1024),
   RSDC_FILE_ADDRESS    VARCHAR(512),
   constraint PK_RES_SOURCE_DATA_CACHE primary key (RSDC_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SOURCE_FILE                                       */
/*==============================================================*/
create table RES_SOURCE_FILE  (
   RSFI_ID              NUMERIC(19)                     not null,
   RES_RSDA_ID          NUMERIC(19),
   RES_LOCAL_PATH       VARCHAR(512),
   RES_FILE_PATH        VARCHAR(1024),
   RES_VIEW_PATH        VARCHAR(1024),
   RES_FILE_REMARK      VARCHAR(512),
   RES_INDEX            NUMERIC(5,0),
   RES_FILE_NAME        VARCHAR(512),
   constraint PK_RES_SOURCE_FILE primary key (RSFI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SOURCE_LANGUAGE                                   */
/*==============================================================*/
create table RES_SOURCE_LANGUAGE  (
   RSLA_ID              NUMERIC(19)                     not null,
   RSLA_RSDA_ID         NUMERIC(19),
   RSLA_RLTY_ID         NUMERIC(19),
   RSLA_USERNAME        VARCHAR(64),
   RSLA_USER_ID         NUMERIC(19),
   RSLA_DATE            VARCHAR(32),
   RSLA_STATUS          NUMERIC(1,0),
   RSLA_ISFIRST         NUMERIC(1,0),
   RSLA_AUDITING        VARCHAR(512),
   RSLA_AUTHORNAME      VARCHAR(256),
   RSLA_CHECK_LOGINNAME VARCHAR(64),
   RSLA_CHECK_SUINNAME  VARCHAR(256),
   constraint PK_RES_SOURCE_LANGUAGE primary key (RSLA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRSLA_RSDA_ID                                         */
/*==============================================================*/
create index IRSLA_RSDA_ID on RES_SOURCE_LANGUAGE (
   RSLA_RSDA_ID ASC
);

/*==============================================================*/
/* Index: IRSLA_RLTY_ID                                         */
/*==============================================================*/
create index IRSLA_RLTY_ID on RES_SOURCE_LANGUAGE (
   RSLA_RLTY_ID ASC
);

/*==============================================================*/
/* Index: IRSLA_STATUS                                          */
/*==============================================================*/
create index IRSLA_STATUS on RES_SOURCE_LANGUAGE (
   RSLA_STATUS ASC
);

/*==============================================================*/
/* Table: RES_SOURCE_REMARK                                     */
/*==============================================================*/
create table RES_SOURCE_REMARK  (
   RSRE_ID              NUMERIC(19,0)                   not null,
   RSRE_RSDA_ID         NUMERIC(19),
   RSRE_REMARK          LONGTEXT,
   constraint PK_RES_SOURCE_REMARK primary key (RSRE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SOURCE_REPEAT                                     */
/*==============================================================*/
create table RES_SOURCE_REPEAT  (
   RSRE_ID              NUMERIC(19)                     not null,
   RSRE_RMIN_ID         NUMERIC(19),
   RSRE_RSDA_ID         NUMERIC(19),
   RSRE_RSDA_TITLE      VARCHAR(3000),
   RSRE_TITLE           NUMERIC(9,0),
   RSRE_PUBLISHER       NUMERIC(9,0),
   RSRE_FILE_SIZE       NUMERIC(9,0),
   RSRE_FILE_TYPE       NUMERIC(9,0),
   RSRE_SUBJECT         NUMERIC(9,0),
   RSRE_DESCRIPTION     NUMERIC(9,0),
   RSRE_SOURCE          NUMERIC(9,0),
   RSRE_LANGUAGE        NUMERIC(9,0),
   RSRE_RELATION        NUMERIC(9,0),
   RSRE_COVERAGE        NUMERIC(9,0),
   RSRE_CREATOR         NUMERIC(9,0),
   RSRE_CONTRIBUTOR     NUMERIC(9,0),
   RSRE_RIGHTS          NUMERIC(9,0),
   RSRE_DATE            NUMERIC(9,0),
   RSRE_TYPE            NUMERIC(9,0),
   RSRE_FORMAT          NUMERIC(9,0),
   RSRE_IDENTIFIER      NUMERIC(9,0),
   RSRE_REPEAT_ALL      NUMERIC(9,0),
   constraint PK_RES_SOURCE_REPEAT primary key (RSRE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SOURCE_SUBJECT                                    */
/*==============================================================*/
create table RES_SOURCE_SUBJECT  (
   RSSU_ID              NUMERIC(19)                     not null,
   RSSU_RMIN_ID         NUMERIC(19),
   RSSU_NAME            VARCHAR(256),
   RSSU_REMARK          VARCHAR(1024),
   constraint PK_RES_SOURCE_SUBJECT primary key (RSSU_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_STORE_IP_DEPLOY                                   */
/*==============================================================*/
create table RES_STORE_IP_DEPLOY  (
   RSID_ID              NUMERIC(19,0)                   not null,
   RSID_RMST_ID         NUMERIC(19),
   RSID_IP_PART         VARCHAR(512)                   not null,
   constraint PK_RES_STORE_IP_DEPLOY primary key (RSID_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_STORE_LITER                                       */
/*==============================================================*/
create table RES_STORE_LITER  (
   RSLI_ID              NUMERIC(19)                     not null,
   RMST_ID              NUMERIC(19),
   RLTY_ID              NUMERIC(19),
   constraint PK_RES_STORE_LITER primary key (RSLI_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SUBJECT_TYPE                                      */
/*==============================================================*/
create table RES_SUBJECT_TYPE  (
   RSTY_ID              NUMERIC(19)                     not null,
   RSTY_PID             NUMERIC(19),
   RSTY_NAME            VARCHAR(64),
   RSTY_GRADE           NUMERIC(1,0),
   RSTY_INDEX           NUMERIC(9,0),
   RSTY_CODE            VARCHAR(128),
   constraint PK_RES_SUBJECT_TYPE primary key (RSTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_GRADE) values(0,0);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(1,0,'经济学',1,1);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(2,0,'理学',1,2);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(3,0,'法学',1,3);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(4,0,'工学',1,4);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(5,0,'管理学',1,5);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(6,0,'哲学',1,6);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(7,0,'教育学',1,7);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(8,0,'文学',1,8);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(9,0,'农学',1,9);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(10,0,'医学',1,10);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(11,0,'军事学',1,11);
insert into RES_SUBJECT_TYPE(RSTY_ID,RSTY_PID,RSTY_NAME,RSTY_GRADE,RSTY_INDEX) values(12,0,'情报学',1,12);

/*==============================================================*/
/* Index: IRSTY_PID                                             */
/*==============================================================*/
create index IRSTY_PID on RES_SUBJECT_TYPE (
   RSTY_PID ASC
);

/*==============================================================*/
/* Index: IRSTY_GRADE                                           */
/*==============================================================*/
create index IRSTY_GRADE on RES_SUBJECT_TYPE (
   RSTY_GRADE ASC
);

/*==============================================================*/
/* Index: IRSTY_INDEX                                           */
/*==============================================================*/
create index IRSTY_INDEX on RES_SUBJECT_TYPE (
   RSTY_INDEX ASC
);

/*==============================================================*/
/* Table: RES_SYNONYMS_KEY                                      */
/*==============================================================*/
create table RES_SYNONYMS_KEY  (
   RSKE_ID              NUMERIC(19)                     not null,
   RSKE_RIKE_ID         NUMERIC(19),
   RSKE_WORD            VARCHAR(128),
   constraint PK_RES_SYNONYMS_KEY primary key (RSKE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_SYSTEM_REPEAT                                     */
/*==============================================================*/
create table RES_SYSTEM_REPEAT  (
   RSYR_ID              NUMERIC(19)                     not null,
   RSYR_RSDA_ID         NUMERIC(19),
   RSYR_RSDA_TITLE      VARCHAR(3000),
   RSYR_REPEATNUM       NUMERIC(9,0),
   constraint PK_RES_SYSTEM_REPEAT primary key (RSYR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_TAG_INFO                                          */
/*==============================================================*/
create table RES_TAG_INFO  (
   RTIN_ID              NUMERIC(19)                     not null,
   RTIN_RSDA_ID         NUMERIC(19),
   RTIN_SUIN_ID         NUMERIC(19),
   RTIN_TITLE           VARCHAR(50)                    not null,
   RTIN_DATE            DATETIME,
   RTIN_HIT_NUM         NUMERIC(9,0),
   constraint PK_RES_TAG_INFO primary key (RTIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRTIN_RSDA_ID                                         */
/*==============================================================*/
create index IRTIN_RSDA_ID on RES_TAG_INFO (
   RTIN_RSDA_ID ASC
);

/*==============================================================*/
/* Index: IRTIN_SUIN_ID                                         */
/*==============================================================*/
create index IRTIN_SUIN_ID on RES_TAG_INFO (
   RTIN_SUIN_ID ASC
);

/*==============================================================*/
/* Index: IRTIN_HIT_NUM                                         */
/*==============================================================*/
create index IRTIN_HIT_NUM on RES_TAG_INFO (
   RTIN_HIT_NUM ASC
);

/*==============================================================*/
/* Table: RES_TEMPLATE_IMP                                      */
/*==============================================================*/
create table RES_TEMPLATE_IMP  (
   RTIM_ID              NUMERIC(19)                     not null,
   RTIM_RLTY_ID         NUMERIC(19),
   RTIM_RMST_ID         NUMERIC(19),
   RTIM_RTME_ID         NUMERIC(19),
   RTIM_NAME            VARCHAR(128)                   not null,
   constraint PK_RES_TEMPLATE_IMP primary key (RTIM_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RES_TEMPLET_META                                      */
/*==============================================================*/
create table RES_TEMPLET_META  (
   RTME_ID              NUMERIC(19)                     not null,
   RTME_REEL_ID         NUMERIC(19),
   RTME_RSIN_ID         NUMERIC(19),
   RTME_RLTY_ID         NUMERIC(19),
   constraint PK_RES_TEMPLET_META primary key (RTME_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: RSS_USER_DATA                                         */
/*==============================================================*/
create table RSS_USER_DATA  (
   RSS_DATA_ID          NUMERIC(19,0)                   not null,
   RSS_DATA_USER_ID     NUMERIC(19)                     not null,
   RSS_DATA_NAME        VARCHAR(256)                   not null,
   RSS_DATA_SOURCE_URL  VARCHAR(512)                   not null,
   RSS_DATA_ADD_DATE    DATETIME                       not null,
   RSS_DATA_LAST_MODIFY_DATE DATETIME,
   RSS_DATA_ORDER       NUMERIC(5,0)                    not null,
   RSS_DATA_DISPLAY_NUM NUMERIC(5,0),
   constraint PK_RSS_USER_DATA primary key (RSS_DATA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: IRSS_DATA_USER_ID                                     */
/*==============================================================*/
create index IRSS_DATA_USER_ID on RSS_USER_DATA (
   RSS_DATA_USER_ID ASC
);

/*==============================================================*/
/* Index: IRSS_DATA_ORDER                                       */
/*==============================================================*/
create index IRSS_DATA_ORDER on RSS_USER_DATA (
   RSS_DATA_ORDER ASC
);

/*==============================================================*/
/* Table: SC_CONSULTATION                                       */
/*==============================================================*/
create table SC_CONSULTATION  (
   SCCO_ID              NUMERIC(19,0)                   not null,
   SCCO_SUIN_ID         NUMERIC(19),
   SCCO_SMAN_ID         NUMERIC(19,0),
   SCCO_SCTY_ID         NUMERIC(19,0),
   SCCO_TITLE           VARCHAR(1024)                  not null,
   SCCO_CONTENT         LONGTEXT,
   SCCO_CREATION_TIME   DATETIME                       not null,
   SCCO_STATE           NUMERIC(5,0)                    not null,
   SCCO_REPLY_CONTENT   LONGTEXT,
   SCCO_REPLY_TIME      DATETIME,
   constraint PK_SC_CONSULTATION primary key (SCCO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCCO_SCTY_ID                                         */
/*==============================================================*/
create index ISCCO_SCTY_ID on SC_CONSULTATION (
   SCCO_SCTY_ID ASC
);

/*==============================================================*/
/* Index: ISCCO_TITLE                                           */
/*==============================================================*/
create index ISCCO_TITLE on SC_CONSULTATION (
   SCCO_TITLE ASC
);

/*==============================================================*/
/* Index: ISCCO_CREATION_TIME                                   */
/*==============================================================*/
create index ISCCO_CREATION_TIME on SC_CONSULTATION (
   SCCO_CREATION_TIME ASC
);

/*==============================================================*/
/* Index: ISCCO_STATE                                           */
/*==============================================================*/
create index ISCCO_STATE on SC_CONSULTATION (
   SCCO_STATE ASC
);

/*==============================================================*/
/* Table: SC_CONSULTATION_TYPE                                  */
/*==============================================================*/
create table SC_CONSULTATION_TYPE  (
   SCTY_ID              NUMERIC(19,0)                   not null,
   SCTY_NAME            VARCHAR(128)                   not null,
   SCTY_INDEX           NUMERIC(5,0),
   SCTY_NUMBER          NUMERIC(19,0)                   not null,
   constraint PK_SC_CONSULTATION_TYPE primary key (SCTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCTY_INDEX                                           */
/*==============================================================*/
create index ISCTY_INDEX on SC_CONSULTATION_TYPE (
   SCTY_INDEX ASC
);

/*==============================================================*/
/* Table: SC_FAQ_LIBRARY                                        */
/*==============================================================*/
create table SC_FAQ_LIBRARY  (
   SCFL_ID              NUMERIC(19,0)                   not null,
   SCFL_SCFT_ID         NUMERIC(19,0)                   not null,
   SCFL_QUESTION        VARCHAR(3072)                  not null,
   SCFL_ANSWER          VARCHAR(3072)                  not null,
   SCFL_ISTOP           NUMERIC(1,0),
   SCFL_TOP_TIME        DATETIME,
   constraint PK_SC_FAQ_LIBRARY primary key (SCFL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCFL_SCFT_ID                                         */
/*==============================================================*/
create index ISCFL_SCFT_ID on SC_FAQ_LIBRARY (
   SCFL_SCFT_ID ASC
);

/*==============================================================*/
/* Index: ISCFL_ISTOP                                           */
/*==============================================================*/
create index ISCFL_ISTOP on SC_FAQ_LIBRARY (
   SCFL_ISTOP ASC
);

/*==============================================================*/
/* Index: ISCFL_TOP_TIME                                        */
/*==============================================================*/
create index ISCFL_TOP_TIME on SC_FAQ_LIBRARY (
   SCFL_TOP_TIME ASC
);

/*==============================================================*/
/* Table: SC_FAQ_TYPE                                           */
/*==============================================================*/
create table SC_FAQ_TYPE  (
   SCFT_ID              NUMERIC(19,0)                   not null,
   SCFT_PARENT_ID       NUMERIC(19,0),
   SCFT_NAME            VARCHAR(128)                   not null,
   SCFT_GRADE           NUMERIC(5,0),
   SCFT_INDEX           NUMERIC(9,0),
   constraint PK_SC_FAQ_TYPE primary key (SCFT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SC_FAQ_TYPE(SCFT_ID,SCFT_NAME,SCFT_GRADE,SCFT_INDEX) values(1,'文献求助',1,1);
insert into SC_FAQ_TYPE(SCFT_ID,SCFT_NAME,SCFT_GRADE,SCFT_INDEX) values(2,'参考咨询',1,2);

/*==============================================================*/
/* Index: ISCFT_PARENT_ID                                       */
/*==============================================================*/
create index ISCFT_PARENT_ID on SC_FAQ_TYPE (
   SCFT_PARENT_ID ASC
);

/*==============================================================*/
/* Index: ISCFT_GRADE                                           */
/*==============================================================*/
create index ISCFT_GRADE on SC_FAQ_TYPE (
   SCFT_GRADE ASC
);

/*==============================================================*/
/* Index: ISCFT_INDEX                                           */
/*==============================================================*/
create index ISCFT_INDEX on SC_FAQ_TYPE (
   SCFT_INDEX ASC
);

/*==============================================================*/
/* Table: SC_FAVORITES                                          */
/*==============================================================*/
create table SC_FAVORITES  (
   SCF_ID               NUMERIC(19,0)                   not null,
   SCF_SYS_USER_ID      NUMERIC(19)                     not null,
   SCF_RES_SOURCE_ID    NUMERIC(19)                     not null,
   SCF_DATE             DATETIME                       not null,
   constraint PK_SC_FAVORITES primary key (SCF_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCF_SYS_USER_ID                                      */
/*==============================================================*/
create index ISCF_SYS_USER_ID on SC_FAVORITES (
   SCF_SYS_USER_ID ASC
);

/*==============================================================*/
/* Index: ISCF_RES_SOURCE_ID                                    */
/*==============================================================*/
create index ISCF_RES_SOURCE_ID on SC_FAVORITES (
   SCF_RES_SOURCE_ID ASC
);

/*==============================================================*/
/* Table: SC_ISSUE                                              */
/*==============================================================*/
create table SC_ISSUE  (
   SCIS_ID              NUMERIC(19,0)                   not null,
   SCIS_SCIT_ID         NUMERIC(19,0)                   not null,
   SCIS_SUIN_ID         NUMERIC(19)                     not null,
   SCIS_LAST_REPLY_MAN_ID NUMERIC(19),
   SCIS_LAST_REPLY_TIME DATETIME,
   SCIS_TITLE           VARCHAR(1024)                   not null,
   SCIS_CONTENT         LONGTEXT,
   SCIS_CREATION_TIME   DATETIME                       not null,
   SCIS_STATE           NUMERIC(5,0)                    not null,
   SCIS_CLOSE_TIME      DATETIME,
   SCIS_CLOSE_COMMENT   VARCHAR(1024),
   SCIS_CLOSE_SCIR_ID   NUMERIC(19,0),
   SCIS_REPLY_NUMBER    NUMERIC(19,0)                   not null,
   SCIS_ISUSE           NUMERIC(1,0),
   constraint PK_SC_ISSUE primary key (SCIS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCIS_SCIT_ID                                         */
/*==============================================================*/
create index ISCIS_SCIT_ID on SC_ISSUE (
   SCIS_SCIT_ID ASC
);

/*==============================================================*/
/* Index: ISCIS_LAST_REPLY_TIME                                 */
/*==============================================================*/
create index ISCIS_LAST_REPLY_TIME on SC_ISSUE (
   SCIS_LAST_REPLY_TIME ASC
);

/*==============================================================*/
/* Index: ISCIS_CLOSE_TIME                                      */
/*==============================================================*/
create index ISCIS_CLOSE_TIME on SC_ISSUE (
   SCIS_CLOSE_TIME ASC
);

/*==============================================================*/
/* Table: SC_ISSUE_REPLY                                        */
/*==============================================================*/
create table SC_ISSUE_REPLY  (
   SCIR_ID              NUMERIC(19,0)                   not null,
   SCIR_SCIS_ID         NUMERIC(19,0)                   not null,
   SCIR_SUIN_ID         NUMERIC(19)                     not null,
   SCIR_CONTENT         LONGTEXT,
   SCIR_REPLY_TIME      DATETIME                       not null,
   SCIR_ATTACHMENT_NAME VARCHAR(256),
   SCIR_ATTACHMENT_FILE_NAME VARCHAR(256),
   SCIR_ATTACHMENT_URL  VARCHAR(512),
   SCIR_ISUSE           NUMERIC(1,0),
   SCIR_GRADE           NUMERIC(5,0),
   constraint PK_SC_ISSUE_REPLY primary key (SCIR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCIR_SCIS_ID                                         */
/*==============================================================*/
create index ISCIR_SCIS_ID on SC_ISSUE_REPLY (
   SCIR_SCIS_ID ASC
);

/*==============================================================*/
/* Index: ISCIR_REPLY_TIME                                      */
/*==============================================================*/
create index ISCIR_REPLY_TIME on SC_ISSUE_REPLY (
   SCIR_REPLY_TIME ASC
);

/*==============================================================*/
/* Index: ISCIR_ISUSE                                           */
/*==============================================================*/
create index ISCIR_ISUSE on SC_ISSUE_REPLY (
   SCIR_ISUSE ASC
);

/*==============================================================*/
/* Table: SC_ISSUE_TYPE                                         */
/*==============================================================*/
create table SC_ISSUE_TYPE  (
   SCIT_ID              NUMERIC(19,0)                   not null,
   SCIT_NAME            VARCHAR(128)                   not null,
   SCIT_INDEX           NUMERIC(5,0),
   SCIT_NUMBER          NUMERIC(19,0)                   not null,
   constraint PK_SC_ISSUE_TYPE primary key (SCIT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Index: ISCIT_NAME                                            */
/*==============================================================*/
create unique index ISCIT_NAME on SC_ISSUE_TYPE (
   SCIT_NAME ASC
);

/*==============================================================*/
/* Index: ISCIT_INDEX                                           */
/*==============================================================*/
create index ISCIT_INDEX on SC_ISSUE_TYPE (
   SCIT_INDEX ASC
);

/*==============================================================*/
/* Table: SYS_ACCESS_COUNTER                                    */
/*==============================================================*/
create table SYS_ACCESS_COUNTER  (
   SACO_ID              NUMERIC(19,0)                   not null,
   SACO_SUIN_ID         NUMERIC(19),
   SACO_DATE            DATETIME                       not null,
   SACO_IP              VARCHAR(128),
   constraint PK_SYS_ACCESS_COUNTER primary key (SACO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_CATALOG                                           */
/*==============================================================*/
create table SYS_CATALOG  (
   SCAT_ID              NUMERIC(19)                     not null,
   SCAT_PARENT_ID       NUMERIC(19),
   SCAT_NAME            VARCHAR(128)                   not null,
   SCAT_DESC            VARCHAR(512),
   SCAT_CODE            NUMERIC(5)                      not null,
   constraint PK_SYS_CATALOG primary key (SCAT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户管理
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(101,null,'用户管理','用户管理',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(102,101,'用户管理_管理员设置','管理员设置',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(103,101,'用户管理_会员管理','会员管理',200);



-- 元数据管理
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(201,null,'元数据管理','元数据管理',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(202,201,'DC元数据','元数据管理_DC元数据',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(203,201,'学科分类','元数据管理_学科分类',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(204,201,'语种分类','元数据管理_语种分类',300);



-- 资源管理
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(301,null,'资源管理','资源管理',300);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(302,301,'文献库管理','资源管理_文献库管理',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(303,301,'资源建设','资源管理_资源建设',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(304,301,'校验审核','资源管理_校验审核',300);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(305,301,'多语种处理','资源管理_多语种处理',400);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(306,301,'资源维护','资源管理_资源维护',500);



-- 服务管理
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(401,null,'服务管理','服务管理',400);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(402,401,'信息发布','服务管理_信息发布',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(403,401,'交流管理','服务管理_交流管理',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(4031,403,'文献求助','服务管理_交流管理_文献求助',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(404,401,'FAQ管理','服务管理_FAQ管理',300);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(405,401,'参考咨询','服务管理_参考咨询',400);



-- 系统管理
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(501,null,'系统管理','系统管理',500);
-- insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(502,501,'统计报表','系统管理_统计报表',100);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(503,501,'全局参数','系统管理_全局参数',200);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(504,501,'IP地址表','系统管理_IP地址表',300);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(505,501,'数据备份','系统管理_数据备份',400);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(506,501,'服务器设置','系统管理_服务器设置',500);



-- 统计报表
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_DESC,SCAT_NAME,SCAT_CODE) values(601,null,'统计报表','统计报表',600);
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE) values(602,601,'统计报表','统计报表',100);

/*==============================================================*/
/* Index: ISCAT_PARENT_ID                                       */
/*==============================================================*/
create index ISCAT_PARENT_ID on SYS_CATALOG (
   SCAT_PARENT_ID ASC
);

/*==============================================================*/
/* Table: SYS_COLLECTION_POSITION                               */
/*==============================================================*/
create table SYS_COLLECTION_POSITION  (
   SCP_ID               NUMERIC(19,0)                   not null,
   SCP_NAME             VARCHAR(256)                   not null,
   SCP_INDEX            NUMERIC(9,0)                    not null,
   constraint PK_SYS_COLLECTION_POSITION primary key (SCP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_EMAIL_ACTIVATION                                  */
/*==============================================================*/
create table SYS_EMAIL_ACTIVATION  (
   SEAC_ID              NUMERIC(19,0)                   not null,
   SEAC_SUIN_ID         NUMERIC(19)                     not null,
   SEAC_KEY             NUMERIC(19,0)                   not null,
   constraint PK_SYS_EMAIL_ACTIVATION primary key (SEAC_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_FORGET_PASSWORD                                   */
/*==============================================================*/
create table SYS_FORGET_PASSWORD  (
   SFPA_ID              NUMERIC(19,0)                   not null,
   SFPA_SUIN_ID         NUMERIC(19,0)                   not null,
   SFPA_DATE            DATETIME                            not null,
   SFPA_KEY             NUMERIC(19,0)                   not null,
   constraint PK_SYS_FORGET_PASSWORD primary key (SFPA_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_FUNCTION                                          */
/*==============================================================*/
create table SYS_FUNCTION  (
   SFUN_ID              NUMERIC(19)                     not null,
   SFUN_SCAT_ID         NUMERIC(19),
   SFUN_NAME            VARCHAR(128)                   not null,
   SFUN_DESC            VARCHAR(512),
   SFUN_URL             VARCHAR(512),
   SFUN_INDEX           NUMERIC(5),
   SFUN_CODE            VARCHAR(64),
   SFUN_IS_HIDDEN       NUMERIC(1,0)                   default 0 not null,
   constraint PK_SYS_FUNCTION primary key (SFUN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户管理
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(10021,102,'管理员信息','用户管理_管理员设置_管理员信息','/usercenter/adminManage.htm?action=adminManagement',100,'10021',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(10022,102,'角色管理','用户管理_管理员设置_角色管理','/usercenter/roleManage.htm?action=roleManagement',200,'10022',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(10031,103,'会员信息','用户管理_会员管理_会员信息','/usercenter/userManage.htm?action=userManagement',100,'10031',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(10032,103,'会员审核','用户管理_会员管理_会员审核','/usercenter/userManage.htm?action=auditUser',200,'10032',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(10033,103,'类型设置','用户管理_会员管理_类型设置','/usercenter/userTypeManage.htm?action=userTypeManagement',300,'10033',1);



-- 元数据管理
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(20021,202,'DC元数据','元数据管理_DC元数据_DC元数据','/console/data/sourceType.htm?action=SourceTypeList',100,'20021',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(20031,203,'学科分类','元数据管理_学科分类_学科分类','/console/data/resSubjectType.htm?action=initResSubjectTypeView',100,'20031',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(20041,204,'语种分类','元数据管理_语种分类_语种分类','/console/data/resLanguageType.htm?action=initResLanguageTypeView',100,'20041',1);



-- 资源管理
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30021,302,'文献库设置','资源管理_文献库管理_文献库设置','/console/data/resource/defineStore.htm?action=initDefineStoreView',100,'30021',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30022,302,'数据模板','资源管理_文献库管理_数据模板','/console/data/templetManager.htm?action=SourceTypeList',200,'30022',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30031,303,'数据录入','资源管理_资源建设_数据录入','/console/data/resource/missionInfoManagement.htm?action=initMissionInfoManagementView',100,'30031',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30033,303,'作业转交','资源管理_资源建设_作业转交','/console/data/resource/missionInfoManagement.htm?action=missionDeliver',300,'30033',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30041,304,'数据审核','资源管理_校验审核_数据审核','/console/data/rmsMissionInfoCheck.htm?action=initRmsMissionInfoCheckView',100,'30041',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30042,304,'任务转交','资源管理_校验审核_任务转交','/console/data/resource/missionInfoManagement.htm?action=missionCheckedDeliver',200,'30042',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30043,304,'读者数据','资源管理_校验审核_读者数据','/console/data/memberResource.htm?action=initMemberResourceList',300,'30043',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30044,304,'批量审核','资源管理_校验审核_批量审核','/console/data/rmsMissionInfoBatchCheck.htm?action=initRmsMissionInfoView',150,'30044',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30051,305,'多语种录入','资源管理_多语种处理_多语种录入','/console/data/inputSingle.htm?action=doInterpretMission',100,'30051',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30052,305,'多语种审核','资源管理_多语种处理_多语种审核','/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageMissionInfo',200,'30052',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(30061,306,'资源维护','资源管理_资源维护_资源维护','/console/data/resource/SourceManager.htm?action=managerResourceList',100,'30061',1);



-- 服务管理
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40021,402,'信息管理','服务管理_信息发布系统_信息管理','/console/data/idsInformation.htm?action=initIdsView',100,'40021',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40022,402,'分类设置','服务管理_信息发布系统_分类设置','/console/data/idsInformationType.htm?action=initIdsInformationTypeView',200,'40022',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40031,403,'评论审核','服务管理_交流管理_评论审核','/console/data/reviewManage.htm?action=initView',100,'40031',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(400311,4031,'求助分类','服务管理_交流管理_文献求助_求助分类','/console/data/scIssueType.htm?action=initScIssueTypeView',100,'400311',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(400312,4031,'求助信息管理','服务管理_交流管理_文献求助_求助信息管理','/console/scIssue.htm?action=initScIssueView',200,'400312',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(400313,4031,'求助回复管理','服务管理_交流管理_文献求助_求助回复管理','/console/data/scIssueReply.htm?action=initScIssueReplyView',300,'400313',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40041,404,'FAQ维护','服务管理_FAQ管理_FAQ维护','/console/data/scFaqLibrary.htm?action=initScFaqLibraryView',100,'40041',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40042,404,'分类设置','服务管理_FAQ管理_分类设置','/console/data/scFaqType.htm?action=initScFaqTypeView',200,'40042',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40051,405,'咨询管理','服务管理_参考咨询_咨询管理','/console/data/scConsultation.htm?action=initScConsultationView',100,'40051',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(40052,405,'咨询分类','服务管理_参考咨询_咨询分类','/console/data/scConsultation.htm?action=initScConsultationTypeView',200,'40052',1);



-- 系统管理
-- insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50027,502,'站点访问量统计','系统管理_统计报表_站点访问量统计','/console/data/accessCounter.htm?action=initView',700,'50027',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50031,503,'系统参数设置','系统管理_全局参数_系统参数设置','/console/data/sysSetting.htm?action=initMmSettingView',100,'50031',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50032,503,'网站参数设置','系统管理_全局参数_网站参数设置','/console/data/sysSetting.htm?action=initWwebsiteSettingView',200,'50032',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50033,503,'馆藏位置设置','系统管理_全局参数_馆藏位置设置','/console/data/sysCollectionPositionSet.htm?action=initView',300,'50033',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50034,503,'敏感词设置','系统管理_全局参数_敏感词设置','/console/data/sysSensitiveWordSet.htm?action=initView',400,'50034',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(50061,506,'服务器设置','系统管理_服务器设置_服务器设置','/console/data/fileService.htm?action=initView',100,'50061',1);



-- 统计报表
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60021,602,'工作量统计','统计报表_工作量统计','/console/count/resourceHandlerCount.htm?action=initResourceHandlerCountView',100,'60021',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60022,602,'资源增长情况统计','统计报表_资源增长情况统计','/console/count/resourceAddInfo.htm?action=initPage',200,'60022',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60023,602,'资源分布统计','统计报表_资源分布统计','/console/data/distributingCounter.htm?action=initResourceDistributingCountView',300,'60023',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60024,602,'会员上传资源统计','系统管理_统计报表_会员上传资源统计','/console/count/uploadResources.htm?action=initView',400,'60024',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60025,602,'会员登录统计','系统管理_统计报表_会员登录统计','/console/count/userLoginCount.htm?action=initView',500,'60025',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60026,602,'资源使用情况统计','系统管理_统计报表_资源使用情况统计','/console/count/resourcesUsed.htm?action=initView',600,'60026',1);
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN) values(60027,602,'站点访问量统计','系统管理_统计报表_站点访问量统计','/console/data/accessCounter.htm?action=initView',700,'60027',1);

/*==============================================================*/
/* Index: ISFUN_SCAT_ID                                         */
/*==============================================================*/
create index ISFUN_SCAT_ID on SYS_FUNCTION (
   SFUN_SCAT_ID ASC
);

/*==============================================================*/
/* Index: ISFUN_INDEX                                           */
/*==============================================================*/
create index ISFUN_INDEX on SYS_FUNCTION (
   SFUN_INDEX ASC
);

/*==============================================================*/
/* Table: SYS_LINK                                              */
/*==============================================================*/
create table SYS_LINK  (
   SLIN_ID              NUMERIC(19)                     not null,
   SLIN_NAME            VARCHAR(512),
   SLIN_IMGPATH         VARCHAR(1024),
   SLIN_LINKPATH        VARCHAR(1024),
   SLIN_TYPE            NUMERIC(5,0),
   SLIN_INDEX           NUMERIC(5,0),
   constraint PK_SYS_LINK primary key (SLIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_MANAGER                                           */
/*==============================================================*/
create table SYS_MANAGER  (
   SMAN_ID              NUMERIC(19,0)                   not null,
   SMAN_PDEP_ID         NUMERIC(19),
   SMAN_NAME            VARCHAR(256)                   not null,
   SMAN_LOGIN_NAME      VARCHAR(64)                    not null,
   SMAN_PWD             VARCHAR(64)                    not null,
   SMAN_EMAIL           VARCHAR(256),
   SMAN_TEL             VARCHAR(64),
   SMAN_REMARK          VARCHAR(1024),
   SMAN_IS_STOP         NUMERIC(1,0)                    not null,
   SMAN_TYPE            NUMERIC(5,0),
   constraint PK_SYS_MANAGER primary key (SMAN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_MANAGER(SMAN_ID,SMAN_NAME,SMAN_LOGIN_NAME,SMAN_PWD,SMAN_EMAIL,SMAN_TEL,SMAN_REMARK,SMAN_IS_STOP) values(1,'root','root','rCWNASn8Jgj0+XzE3Qcptrap9NDMOPpaKVj5FA==','','','系统内置管理员',0);

/*==============================================================*/
/* Index: ISMAN_NAME                                            */
/*==============================================================*/
create index ISMAN_NAME on SYS_MANAGER (
   SMAN_NAME ASC
);

/*==============================================================*/
/* Index: ISMAN_LOGIN_NAME                                      */
/*==============================================================*/
create unique index ISMAN_LOGIN_NAME on SYS_MANAGER (
   SMAN_LOGIN_NAME ASC
);

/*==============================================================*/
/* Index: ISMAN_IS_STOP                                         */
/*==============================================================*/
create index ISMAN_IS_STOP on SYS_MANAGER (
   SMAN_IS_STOP ASC
);

/*==============================================================*/
/* Table: SYS_PAGE_IMAGE                                        */
/*==============================================================*/
create table SYS_PAGE_IMAGE  (
   SPIM_ID              NUMERIC(19)                     not null,
   SPIM_STYLE           NUMERIC(5,0),
   SPIM_AREA            NUMERIC(5,0),
   SPIM_FILEPATH        VARCHAR(512),
   SPIM_FILETYPE        NUMERIC(5,0),
   SPIM_REMARK          VARCHAR(1024),
   constraint PK_SYS_PAGE_IMAGE primary key (SPIM_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
create table SYS_ROLE  (
   SROL_ID              NUMERIC(19)                     not null,
   SROL_NAME            VARCHAR(128)                   not null,
   SROL_CODE            VARCHAR(64)                    not null,
   SROL_DESC            VARCHAR(512),
   SROL_IS_SYS_ROLE     NUMERIC(1,0)                   default 0 not null,
   constraint PK_SYS_ROLE primary key (SROL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (1,'系统管理员','001','系统管理员',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (2,'资源类型管理员','002','资源管理_资源类型管理',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (3,'特色库管理员','003','资源管理_特色库管理',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (4,'资源建设管理员','004','资源管理_资源建设',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (5,'校验审核管理员','005','资源管理_校验审核',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (6,'资源维护管理员','006','资源管理_资源维护',1);
insert into SYS_ROLE(SROL_ID,SROL_NAME,SROL_CODE,SROL_DESC,SROL_IS_SYS_ROLE) values (7,'用户中心管理员','007','用户管理中心',1);
insert into sys_role(srol_id,srol_name,srol_code,srol_desc,srol_is_sys_role) values(8,'导师','008','导师',1);

/*==============================================================*/
/* Table: SYS_ROLE_FUNCTION                                     */
/*==============================================================*/
create table SYS_ROLE_FUNCTION  (
   SRFU_ID              NUMERIC(19)                     not null,
   SRFU_SFUN_ID         NUMERIC(19)                     not null,
   SRFU_SROL_ID         NUMERIC(19)                     not null,
   constraint PK_SYS_ROLE_FUNCTION primary key (SRFU_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(110021,10021,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(110022,10022,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(110031,10031,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(110032,10032,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(110033,10033,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(120021,20021,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(120031,20031,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(120041,20041,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130021,30021,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130022,30022,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130031,30031,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130033,30033,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130041,30041,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130042,30042,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130043,30043,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130044,30044,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130051,30051,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130052,30052,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(130061,30061,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140021,40021,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140022,40022,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140031,40031,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(1400311,400311,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(1400312,400312,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(1400313,400313,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140041,40041,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140042,40042,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140051,40051,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(140052,40052,1);
-- insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150027,50027,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150031,50031,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150032,50032,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150033,50033,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150034,50034,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(150061,50061,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160021,60021,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160022,60022,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160023,60023,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160024,60024,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160025,60025,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160026,60026,1);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(160027,60027,1);
-- 资源类型管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(220021,20021,2);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(220031,20031,2);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(220041,20041,2);
-- 特色库管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(330021,30021,3);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(330022,30022,3);
-- 资源建设管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(430031,30031,4);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(430051,30051,4);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(430033,30033,4);
-- 校验审核管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(530041,30041,5);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(530052,30052,5);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(530042,30042,5);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(530043,30043,5);
-- 资源维护管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(630061,30061,6);
-- 用户中心管理员
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(710021,10021,7);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(710022,10022,7);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(710031,10031,7);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(710032,10032,7);
insert into SYS_ROLE_FUNCTION(SRFU_ID,SRFU_SFUN_ID,SRFU_SROL_ID) values(710033,10033,7);
--导师功能点
insert into SYS_ROLE_FUNCTION(srfu_id,srfu_sfun_id,srfu_srol_id) values(810010,70041,8);

/*==============================================================*/
/* Table: SYS_SENSITIVE_WORD                                    */
/*==============================================================*/
create table SYS_SENSITIVE_WORD  (
   SSW_ID               NUMERIC(19,0)                   not null,
   SSW_NAME             VARCHAR(256)                   not null,
   SSW_INDEX            NUMERIC(9,0)                    not null,
   constraint PK_SYS_SENSITIVE_WORD primary key (SSW_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_SETTINGS                                          */
/*==============================================================*/
create table SYS_SETTINGS  (
   SSET_ID              NUMERIC(19,0)                   not null,
   SSET_NAME            VARCHAR(512)                   not null,
   SSET_KEY             VARCHAR(128)                   not null,
   SSET_VALUE           VARCHAR(1024),
   SSET_TYPE            NUMERIC(5)                      not null,
   constraint PK_SYS_SETTINGS primary key (SSET_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统参数设置
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(101,'允许上传的文件类型','the_type_allow_to_upload','',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(102,'允许上传的文件大小(M)','the_size_allow_to_upload','100',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(103,'是否允许会员上传文件','is_allow_member_to_upload','0',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(104,'允许会员上传文件类型','the_type_allow_member_to_upload','',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(105,'允许会员上传文件大小(M)','the_size_allow_member_to_upload','10',1);
-- 热门资源范围：最近一个月(1)、最近三个月(2)、最近半年(3)
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(106,'热门资源范围','hot_resource_range','1',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(107,'是否开放上传时音视频转化功能','is_open_av_convert_when_uploading','0',1);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(108,'游客每天可播放普通视频个数','the_number_of_video_guest_can_play','5',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(109,'敏感词','sensitive_words','',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(110,'馆藏位置选项','source_location_options','重大A区,重大B区,重大C区',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(111,'允许上传的源文件大小(M)','resource_upload_sourcefile_maxsize','100',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(112,'允许上传的预览文件大小(M)','resource_upload_viewfile_maxsize','20',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(113,'允许上传的封面文件大小(M)','resource_upload_photofile_maxsize','3',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(114,'会员审核方式','user_register_check_type','1',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(115,'邮件发送服务器(SMTP)','sendmail_host','mail.adtech.com.cn',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(116,'邮件发件人地址','sendmail_from_address','lujiangnan@adtech.com.cn',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(117,'邮件发件人登录名','sendmail_from_username','lujiangnan',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(118,'邮件发件人登录密码','sendmail_from_password','lujiangnan',1);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(205,'是否允许会员上传资源','user_upload_resource_is_open','1',1);



-- 网站参数设置
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(119,'域名设置','riss2_website','127.0.0.1:8888/RISS2',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(120,'网站版权','web_site_footer','版权所有：重庆大学图书馆',2);
-- 热门资源设置：最近一个月(1)、最近三个月(3)、最近半年(6)
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(121,'热门资源设置','riss2_index_hotresource','1',2);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(122,'允许会员上传的资源类型','user_upload_resource_type','doc|docx|ppt|pptx|xls|xlsx|vsd|pot|pps|rtf|wps|et|dps|txt',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(123,'游客每天允许的浏览视频数','unlogin_user_can_browse_video_number','10',2);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(201,'网站名称','site_name','RISS2 资源服务系统',2);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(202,'备案号','site_icp_number','',2);
-- insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(203,'底部文字','site_foot_copyright','',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(204,'列表默认显示条数(条)','default_page_size','20',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(206,'求助回复允许附件大小(M)','default_issue_attachement_size','20',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(207,'限制会员上传图片类型','allow_user_upload_file_type_picture','gif|bmp|jpeg|psd|png|jpg|ico',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(208,'限制会员上传文档类型','allow_user_upload_file_type_document','doc|docx|ppt|pptx|xls|xlsx|pot|pps|wps|et|pdf|txt',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(209,'限制会员上传音频类型','allow_user_upload_file_type_audieo','midi|mp3|wma|wav|ape|cd|ogg',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(210,'限制会员上传视频类型','allow_user_upload_file_type_video','avi|navi|mpeg|divx|asf|wmv|rm|rmvb',2);
insert into SYS_SETTINGS(SSET_ID,SSET_NAME,SSET_KEY,SSET_VALUE,SSET_TYPE) values(211,'默认皮肤设置','riss2_default_skin','2',2);
update sys_settings set sset_name = '中文网站版权',sset_key = 'web_site_footer_zh' where sset_id = 120;
insert into sys_settings(sset_id,sset_name,sset_key,sset_value,sset_type) values(212,'英文网站版权','web_site_footer_en','Copyright: Chongqing University Library',2);
insert into sys_settings(sset_id,sset_name,sset_key,sset_value,sset_type) values(213,'藏文网站版权','web_site_footer_zw','',2);

/*==============================================================*/
/* Table: SYS_STYLE                                             */
/*==============================================================*/
create table SYS_STYLE  (
   SSTY_ID              NUMERIC(19)                     not null,
   SSTY_NAME            VARCHAR(128)                   not null,
   SSTY_IS_ACTIVATION   NUMERIC(1)                     default 1,
   constraint PK_SYS_STYLE primary key (SSTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_STYLE(SSTY_ID,SSTY_NAME,SSTY_IS_ACTIVATION) values(1,'绿色风格',1);
insert into SYS_STYLE(SSTY_ID,SSTY_NAME,SSTY_IS_ACTIVATION) values(2,'蓝色风格',1);
insert into SYS_STYLE(SSTY_ID,SSTY_NAME,SSTY_IS_ACTIVATION) values(3,'粉红色风格',1);

/*==============================================================*/
/* Table: SYS_TOURIST_BROWSE_CACHE                              */
/*==============================================================*/
create table SYS_TOURIST_BROWSE_CACHE  (
   STBHC_ID             NUMERIC(19,0)                   not null,
   STBHC_KEY            VARCHAR(256)                   not null,
   STBHC_VALUE          NUMERIC(19,0)                   not null,
   constraint PK_SYS_TOURIST_BROWSE_CACHE primary key (STBHC_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*==============================================================*/
/* Table: SYS_USER_INFO                                         */
/*==============================================================*/
create table SYS_USER_INFO  (
   SUIN_ID              NUMERIC(19)                     not null,
   SUIN_SUTY_ID         NUMERIC(19),
   SUIN_STYLE_ID        NUMERIC(19),
   SUIN_LOGIN_NAME      VARCHAR(128)                   not null,
   SUIN_PASSWORD        VARCHAR(128)                   not null,
   SUIN_NAME            VARCHAR(128)                   not null,
   SUIN_SEX             NUMERIC(5,0),
   SUIN_BIRTHDAY        DATETIME,
   SUIN_EMAIL           VARCHAR(256),
   SUIN_IS_STOP         NUMERIC(1)                     default 0 not null,
   SUIN_JOIN_DATE       DATETIME,
   SUIN_LASTVISIT_DATE  DATETIME,
   SUIN_MOBILE_TELEPHONE VARCHAR(32),
   SUIN_IMAGE_URL       VARCHAR(1024),
   SUIN_IMAGE_LOCAL_NAME VARCHAR(256),
   SUIN_STATE           NUMERIC(5,0)                   default 0 not null,
   SUIN_AUDIT_MODE      NUMERIC(5,0)                    not null,
   SUIN_HINT            VARCHAR(256),
   SUIN_ANSWER          VARCHAR(256),
   SUIN_QUESTION        VARCHAR(256),
   SUIN_INTEGRAL        NUMERIC(9,0),
   constraint PK_SYS_USER_INFO primary key (SUIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_USER_INFO(SUIN_ID,SUIN_SUTY_ID,SUIN_STYLE_ID,SUIN_LOGIN_NAME,SUIN_PASSWORD,SUIN_NAME,SUIN_IS_STOP,SUIN_STATE,SUIN_AUDIT_MODE) values(1,2,1,'root','zkVNqIw0004Kkscwawea1LryeDMwrrL4bQ/Ucw==','root',0,1,1);

/*==============================================================*/
/* Index: ISUIN_LOGIN_NAME                                      */
/*==============================================================*/
create unique index ISUIN_LOGIN_NAME on SYS_USER_INFO (
   SUIN_LOGIN_NAME ASC
);

/*==============================================================*/
/* Index: ISUIN_NAME                                            */
/*==============================================================*/
create index ISUIN_NAME on SYS_USER_INFO (
   SUIN_NAME ASC
);

/*==============================================================*/
/* Index: ISUIN_SEX                                             */
/*==============================================================*/
create index ISUIN_SEX on SYS_USER_INFO (
   SUIN_SEX ASC
);

/*==============================================================*/
/* Index: ISUIN_IS_STOP                                         */
/*==============================================================*/
create index ISUIN_IS_STOP on SYS_USER_INFO (
   SUIN_IS_STOP ASC
);

/*==============================================================*/
/* Index: ISUIN_STATE                                           */
/*==============================================================*/
create index ISUIN_STATE on SYS_USER_INFO (
   SUIN_STATE ASC
);

/*==============================================================*/
/* Table: SYS_USER_ROLE                                         */
/*==============================================================*/
create table SYS_USER_ROLE  (
   SURO_ID              NUMERIC(19)                     not null,
   SURO_SROL_ID         NUMERIC(19)                     not null,
   SURO_SMAN_ID         NUMERIC(19,0),
   constraint PK_SYS_USER_ROLE primary key (SURO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_USER_ROLE(SURO_ID,SURO_SROL_ID,SURO_SMAN_ID) values(1,1,1);

/*==============================================================*/
/* Table: SYS_USER_TYPE                                         */
/*==============================================================*/
create table SYS_USER_TYPE  (
   SUTY_ID              NUMERIC(19)                     not null,
   SUTY_NAME            VARCHAR(128)                   not null,
   SUTY_DOC             NUMERIC(5,0)                    not null,
   SUTY_PIC             NUMERIC(5,0)                    not null,
   SUTY_VIDEO           NUMERIC(5,0)                    not null,
   SUTY_DEFAULT         NUMERIC(1,0)                    not null,
   SUTY_PURVIEW         NUMERIC(5,0),
   constraint PK_SYS_USER_TYPE primary key (SUTY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into SYS_USER_TYPE(SUTY_ID,SUTY_NAME,SUTY_DOC,SUTY_PIC,SUTY_VIDEO,SUTY_DEFAULT,SUTY_PURVIEW) values(1,'游客',0,0,0,0,1);
insert into SYS_USER_TYPE(SUTY_ID,SUTY_NAME,SUTY_DOC,SUTY_PIC,SUTY_VIDEO,SUTY_DEFAULT,SUTY_PURVIEW) values(2,'普通会员',2,2,1,1,1);

alter table CMA_COMPANY_CLASSIFY
   add constraint FK_CMA_COMP_REFERENCE_CMA_CLAS foreign key (CCCL_CLTY_ID)
      references CMA_CLASSIFY_TYPE (CLTY_ID);

alter table CMA_COMPANY_CLASSIFY
   add constraint FK_CMA_COMP_REFERENCE_CMA_COMP foreign key (CCCL_CCTY_ID)
      references CMA_COMPANY_TYPE (CCTY_ID);

alter table CMA_COMPANY_CLASSIFY
   add constraint FK_CMA_COMP_REFERENCE_CMA_COMP foreign key (CCCL_CCIN_ID)
      references CMA_COMPANY_INFO (CCIN_ID);

alter table CMA_COMPANY_FILE
   add constraint FK_CMA_COMP_REFERENCE_RES_META foreign key (CCFI_RMST_ID)
      references RES_META_STORE (RMST_ID);

alter table CMA_COMPANY_TYPE
   add constraint FK_CMA_COMP_REFERENCE_CMA_CLAS foreign key (CCTY_CLTY_ID)
      references CMA_CLASSIFY_TYPE (CLTY_ID);

alter table IDS_INFORMATION
   add constraint FK_IDS_INFO_REFERENCE_IDS_INFO foreign key (IDSI_IDST_ID)
      references IDS_INFORMATION_TYPE (IDST_ID);

alter table IDS_INFORMATION
   add constraint FK_IDS_INFO_REFERENCE_SYS_MANA foreign key (IDSI_SMAN_ID)
      references SYS_MANAGER (SMAN_ID);

alter table IS_IGNORE_RESSOURCE
   add constraint FK_IS_IGNOR_REFERENCE_SYS_USER foreign key (IS_IGNORE_USER_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table IS_IGNORE_RESSOURCE
   add constraint FK_IS_IGNOR_REFERENCE_RES_SOUR foreign key (IS_IGNORE_RESOURCE_ID)
      references RES_SOURCE_DATA (RSDA_ID);

alter table IS_INFO_SUBSCRIPTION
   add constraint FK_IS_INFO__REFERENCE_SYS_USER foreign key (IS_USER_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table PSA_DEPT_TEACHER
   add constraint FK_PSA_DEPT_REFERENCE_PSA_DEPA foreign key (PDTE_PDEP_ID)
      references PSA_DEPARTMENTS (PDEP_ID);

alter table PSA_ELEMENTS
   add constraint FK_PSA_ELEM_REFERENCE_RES_ELEM foreign key (PELE_REPO_ID)
      references RES_ELEMENT_POOL (REPO_ID);

alter table PSA_ELEMENT_DATA
   add constraint FK_PSA_ELEM_REFERENCE_RES_ELEM foreign key (PEDA_REPO_ID)
      references RES_ELEMENT_POOL (REPO_ID);

alter table PSA_ELEMENT_DATA
   add constraint FK_PSA_ELEM_REFERENCE_PSA_PAPE foreign key (PEDA_PPIN_ID)
      references PSA_PAPERS_INFO (PPIN_ID);

alter table PSA_PAPERS_INFO
   add constraint FK_PSA_PAPE_REFERENCE_PSA_STUD foreign key (PPIN_PSTU_ID)
      references PSA_STUDENTS (PSTU_ID);

alter table PSA_PAPERS_INFO
   add constraint FK_PSA_PAPE_REFERENCE_PSA_DEGR foreign key (PPIN_PDEG_ID)
      references PSA_DEGREE (PDEG_ID);

alter table PSA_PAPERS_INFO
   add constraint FK_PSA_PAPE_REFERENCE_RES_SUBJ foreign key (PPIN_RSTY_ID)
      references RES_SUBJECT_TYPE (RSTY_ID);

alter table PSA_PAPERS_INFO
   add constraint FK_PSA_PAPE_REFERENCE_SYS_MANA foreign key (PPIN_SMAN_ID)
      references SYS_MANAGER (SMAN_ID);

alter table PSA_STUDENTS
   add constraint FK_PSA_STUD_REFERENCE_PSA_DEPA foreign key (PSTU_PDEP_ID)
      references PSA_DEPARTMENTS (PDEP_ID);

alter table RES_DYNAMIC_TYPE
   add constraint FK_RES_DYNA_REFERENCE_RES_META foreign key (RMST_ID)
      references RES_META_STORE (RMST_ID)
      on delete cascade;

alter table RES_EXTEND_DATA
   add constraint FK_RES_EXTE_REFERENCE_RES_ELEM foreign key (REDA_REPO_ID)
      references RES_ELEMENT_POOL (REPO_ID);

alter table RES_IMPORT_HISTORY
   add constraint FK_RES_IMPO_REFERENCE_RES_MISS foreign key (RMHI_RMIN_ID)
      references RES_MISSION_INFO (RMIN_ID);

alter table RES_IMPORT_TEMPLET
   add constraint FK_RES_IMPO_REFERENCE_RES_META foreign key (RITE_RMST_ID)
      references RES_META_STORE (RMST_ID);

alter table RES_IMPORT_TEMPLET
   add constraint FK_RES_IMPO_REFERENCE_RES_LITE foreign key (RITE_RLTY_ID)
      references RES_LITER_TYPE (RLTY_ID);

alter table RES_IMP_COLS
   add constraint FK_RES_COLS_REFERENCE_RES_TEMP foreign key (RICO_RTIM_ID)
      references RES_TEMPLATE_IMP (RTIM_ID);

alter table RES_LOADS_LIST
   add constraint FK_RES_LOAD_REFERENCE_RES_MISS foreign key (RLLI_RMIN_ID)
      references RES_MISSION_INFO (RMIN_ID);

alter table RES_LOADS_LIST
   add constraint FK_RES_LOAD_REFERENCE_SYS_MANA foreign key (RLLI_AUTHOR)
      references SYS_MANAGER (SMAN_ID)
      on delete set null;

alter table RES_META_PERM
   add constraint FK_RES_META_REFERENCE_RES_META foreign key (RMST_ID)
      references RES_META_STORE (RMST_ID)
      on delete set null;

alter table RES_META_PERM
   add constraint FK_RES_PERM_REFERENCE_SYS_MANA foreign key (RMPE_ADMINID)
      references SYS_MANAGER (SMAN_ID);

alter table RES_META_STORE
   add constraint FK_RES_META_REFERENCE_SYS_MANA foreign key (SMAN_ID)
      references SYS_MANAGER (SMAN_ID)
      on delete set null;

alter table RES_META_STORE
   add constraint FK_RES_META_REFERENCE_RES_FILE foreign key (RMST_RFIL_ID)
      references RES_FILESYSTEM (RFIL_ID);

alter table RES_MISSION_INFO
   add constraint FK_RES_MISS_REFERENCE_RES_META foreign key (RMIN_RMST_ID)
      references RES_META_STORE (RMST_ID);

alter table RES_MISSION_INFO
   add constraint FK_RES_MISS_REFERENCE_SYS_AUTH foreign key (RMIN_AUTHOR)
      references SYS_MANAGER (SMAN_ID)
      on delete set null;

alter table RES_MISSION_INFO
   add constraint FK_RES_MISS_REFERENCE_SYS_USER foreign key (RMIN_USER)
      references SYS_MANAGER (SMAN_ID)
      on delete set null;

alter table RES_MISSION_INFO
   add constraint FK_RES_MISS_REFERENCE_RES_LANG foreign key (RMIN_LANGUAGE)
      references RES_LANGUAGE_TYPE (RLTY_ID);

alter table RES_MISSION_LANGUAGE
   add constraint FK_RES_MILA_REFERENCE_RES_MISS foreign key (RMLA_RMIN_ID)
      references RES_MISSION_INFO (RMIN_ID);

alter table RES_MISSION_LANGUAGE
   add constraint FK_RES_MILA_REFERENCE_RES_LANG foreign key (RMLA_RLTY_ID)
      references RES_LANGUAGE_TYPE (RLTY_ID);

alter table RES_MISS_DEVOLVE
   add constraint FK_RES_MISS_REFERENCE_SYS_PREU foreign key (RMDE_PREUID)
      references SYS_MANAGER (SMAN_ID);

alter table RES_MISS_DEVOLVE
   add constraint FK_RES_MISS_REFERENCE_SYS_AFTU foreign key (RMDE_AFTUID)
      references SYS_MANAGER (SMAN_ID);

alter table RES_SOURCE_BROWSE_HISTOREY
   add constraint FK_RES_BRHI_REFERENCE_RES_SOUR foreign key (RSBH_RSDA_ID)
      references RES_SOURCE_DATA (RSDA_ID)
      on delete cascade;

alter table RES_SOURCE_BROWSE_HISTOREY
   add constraint FK_RES_BRHI_REFERENCE_SYS_USER foreign key (RSBH_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID)
      on delete cascade;

alter table RES_SOURCE_COMT
   add constraint FK_RES_COMT_REFERENCE_RES_SOUR foreign key (RSCO_RSDA_ID)
      references RES_SOURCE_DATA (RSDA_ID)
      on delete cascade;

alter table RES_SOURCE_COMT
   add constraint FK_RES_SOUR_REFERENCE_SYS_USER foreign key (RSCO_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID)
      on delete set null;

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_RES_LITE foreign key (RSDA_RLTY_ID)
      references RES_LITER_TYPE (RLTY_ID)
      on delete set null;

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_RES_DYNA foreign key (RSDA_RDTY_ID)
      references RES_DYNAMIC_TYPE (RDTY_ID);

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_RES_SUBJ foreign key (RSDA_RSTY_ID)
      references RES_SUBJECT_TYPE (RSTY_ID)
      on delete set null;

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_SYS_MANA foreign key (RSDA_SMAN_ID)
      references SYS_MANAGER (SMAN_ID);

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_RES_META foreign key (RSDA_RMST_ID)
      references RES_META_STORE (RMST_ID);

alter table RES_SOURCE_DATA
   add constraint FK_RES_SOUR_REFERENCE_RES_MISS foreign key (RSDA_RMIN_ID)
      references RES_MISSION_INFO (RMIN_ID);

alter table RES_SOURCE_LANGUAGE
   add constraint FK_RES_LANG_REFERENCE_RES_SOUR foreign key (RSLA_RSDA_ID)
      references RES_SOURCE_DATA (RSDA_ID);

alter table RES_SOURCE_LANGUAGE
   add constraint FK_RES_SOUR_REFERENCE_RES_LANG foreign key (RSLA_RLTY_ID)
      references RES_LANGUAGE_TYPE (RLTY_ID);

alter table RES_SOURCE_REMARK
   add constraint FK_RES_SOUR_REFERENCE_RES_SOUR foreign key (RSRE_RSDA_ID)
      references RES_SOURCE_DATA (RSDA_ID);

alter table RES_STORE_IP_DEPLOY
   add constraint FK_RES_STOR_IP_REFER_RES_META foreign key (RSID_RMST_ID)
      references RES_META_STORE (RMST_ID)
      on delete cascade;

alter table RES_STORE_LITER
   add constraint FK_RES_STOR_REFERENCE_RES_META foreign key (RMST_ID)
      references RES_META_STORE (RMST_ID)
      on delete set null;

alter table RES_STORE_LITER
   add constraint FK_RES_STOR_REFERENCE_RES_LITE foreign key (RLTY_ID)
      references RES_LITER_TYPE (RLTY_ID)
      on delete set null;

alter table RES_SYNONYMS_KEY
   add constraint FK_RES_SYNO_REFERENCE_RES_INDE foreign key (RSKE_RIKE_ID)
      references RES_INDEX_KEY (RIKE_ID);

alter table RES_TAG_INFO
   add constraint FK_RES_TAG__REFERENCE_RES_SOUR foreign key (RTIN_RSDA_ID)
      references RES_SOURCE_DATA (RSDA_ID);

alter table RES_TAG_INFO
   add constraint FK_RES_TAG__REFERENCE_SYS_USER foreign key (RTIN_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table RES_TEMPLET_META
   add constraint FK_RES_TEMP_REFERENCE_RES_ELEM foreign key (RTME_REEL_ID)
      references RES_ELEMENT_POOL (REPO_ID);

alter table RES_TEMPLET_META
   add constraint FK_RES_TEMP_REFERENCE_RES_META foreign key (RTME_RSIN_ID)
      references RES_META_STORE (RMST_ID);

alter table RES_TEMPLET_META
   add constraint FK_RES_TEMP_REFERENCE_RES_LITE foreign key (RTME_RLTY_ID)
      references RES_LITER_TYPE (RLTY_ID);

alter table RSS_USER_DATA
   add constraint FK_RSS_USER_REFERENCE_SYS_USER foreign key (RSS_DATA_USER_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SC_CONSULTATION
   add constraint FK_SC_CONSU_REFERENCE_SYS_USER foreign key (SCCO_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SC_CONSULTATION
   add constraint FK_SC_CONSU_REFERENCE_SYS_MANA foreign key (SCCO_SMAN_ID)
      references SYS_MANAGER (SMAN_ID);

alter table SC_CONSULTATION
   add constraint FK_SC_CONSU_REFERENCE_SC_TYPE foreign key (SCCO_SCTY_ID)
      references SC_CONSULTATION_TYPE (SCTY_ID);

alter table SC_FAQ_LIBRARY
   add constraint FK_SC_FAQ_L_REFERENCE_SC_FAQ_T foreign key (SCFL_SCFT_ID)
      references SC_FAQ_TYPE (SCFT_ID);

alter table SC_FAQ_TYPE
   add constraint FK_SC_FAQ_T_REFERENCE_SC_FAQ_T foreign key (SCFT_PARENT_ID)
      references SC_FAQ_TYPE (SCFT_ID);

alter table SC_FAVORITES
   add constraint FK_SC_FAVOR_REFERENCE_SYS_USER foreign key (SCF_SYS_USER_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SC_FAVORITES
   add constraint FK_SC_FAVOR_REFERENCE_RES_SOUR foreign key (SCF_RES_SOURCE_ID)
      references RES_SOURCE_DATA (RSDA_ID);

alter table SC_ISSUE
   add constraint FK_SC_ISSUE_REFERENCE_SYS_USER foreign key (SCIS_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SC_ISSUE
   add constraint FK_SC_ISSUE_REFERENCE_SYS_REPL foreign key (SCIS_LAST_REPLY_MAN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SC_ISSUE
   add constraint FK_SC_ISSUE_REFERENCE_SC_TYPE foreign key (SCIS_SCIT_ID)
      references SC_ISSUE_TYPE (SCIT_ID);

alter table SC_ISSUE_REPLY
   add constraint FK_SC_REPL_REFERENCE_SC_ISSUE foreign key (SCIR_SCIS_ID)
      references SC_ISSUE (SCIS_ID)
      on delete cascade;

alter table SC_ISSUE_REPLY
   add constraint FK_SC_REPL_REFERENCE_SYS_USER foreign key (SCIR_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SYS_ACCESS_COUNTER
   add constraint FK_SYS_ACCE_REFERENCE_SYS_USER foreign key (SACO_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SYS_CATALOG
   add constraint FK_SYS_CATA_REFERENCE_SYS_CATA foreign key (SCAT_PARENT_ID)
      references SYS_CATALOG (SCAT_ID)
      on delete cascade;

alter table SYS_EMAIL_ACTIVATION
   add constraint FK_SYS_EMAI_REFERENCE_SYS_USER foreign key (SEAC_SUIN_ID)
      references SYS_USER_INFO (SUIN_ID);

alter table SYS_FUNCTION
   add constraint FK_SYS_FUNC_REFERENCE_SYS_CATA foreign key (SFUN_SCAT_ID)
      references SYS_CATALOG (SCAT_ID)
      on delete cascade;

alter table SYS_MANAGER
   add constraint FK_SYS_MANA_REFERENCE_PSA_DEPA foreign key (SMAN_PDEP_ID)
      references PSA_DEPARTMENTS (PDEP_ID);

alter table SYS_ROLE_FUNCTION
   add constraint FK_SYS_ROLE_REFERENCE_SYS_FUNC foreign key (SRFU_SFUN_ID)
      references SYS_FUNCTION (SFUN_ID)
      on delete cascade;

alter table SYS_ROLE_FUNCTION
   add constraint FK_SYS_ROLE_REFERENCE_SYS_ROLE foreign key (SRFU_SROL_ID)
      references SYS_ROLE (SROL_ID)
      on delete cascade;

alter table SYS_USER_INFO
   add constraint FK_SYS_USER_REFERENCE_SYS_USER foreign key (SUIN_SUTY_ID)
      references SYS_USER_TYPE (SUTY_ID);

alter table SYS_USER_INFO
   add constraint FK_SYS_USER_REFERENCE_SYS_STYL foreign key (SUIN_STYLE_ID)
      references SYS_STYLE (SSTY_ID);

alter table SYS_USER_ROLE
   add constraint FK_SYS_USER_REFERENCE_SYS_ROLE foreign key (SURO_SROL_ID)
      references SYS_ROLE (SROL_ID)
      on delete cascade;

alter table SYS_USER_ROLE
   add constraint FK_SYS_USER_REFERENCE_SYS_MANA foreign key (SURO_SMAN_ID)
      references SYS_MANAGER (SMAN_ID)
      on delete cascade;


SET FOREIGN_KEY_CHECKS=1;
