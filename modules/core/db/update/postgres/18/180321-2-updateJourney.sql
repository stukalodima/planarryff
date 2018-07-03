alter table ERP_JOURNEY add column MANUAL_JOURNEY boolean ^
update ERP_JOURNEY set MANUAL_JOURNEY = false where MANUAL_JOURNEY is null ;
alter table ERP_JOURNEY alter column MANUAL_JOURNEY set not null ;
alter table ERP_JOURNEY alter column CURRENCY_ID drop not null ;
