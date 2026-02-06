package it.OrderFlow.DAO.File;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.EmployeeDAO;
import it.OrderFlow.Model.Employee;
import it.OrderFlow.Model.UserRole;

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

    private Employee findByIdFromPersistence(UUID id) throws Exception {
        for (Employee employee : this.loadAll()) {
            if (employee.getId().equals(id)) return employee;
        }
        return null;
    }

    private Employee findByEmailFromPersistence(String email) throws Exception {
        for (Employee employee : this.loadAll()) {
            if (employee.getEmail().equals(email)) return employee;
        }
        return null;
    }

    private Employee findEmployee(UUID id) throws Exception {
        Employee employee = this.findByIdFromCache(id);
        if (employee == null) {
            employee = this.findByIdFromPersistence(id);
            if (employee != null) {
                this.getCache().add(employee);
            }
        }
        return employee;
    }

    private void saveNewEmployee(Employee employee) throws Exception {
        Employee employeeById = null;
        try {
            employeeById = loadEmployee(employee.getEmail());
        } catch (Exception ignore) {
        }
        if (employeeById == null) {
            this.getCache().add(employee.clone());
            this.saveToFile();
        }
    }

    private void updateEmployee(Employee employee) throws Exception {
        Employee employeeById = this.findEmployee(employee.getId());

        if (employeeById != null) {
            this.getCache().set(this.getCache().indexOf(employeeById), employee);
            this.saveToFile();
        }
    }

    private void deleteEmployee(Employee employee) throws Exception {
        Employee employeeById = this.findEmployee(employee.getId());

        if (employeeById != null) {
            this.getCache().remove(employeeById);
            this.saveToFile();
        }
    }

    private void saveToFile() throws Exception {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(this.getCache(), writer);
            this.isUnmodified = false;
        } catch (IOException e) {
            throw new Exception("Errore salvataggio");
        }
    }

    @Override
    public Employee loadEmployee(String email) throws Exception {
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
    public List<Employee> loadByRole(UserRole role) throws Exception {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : this.loadAll()) {
            if (employee.getUserRole() == role) result.add(employee);
        }
        return result;
    }

    @Override
    public List<Employee> loadAll() throws Exception {
        if (this.isUnmodified) {
            return this.getCache();
        } else {
            File file = new File(FILE_PATH);
            if (!file.exists()) throw new Exception("File non esiste");

            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<Employee>>() {
                }.getType();
                this.employees = gson.fromJson(reader, listType);
                this.isUnmodified = true;
                return this.employees;
            } catch (IOException e) {
                throw new Exception("Errore");
            }
        }

    }

    @Override
    public boolean isEmpty() throws Exception {
        return this.loadAll().isEmpty();
    }

    private void rollback(List<Statement<Employee>> statements) throws Exception {
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
            throw new Exception("Integrity", e);
        }
    }

    @Override
    public void executeTransaction(List<Statement<Employee>> statements) throws Exception {
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
        } catch (Exception e) {
            // one statement failed. Rollback to previous state
            this.rollback(statements);

            throw e;
        }
    }

    @Override
    public void keepIntegrity(List<Statement<Employee>> statements) throws Exception {
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
