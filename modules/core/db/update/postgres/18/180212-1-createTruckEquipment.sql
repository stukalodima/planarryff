create table ERP_TRUCK_EQUIPMENT (
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
    DOC_TYPE integer,
    DATE timestamp,
    COMMENT varchar(255),
    EMPTY_TRUCK_WEIGHT double precision,
    TRANSPORT_LENGTH double precision,
    TRANSPORT_WIDTH double precision,
    TRANSPORT_HEIGHT double precision,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    MAX_CARGO_WEIGHT double precision,
    MAX_CARGO_VOLUME double precision,
    TRANSPORT_ID uuid not null,
    CURRENCY_ID uuid,
    --
    primary key (ID)
);
