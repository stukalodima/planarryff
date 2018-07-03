alter table ERP_CARGO add constraint FK_ERP_CARGO_MANAGER foreign key (MANAGER_ID) references ERP_EMPLOYEE(ID);
create index IDX_ERP_CARGO_MANAGER on ERP_CARGO (MANAGER_ID);
