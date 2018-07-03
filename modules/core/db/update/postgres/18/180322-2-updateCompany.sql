alter table ERP_COMPANY add column ACTIVITIES_TYPE_ID uuid ^
update ERP_COMPANY set ACTIVITIES_TYPE_ID = 'f5d9c3e0-51dc-4c07-99f2-183f18317bda' ;
alter table ERP_COMPANY alter column ACTIVITIES_TYPE_ID set not null ;
