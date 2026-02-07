package it.orderflow.dao.dbms;

import it.orderflow.control.Statement;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Employee;
import it.orderflow.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSEmployeeDAO extends DBMSGeneralDAO<Employee> implements EmployeeDAO {

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

    private Employee findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getEmployeeId);
    }

    private Employee findByEmailFromCache(String email) {
        return this.findFromCache(email, this::getEmployeeEmail);
    }

    private List<Employee> findByRoleFromCache(UserRole userRole) {
        return this.findMatchesFromCache(userRole, this::getEmployeeRole);
    }

    private Employee findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence("employee", "id", id,
                this::getEmployee, EntityException.Entity.EMPLOYEE);
    }

    private Employee findByEmailFromPersistence(String email) throws DatabaseException {
        return this.findFromPersistence("employee", "email", email,
                this::getEmployee, EntityException.Entity.EMPLOYEE);
    }

    private List<Employee> findByRoleFromPersistence(UserRole userRole) throws DatabaseException {
        return this.findMatchesFromPersistence("employee", "userRole", userRole.toString(),
                this::getEmployeeList, EntityException.Entity.EMPLOYEE);
    }

    private Employee findEmployee(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    private Employee getEmployee(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytes = rs.getBytes("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String passwordHash = rs.getString("passwordHash");
            UserRole userRole = UserRole.getRole(rs.getString("userRole"));

            return new Employee(this.bytesToUUID(bytes), name, email, phone, passwordHash, userRole);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.EMPLOYEE, e);
        }
    }

    private List<Employee> getEmployeeList(ResultSet rs) throws DatabaseException {
        return this.getEntityList(rs, this::getEmployee, EntityException.Entity.EMPLOYEE);
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, Employee employee)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(employee.getId()));
                    pstmt.setString(2, employee.getName());
                    pstmt.setString(3, employee.getEmail());
                    pstmt.setString(4, employee.getPhone());
                    pstmt.setString(5, employee.getPasswordHash());
                    pstmt.setString(6, employee.getUserRole().toString());
                }
                case UPDATE -> {
                    pstmt.setString(1, employee.getName());
                    pstmt.setString(2, employee.getEmail());
                    pstmt.setString(3, employee.getPhone());
                    pstmt.setString(4, employee.getPasswordHash());
                    pstmt.setString(5, employee.getUserRole().toString());
                    pstmt.setBytes(6, this.uuidToBytes(employee.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(employee.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.EMPLOYEE);
        }
    }

    private void saveNewEmployee(Employee employee) throws DatabaseException {
        this.saveNewEntity(employee, this::loadEmployee, this::getEmployeeEmail, this::copy,
                "INSERT INTO employee (id, name, email, phone, passwordHash, userRole) VALUES (?,?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.EMPLOYEE);
    }

    private void updateEmployee(Employee employee) throws DatabaseException {
        this.updateEntity(employee, this::findEmployee, this::getEmployeeId,
                "UPDATE employee SET name = ?, email = ?, phone = ?, passwordHash = ?, userRole = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.EMPLOYEE);
    }

    private void deleteEmployee(Employee employee) throws DatabaseException {
        this.deleteEntity(employee, this::findEmployee, this::getEmployeeId,
                "DELETE FROM employee WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.EMPLOYEE);
    }

    @Override
    public Employee loadEmployee(String email) throws DatabaseException {
        return this.findSingleResult(email, this::findByEmailFromCache, this::findByEmailFromPersistence);
    }

    @Override
    public List<Employee> loadByRole(UserRole role) throws DatabaseException {
        return this.findMultipleResults(role, this::getEmployeeId, this::findByRoleFromCache,
                this::findByRoleFromPersistence);
    }

    @Override
    public List<Employee> loadAll() throws DatabaseException {
        return this.loadAll("employee", this::getEmployee, EntityException.Entity.EMPLOYEE);
    }

    @Override
    public boolean isEmpty() throws Exception {
        return this.isEmpty("employee", EntityException.Entity.EMPLOYEE);
    }

    @Override
    public void executeTransaction(List<Statement<Employee>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewEmployee, this::updateEmployee,
                this::deleteEmployee);
    }

    @Override
    public void keepIntegrity(List<Statement<Employee>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewEmployee, this::updateEmployee,
                this::deleteEmployee);
    }
}
