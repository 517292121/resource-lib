drop trigger UPDATE_STATUS_FOR_META_STORE
/

drop trigger UPDATE_STATUS_FOR_MISSION
/


create or replace trigger UPDATE_STATUS_FOR_META_STORE after update on RES_META_STORE for each row
begin
	if updating then
		update RES_SOURCE_DATA set RSDA_RMST_STATUS=:new.RMST_STATUS where RSDA_RMST_ID=:new.RMST_ID;
	end if;
end;
/


create or replace trigger UPDATE_STATUS_FOR_MISSION after update on RES_MISSION_INFO for each row
begin
	if updating then
		update RES_SOURCE_DATA set RSDA_RMIN_STATUS=:new.RMIN_STATUS where RSDA_RMIN_ID=:new.RMIN_ID;
	end if;
end;
/

--删除资源后删除相关的关联数据
drop trigger DELETE_FOR_RESOURCE;
create or replace trigger DELETE_FOR_RESOURCE after delete on RES_SOURCE_DATA for each row
begin
	if deleting then
		delete from SC_FAVORITES where SCF_RES_SOURCE_ID=:old.RSDA_ID;
		delete from RES_TAG_INFO where RTIN_RSDA_ID=:old.RSDA_ID;
		--delete from RES_SOURCE_COMT where RSCO_RSDA_ID=:old.RSDA_ID;
		delete from RES_SOURCE_BROWSE_HISTOREY where RSBH_RSDA_ID=:old.RSDA_ID;
		delete from RES_SOURCE_DATA_CACHE where RSDC_ID=:old.RSDA_ID;
	end if;
end;
/	

