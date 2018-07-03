alter table ERP_TRANSPORT_STATE rename column truck_equipment_id to truck_equipment_id__UNUSED ;
alter table ERP_TRANSPORT_STATE alter column truck_equipment_id__UNUSED drop not null ;
drop index IDX_ERP_TRANSPORT_STATE_TRUCK_EQUIPMENT ;
alter table ERP_TRANSPORT_STATE drop constraint FK_ERP_TRANSPORT_STATE_TRUCK_EQUIPMENT ;
-- alter table ERP_TRANSPORT_STATE add column TRANSPORT_ID uuid ^
-- update ERP_TRANSPORT_STATE set TRANSPORT_ID = <default_value> ;
-- alter table ERP_TRANSPORT_STATE alter column TRANSPORT_ID set not null ;
alter table ERP_TRANSPORT_STATE add column TRANSPORT_ID uuid not null ;
