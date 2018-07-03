
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
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
import javax.persistence.Lob;

@Table(name = "ERP_JOURNEY_STATE")
@Entity(name = "erp$JourneyState")
public class JourneyState extends StandardEntity {
    private static final long serialVersionUID = -3299206406756835294L;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "STATE_DATE", nullable = false)
    private Date stateDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private ExtUser user;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Lob
    @Column(name = "USER_COMMENT")
    private String comment;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }


    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setUser(ExtUser user) {
        this.user = user;
    }

    public ExtUser getUser() {
        return user;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setStatus(EStatusItems status) {
        this.status = status == null ? null : status.getId();
    }

    public EStatusItems getStatus() {
        return status == null ? null : EStatusItems.fromId(status);
    }


}