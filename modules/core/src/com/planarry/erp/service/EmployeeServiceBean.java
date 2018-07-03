
package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Employee;
import com.planarry.erp.entity.EmployeeRole;
import com.planarry.erp.entity.ExtUser;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service(EmployeeService.NAME)
public class EmployeeServiceBean implements EmployeeService {

    @Inject
    private Persistence persistence;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private DataManager dataManager;

    public Employee getEmployeeByUser(ExtUser user){
        Employee employee;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Employee e WHERE e.user.id = :user";
            TypedQuery<Employee> query = persistence.getEntityManager().createQuery(queryStr, Employee.class);
            query.setViewName("_minimal");
            query.setParameter("user", user);
            employee = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            employee = null;
        }
        return employee;
    }

    @Override
    public Employee getEmployeeByCurrentUser() {
        ExtUser user = (ExtUser)userSessionSource.getUserSession().getUser();
        return getEmployeeByUser(user);
    }

    @Override
    public List<Employee> getEmployeesByUserCompany() {
        List<Employee> employeeList;
        try (Transaction transaction = persistence.createTransaction()) {
            ExtUser currentUser = dataManager.reload((ExtUser) userSessionSource.getUserSession().getUser(), "extUser-view");
            Company company = currentUser.getCompany();

            String queryStr = "SELECT e FROM erp$Employee e WHERE e.company.id = :company";
            TypedQuery<Employee> query = persistence.getEntityManager().createQuery(queryStr, Employee.class);
            query.setViewName("_minimal");
            query.setParameter("company", company);
            employeeList = query.getResultList();
            transaction.commit();
        }
        return employeeList;
    }

    @Override
    public EmployeeRole getEmployeeRoleById(String id) {
        EmployeeRole role;
        UUID roleUuid = UUID.fromString(id);
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$EmployeeRole e WHERE e.id = :roleId";
            TypedQuery<EmployeeRole> query = persistence.getEntityManager().createQuery(queryStr, EmployeeRole.class);
            query.setViewName("_minimal");
            query.setParameter("roleId", roleUuid);
            role = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            role = null;
        }
        return role;
    }
}