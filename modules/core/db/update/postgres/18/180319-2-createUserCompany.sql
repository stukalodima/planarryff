alter table ERP_USER_COMPANY add constraint FK_ERP_USER_COMPANY_USER foreign key (USER_ID) references SEC_USER(ID);
alter table ERP_USER_COMPANY add constraint FK_ERP_USER_COMPANY_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
create index IDX_ERP_USER_COMPANY_USER on ERP_USER_COMPANY (USER_ID);
create index IDX_ERP_USER_COMPANY_COMPANY on ERP_USER_COMPANY (COMPANY_ID);
