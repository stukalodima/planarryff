create table ERP_CARGO_DELIVERY_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DELIVERY_DATE timestamp not null,
    DELIVERY_DATE_DELTA integer,
    POINT_ID uuid not null,
    WEIGHT double precision,
    VOLUME double precision,
    NUMBER_OF_PALLETS integer,
    SERVICE_TIME integer,
    CARGO_ID uuid not null,
    --
    primary key (ID)
);
