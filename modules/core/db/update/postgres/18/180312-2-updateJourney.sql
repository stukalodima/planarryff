alter table ERP_JOURNEY rename column truck_equipment_id to truck_equipment_id__UNUSED ;
alter table ERP_JOURNEY alter column truck_equipment_id__UNUSED drop not null ;
drop index IDX_ERP_JOURNEY_TRUCK_EQUIPMENT ;
alter table ERP_JOURNEY drop constraint FK_ERP_JOURNEY_TRUCK_EQUIPMENT ;
-- alter table ERP_JOURNEY add column TRANSPORT_ID uuid ^
-- update ERP_JOURNEY set TRANSPORT_ID = <default_value> ;
-- alter table ERP_JOURNEY alter column TRANSPORT_ID set not null ;
alter table ERP_JOURNEY add column TRANSPORT_ID uuid not null ;
