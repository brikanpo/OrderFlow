package it.OrderFlow.DAO.Cache;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.EmployeeDAO;
import it.OrderFlow.Exceptions.CacheIntegrityException;
import it.OrderFlow.Model.Employee;
import it.OrderFlow.Model.UserRole;

import java.util.List;
import java.util.UUID;

public class CacheEmployeeDAO extends CacheGeneralDAO<Employee> implements EmployeeDAO {

    private UUID getEmployeeId(Employee employee) {
        return employee.getId();
    }

    private String getEmployeeEmail(Employee employee) {
        return employee.getEmail();
    }

    private UserRole getEmployeeRole(Employee employee) {
        return employee.getUserRole();
    }

    private Employee copy(Employee employee) {
        return employee.clone();
    }

    private Employee findEmployee(UUID id) {
        return this.findFromCache(id, this::getEmployeeId);
    }

    private void saveNewEmployee(Employee employee) {
        this.saveNewEntity(employee, this::loadEmployee, this::getEmployeeEmail, this::copy);
    }

    private void updateEmployee(Employee employee) {
        this.updateEntity(employee, this::findEmployee, this::getEmployeeId);
    }

    private void deleteEmployee(Employee employee) {
        this.deleteEntity(employee, this::findEmployee, this::getEmployeeId);
    }

    @Override
    public Employee loadEmployee(String email) {
        return this.findFromCache(email, this::getEmployeeEmail);
    }

    @Override
    public List<Employee> loadByRole(UserRole role) {
        return this.findMatchesFromCache(role, this::getEmployeeRole);
    }

    @Override
    public List<Employee> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public boolean isEmpty() {
        return this.getCache().isEmpty();
    }

    @Override
    public void executeTransaction(List<Statement<Employee>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewEmployee, this::updateEmployee,
                this::deleteEmployee);
    }

    @Override
    public void keepIntegrity(List<Statement<Employee>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewEmployee, this::updateEmployee,
                this::deleteEmployee);
    }
}
