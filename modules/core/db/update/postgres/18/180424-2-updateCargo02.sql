alter table ERP_CARGO add column ACCESSIBLE_TO_ALL boolean ;
alter table ERP_CARGO add column ACCESSIBLE_TO_OWNER boolean ^
update ERP_CARGO set ACCESSIBLE_TO_OWNER = true where ACCESSIBLE_TO_OWNER is null;
