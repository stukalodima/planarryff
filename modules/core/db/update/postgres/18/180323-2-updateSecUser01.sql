alter table SEC_USER add constraint FK_SEC_USER_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
create index IDX_SEC_USER_COMPANY on SEC_USER (COMPANY_ID);
