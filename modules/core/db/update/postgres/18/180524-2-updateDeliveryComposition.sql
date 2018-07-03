alter table ERP_DELIVERY_COMPOSITION add column STATUS integer ^
update ERP_DELIVERY_COMPOSITION set STATUS = 2 where STATUS is null ;
alter table ERP_DELIVERY_COMPOSITION alter column STATUS set not null ;
