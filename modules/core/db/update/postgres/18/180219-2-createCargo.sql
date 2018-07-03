alter table ERP_CARGO add constraint FK_ERP_CARGO_TRANSPORT_TYPE foreign key (TRANSPORT_TYPE_ID) references ERP_TRANSPORT_TYPE(ID);
alter table ERP_CARGO add constraint FK_ERP_CARGO_CLIENT foreign key (CLIENT_ID) references erp_client(ID);
alter table ERP_CARGO add constraint FK_ERP_CARGO_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
create index IDX_ERP_CARGO_TRANSPORT_TYPE on ERP_CARGO (TRANSPORT_TYPE_ID);
create index IDX_ERP_CARGO_CLIENT on ERP_CARGO (CLIENT_ID);
create index IDX_ERP_CARGO_COMPANY on ERP_CARGO (COMPANY_ID);
