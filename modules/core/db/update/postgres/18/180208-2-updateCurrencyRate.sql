alter table ERP_CURRENCY_RATE add constraint FK_ERP_CURRENCY_RATE_CURRENCY foreign key (CURRENCY_ID) references ERP_CURRENCY(ID);
create index IDX_ERP_CURRENCY_RATE_CURRENCY on ERP_CURRENCY_RATE (CURRENCY_ID);