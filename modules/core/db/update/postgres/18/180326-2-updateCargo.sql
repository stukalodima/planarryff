alter table ERP_CARGO rename column client_id to client_id__UNUSED ;
alter table ERP_CARGO alter column client_id__UNUSED drop not null ;
drop index IDX_ERP_CARGO_CLIENT ;
