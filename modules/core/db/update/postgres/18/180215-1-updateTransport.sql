alter table ERP_TRANSPORT add column IS_FREE boolean^
alter table ERP_TRANSPORT alter column IS_FREE set default true;
create index IDX_ERP_TRANSPORT_IS_FREE on ERP_TRANSPORT (IS_FREE);
