create table ERP_SERVICES_PAYMENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_PAYMENT timestamp not null,
    COUNTERPARTY_ID uuid,
    NUMBER_PAYMENT varchar(255) not null,
    COMPANY_ID uuid not null,
    TYPE_PAYMENT integer not null,
    COMMENT_ varchar(255),
    VALUE_ double precision not null,
    --
    primary key (ID)
);
