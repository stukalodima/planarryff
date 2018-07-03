create table ERP_CLIENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CAPTION varchar(255) not null,
    GUID varchar(255),
    COMPANY varchar(255),
    LAST_NAME varchar(255),
    FIRST_NAME varchar(255),
    PATRONYMIC varchar(255),
    PHONE_NUMBER varchar(255) not null,
    EMAIL varchar(255),
    AUTO_CREATION boolean,
    --
    primary key (ID)
)