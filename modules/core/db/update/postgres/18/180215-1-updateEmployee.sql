alter table ERP_EMPLOYEE add column IS_FREE boolean^
alter table ERP_EMPLOYEE alter column IS_FREE set default true;
create index IDX_ERP_EMPLOYEE_IS_FREE on ERP_EMPLOYEE (IS_FREE);
