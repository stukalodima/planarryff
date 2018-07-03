create table ERP_FORWARDER (
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
    GUID varchar(255),
    COMPANY_ID uuid not null,
    USER_ID uuid not null,
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    LAST_NAME varchar(255),
    PHONE varchar(255),
    EMAIL varchar(255),
    --
    primary key (ID)
);