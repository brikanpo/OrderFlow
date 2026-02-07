package mock.dao;

import it.orderflow.control.Statement;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.model.ClientArticle;
import mock.model.MockEntity;

import java.util.ArrayList;
import java.util.List;

public class MockClientArticleDAO implements ClientArticleDAO {

    private List<ClientArticle> mockCache;

    public MockClientArticleDAO() {
        mockCache = new ArrayList<>(List.of(
                new MockEntity().getMockClientArticle()));
    }

    @Override
    public ClientArticle loadClientArticle(String name) {
        for (ClientArticle clientArticle : mockCache) {
            if (clientArticle.getName().equals(name)) return clientArticle;
        }
        return null;
    }

    @Override
    public List<ClientArticle> loadAll() {
        return List.copyOf(mockCache);
    }

    @Override
    public void executeTransaction(List<Statement<ClientArticle>> statements) {
        for (Statement<ClientArticle> statement : statements) {
            switch (statement.getStatementType()) {
                case SAVE -> mockCache.add(statement.getNewEntity());
                case UPDATE -> {
                    ClientArticle oldEntity = statement.getOldEntity();
                    mockCache.set(mockCache.indexOf(oldEntity), statement.getNewEntity());
                }
                case DELETE -> mockCache.remove(statement.getNewEntity());
            }
        }
    }

    @Override
    public void keepIntegrity(List<Statement<ClientArticle>> statements) {

    }
}
