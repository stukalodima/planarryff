alter table ERP_CARGO add constraint FK_ERP_CARGO_CLIENT foreign key (CLIENT_ID) references ERP_COMPANY(ID);
create index IDX_ERP_CARGO_CLIENT on ERP_CARGO (CLIENT_ID);
