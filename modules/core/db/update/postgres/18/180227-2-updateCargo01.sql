alter table ERP_CARGO add column CLIENT_CAPTION varchar(255) ^
update ERP_CARGO set CLIENT_CAPTION = '' where CLIENT_CAPTION is null ;
alter table ERP_CARGO alter column CLIENT_CAPTION set not null ;
