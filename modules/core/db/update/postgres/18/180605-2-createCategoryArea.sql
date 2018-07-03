alter table ERP_CATEGORY_AREA add constraint FK_ERP_CATEGORY_AREA_CATEGORY foreign key (CATEGORY_ID) references ERP_CATEGORY(ID);
alter table ERP_CATEGORY_AREA add constraint FK_ERP_CATEGORY_AREA_POLYGON foreign key (POLYGON_ID) references ERP_POLYGON_MAP(ID);
create index IDX_ERP_CATEGORY_AREA_CATEGORY on ERP_CATEGORY_AREA (CATEGORY_ID);
create index IDX_ERP_CATEGORY_AREA_POLYGON on ERP_CATEGORY_AREA (POLYGON_ID);
