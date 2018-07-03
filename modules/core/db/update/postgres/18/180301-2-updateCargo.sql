alter table ERP_CARGO add column DESCRIPTION varchar(255) ^
update ERP_CARGO set DESCRIPTION = '' where DESCRIPTION is null ;
alter table ERP_CARGO alter column DESCRIPTION set not null ;
