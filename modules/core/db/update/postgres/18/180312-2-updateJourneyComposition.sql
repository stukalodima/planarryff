alter table ERP_JOURNEY_COMPOSITION rename column truck_equipment_id to truck_equipment_id__UNUSED ;
alter table ERP_JOURNEY_COMPOSITION alter column truck_equipment_id__UNUSED drop not null ;
drop index IDX_ERP_JOURNEY_COMPOSITION_TRUCK_EQUIPMENT ;
alter table ERP_JOURNEY_COMPOSITION drop constraint FK_ERP_JOURNEY_COMPOSITION_TRUCK_EQUIPMENT ;
-- alter table ERP_JOURNEY_COMPOSITION add column TRANSPORT_ID uuid ^
-- update ERP_JOURNEY_COMPOSITION set TRANSPORT_ID = <default_value> ;
-- alter table ERP_JOURNEY_COMPOSITION alter column TRANSPORT_ID set not null ;
alter table ERP_JOURNEY_COMPOSITION add column TRANSPORT_ID uuid not null ;
