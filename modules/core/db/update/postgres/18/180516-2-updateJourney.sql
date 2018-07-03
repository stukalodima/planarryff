alter table ERP_JOURNEY rename column transportation_time to transportation_time__UNUSED ;
alter table ERP_JOURNEY add column TRANSPORTATION_TIME integer ;
alter table ERP_JOURNEY drop column TRANSPORTATION_TIME__UNUSED cascade ;
