create table ERP_JOURNEY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    START_DATE timestamp,
    END_DATE timestamp,
    START_ADDRESS varchar(255),
    END_ADDRESS varchar(255),
    TRUCK_EQUIPMENT_ID uuid not null,
    CURRENCY_ID uuid not null,
    DISTANCE_TO_LOAD_POINT double precision,
    ATTRACTING_PRICE double precision,
    TRANSPORTATION_PRICE double precision,
    STATUS integer,
    --
    primary key (ID)
);
