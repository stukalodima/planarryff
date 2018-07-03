CREATE TABLE ERP_TRANSPORTS_ACCESS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
--
    TRANSPORT_ID uuid not null,
    COMPANY_ID uuid,
--
    PRIMARY KEY (ID)
)
