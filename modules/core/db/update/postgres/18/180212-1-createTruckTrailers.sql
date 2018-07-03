create table ERP_TRUCK_TRAILERS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORDER_NUMBER integer,
    TRUCK_EQUIPMENT_ID uuid,
    TRAILER_ID uuid,
    --
    primary key (ID)
);
