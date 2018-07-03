
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.Transient;

@Table(name = "ERP_TRANSPORT_STATE")
@Entity(name = "erp$TransportState")
public class TransportState extends StandardEntity implements MonitoringEntity, UpdatableMonitoringEntity {
    private static final long serialVersionUID = -735396685406147505L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Transport transport;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATE_DATE", nullable = false)
    private Date stateDate;

    @Column(name = "STATE")
    private Integer state;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    @Column(name = "LOCATION_ADDRESS")
    private String locationAddress;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LOCATION_LATITUDE", nullable = false)
    private Double locationLatitude;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LOCATION_LONGITUDE", nullable = false)
    private Double locationLongitude;

    @NotNull
    @Column(name = "DOC_TYPE", nullable = false)
    private Integer docType;

    @Transient
    @MetaProperty
    private Boolean flag = false;

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getFlag() {
        return flag;
    }


    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setDocType(ETransportStateDocTypeItems docType) {
        this.docType = docType == null ? null : docType.getId();
    }

    public ETransportStateDocTypeItems getDocType() {
        return docType == null ? null : ETransportStateDocTypeItems.fromId(docType);
    }



    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }


    public void setState(ETransportStateItems state) {
        this.state = state == null ? null : state.getId();
    }

    public ETransportStateItems getState() {
        return state == null ? null : ETransportStateItems.fromId(state);
    }


    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return journey;
    }


}