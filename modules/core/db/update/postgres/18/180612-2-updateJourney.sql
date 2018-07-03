alter table ERP_JOURNEY rename column prior_approval to prior_approval__UNUSED ;
alter table ERP_JOURNEY alter column prior_approval__UNUSED drop not null ;
