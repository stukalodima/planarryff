alter table ERP_TRANSPORT drop column PHOTO;
alter table ERP_TRANSPORT add column PHOTO_ID uuid^

alter table ERP_TRANSPORT add constraint FK_ERP_TRANSPORT_PHOTO foreign key (PHOTO_ID) references SYS_FILE(ID);
create index IDX_ERP_TRANSPORT_PHOTO on ERP_TRANSPORT (PHOTO_ID);
