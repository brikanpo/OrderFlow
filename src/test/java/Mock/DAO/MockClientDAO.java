package Mock.DAO;

import Mock.Model.MockEntity;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.ClientDAO;
import it.OrderFlow.Model.Client;

import java.util.ArrayList;
import java.util.List;

public class MockClientDAO implements ClientDAO {

    private List<Client> mockCache;

    public MockClientDAO() {
        MockEntity me = new MockEntity();
        mockCache = new ArrayList<>(List.of(
                me.getMockClient()));
    }

    @Override
    public Client loadClient(String email) {
        for (Client client : mockCache) {
            if (client.getEmail().equals(email)) return client;
        }
        return null;
    }

    @Override
    public List<Client> loadAll() {
        return List.copyOf(mockCache);
    }

    @Override
    public void executeTransaction(List<Statement<Client>> statements) {
        for (Statement<Client> statement : statements) {
            switch (statement.getStatementType()) {
                case SAVE -> mockCache.add(statement.getNewEntity());
                case UPDATE -> {
                    Client oldEntity = statement.getOldEntity();
                    mockCache.set(mockCache.indexOf(oldEntity), statement.getNewEntity());
                }
                case DELETE -> mockCache.remove(statement.getNewEntity());
            }
        }
    }

    @Override
    public void keepIntegrity(List<Statement<Client>> statements) {

    }
}
