create table ERP_CLIENT_CREDIT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TRANSPORT_OWNER_ID uuid not null,
    COUNTERPARTY_ID uuid not null,
    PAY_DATE timestamp not null,
    VALUE_ double precision not null,
    JOURNEY_ID uuid not null,
    --
    primary key (ID)
);
