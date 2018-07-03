alter table ERP_TRANSPORT rename column limit_speed to limit_speed__UNUSED ;
alter table ERP_TRANSPORT rename column end_time to end_time__UNUSED ;
alter table ERP_TRANSPORT rename column start_time to start_time__UNUSED ;
alter table ERP_TRANSPORT rename column min_service_time to min_service_time__UNUSED ;
alter table ERP_TRANSPORT rename column limit_volume to limit_volume__UNUSED ;
alter table ERP_TRANSPORT rename column end_point_id to end_point_id__UNUSED ;
alter table ERP_TRANSPORT rename column start_point_id to start_point_id__UNUSED ;
alter table ERP_TRANSPORT add column CLASS_ADR integer ;
