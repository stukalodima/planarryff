/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

/**
* @author Iushko Aleksandr
*/
@Table(name = "ERP_SERVICES_PAYMENT")
@Entity(name = "erp$ServicesPayment")
public class ServicesPayment extends StandardEntity {
    private static final long serialVersionUID = 7186896443939345637L;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DATE_PAYMENT", nullable = false)
    private Date datePayment;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTERPARTY_ID")
    private Company counterparty;

    @NotNull
    @Column(name = "NUMBER_PAYMENT", nullable = false)
    private String numberPayment;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @NotNull
    @Column(name = "TYPE_PAYMENT", nullable = false)
    private Integer typePayment;

    @Column(name = "COMMENT_")
    private String comment;

    @NotNull
    @Column(name = "VALUE_", nullable = false)
    private Double value;

    public void setCounterparty(Company counterparty) {
        this.counterparty = counterparty;
    }

    public Company getCounterparty() {
        return counterparty;
    }


    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setNumberPayment(String numberPayment) {
        this.numberPayment = numberPayment;
    }

    public String getNumberPayment() {
        return numberPayment;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setTypePayment(ETypePayment typePayment) {
        this.typePayment = typePayment == null ? null : typePayment.getId();
    }

    public ETypePayment getTypePayment() {
        return typePayment == null ? null : ETypePayment.fromId(typePayment);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }


}