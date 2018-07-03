package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_ACCESS_TO_TRAILER")
@Entity(name = "erp$AccessToTrailer")
public class AccessToTrailer extends StandardEntity implements AccessEntity {
    private static final long serialVersionUID = -8297489486770861620L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRAILER_ID")
    private Trailer trailer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }


}