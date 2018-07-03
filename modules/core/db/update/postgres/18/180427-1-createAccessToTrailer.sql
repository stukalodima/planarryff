create table ERP_ACCESS_TO_TRAILER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TRAILER_ID uuid not null,
    COMPANY_ID uuid not null,
    --
    primary key (ID)
);
