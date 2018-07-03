alter table ERP_TRAILER rename column end_time to end_time__UNUSED ;
alter table ERP_TRAILER rename column start_time to start_time__UNUSED ;
alter table ERP_TRAILER rename column min_service_time to min_service_time__UNUSED ;
alter table ERP_TRAILER rename column limit_volume to limit_volume__UNUSED ;
alter table ERP_TRAILER rename column max_cargo_cost to max_cargo_cost__UNUSED ;
alter table ERP_TRAILER rename column end_point_id to end_point_id__UNUSED ;
alter table ERP_TRAILER rename column start_point_id to start_point_id__UNUSED ;
alter table ERP_TRAILER add column DOWNLOAD_TYPE integer ^
update ERP_TRAILER set DOWNLOAD_TYPE = 1 where DOWNLOAD_TYPE is null ;
alter table ERP_TRAILER alter column DOWNLOAD_TYPE set not null ;
update ERP_TRAILER set IDENT_NUMBER = '' where IDENT_NUMBER is null ;
alter table ERP_TRAILER alter column IDENT_NUMBER set not null ;
-- update ERP_TRAILER set FORWARDER_ID = <default value> where FORWARDER_ID is null ;
alter table ERP_TRAILER alter column FORWARDER_ID set not null ;
