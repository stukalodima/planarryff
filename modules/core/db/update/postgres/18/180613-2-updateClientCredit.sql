alter table ERP_CLIENT_CREDIT rename column deleted_by to deleted_by__u57762 ;
alter table ERP_CLIENT_CREDIT rename column delete_ts to delete_ts__u54387 ;
alter table ERP_CLIENT_CREDIT rename column updated_by to updated_by__u96464 ;
alter table ERP_CLIENT_CREDIT rename column update_ts to update_ts__u29284 ;
alter table ERP_CLIENT_CREDIT rename column created_by to created_by__u90461 ;
alter table ERP_CLIENT_CREDIT rename column create_ts to create_ts__u83680 ;
alter table ERP_CLIENT_CREDIT rename column version to version__u45249 ;
alter table ERP_CLIENT_CREDIT alter column version__u45249 drop not null ;
