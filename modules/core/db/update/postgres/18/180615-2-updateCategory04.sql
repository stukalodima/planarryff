update ERP_CATEGORY set NAME = '' where NAME is null ;
alter table ERP_CATEGORY alter column NAME set not null ;
