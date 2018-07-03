alter table ERP_TRANSPORT drop column type cascade ;
alter table ERP_TRANSPORT add column TRUCK_TYPE integer ^
update ERP_TRANSPORT set TRUCK_TYPE = 1 where TRUCK_TYPE is null ;
alter table ERP_TRANSPORT alter column TRUCK_TYPE set not null ;
-- update ERP_TRANSPORT set CURRENCY_BILLING_ID = <default_value> where CURRENCY_BILLING_ID is null ;
alter table ERP_TRANSPORT alter column CURRENCY_BILLING_ID set not null ;
update ERP_TRANSPORT set IS_FREE = true where IS_FREE is null ;
alter table ERP_TRANSPORT alter column IS_FREE set not null ;
