update ERP_JOURNEY set APPROVED = false where APPROVED is null ;
alter table ERP_JOURNEY alter column APPROVED set not null ;
