alter table ERP_COMPANY add constraint FK_ERP_COMPANY_ACTIVITIES_TYPE foreign key (ACTIVITIES_TYPE_ID) references ERP_ACTIVITIES_TYPE(ID);
create index IDX_ERP_COMPANY_ACTIVITIES_TYPE on ERP_COMPANY (ACTIVITIES_TYPE_ID);
