alter table ERP_JOURNEY add column RESIDUAL_WEIGHT double precision ^
update ERP_JOURNEY set RESIDUAL_WEIGHT = 0 where RESIDUAL_WEIGHT is null ;
alter table ERP_JOURNEY alter column RESIDUAL_WEIGHT set not null ;
alter table ERP_JOURNEY add column RESIDUAL_VOLUME double precision ^
update ERP_JOURNEY set RESIDUAL_VOLUME = 0 where RESIDUAL_VOLUME is null ;
alter table ERP_JOURNEY alter column RESIDUAL_VOLUME set not null ;
