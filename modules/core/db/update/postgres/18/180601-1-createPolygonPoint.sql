create table ERP_POLYGON_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LAT double precision not null,
    LON double precision not null,
    POLYGON_ID uuid not null,
    --
    primary key (ID)
);
