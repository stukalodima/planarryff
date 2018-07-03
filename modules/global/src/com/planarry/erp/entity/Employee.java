
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.Index;

@NamePattern("%s %s|name,company")
@Table(name = "ERP_EMPLOYEE", indexes = {
    @Index(name = "IDX_ERP_EMPLOYEE_IS_FREE", columnList = "IS_FREE")
})
@Entity(name = "erp$Employee")
public class Employee extends StandardEntity implements TransportEquipmentTargetEntity {
    private static final long serialVersionUID = 1331454748154705772L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID")
    private EmployeeRole role;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @Column(name = "IS_FREE", nullable = false)
    private Boolean isFree = true;


    @Override
    public Boolean getIsFree() {
        return isFree;
    }

    @Override
    public void setIsFree(Boolean free) {
        isFree = free;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public EmployeeRole getRole() {
        return role;
    }


}