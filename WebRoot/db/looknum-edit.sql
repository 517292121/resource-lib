--删除指定年份以前的访问量
delete from SYS_ACCESS_COUNTER counter 
where counter.SACO_SUIN_ID is null and TO_CHAR(counter.SACO_DATE,'yyyy') <= '2015';

--追加访问量
declare 
	i number;
	y varchar2(10):='2016-12';--追加年份-月份 格式：2012-02，与下方格式对应
	f varchar2(10):='YYYY-MM';--年份格式 YYYY-MM-DD
  	t1 number:=100;--追加量
begin   
		i := 1;   
		while i<=t1 
		loop
     		INSERT INTO SYS_ACCESS_COUNTER VALUES (trunc(dbms_random.value(100,50000))+trunc(dbms_random.value(100,50000)),'',TO_TIMESTAMP(y,f),'');
      		i := i+1;   
		end loop;   
end;
