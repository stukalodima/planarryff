alter table erp_client add constraint FK_ERP_CLIENT_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
create index IDX_ERP_CLIENT_COMPANY on erp_client (COMPANY_ID);
