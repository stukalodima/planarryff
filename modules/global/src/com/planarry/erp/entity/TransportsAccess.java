package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Aleksandr Iushko
 */
@Table(name = "ERP_TRANSPORTS_ACCESS")
@Entity(name = "erp$TransportsAccess")
public class TransportsAccess extends StandardEntity implements AccessEntity {
    private static final long serialVersionUID = 7945556289740625955L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
