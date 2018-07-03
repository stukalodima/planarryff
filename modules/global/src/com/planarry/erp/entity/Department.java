/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Dima Stukalo
 * @author Aleksandr Iushko
 */
@NamePattern("%s|name")
@Table(name = "ERP_DEPARTMENT")
@Entity(name = "erp$Department")
public class Department extends StandardEntity {
    private static final long serialVersionUID = 7669520604427698087L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PID_ID")
    private Department pid;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "department")
    private List<DepartmentEmployee> employeeDepartment;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CHIEF_ID")
    private Employee chief;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public Department getPid() {
        return pid;
    }

    public void setPid(Department pid) {
        this.pid = pid;
    }


    public void setEmployeeDepartment(List<DepartmentEmployee> employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public List<DepartmentEmployee> getEmployeeDepartment() {
        return employeeDepartment;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChief(Employee chief) {
        this.chief = chief;
    }

    public Employee getChief() {
        return chief;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }


}