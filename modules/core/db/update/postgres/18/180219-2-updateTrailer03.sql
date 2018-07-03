-- update ERP_TRAILER set CURRENCY_BILLING_ID = <default_value> where CURRENCY_BILLING_ID is null ;
alter table ERP_TRAILER alter column CURRENCY_BILLING_ID set not null ;
update ERP_TRAILER set IS_FREE = true where IS_FREE is null ;
alter table ERP_TRAILER alter column IS_FREE set not null ;
update ERP_TRAILER set TRAILER_TYPE = 1 where TRAILER_TYPE is null ;
alter table ERP_TRAILER alter column TRAILER_TYPE set not null ;
