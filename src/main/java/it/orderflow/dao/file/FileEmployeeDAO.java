package it.orderflow.dao.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.orderflow.control.Statement;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.exceptions.EntityException;
import it.orderflow.exceptions.FilePersistenceException;
import it.orderflow.model.Employee;
import it.orderflow.model.UserRole;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileEmployeeDAO implements EmployeeDAO {

    private static final String FILE_PATH = "Employees.json";
    private final Gson gson;

    private List<Employee> employees;
    private boolean isUnmodified;

    public FileEmployeeDAO() {
        this.gson = new Gson();
        this.employees = new ArrayList<>();
        this.isUnmodified = false;
    }

    private List<Employee> getCache() {
        return this.employees;
    }

    private Employee findByIdFromCache(UUID id) {
        for (Employee employee : this.getCache()) {
            if (employee.getId().equals(id)) return employee;
        }
        return null;
    }

    private Employee findByEmailFromCache(String email) {
        for (Employee employee : this.getCache()) {
            if (employee.getEmail().equals(email)) return employee;
        }
        return null;
    }

    private Employee findByIdFromPersistence(UUID id) throws FilePersistenceException {
        for (Employee employee : this.loadAll()) {
            if (employee.getId().equals(id)) return employee;
        }
        return null;
    }

    private Employee findByEmailFromPersistence(String email) throws FilePersistenceException {
        for (Employee employee : this.loadAll()) {
            if (employee.getEmail().equals(email)) return employee;
        }
        return null;
    }

    private Employee findEmployee(UUID id) throws FilePersistenceException {
        Employee employee = this.findByIdFromCache(id);
        if (employee == null) {
            employee = this.findByIdFromPersistence(id);
            if (employee != null) {
                this.getCache().add(employee);
            }
        }
        return employee;
    }

    private void saveNewEmployee(Employee employee) throws FilePersistenceException {
        Employee employeeById = loadEmployee(employee.getEmail());

        if (employeeById == null) {
            this.getCache().add(employee.copy());
            this.saveToFile();
        }
    }

    private void updateEmployee(Employee employee) throws FilePersistenceException {
        Employee employeeById = this.findEmployee(employee.getId());

        if (employeeById != null) {
            this.getCache().set(this.getCache().indexOf(employeeById), employee);
            this.saveToFile();
        }
    }

    private void deleteEmployee(Employee employee) throws FilePersistenceException {
        Employee employeeById = this.findEmployee(employee.getId());

        if (employeeById != null) {
            this.getCache().remove(employeeById);
            this.saveToFile();
        }
    }

    private void saveToFile() throws FilePersistenceException {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(this.getCache(), writer);
            this.isUnmodified = false;
        } catch (IOException e) {
            throw new FilePersistenceException(FilePersistenceException.ErrorType.WRITE, e);
        }
    }

    @Override
    public Employee loadEmployee(String email) throws FilePersistenceException {
        Employee employee = this.findByEmailFromCache(email);
        if (employee == null) {
            employee = this.findByEmailFromPersistence(email);
            if (employee != null) {
                this.getCache().add(employee);
            }
        }
        return employee;
    }

    @Override
    public List<Employee> loadByRole(UserRole role) throws FilePersistenceException {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : this.loadAll()) {
            if (employee.getUserRole() == role) result.add(employee);
        }
        return result;
    }

    @Override
    public List<Employee> loadAll() throws FilePersistenceException {
        if (this.isUnmodified) {
            return this.getCache();
        } else {
            File file = new File(FILE_PATH);
            if (!file.exists())
                throw new FilePersistenceException(FilePersistenceException.ErrorType.NOT_FOUND, EntityException.Entity.EMPLOYEE);

            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<Employee>>() {
                }.getType();
                this.employees = gson.fromJson(reader, listType);
                this.isUnmodified = true;
                return this.employees;
            } catch (IOException e) {
                throw new FilePersistenceException(FilePersistenceException.ErrorType.READ, e);
            }
        }

    }

    @Override
    public boolean isEmpty() throws FilePersistenceException {
        return this.loadAll().isEmpty();
    }

    private void rollback(List<Statement<Employee>> statements) throws FilePersistenceException {
        try {
            for (Statement<Employee> statement : statements.reversed()) {
                if (statement.isCompleted()) {
                    switch (statement.getStatementType()) {
                        case DELETE -> saveNewEmployee(statement.getNewEntity());
                        case UPDATE -> updateEmployee(statement.getOldEntity());
                        case SAVE -> deleteEmployee(statement.getNewEntity());
                    }

                    statement.reverted();
                }
            }
            // all completed statement reverted
        } catch (Exception e) {
            throw new FilePersistenceException(FilePersistenceException.ErrorType.INTEGRITY, e);
        }
    }

    @Override
    public void executeTransaction(List<Statement<Employee>> statements) throws FilePersistenceException {
        try {
            for (Statement<Employee> statement : statements) {
                switch (statement.getStatementType()) {
                    case SAVE -> saveNewEmployee(statement.getNewEntity());
                    case UPDATE -> updateEmployee(statement.getNewEntity());
                    case DELETE -> deleteEmployee(statement.getNewEntity());
                }

                statement.completed();
            }
            // all statements successful
        } catch (FilePersistenceException e) {
            // one statement failed. Rollback to previous state
            this.rollback(statements);
        }
    }

    @Override
    public void keepIntegrity(List<Statement<Employee>> statements) throws FilePersistenceException {
        // check if all the statement of this dao have already been completed
        boolean result = true;
        for (Statement<Employee> statement : statements) {
            if (!statement.isCompleted()) {
                result = false;
                break;
            }
        }
        // if they have all been completed we have to revert them in the reversed order
        if (result) {
            this.rollback(statements);
        }
    }
}
