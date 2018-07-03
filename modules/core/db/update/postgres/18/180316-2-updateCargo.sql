alter table ERP_CARGO rename column description to description__UNUSED ;
alter table ERP_CARGO alter column description__UNUSED drop not null ;
