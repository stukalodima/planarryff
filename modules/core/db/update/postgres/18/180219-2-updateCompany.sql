alter table ERP_COMPANY drop column type cascade ;
alter table ERP_COMPANY add column COMPANY_TYPE integer ^
update ERP_COMPANY set COMPANY_TYPE = 1 where COMPANY_TYPE is null ;
alter table ERP_COMPANY alter column COMPANY_TYPE set not null ;
