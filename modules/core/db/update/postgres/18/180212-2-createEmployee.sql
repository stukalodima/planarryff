alter table ERP_EMPLOYEE add constraint FK_ERP_EMPLOYEE_ROLE foreign key (ROLE_ID) references ERP_EMPLOYEE_ROLE(ID);
alter table ERP_EMPLOYEE add constraint FK_ERP_EMPLOYEE_COMPANY foreign key (COMPANY_ID) references ERP_COMPANY(ID);
alter table ERP_EMPLOYEE add constraint FK_ERP_EMPLOYEE_USER foreign key (USER_ID) references SEC_USER(ID);
create index IDX_ERP_EMPLOYEE_ROLE on ERP_EMPLOYEE (ROLE_ID);
create index IDX_ERP_EMPLOYEE_COMPANY on ERP_EMPLOYEE (COMPANY_ID);
create index IDX_ERP_EMPLOYEE_USER on ERP_EMPLOYEE (USER_ID);
