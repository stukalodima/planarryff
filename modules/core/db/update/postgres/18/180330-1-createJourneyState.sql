create table ERP_JOURNEY_STATE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    STATE_DATE timestamp not null,
    USER_ID uuid not null,
    JOURNEY_ID uuid not null,
    STATUS integer not null,
    USER_COMMENT text,
    --
    primary key (ID)
);
