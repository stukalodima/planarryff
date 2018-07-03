package com.planarry.erp.web.utils;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.web.App;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.gui.ReportGuiManager;
import com.planarry.erp.entity.Constant;
import com.planarry.erp.service.ConstantService;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Aleksandr Iushko
 */
@Component(RunReportBean.NAME)
public class RunReportBean {

    public static final String NAME = "cuba_RunReportBean";

    @Named(value = "cuba_ReportGuiManager")
    private ReportGuiManager reportGuiManager;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(ConstantService.NAME)
    private ConstantService constantService;

    /**
     * Run mutualSettlement report.
     * Gets name report from constant "mutualSettlement".
     */
    public ArrayList<String> runReportMutualSettlement(ArrayList<String> error) {
        Constant mutualSettlement = constantService.getConstant("mutualSettlement");
        if (mutualSettlement != null && mutualSettlement.getValue() != null) {
            String strQuery = "SELECT e FROM report$Report e WHERE e.name like '%" + mutualSettlement.getValue() + "%'";
            runReport(strQuery, error);
        } else {
            error.add("Don`t found your constant");
        }
        return error;
    }

    /**
     * Run clientCredit report.
     * Gets name report from constant "clientCredit".
     */
    public ArrayList<String> runReportClientCredit(ArrayList<String> error) {
        Constant clientCredit = constantService.getConstant("clientCredit");
        if (clientCredit != null && clientCredit.getValue() != null) {
            String strQuery = "SELECT e FROM report$Report e WHERE e.name like '%" + clientCredit.getValue() + "%'";
            runReport(strQuery, error);
        } else {
            error.add("Don`t found your constant");
        }
        return error;
    }

    /**
     * @param strQuery is jpql query for load necessary report.
     */
    private void runReport(String strQuery, ArrayList<String> error) {
        try {
            LoadContext<Report> loadContext = LoadContext.create(Report.class)
                    .setQuery(LoadContext.createQuery(strQuery))
                    .setView("report.edit");

            Report report = dataManager.load(loadContext);
            if (Objects.isNull(report)) {
                error.add("Don`t found your constant");
                return;
            }
            Frame window = App.getInstance().getTopLevelWindow();

            reportGuiManager.runReport(report, window);
        } catch (Exception e) {
            error.add(e.getLocalizedMessage());
        }
    }
}
