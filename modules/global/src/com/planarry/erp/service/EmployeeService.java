
package com.planarry.erp.service;

import com.planarry.erp.entity.Employee;
import com.planarry.erp.entity.EmployeeRole;
import com.planarry.erp.entity.ExtUser;

import java.util.List;

public interface EmployeeService {
    String NAME = "erp_EmployeeService";

    Employee getEmployeeByUser(ExtUser user);

    Employee getEmployeeByCurrentUser();

    List<Employee> getEmployeesByUserCompany();

    EmployeeRole getEmployeeRoleById(String roleId);
}