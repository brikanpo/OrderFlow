package Mock.DAO;

import Mock.Model.MockEntity;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.ClientOrderDAO;
import it.OrderFlow.Model.ClientOrder;
import it.OrderFlow.Model.OrderState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockClientOrderDAO implements ClientOrderDAO {

    private final List<ClientOrder> mockCache;

    public MockClientOrderDAO() {
        MockEntity me = new MockEntity();
        mockCache = new ArrayList<>(List.of(
                me.getMockClosedClientOrder()));
    }

    @Override
    public ClientOrder loadClientOrder(UUID id) {
        for (ClientOrder clientOrder : mockCache) {
            if (clientOrder.getId().equals(id)) return clientOrder;
        }
        return null;
    }

    @Override
    public List<ClientOrder> loadByState(OrderState state) {
        List<ClientOrder> result = new ArrayList<>();
        for (ClientOrder clientOrder : mockCache) {
            if (clientOrder.getState().equals(state)) result.add(clientOrder);
        }
        return result;
    }

    @Override
    public List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) {
        List<ClientOrder> result = new ArrayList<>();
        for (ClientOrder clientOrder : mockCache) {
            if (clientOrder.getState().equals(state) && clientOrder.getClient().getId().equals(clientId)) {
                result.add(clientOrder);
            }
        }
        return result;
    }

    @Override
    public List<ClientOrder> loadAll() {
        return List.copyOf(mockCache);
    }

    @Override
    public void executeTransaction(List<Statement<ClientOrder>> statements) {
        for (Statement<ClientOrder> statement : statements) {
            switch (statement.getStatementType()) {
                case SAVE -> mockCache.add(statement.getNewEntity());
                case UPDATE -> {
                    ClientOrder oldEntity = statement.getOldEntity();
                    mockCache.set(mockCache.indexOf(oldEntity), statement.getNewEntity());
                }
                case DELETE -> mockCache.remove(statement.getNewEntity());
            }
        }
    }

    @Override
    public void keepIntegrity(List<Statement<ClientOrder>> statements) {

    }
}
