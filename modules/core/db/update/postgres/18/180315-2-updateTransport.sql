update ERP_TRANSPORT set IDENT_NUMBER = '' where IDENT_NUMBER is null ;
alter table ERP_TRANSPORT alter column IDENT_NUMBER set not null ;
update ERP_TRANSPORT set DOWNLOAD_TYPE = 1 where DOWNLOAD_TYPE is null ;
alter table ERP_TRANSPORT alter column DOWNLOAD_TYPE set not null ;
-- update ERP_TRANSPORT set FORWARDER_ID = <default value> where FORWARDER_ID is null ;
alter table ERP_TRANSPORT alter column FORWARDER_ID set not null ;
