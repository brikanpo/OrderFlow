package Mock.DAO;

import Mock.Model.MockEntity;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.EmployeeDAO;
import it.OrderFlow.Model.Employee;
import it.OrderFlow.Model.UserRole;

import java.util.ArrayList;
import java.util.List;

public class MockEmployeeDAO implements EmployeeDAO {

    private final List<Employee> mockCache;

    public MockEmployeeDAO() {
        MockEntity me = new MockEntity();
        mockCache = new ArrayList<>(List.of(
                me.getMockManager(),
                me.getMockRepresentative(),
                me.getMockWarehouseWorker(),
                me.getMockDeliveryWorker()));
    }

    @Override
    public Employee loadEmployee(String email) {
        for (Employee employee : mockCache) {
            if (employee.getEmail().equals(email)) return employee;
        }
        return null;
    }

    @Override
    public List<Employee> loadByRole(UserRole role) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : mockCache) {
            if (employee.getUserRole().equals(role)) result.add(employee);
        }
        return result;
    }

    @Override
    public List<Employee> loadAll() {
        return List.copyOf(mockCache);
    }

    @Override
    public boolean isEmpty() {
        return mockCache.isEmpty();
    }

    @Override
    public void executeTransaction(List<Statement<Employee>> statements) {
        for (Statement<Employee> statement : statements) {
            switch (statement.getStatementType()) {
                case SAVE -> mockCache.add(statement.getNewEntity());
                case UPDATE -> {
                    Employee oldEntity = statement.getOldEntity();
                    mockCache.set(mockCache.indexOf(oldEntity), statement.getNewEntity());
                }
                case DELETE -> mockCache.remove(statement.getNewEntity());
            }
        }
    }

    @Override
    public void keepIntegrity(List<Statement<Employee>> statements) {

    }
}
