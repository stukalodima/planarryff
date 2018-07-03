alter table ERP_JOURNEY rename column distance_to_load_point to transportation_distance ;
alter table ERP_JOURNEY add column transportation_time bigint;
alter table ERP_JOURNEY add column track text;

