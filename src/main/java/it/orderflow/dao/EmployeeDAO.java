package it.orderflow.dao;

import it.orderflow.model.Employee;
import it.orderflow.model.UserRole;

import java.util.List;

public interface EmployeeDAO extends TransactionControl<Employee> {

    Employee loadEmployee(String email) throws Exception;

    List<Employee> loadByRole(UserRole role) throws Exception;

    List<Employee> loadAll() throws Exception;

    boolean isEmpty() throws Exception;

}
