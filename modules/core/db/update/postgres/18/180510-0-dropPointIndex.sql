alter table erp_point_index rename to erp_point_index__UNUSED ;
alter table erp_cargo drop constraint FK_ERP_CARGO_END_POINT_INDEX ;
alter table erp_cargo drop constraint FK_ERP_CARGO_START_POINT_INDEX ;
