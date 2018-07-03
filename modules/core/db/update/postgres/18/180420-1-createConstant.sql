create table ERP_CONSTANT (
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
    VALUE_TYPE integer not null,
    CONSTANT_VALUE varchar(255) not null,
    CONSTANT_KEY varchar(255) not null,
    --
    primary key (ID)
);
