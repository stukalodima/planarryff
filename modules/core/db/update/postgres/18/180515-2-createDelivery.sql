alter table ERP_DELIVERY add constraint FK_ERP_DELIVERY_CARGO foreign key (CARGO_ID) references ERP_CARGO(ID);
alter table ERP_DELIVERY add constraint FK_ERP_DELIVERY_JOURNEY foreign key (JOURNEY_ID) references ERP_JOURNEY(ID);
alter table ERP_DELIVERY add constraint FK_ERP_DELIVERY_TRANSPORT foreign key (TRANSPORT_ID) references ERP_TRANSPORT(ID);
alter table ERP_DELIVERY add constraint FK_ERP_DELIVERY_CURRENCY foreign key (CURRENCY_ID) references ERP_CURRENCY(ID);
create index IDX_ERP_DELIVERY_CARGO on ERP_DELIVERY (CARGO_ID);
create index IDX_ERP_DELIVERY_JOURNEY on ERP_DELIVERY (JOURNEY_ID);
create index IDX_ERP_DELIVERY_TRANSPORT on ERP_DELIVERY (TRANSPORT_ID);
create index IDX_ERP_DELIVERY_CURRENCY on ERP_DELIVERY (CURRENCY_ID);
