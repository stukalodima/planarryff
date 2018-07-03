
package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;

@Entity(name = "erp$ExtUser")
@Extends(User.class)
public class ExtUser extends User {
    private static final long serialVersionUID = 2310826382413133777L;

    @Column(name = "IS_ADMIN")
    private Boolean isAdmin = false;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAdmin() {
        return isAdmin == null ? false : isAdmin;
    }

}