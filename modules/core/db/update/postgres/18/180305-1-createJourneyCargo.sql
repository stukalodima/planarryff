create table ERP_JOURNEY_CARGO (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    JOURNEY_ID uuid not null,
    CARGO_ID uuid,
    DRIVER_COMMENT varchar(255),
    --
    primary key (ID)
);
