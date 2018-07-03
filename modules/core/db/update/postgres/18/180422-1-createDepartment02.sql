create table ERP_DEPARTMENT (
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
    NAME varchar(255) not null,
    CHIEF_ID uuid not null,
    COMPANY_ID uuid not null,
    --
    primary key (ID)
);
