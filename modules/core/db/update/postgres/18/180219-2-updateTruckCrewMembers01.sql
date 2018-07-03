update ERP_TRUCK_CREW_MEMBERS set ORDER_NUMBER = 1 where ORDER_NUMBER is null ;
alter table ERP_TRUCK_CREW_MEMBERS alter column ORDER_NUMBER set not null ;
-- update ERP_TRUCK_CREW_MEMBERS set TRUCK_EQUIPMENT_ID = <default_value> where TRUCK_EQUIPMENT_ID is null ;
alter table ERP_TRUCK_CREW_MEMBERS alter column TRUCK_EQUIPMENT_ID set not null ;
-- update ERP_TRUCK_CREW_MEMBERS set EMPLOYEE_ID = <default_value> where EMPLOYEE_ID is null ;
alter table ERP_TRUCK_CREW_MEMBERS alter column EMPLOYEE_ID set not null ;
