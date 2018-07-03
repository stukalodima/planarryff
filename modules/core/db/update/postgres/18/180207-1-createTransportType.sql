create table ERP_TRANSPORT_TYPE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PID uuid,
    NAME varchar(100) not null,
    --
    primary key (ID)
);