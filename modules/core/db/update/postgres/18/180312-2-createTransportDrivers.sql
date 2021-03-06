alter table ERP_TRANSPORT_DRIVERS add constraint FK_ERP_TRANSPORT_DRIVERS_TRANSPORT foreign key (TRANSPORT_ID) references ERP_TRANSPORT(ID);
alter table ERP_TRANSPORT_DRIVERS add constraint FK_ERP_TRANSPORT_DRIVERS_EMPLOYEE foreign key (EMPLOYEE_ID) references ERP_EMPLOYEE(ID);
create index IDX_ERP_TRANSPORT_DRIVERS_TRANSPORT on ERP_TRANSPORT_DRIVERS (TRANSPORT_ID);
create index IDX_ERP_TRANSPORT_DRIVERS_EMPLOYEE on ERP_TRANSPORT_DRIVERS (EMPLOYEE_ID);
