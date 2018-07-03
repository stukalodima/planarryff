
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|classADR")
@Table(name = "ERP_TRANSPORT_CLASS_ADR")
@Entity(name = "erp$TransportClassADR")
public class TransportClassADR extends StandardEntity {
    private static final long serialVersionUID = 8006712654938436130L;

    @NotNull
    @Column(name = "CLASS_ADR", nullable = false)
    private Integer classADR;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    public void setClassADR(ECargoADRTypeItems classADR) {
        this.classADR = classADR == null ? null : classADR.getId();
    }

    public ECargoADRTypeItems getClassADR() {
        return classADR == null ? null : ECargoADRTypeItems.fromId(classADR);
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


}