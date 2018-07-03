--function for update each row in erp_cargo
CREATE OR REPLACE FUNCTION create_point_index() RETURNS void AS
$func$
  DECLARE
    curs CURSOR FOR SELECT * FROM erp_cargo for update;
    start_point_ID uuid;
    end_point_ID uuid;
  BEGIN
  FOR row IN curs LOOP
    start_point_ID = uuid_generate_v4();
    end_point_ID = uuid_generate_v4();
    INSERT INTO erp_point_index (ID, VERSION, POINT_TYPE) VALUES (start_point_ID, 1, 1);
    INSERT INTO erp_point_index (ID, VERSION, POINT_TYPE) VALUES (end_point_ID, 1, 2);
    update ERP_CARGO set START_POINT_INDEX_ID = start_point_ID, END_POINT_INDEX_ID = end_point_ID WHERE CURRENT OF curs;
  END LOOP;
END
$func$  LANGUAGE plpgsql;

--trigger function for a new row in erp_point_index table
CREATE FUNCTION trigger_point_index_id_before_ins () RETURNS trigger AS
$$
BEGIN
NEW.POINT_ID = nextval('point_id_seq');
return NEW;
END;
$$ LANGUAGE  plpgsql;

--sequence for point id
CREATE SEQUENCE point_id_seq START 1;

--trigger
CREATE TRIGGER trigger_point_index_insert
  BEFORE INSERT ON erp_point_index FOR EACH ROW
  EXECUTE PROCEDURE trigger_point_index_id_before_ins();

select create_point_index();

alter table ERP_CARGO alter column START_POINT_INDEX_ID set not null ;
alter table ERP_CARGO alter column END_POINT_INDEX_ID set not null ;