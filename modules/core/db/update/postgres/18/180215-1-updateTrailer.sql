alter table ERP_TRAILER add column IS_FREE boolean^
alter table ERP_TRAILER alter column IS_FREE set default true;
create index IDX_ERP_TRAILER_IS_FREE on ERP_TRAILER (IS_FREE);
