alter table ERP_MUTUAL_SETTLEMENTS rename column deleted_by to deleted_by__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column delete_ts to delete_ts__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column updated_by to updated_by__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column update_ts to update_ts__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column created_by to created_by__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column create_ts to create_ts__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS rename column version to version__UNUSED ;
alter table ERP_MUTUAL_SETTLEMENTS alter column version__UNUSED drop not null ;
