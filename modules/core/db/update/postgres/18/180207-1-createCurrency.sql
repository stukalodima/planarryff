create table ERP_CURRENCY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(3) not null,
    NAME varchar(50) not null,
    SHIRT_NAME varchar(3) not null,
    BASE_CURRENCY boolean,
    COEFFICIENT double precision,
    --
    primary key (ID)
);