create table ERP_POINT_INDEX (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    POINT_TYPE integer not null,
    POINT_ID bigint,
    --
    primary key (ID)
);
