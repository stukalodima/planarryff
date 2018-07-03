create table ERP_CURRENCY_RATE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE date not null,
    CURRENCY_ID uuid not null,
    RATE double precision not null,
    --
    primary key (ID)
);