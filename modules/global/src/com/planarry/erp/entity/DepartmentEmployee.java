/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Dima Stukalo
 * @author Aleksandr Iushko
 */
@NamePattern("%s (%s)|employee,department")
@Table(name = "ERP_DEPARTMENT_EMPLOYEE")
@Entity(name = "erp$DepartmentEmployee")
public class DepartmentEmployee extends StandardEntity {
    private static final long serialVersionUID = -3728722740507860972L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }


}