CREATE OR REPLACE FUNCTION create_prefix()
  RETURNS void AS
$func$
DECLARE
curs CURSOR FOR SELECT * FROM erp_company for update;
BEGIN
FOR row IN curs LOOP
update ERP_COMPANY set PREFIX = (SELECT array_to_string(array ( select substr('ABCDEFGHIJKL', trunc(random() * 12)::integer + 1, 1) FROM   generate_series(1, 3)), '')) 
	WHERE CURRENT OF curs;
END LOOP;
END
$func$  LANGUAGE plpgsql;

alter table ERP_COMPANY add column PREFIX varchar(3) ^
SELECT create_prefix()^
alter table ERP_COMPANY alter column PREFIX set not null ;
