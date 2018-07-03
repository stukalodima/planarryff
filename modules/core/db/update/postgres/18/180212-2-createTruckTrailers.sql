alter table ERP_TRUCK_TRAILERS add constraint FK_ERP_TRUCK_TRAILERS_TRUCK_EQUIPMENT foreign key (TRUCK_EQUIPMENT_ID) references ERP_TRUCK_EQUIPMENT(ID);
alter table ERP_TRUCK_TRAILERS add constraint FK_ERP_TRUCK_TRAILERS_TRAILER foreign key (TRAILER_ID) references ERP_TRAILER(ID);
create index IDX_ERP_TRUCK_TRAILERS_TRUCK_EQUIPMENT on ERP_TRUCK_TRAILERS (TRUCK_EQUIPMENT_ID);
create index IDX_ERP_TRUCK_TRAILERS_TRAILER on ERP_TRUCK_TRAILERS (TRAILER_ID);
