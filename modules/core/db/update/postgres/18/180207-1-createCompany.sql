create table ERP_COMPANY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(100) not null,
    FULL_NAME varchar(255),
    ADDRESS varchar(255),
    TYPE integer not null,
    --
    primary key (ID)
);