alter table ERP_TRUCK_EQUIPMENT drop column date cascade ;
alter table ERP_TRUCK_EQUIPMENT add column DOC_DATE timestamp ^
update ERP_TRUCK_EQUIPMENT set DOC_DATE = current_timestamp where DOC_DATE is null ;
alter table ERP_TRUCK_EQUIPMENT alter column DOC_DATE set not null ;
update ERP_TRUCK_EQUIPMENT set DOC_TYPE = 1 where DOC_TYPE is null ;
alter table ERP_TRUCK_EQUIPMENT alter column DOC_TYPE set not null ;
-- update ERP_TRUCK_EQUIPMENT set TRANSPORT_ID = <default_value> where TRANSPORT_ID is null ;
alter table ERP_TRUCK_EQUIPMENT alter column TRANSPORT_ID set not null ;
