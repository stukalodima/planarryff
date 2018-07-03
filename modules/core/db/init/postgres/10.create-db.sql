CREATE EXTENSION IF NOT EXISTS "uuid-ossp"^
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch^

-- begin ERP_COMPANY
create table ERP_COMPANY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(100) not null,
    PREFIX varchar(3) not null,
    FULL_NAME varchar(255),
    ADDRESS varchar(255),
    COMPANY_TYPE integer not null,
    PHONE_NUMBER varchar(255) not null,
    EMAIL varchar(255),
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    LAST_NAME varchar(255),
    AUTO_CREATION boolean not null,
    RETURN_AREA_ID uuid,
    ACTIVITIES_TYPE_ID uuid not null,
    TRANSPORT_SEARCH_NARROW_RADIUS integer not null,
    TRANSPORT_SEARCH_WIDE_RADIUS integer not null,
    ROUTER varchar(50),
    --
    primary key (ID)
)^
-- end ERP_COMPANY
-- begin ERP_TRANSPORT_TYPE
create table ERP_TRANSPORT_TYPE (
  ID         uuid,
  VERSION    integer      not null,
  CREATE_TS  timestamp,
  CREATED_BY varchar(50),
  UPDATE_TS  timestamp,
  UPDATED_BY varchar(50),
  DELETE_TS  timestamp,
  DELETED_BY varchar(50),
  --
  PID        uuid,
  NAME       varchar(100) not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_TYPE
-- begin ERP_TRANSPORT
create table ERP_TRANSPORT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    IDENT_NUMBER varchar(255) not null,
    GUID varchar(255),
    TRUCK_TYPE integer not null,
    DOWNLOAD_TYPE integer,
    AVAILABLE_FOR_ALL boolean,
    AVAILABLE_FOR_OWNER boolean,
    COMPANY_ID uuid not null,
    TRANSPORT_TYPE_ID uuid,
    CURRENCY_BILLING_ID uuid,
    FORWARDER_ID uuid not null,
    PHOTO_ID uuid,
    CATEGORY_ID uuid,
    VIN_CODE varchar(255),
    EMPTY_TRUCK_WEIGHT double precision,
    BODY_LENGTH double precision,
    BODY_WIDTH double precision,
    BODY_HEIGHT double precision,
    TRANSPORT_HEIGHT double precision,
    TRANSPORT_LENGTH double precision,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    COST_TON_KILOMETER double precision,
    MAX_WEIGHT double precision,
    MAX_VOLUME double precision,
    MIN_WEIGHT double precision,
    MIN_VOLUME double precision,
    LIMIT_WEIGHT double precision,
    SENSOR_CODE integer,
    SERVICE_POINTS_WITH_RAMP boolean,
    SERVICE_POINTS_WITHOUT_RAMP boolean,
    MAX_SPEED double precision,
    DANGEROUS_CARGO_PERMISSION boolean,
    TEMPERATURE_CONDITIONS boolean,
    TEMPERATURE_RETENTION_TIME integer,
    LOW_TEMPERATURE integer,
    HIGH_TEMPERATURE integer,
    IS_FREE boolean not null,
    TOTAL_EMPTY_TRANSPORT_WEIGHT double precision,
    TOTAL_TRANSPORT_LENGTH double precision,
    TOTAL_TRANSPORT_HEIGHT double precision,
    TOTAL_TRANSPORT_WIDTH double precision,
    TOTAL_MAX_CARGO_VOLUME double precision,
    TOTAL_MAX_CARGO_WEIGHT double precision,
    TOTAL_COST_KILOMETER double precision,
    TOTAL_COST_SUPPLY double precision,
    TOTAL_COST_HOUR double precision,
    TOTAL_COST_TON_KILOMETER double precision,
    BASE_COST_ATTRACTION double precision,
    EXTRA_COST_ATTRACTION double precision,
    TOTAL_BASE_COST_ATTRACTION double precision,
    TOTAL_EXTRA_COST_ATTRACTION double precision,
    MIN_HOUR_NUMBER integer,
    --
    primary key (ID)
)^
-- end ERP_TRANSPORT

