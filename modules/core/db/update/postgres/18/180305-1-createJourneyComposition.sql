create table ERP_JOURNEY_COMPOSITION (
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
    JOURNEY_ID uuid not null,
    LOCATION_DATE timestamp,
    LOCATION varchar(255),
    --
    primary key (ID)
);
