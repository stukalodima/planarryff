alter table ERP_CARGO add constraint FK_ERP_CARGO_POINT foreign key (POINT_ID) references ERP_POINT(ID);
create index IDX_ERP_CARGO_POINT on ERP_CARGO (POINT_ID);
