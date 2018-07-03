alter table ERP_CARGO add constraint FK_ERP_CARGO_CURRENCY foreign key (CURRENCY_ID) references ERP_CURRENCY(ID);
create index IDX_ERP_CARGO_CURRENCY on ERP_CARGO (CURRENCY_ID);
