create table ERP_TRANSPORT_STATE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TRUCK_EQUIPMENT_ID uuid,
    STATE_DATE timestamp,
    STATE integer,
    JOURNEY_ID uuid,
    LOCATION_LATITUDE double precision,
    LOCATION_LONGITUDE double precision,
    --
    primary key (ID)
);
