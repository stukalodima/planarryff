package com.planarry.erp.web.extuser;

import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.web.auth.WebAuthConfig;

import javax.inject.Inject;

public class ExtUserEditCompanion implements ExtUserEdit.Companion {

    @Inject
    protected WebAuthConfig config;

    @Override
    public void initPasswordField(PasswordField passwordField) {
        passwordField.setRequired(config.getRequirePasswordForNewUsers());
    }
}
