package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.CaseConversion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

@NamePattern("%s|name")
@Table(name = "ERP_COMPANY")
@Entity(name = "erp$Company")
public class Company extends StandardEntity implements ErpEntity {
    private static final long serialVersionUID = -2831011690326422030L;

    @NotNull
    @CaseConversion
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "PREFIX", nullable = false, unique = true, length = 3)
    private String prefix;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "ADDRESS")
    private String address;

    @NotNull
    @Column(name = "COMPANY_TYPE", nullable = false)
    private Integer type;

    @NotNull
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "AUTO_CREATION", nullable = false)
    private Boolean autoCreation = false;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETURN_AREA_ID")
    private PolygonMap returnArea;

    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACTIVITIES_TYPE_ID")
    private ActivitiesType activitiesType;

    @NotNull
    @Column(name = "TRANSPORT_SEARCH_NARROW_RADIUS", nullable = false)
    private Integer transportSearchNarrowRadius;

    @NotNull
    @Column(name = "TRANSPORT_SEARCH_WIDE_RADIUS", nullable = false)
    private Integer transportSearchWideRadius;

    @Column(name = "ROUTER")
    private String router;

    public ERouterType getRouter() {
        return router == null ? null : ERouterType.fromId(router);
    }

    public void setRouter(ERouterType router) {
        this.router = router == null ? null : router.getId();
    }

    public void setReturnArea(PolygonMap returnArea) {
        this.returnArea = returnArea;
    }

    public PolygonMap getReturnArea() {
        return returnArea;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }


    public void setTransportSearchNarrowRadius(Integer transportSearchNarrowRadius) {
        this.transportSearchNarrowRadius = transportSearchNarrowRadius;
    }

    public Integer getTransportSearchNarrowRadius() {
        return transportSearchNarrowRadius;
    }

    public void setTransportSearchWideRadius(Integer transportSearchWideRadius) {
        this.transportSearchWideRadius = transportSearchWideRadius;
    }

    public Integer getTransportSearchWideRadius() {
        return transportSearchWideRadius;
    }


    public void setAutoCreation(Boolean autoCreation) {
        this.autoCreation = autoCreation;
    }

    public Boolean getAutoCreation() {
        return autoCreation;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setActivitiesType(ActivitiesType activitiesType) {
        this.activitiesType = activitiesType;
    }

    public ActivitiesType getActivitiesType() {
        return activitiesType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EPersonTypeItems getType() {
        return type == null ? null : EPersonTypeItems.fromId(type);
    }

    public void setType(EPersonTypeItems type) {
        this.type = type == null ? null : type.getId();
    }
}
