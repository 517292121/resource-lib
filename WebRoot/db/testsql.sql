        create user RISS2_2020 identified by RISS2;
        grant dba to RISS2_2020;
        
        
        
        select * from res_source_data order by RSDA_SOURCE_TYPE asc;
        
        --更新资源文件类型到新位置 2018.10.27
        update res_source_data set RSDA_SOURCE_TYPE=1 where RSDA_SOURCE_TYPE=2;
        update res_source_data set RSDA_SOURCE_TYPE=1 where RSDA_SOURCE_TYPE=3;
        update res_source_data set RSDA_SOURCE_TYPE=2 where RSDA_SOURCE_TYPE=4;
        
        
        
select * from res_source_data   where rsda_language is not null;     
        
        
        