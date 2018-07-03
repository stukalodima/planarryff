alter table erp_client rename column company to company__UNUSED ;
alter table erp_client add column COMPANY_ID uuid ;
