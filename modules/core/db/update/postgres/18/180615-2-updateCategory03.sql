-- update ERP_CATEGORY set COMPANY_ID = <default_value> where COMPANY_ID is null ;
alter table ERP_CATEGORY alter column COMPANY_ID set not null ;
