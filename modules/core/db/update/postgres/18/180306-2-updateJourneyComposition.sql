alter table ERP_JOURNEY_COMPOSITION rename column location to LOCATION_ADDRESS ;

alter table ERP_JOURNEY_COMPOSITION add column LOCATION_LATITUDE double precision^
update ERP_JOURNEY_COMPOSITION set LOCATION_LATITUDE = 0 where LOCATION_LATITUDE is null ;
alter table ERP_JOURNEY_COMPOSITION alter column LOCATION_LATITUDE set not null ;

alter table ERP_JOURNEY_COMPOSITION add column LOCATION_LONGITUDE double precision^
update ERP_JOURNEY_COMPOSITION set LOCATION_LONGITUDE = 0 where LOCATION_LONGITUDE is null ;
alter table ERP_JOURNEY_COMPOSITION alter column LOCATION_LONGITUDE set not null ;

-- update ERP_JOURNEY_COMPOSITION set TRUCK_EQUIPMENT_ID = <default_value> where TRUCK_EQUIPMENT_ID is null ;
alter table ERP_JOURNEY_COMPOSITION alter column TRUCK_EQUIPMENT_ID set not null ;
alter table ERP_JOURNEY_COMPOSITION alter column JOURNEY_ID drop not null ;
