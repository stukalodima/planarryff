alter table ERP_EMPLOYEE_ROLE drop column type cascade ;
alter table ERP_EMPLOYEE_ROLE add column EMPLOYEE_TYPE integer ^
update ERP_EMPLOYEE_ROLE set EMPLOYEE_TYPE = 1 where EMPLOYEE_TYPE is null ;
alter table ERP_EMPLOYEE_ROLE alter column EMPLOYEE_TYPE set not null ;
update ERP_EMPLOYEE_ROLE set NAME = '' where NAME is null ;
alter table ERP_EMPLOYEE_ROLE alter column NAME set not null ;
