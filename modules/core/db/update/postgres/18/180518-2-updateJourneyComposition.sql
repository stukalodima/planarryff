CREATE OR REPLACE FUNCTION update_journey_composition()
  RETURNS void AS
$func$
DECLARE
curs CURSOR FOR SELECT * FROM erp_journey_composition for update;
pointId uuid;
BEGIN
FOR row IN curs LOOP
  pointId := (select uuid_generate_v4());
  insert into ERP_POINT (ID, VERSION, ADDRESS, COUNTRY, CITY, LATITUDE, LONGITUDE)
    values (pointId, 1, row.location_address, '', '', row.location_latitude, row.location_longitude);
  update erp_journey_composition set POINT_ID = pointId WHERE CURRENT OF curs;
END LOOP;
END
$func$  LANGUAGE plpgsql;


alter table ERP_JOURNEY_COMPOSITION add column POINT_ID uuid ^
select update_journey_composition();
alter table ERP_JOURNEY_COMPOSITION alter column POINT_ID set not null ;

alter table ERP_JOURNEY_COMPOSITION alter column location_longitude drop not null ;
alter table ERP_JOURNEY_COMPOSITION drop column location_longitude ;
alter table ERP_JOURNEY_COMPOSITION alter column location_latitude drop not null ;
alter table ERP_JOURNEY_COMPOSITION drop column location_latitude ;
alter table ERP_JOURNEY_COMPOSITION drop column location_address ;

