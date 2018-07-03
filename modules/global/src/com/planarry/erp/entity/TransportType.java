package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamePattern("%s|name")
@Table(name = "ERP_TRANSPORT_TYPE")
@Entity(name = "erp$TransportType")
public class TransportType extends StandardEntity {
    private static final long serialVersionUID = -1662988746766777053L;

    @JoinColumn(name = "PID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.DENY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    private TransportType category;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    public TransportType getCategory() {
        return category;
    }

    public void setCategory(TransportType category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoot(){
        return category == null;
    }
}
