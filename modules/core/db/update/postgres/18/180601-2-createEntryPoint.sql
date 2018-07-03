alter table ERP_ENTRY_POINT add constraint FK_ERP_ENTRY_POINT_ON_POLYGON_MAP foreign key (POLYGON_MAP_ID) references ERP_POLYGON_MAP(ID);
create index IDX_ERP_ENTRY_POINT_ON_POLYGON_MAP on ERP_ENTRY_POINT (POLYGON_MAP_ID);
