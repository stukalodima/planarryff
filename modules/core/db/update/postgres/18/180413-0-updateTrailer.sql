UPDATE ERP_TRAILER SET
FORWARDER_ID = (SELECT EMPLOYEE_ID FROM ERP_TRAILER_FORWARD_TABLE WHERE TRAILER_ID = ERP_TRAILER.ID);