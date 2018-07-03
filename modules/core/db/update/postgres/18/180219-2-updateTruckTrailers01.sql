update ERP_TRUCK_TRAILERS set ORDER_NUMBER = 1 where ORDER_NUMBER is null ;
alter table ERP_TRUCK_TRAILERS alter column ORDER_NUMBER set not null ;
-- update ERP_TRUCK_TRAILERS set TRUCK_EQUIPMENT_ID = <default_value> where TRUCK_EQUIPMENT_ID is null ;
alter table ERP_TRUCK_TRAILERS alter column TRUCK_EQUIPMENT_ID set not null ;
-- update ERP_TRUCK_TRAILERS set TRAILER_ID = <default_value> where TRAILER_ID is null ;
alter table ERP_TRUCK_TRAILERS alter column TRAILER_ID set not null ;
