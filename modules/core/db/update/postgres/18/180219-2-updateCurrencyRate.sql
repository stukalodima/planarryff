alter table ERP_CURRENCY_RATE drop column date cascade ;
alter table ERP_CURRENCY_RATE add column RATE_DATE timestamp ^
update ERP_CURRENCY_RATE set RATE_DATE = current_date where RATE_DATE is null ;
alter table ERP_CURRENCY_RATE alter column RATE_DATE set not null ;
