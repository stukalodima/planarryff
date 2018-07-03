
package com.planarry.erp.web.profitability;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.reports.gui.ReportGuiManager;
import com.haulmont.reports.gui.actions.RunReportAction;

import javax.inject.Inject;
import java.util.Map;

public class ProfitabilityReport extends AbstractWindow {

    @Inject
    private Button reportBtn;

    @Inject
    private ReportGuiManager reportGuiManager;

    @Override
    public void init(Map<String, Object> params) {
        reportBtn.setAction(new RunReportAction("report"));
    }

    public void onClear() {

    }

    public void onReport() {
        //reportGuiManager.runReport();
    }
}