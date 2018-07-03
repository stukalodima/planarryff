update erp_client set AUTO_CREATION = false where AUTO_CREATION is null ;
alter table erp_client alter column AUTO_CREATION set not null ;
