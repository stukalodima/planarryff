create table ERP_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    GUID varchar(255),
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    LAST_NAME varchar(255),
    PHONE varchar(255),
    EMAIL varchar(255),
    ROLE_ID uuid,
    COMPANY_ID uuid,
    USER_ID uuid,
    --
    primary key (ID)
);
