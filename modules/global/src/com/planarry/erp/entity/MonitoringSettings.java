
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_MONITORING_SETTINGS")
@Entity(name = "erp$MonitoringSettings")
public class MonitoringSettings extends StandardEntity {
    private static final long serialVersionUID = 903683354203400707L;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private ExtUser user;

    @NotNull
    @Column(name = "UPDATE_PERIOD", nullable = false)
    private Integer updatePeriod;

    public void setUser(ExtUser user) {
        this.user = user;
    }

    public ExtUser getUser() {
        return user;
    }

    public void setUpdatePeriod(Integer updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public Integer getUpdatePeriod() {
        return updatePeriod;
    }


}