-- begin ERP_CURRENCY
create table ERP_CURRENCY (
  ID            uuid,
  VERSION       integer     not null,
  CREATE_TS     timestamp,
  CREATED_BY    varchar(50),
  UPDATE_TS     timestamp,
  UPDATED_BY    varchar(50),
  DELETE_TS     timestamp,
  DELETED_BY    varchar(50),
  --
  CODE          varchar(3)  not null,
  NAME          varchar(50) not null,
  SHIRT_NAME    varchar(3)  not null,
  BASE_CURRENCY boolean,
  COEFFICIENT   double precision,
  --
  primary key (ID)
)^
-- end ERP_CURRENCY
-- begin ERP_CURRENCY_RATE
create table ERP_CURRENCY_RATE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    RATE_DATE timestamp not null,
    RATE double precision not null,
    CURRENCY_ID uuid not null,
    --
    primary key (ID)
)^
-- end ERP_CURRENCY_RATE
-- begin ERP_EMPLOYEE
create table ERP_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    LAST_NAME varchar(255),
    PHONE varchar(255),
    EMAIL varchar(255),
    ROLE_ID uuid not null,
    COMPANY_ID uuid not null,
    USER_ID uuid not null,
    IS_FREE boolean not null,
    --
    primary key (ID)
)^
-- end ERP_EMPLOYEE
-- begin ERP_EMPLOYEE_ROLE
create table ERP_EMPLOYEE_ROLE (
  ID            uuid,
  VERSION       integer      not null,
  CREATE_TS     timestamp,
  CREATED_BY    varchar(50),
  UPDATE_TS     timestamp,
  UPDATED_BY    varchar(50),
  DELETE_TS     timestamp,
  DELETED_BY    varchar(50),
  --
  NAME          varchar(255) not null,
  EMPLOYEE_TYPE integer      not null,
  --
  primary key (ID)
)^
-- end ERP_EMPLOYEE_ROLE
-- begin ERP_TRAILER
create table ERP_TRAILER (
  ID                          uuid,
  VERSION                     integer      not null,
  CREATE_TS                   timestamp,
  CREATED_BY                  varchar(50),
  UPDATE_TS                   timestamp,
  UPDATED_BY                  varchar(50),
  DELETE_TS                   timestamp,
  DELETED_BY                  varchar(50),
  --
  NAME                        varchar(255) not null,
  IDENT_NUMBER                varchar(255) not null,
  GUID                        varchar(255),
  TRAILER_TYPE                integer      not null,
  DOWNLOAD_TYPE               integer      not null,
  ACCESSIBLE_TO_ALL           boolean,
  ACCESSIBLE_TO_OWNER         boolean,
  COMPANY_ID                  uuid         not null,
  CURRENCY_BILLING_ID         uuid,
  FORWARDER_ID                uuid         not null,
  EMPTY_TRAILER_WEIGHT        double precision,
  LENGTH                      double precision,
  WIDTH                       double precision,
  HEIGHT                      double precision,
  COST_KILOMETER              double precision,
  COST_TON_KILOMETER          double precision,
  COST_HOUR                   double precision,
  COST_SUPPLY                 double precision,
  MAX_WEIGHT                  double precision,
  MAX_VOLUME                  double precision,
  MIN_WEIGHT                  double precision,
  MIN_VOLUME                  double precision,
  LIMIT_WEIGHT                double precision,
  SERVICE_POINTS_WITH_RAMP    boolean,
  SERVICE_POINTS_WITHOUT_RAMP boolean,
  TEMPERATURE_CONDITIONS      boolean,
  TEMPERATURE_RETENTION_TIME  integer,
  LOW_TEMPERATURE             integer,
  HIGH_TEMPERATURE            integer,
  BASE_COST_ATTRACTION        double precision,
  EXTRA_COST_ATTRACTION       double precision,
  IS_FREE                     boolean      not null,
  --
  primary key (ID)
)^
-- end ERP_TRAILER
-- begin ERP_CARGO
create table ERP_CARGO (
  ID                  uuid,
  VERSION             integer      not null,
  CREATE_TS           timestamp,
  CREATED_BY          varchar(50),
  UPDATE_TS           timestamp,
  UPDATED_BY          varchar(50),
  DELETE_TS           timestamp,
  DELETED_BY          varchar(50),
  --
  STATUS              integer,
  DOC_COMMENT         text,
  CARGO_TYPE          integer      not null,
  PALLETS_TYPE        integer,
  WEIGHT              double precision,
  VOLUME              double precision,
  NUMBER_OF_PALLETS   integer,
  TEMPERATURE_CARGO   boolean,
  DANGEROUS_CARGO     boolean,
  CLASS_ADR           integer,
  DESIRED_PRICE       double precision,
  SENT_DATE           timestamp,
  SENT_DATE_DELTA     integer,
  POINT_ID            uuid         not null,
  CURRENCY_ID         uuid,
  TRANSPORT_TYPE_ID   uuid,
  COMPANY_ID          uuid         not null,
  MANAGER_ID          uuid         not null,
  CLIENT_ID           uuid         not null,
  CLIENT_CAPTION      varchar(255) not null,
  PHONE               varchar(255) not null,
  ACCESSIBLE_TO_ALL   boolean,
  ACCESSIBLE_TO_OWNER boolean,
  --
  primary key (ID)
)^
-- end ERP_CARGO
-- begin ERP_JOURNEY
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
    JOURNEY_NUMBER varchar(15),
    FINAL_PRICE double precision,
    START_DATE timestamp,
    END_DATE timestamp,
    COMMENT_ varchar(255),
    START_ADDRESS varchar(255),
    END_ADDRESS varchar(255),
    TRANSPORT_ID uuid not null,
    EMPLOYEE_ID uuid,
    CURRENCY_ID uuid,
    FREIGHTER_ID uuid,
    TRANSPORTATION_DISTANCE double precision,
    TRANSPORTATION_TIME integer,
    TRACK text,
    ATTRACTING_PRICE double precision,
    TRANSPORTATION_PRICE double precision,
    RESIDUAL_WEIGHT double precision not null,
    RESIDUAL_VOLUME double precision not null,
    STATUS integer,
    RATING integer,
    MANUAL_JOURNEY boolean not null,
    APPROVED boolean not null,
    --
    primary key (ID)
)^
-- end ERP_JOURNEY
-- begin ERP_JOURNEY_COMPOSITION
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
    TRANSPORT_ID uuid not null,
    JOURNEY_ID uuid,
    POINT_ID uuid not null,
    LOCATION_DATE timestamp,
    --
    primary key (ID)
)^
-- end ERP_JOURNEY_COMPOSITION
-- begin ERP_JOURNEY_CARGO
create table ERP_JOURNEY_CARGO (
  ID             uuid,
  VERSION        integer not null,
  CREATE_TS      timestamp,
  CREATED_BY     varchar(50),
  UPDATE_TS      timestamp,
  UPDATED_BY     varchar(50),
  DELETE_TS      timestamp,
  DELETED_BY     varchar(50),
  --
  JOURNEY_ID     uuid    not null,
  CARGO_ID       uuid,
  DRIVER_COMMENT varchar(255),
  --
  primary key (ID)
)^
-- end ERP_JOURNEY_CARGO
-- begin ERP_TRANSPORT_STATE
create table ERP_TRANSPORT_STATE (
  ID                 uuid,
  VERSION            integer          not null,
  CREATE_TS          timestamp,
  CREATED_BY         varchar(50),
  UPDATE_TS          timestamp,
  UPDATED_BY         varchar(50),
  DELETE_TS          timestamp,
  DELETED_BY         varchar(50),
  --
  TRANSPORT_ID       uuid             not null,
  STATE_DATE         timestamp        not null,
  STATE              integer,
  JOURNEY_ID         uuid,
  LOCATION_ADDRESS   varchar(255),
  LOCATION_LATITUDE  double precision not null,
  LOCATION_LONGITUDE double precision not null,
  DOC_TYPE           integer          not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_STATE
-- begin ERP_TRANSPORT_TRAILERS
create table ERP_TRANSPORT_TRAILERS (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  ORDER_NUMBER integer not null,
  TRANSPORT_ID uuid    not null,
  TRAILER_ID   uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_TRAILERS
-- begin ERP_TRANSPORT_DRIVERS
create table ERP_TRANSPORT_DRIVERS (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  ORDER_NUMBER integer not null,
  TRANSPORT_ID uuid    not null,
  EMPLOYEE_ID  uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_DRIVERS
-- begin ERP_TRANSPORT_FORWARDERS
create table ERP_TRANSPORT_FORWARDERS (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  ORDER_NUMBER integer not null,
  EMPLOYEE_ID  uuid    not null,
  TRANSPORT_ID uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_FORWARDERS
-- begin ERP_TRANSPORT_CREW_MEMBERS
create table ERP_TRANSPORT_CREW_MEMBERS (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  ORDER_NUMBER integer not null,
  TRANSPORT_ID uuid    not null,
  EMPLOYEE_ID  uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_CREW_MEMBERS
-- begin ERP_TRANSPORT_CLASS_ADR
create table ERP_TRANSPORT_CLASS_ADR (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  CLASS_ADR    integer not null,
  TRANSPORT_ID uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORT_CLASS_ADR

-- begin SEC_USER
alter table SEC_USER
  add column IS_ADMIN boolean ^
alter table SEC_USER
  add column COMPANY_ID uuid ^
alter table SEC_USER
  add column DTYPE varchar(100) ^
update SEC_USER
set DTYPE = 'erp$ExtUser'
where DTYPE is null ^
-- end SEC_USER
-- begin ERP_ACTIVITIES_TYPE
create table ERP_ACTIVITIES_TYPE (
  ID         uuid,
  VERSION    integer      not null,
  CREATE_TS  timestamp,
  CREATED_BY varchar(50),
  UPDATE_TS  timestamp,
  UPDATED_BY varchar(50),
  DELETE_TS  timestamp,
  DELETED_BY varchar(50),
  --
  NAME       varchar(255) not null,
  --
  primary key (ID)
)^
-- end ERP_ACTIVITIES_TYPE
-- begin ERP_MONITORING_SETTINGS
create table ERP_MONITORING_SETTINGS (
  ID            uuid,
  VERSION       integer not null,
  CREATE_TS     timestamp,
  CREATED_BY    varchar(50),
  UPDATE_TS     timestamp,
  UPDATED_BY    varchar(50),
  DELETE_TS     timestamp,
  DELETED_BY    varchar(50),
  --
  USER_ID       uuid    not null,
  UPDATE_PERIOD integer not null,
  --
  primary key (ID)
)^
-- end ERP_MONITORING_SETTINGS
-- begin ERP_JOURNEY_STATE
create table ERP_JOURNEY_STATE (
  ID           uuid,
  VERSION      integer   not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  STATE_DATE   timestamp not null,
  USER_ID      uuid      not null,
  JOURNEY_ID   uuid      not null,
  STATUS       integer   not null,
  USER_COMMENT text,
  --
  primary key (ID)
)^
-- end ERP_JOURNEY_STATE
-- begin ERP_TRANSPORTS_ACCESS
create table ERP_TRANSPORTS_ACCESS (
  ID           uuid,
  VERSION      integer not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  TRANSPORT_ID uuid    not null,
  COMPANY_ID   uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_TRANSPORTS_ACCESS

-- begin ERP_CONSTANT
create table ERP_CONSTANT (
  ID             uuid,
  VERSION        integer      not null,
  CREATE_TS      timestamp,
  CREATED_BY     varchar(50),
  UPDATE_TS      timestamp,
  UPDATED_BY     varchar(50),
  DELETE_TS      timestamp,
  DELETED_BY     varchar(50),
  --
  NAME           varchar(255) not null,
  VALUE_TYPE     integer      not null,
  CONSTANT_VALUE varchar(255) not null,
  CONSTANT_KEY   varchar(255) not null,
  --
  primary key (ID)
)^
-- end ERP_CONSTANT
-- begin ERP_ACCESS_TO_CARGO
create table ERP_ACCESS_TO_CARGO (
  ID         uuid,
  VERSION    integer not null,
  CREATE_TS  timestamp,
  CREATED_BY varchar(50),
  UPDATE_TS  timestamp,
  UPDATED_BY varchar(50),
  DELETE_TS  timestamp,
  DELETED_BY varchar(50),
  --
  CARGO_ID   uuid    not null,
  COMPANY_ID uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_ACCESS_TO_CARGO
-- begin ERP_ACCESS_TO_TRAILER
create table ERP_ACCESS_TO_TRAILER (
  ID         uuid,
  VERSION    integer not null,
  CREATE_TS  timestamp,
  CREATED_BY varchar(50),
  UPDATE_TS  timestamp,
  UPDATED_BY varchar(50),
  DELETE_TS  timestamp,
  DELETED_BY varchar(50),
  --
  TRAILER_ID uuid    not null,
  COMPANY_ID uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_ACCESS_TO_TRAILER
-- begin ERP_DEPARTMENT
create table ERP_DEPARTMENT (
  ID         uuid,
  VERSION    integer      not null,
  CREATE_TS  timestamp,
  CREATED_BY varchar(50),
  UPDATE_TS  timestamp,
  UPDATED_BY varchar(50),
  DELETE_TS  timestamp,
  DELETED_BY varchar(50),
  --
  PID_ID     uuid,
  NAME       varchar(255) not null,
  CHIEF_ID   uuid         not null,
  COMPANY_ID uuid         not null,
  --
  primary key (ID)
)^
-- end ERP_DEPARTMENT
-- begin ERP_DEPARTMENT_EMPLOYEE
create table ERP_DEPARTMENT_EMPLOYEE (
  ID            uuid,
  VERSION       integer not null,
  CREATE_TS     timestamp,
  CREATED_BY    varchar(50),
  UPDATE_TS     timestamp,
  UPDATED_BY    varchar(50),
  DELETE_TS     timestamp,
  DELETED_BY    varchar(50),
  --
  DEPARTMENT_ID uuid    not null,
  EMPLOYEE_ID   uuid    not null,
  --
  primary key (ID)
)^
-- end ERP_DEPARTMENT_EMPLOYEE
-- begin ERP_MUTUAL_SETTLEMENTS
create table ERP_MUTUAL_SETTLEMENTS (
    ID uuid,
    --
    TRANSPORT_OWNER_ID uuid not null,
    PAY_DATE timestamp not null,
    VALUE_ double precision not null,
    JOURNEY_ID uuid,
    --
    primary key (ID)
)^
-- end ERP_MUTUAL_SETTLEMENTS
-- begin ERP_CLIENT_CREDIT
create table ERP_CLIENT_CREDIT (
    ID uuid,
    --
    TRANSPORT_OWNER_ID uuid not null,
    COUNTERPARTY_ID uuid not null,
    PAY_DATE timestamp not null,
    VALUE_ double precision not null,
    JOURNEY_ID uuid,
    --
    primary key (ID)
)^
-- end ERP_CLIENT_CREDIT
-- begin ERP_SERVICES_PAYMENT
create table ERP_SERVICES_PAYMENT (
  ID              uuid,
  VERSION         integer          not null,
  CREATE_TS       timestamp,
  CREATED_BY      varchar(50),
  UPDATE_TS       timestamp,
  UPDATED_BY      varchar(50),
  DELETE_TS       timestamp,
  DELETED_BY      varchar(50),
  --
  DATE_PAYMENT    timestamp        not null,
  COUNTERPARTY_ID uuid,
  NUMBER_PAYMENT  varchar(255)     not null,
  COMPANY_ID      uuid             not null,
  TYPE_PAYMENT    integer          not null,
  COMMENT_        varchar(255),
  VALUE_          double precision not null,
  --
  primary key (ID)
)^
-- end ERP_SERVICES_PAYMENT
-- begin ERP_POINT
create table ERP_POINT (
  ID           uuid,
  VERSION      integer          not null,
  CREATE_TS    timestamp,
  CREATED_BY   varchar(50),
  UPDATE_TS    timestamp,
  UPDATED_BY   varchar(50),
  DELETE_TS    timestamp,
  DELETED_BY   varchar(50),
  --
  CODE         integer,
  ADDRESS      varchar(255)     not null,
  COUNTRY      varchar(255)     not null,
  CITY         varchar(255)     not null,
  STREET       varchar(255),
  BUILDING     varchar(255),
  LATITUDE     double precision not null,
  LONGITUDE    double precision not null,
  SERVICE_TIME integer,
  --
  primary key (ID)
)^
-- end ERP_POINT
-- begin ERP_CARGO_DELIVERY_POINT
create table ERP_CARGO_DELIVERY_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DELIVERY_DATE timestamp not null,
    DELIVERY_ORDER integer,
    DELIVERY_DATE_DELTA integer,
    POINT_ID uuid not null,
    WEIGHT double precision,
    VOLUME double precision,
    NUMBER_OF_PALLETS integer,
    CARGO_ID uuid not null,
    --
    primary key (ID)
)^
-- end ERP_CARGO_DELIVERY_POINT
-- begin ERP_DELIVERY
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
    STATUS integer not null,
    DISTANCE double precision,
    TRANSPORTATION_TIME integer,
    TRANSPORTATION_PRICE double precision,
    APPROVED boolean,
    START_DATE timestamp,
    END_DATE timestamp,
    START_ADDRESS varchar(255),
    END_ADDRESS varchar(255),
    --
    primary key (ID)
)^
-- end ERP_DELIVERY
-- begin ERP_DELIVERY_COMPOSITION
create table ERP_DELIVERY_COMPOSITION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DELIVERY_ID uuid not null,
    POINT_ID uuid not null,
    DELIVERY_DATE timestamp not null,
    SERVICE_TIME integer,
    DOWN_TIME integer,
    DISTANCE double precision,
    DURATION integer,
    STATUS integer not null,
    --
    primary key (ID)
)^
-- end ERP_DELIVERY_COMPOSITION
-- begin ERP_POLYGON_MAP
create table ERP_POLYGON_MAP (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    MIN_LAT double precision,
    MIN_LON double precision,
    MAX_LAT double precision,
    MAX_LON double precision,
    --
    primary key (ID)
)^
-- end ERP_POLYGON_MAP
-- begin ERP_POLYGON_POINT
create table ERP_POLYGON_POINT (
  ID uuid,
  --
  LAT double precision not null,
  LON double precision not null,
  POLYGON_ID uuid not null,
  POINT_ORDER integer,
  --
  primary key (ID)
)^
-- end ERP_POLYGON_POINT
-- begin ERP_ENTRY_POINT
create table ERP_ENTRY_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LAT double precision not null,
    LON double precision not null,
    POLYGON_MAP_ID uuid not null,
    --
    primary key (ID)
)^
-- end ERP_ENTRY_POINT
-- begin ERP_CATEGORY
create table ERP_CATEGORY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    COMPANY_ID uuid not null,
    MIN_HOUR_NUMBER integer,
    WEIGHT double precision,
    VOLUME double precision,
    NUMBER_OF_PALLETS integer,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    --
    primary key (ID)
)^
-- end ERP_CATEGORY
-- begin ERP_CATEGORY_AREA
create table ERP_CATEGORY_AREA (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CATEGORY_ID uuid not null,
    POLYGON_ID uuid not null,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    COST_ENTRANCE_PENALTY double precision,
    COST_EXIT_PENALTY double precision,
    --
    primary key (ID)
)^
-- end ERP_CATEGORY_AREA
-- begin ERP_TRANSPORT_AREA
create table ERP_TRANSPORT_AREA (
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
    POLYGON_ID uuid not null,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    COST_ENTRANCE_PENALTY double precision,
    COST_EXIT_PENALTY double precision,
    --
    primary key (ID)
)^
-- end ERP_TRANSPORT_AREA
-- begin ERP_POLYGON_SEGMENT
create table ERP_POLYGON_SEGMENT (
    ID uuid,
    --
    POLYGON_ID uuid not null,
    START_ID uuid not null,
    END_ID uuid not null,
    MIN_LAT double precision,
    MIN_LON double precision,
    MAX_LAT double precision,
    MAX_LON double precision,
    --
    primary key (ID)
)^
-- end ERP_POLYGON_SEGMENT
