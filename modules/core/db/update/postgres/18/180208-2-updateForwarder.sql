alter table ERP_FORWARDER add constraint FK_ERP_FORWARDER_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
alter table ERP_FORWARDER add constraint FK_ERP_FORWARDER_USER foreign key (USER_ID) references SEC_USER(ID);
create index IDX_ERP_FORWARDER_COMPANY on ERP_FORWARDER (COMPANY_ID);
create index IDX_ERP_FORWARDER_USER on ERP_FORWARDER (USER_ID);