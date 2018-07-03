alter table ERP_CARGO rename column delivery_date to delivery_date__UNUSED ;
alter table ERP_CARGO rename column sent_date to sent_date__UNUSED ;
alter table ERP_CARGO rename column comment to comment__UNUSED ;
alter table ERP_CARGO add column DOC_COMMENT varchar(255) ;
alter table ERP_CARGO add column SENT_DATE timestamp ;
alter table ERP_CARGO add column DELIVERY_DATE timestamp ;
