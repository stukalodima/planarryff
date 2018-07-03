/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Table(name = "ERP_TRANSPORT_TRAILERS")
@Entity(name = "erp$TransportTrailers")
public class TransportTrailers extends StandardEntity implements OrderedTableEntity {
    private static final long serialVersionUID = 968036785133886738L;

    @Column(name = "ORDER_NUMBER", nullable = false)
    private Integer order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRAILER_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Trailer trailer;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }



    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }



    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    @Override
    public void setTargetEntity(TransportEquipmentTargetEntity entity) {
        setTrailer((Trailer) entity);
    }

    @Override
    public TransportEquipmentTargetEntity getTargetEntity() {
        return getTrailer();
    }
}