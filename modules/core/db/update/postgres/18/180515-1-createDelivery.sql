create table ERP_DELIVERY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CARGO_ID uuid not null,
    JOURNEY_ID uuid not null,
    TRANSPORT_ID uuid not null,
    CURRENCY_ID uuid not null,
    DISTANCE double precision,
    TRANSPORTATION_TIME integer,
    TRANSPORTATION_PRICE double precision,
    APPROVED boolean,
    --
    primary key (ID)
);
