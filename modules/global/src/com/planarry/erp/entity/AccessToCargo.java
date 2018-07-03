package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_ACCESS_TO_CARGO")
@Entity(name = "erp$AccessToCargo")
public class AccessToCargo extends StandardEntity implements AccessEntity {
    private static final long serialVersionUID = 7852508202215666181L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

}