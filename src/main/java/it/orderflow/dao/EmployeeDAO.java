package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.Employee;
import it.orderflow.model.UserRole;

import java.util.List;

public interface EmployeeDAO extends TransactionControl<Employee> {

    Employee loadEmployee(String email) throws PersistenceException;

    List<Employee> loadByRole(UserRole role) throws PersistenceException;

    List<Employee> loadAll() throws PersistenceException;

    boolean isEmpty() throws PersistenceException;

}
