create table ERP_TRANSPORT_TRAILERS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORDER_NUMBER integer not null,
    TRANSPORT_ID uuid not null,
    TRAILER_ID uuid not null,
    --
    primary key (ID)
);
