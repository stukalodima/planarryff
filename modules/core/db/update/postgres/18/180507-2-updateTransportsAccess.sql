-- update ERP_TRANSPORTS_ACCESS set COMPANY_ID = <default_value> where COMPANY_ID is null ;
alter table ERP_TRANSPORTS_ACCESS alter column COMPANY_ID set not null ;
