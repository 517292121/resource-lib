--添加菜单目录
insert into SYS_CATALOG(SCAT_ID,SCAT_PARENT_ID,SCAT_NAME,SCAT_DESC,SCAT_CODE)
 	values(407,401,"检索词管理","服务管理_检索词管理",600);
 	
--添加菜单信息
insert into SYS_FUNCTION(SFUN_ID,SFUN_SCAT_ID,SFUN_NAME,SFUN_DESC,SFUN_URL,SFUN_INDEX,SFUN_CODE,SFUN_IS_HIDDEN)
 	values(40071,407,"检索词管理","服务管理_检索词管理_检索词管理","/console/data/resIndexKey.htm?action=initKeyView",100,40071,1);