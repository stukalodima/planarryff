alter table ERP_CURRENCY_RATE rename column rate_date to rate_date__u51167 ;
alter table ERP_CURRENCY_RATE alter column rate_date__u51167 drop not null ;
alter table ERP_CURRENCY_RATE add column RATE_DATE date ^
update ERP_CURRENCY_RATE set RATE_DATE = current_date where RATE_DATE is null ;
alter table ERP_CURRENCY_RATE alter column RATE_DATE set not null ;
