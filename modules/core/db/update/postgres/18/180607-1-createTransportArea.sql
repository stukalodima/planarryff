create table ERP_TRANSPORT_AREA (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TRANSPORT_ID uuid not null,
    POLYGON_ID uuid not null,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    --
    primary key (ID)
);
