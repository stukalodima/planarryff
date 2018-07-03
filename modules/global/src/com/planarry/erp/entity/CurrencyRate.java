package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "ERP_CURRENCY_RATE")
@Entity(name = "erp$CurrencyRate")
public class CurrencyRate extends StandardEntity {
    private static final long serialVersionUID = -2441820420272790380L;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "RATE_DATE", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "RATE", nullable = false)
    private Double rate;

    @NotNull
    @JoinColumn(name = "CURRENCY_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Currency currency;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
