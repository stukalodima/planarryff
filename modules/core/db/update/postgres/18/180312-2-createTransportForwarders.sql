alter table ERP_TRANSPORT_FORWARDERS add constraint FK_ERP_TRANSPORT_FORWARDERS_EMPLOYEE foreign key (EMPLOYEE_ID) references ERP_EMPLOYEE(ID);
alter table ERP_TRANSPORT_FORWARDERS add constraint FK_ERP_TRANSPORT_FORWARDERS_TRANSPORT foreign key (TRANSPORT_ID) references ERP_TRANSPORT(ID);
create index IDX_ERP_TRANSPORT_FORWARDERS_EMPLOYEE on ERP_TRANSPORT_FORWARDERS (EMPLOYEE_ID);
create index IDX_ERP_TRANSPORT_FORWARDERS_TRANSPORT on ERP_TRANSPORT_FORWARDERS (TRANSPORT_ID);
