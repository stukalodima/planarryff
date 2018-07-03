create table ERP_DELIVERY_COMPOSITION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DELIVERY_ID uuid not null,
    POINT_ID uuid not null,
    DELIVERY_DATE timestamp not null,
    SERVICE_TIME integer,
    DOWN_TIME integer,
    DISTANCE double precision,
    DURATION integer,
    --
    primary key (ID)
);
