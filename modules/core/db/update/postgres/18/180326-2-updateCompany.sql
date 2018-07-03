alter table ERP_COMPANY add column PHONE_NUMBER varchar(255) ^
update ERP_COMPANY set PHONE_NUMBER = '' where PHONE_NUMBER is null ;
alter table ERP_COMPANY alter column PHONE_NUMBER set not null ;
alter table ERP_COMPANY add column EMAIL varchar(255) ;
alter table ERP_COMPANY add column FIRST_NAME varchar(255) ;
alter table ERP_COMPANY add column MIDDLE_NAME varchar(255) ;
alter table ERP_COMPANY add column LAST_NAME varchar(255) ;
alter table ERP_COMPANY add column AUTO_CREATION boolean ^
update ERP_COMPANY set AUTO_CREATION = false where AUTO_CREATION is null ;
alter table ERP_COMPANY alter column AUTO_CREATION set not null ;
