create table ERP_ENTRY_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LAT double precision not null,
    LON double precision not null,
    POLYGON_MAP_ID uuid not null,
    --
    primary key (ID)
);
