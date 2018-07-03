--trigger function for a new row in erp_point_index table
CREATE FUNCTION trigger_point_code_before_ins () RETURNS trigger AS
$$
BEGIN
NEW.CODE = nextval('point_code_seq');
return NEW;
END;
$$ LANGUAGE  plpgsql;

--sequence for point id
CREATE SEQUENCE point_code_seq START 1;

--trigger
CREATE TRIGGER trigger_point_insert
  BEFORE INSERT ON erp_point FOR EACH ROW
  EXECUTE PROCEDURE trigger_point_code_before_ins();

alter table ERP_CARGO add column POINT_ID uuid ^
insert into ERP_POINT (ID, VERSION, ADDRESS, COUNTRY, CITY, LATITUDE, LONGITUDE) 
    values ('b5952f5f-6488-43dc-8bfe-4461523ca060', 1, 'Украина, Киев', 'Украина', 'Киев', 50.45, 30.52);
update ERP_CARGO set POINT_ID = 'b5952f5f-6488-43dc-8bfe-4461523ca060' ;
alter table ERP_CARGO alter column POINT_ID set not null ;
