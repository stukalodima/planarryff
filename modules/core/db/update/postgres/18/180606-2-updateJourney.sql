alter table ERP_JOURNEY add column PRIOR_APPROVAL boolean ^
update ERP_JOURNEY set PRIOR_APPROVAL = false where PRIOR_APPROVAL is null ;
alter table ERP_JOURNEY alter column PRIOR_APPROVAL set not null ;
