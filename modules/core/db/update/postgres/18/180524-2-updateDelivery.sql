alter table ERP_DELIVERY add column STATUS integer ^
update ERP_DELIVERY set STATUS = 1 where STATUS is null ;
alter table ERP_DELIVERY alter column STATUS set not null ;
