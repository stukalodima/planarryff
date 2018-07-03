alter table ERP_CURRENCY_RATE rename column rate_date to rate_date__u84560 ;
alter table ERP_CURRENCY_RATE alter column rate_date__u84560 drop not null ;
alter table ERP_CURRENCY_RATE add column RATE_DATE timestamp ^
update ERP_CURRENCY_RATE set RATE_DATE = current_timestamp where RATE_DATE is null ;
alter table ERP_CURRENCY_RATE alter column RATE_DATE set not null ;
