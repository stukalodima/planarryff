alter table ERP_CARGO add column CLIENT_ID uuid ^
update ERP_CARGO set CLIENT_ID = COMPANY_ID;
alter table ERP_CARGO alter column CLIENT_ID set not null ;
