create table ERP_CARGO (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    GUID varchar(255),
    COMMENT varchar(255),
    CARGO_TYPE integer not null,
    PALLETS_TYPE integer,
    WEIGHT double precision,
    VOLUME double precision,
    NUMBER_OF_PALLETS integer,
    START_ADDRESS varchar(255) not null,
    END_ADDRESS varchar(255) not null,
    TEMPERATURE_CARGO boolean,
    SENT_DATE date,
    DELIVERY_DATE date,
    PHONE varchar(255),
    TRANSPORT_TYPE_ID uuid,
    CLIENT_ID uuid not null,
    COMPANY_ID uuid,
    --
    primary key (ID)
);
