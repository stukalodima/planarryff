alter table ERP_CURRENCY_RATE rename column date to date__UNUSED ;
alter table ERP_CURRENCY_RATE alter column date__UNUSED drop not null ;
alter table ERP_CURRENCY_RATE add column DATE timestamp ^
update ERP_CURRENCY_RATE set DATE = current_date where DATE is null ;
alter table ERP_CURRENCY_RATE alter column DATE set not null ;
