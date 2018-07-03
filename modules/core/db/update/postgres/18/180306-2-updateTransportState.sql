alter table ERP_TRANSPORT_STATE add column LOCATION_ADDRESS varchar(255) ;
alter table ERP_TRANSPORT_STATE add column DOC_TYPE integer ^
update ERP_TRANSPORT_STATE set DOC_TYPE = 1 where DOC_TYPE is null ;
alter table ERP_TRANSPORT_STATE alter column DOC_TYPE set not null ;
-- update ERP_TRANSPORT_STATE set TRUCK_EQUIPMENT_ID = <default_value> where TRUCK_EQUIPMENT_ID is null ;
alter table ERP_TRANSPORT_STATE alter column TRUCK_EQUIPMENT_ID set not null ;
update ERP_TRANSPORT_STATE set STATE_DATE = current_timestamp where STATE_DATE is null ;
alter table ERP_TRANSPORT_STATE alter column STATE_DATE set not null ;
alter table ERP_TRANSPORT_STATE alter column LOCATION_LATITUDE set not null ;
alter table ERP_TRANSPORT_STATE alter column LOCATION_LONGITUDE set not null ;
