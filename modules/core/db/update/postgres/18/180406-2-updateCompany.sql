alter table ERP_COMPANY add column TRANSPORT_SEARCH_NARROW_RADIUS integer ^
update ERP_COMPANY set TRANSPORT_SEARCH_NARROW_RADIUS = 50 where TRANSPORT_SEARCH_NARROW_RADIUS is null ;
alter table ERP_COMPANY alter column TRANSPORT_SEARCH_NARROW_RADIUS set not null ;
alter table ERP_COMPANY add column TRANSPORT_SEARCH_WIDE_RADIUS integer ^
update ERP_COMPANY set TRANSPORT_SEARCH_WIDE_RADIUS = 100 where TRANSPORT_SEARCH_WIDE_RADIUS is null ;
alter table ERP_COMPANY alter column TRANSPORT_SEARCH_WIDE_RADIUS set not null ;
