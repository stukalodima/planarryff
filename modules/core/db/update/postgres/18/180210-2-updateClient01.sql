alter table erp_client add column PERSON_TYPE integer ^
update erp_client set PERSON_TYPE = 1 where PERSON_TYPE is null ;
alter table erp_client alter column PERSON_TYPE set not null ;
