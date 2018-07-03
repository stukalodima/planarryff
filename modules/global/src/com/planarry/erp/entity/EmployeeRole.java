
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "ERP_EMPLOYEE_ROLE")
@Entity(name = "erp$EmployeeRole")
public class EmployeeRole extends StandardEntity {
    private static final long serialVersionUID = -6220026957063528674L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "EMPLOYEE_TYPE", nullable = false)
    private Integer type;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(EEmployeeTypeItems type) {
        this.type = type == null ? null : type.getId();
    }

    public EEmployeeTypeItems getType() {
        return type == null ? null : EEmployeeTypeItems.fromId(type);
    }


}