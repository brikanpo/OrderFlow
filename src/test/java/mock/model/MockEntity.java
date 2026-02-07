package mock.model;

import it.orderflow.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MockEntity {

    public Address getMockAddress() {
        return new Address("address", "cap", "city", "province");
    }

    public Attributes getMockAttributes() {
        return new Attributes(List.of("color"), List.of("red"));
    }

    public Attributes getMockPossibleAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAttributeId("size");
        return attributes;
    }

    public Attributes getMockProductAttributes() {
        return new Attributes(List.of("size"), List.of("50"));
    }

    public Client getMockClient() {
        return new Client(UUID.randomUUID(), "client1", "c@c.cc", "phone", this.getMockAddress());
    }

    public ClientArticle getMockClientArticle() {
        return new ClientArticle(UUID.randomUUID(), new ArticleData("clientArticle1", "category",
                "description", this.getMockAttributes(), this.getMockPossibleAttributes(),
                new BigDecimal("0.22")), this.getMockSupplierArticle().getId());
    }

    public ClientOrder getMockClosedClientOrder() {
        return new ClientOrder(UUID.randomUUID(),
                LocalDateTime.of(2026, 1, 10, 9, 0),
                this.getMockClientProductsWithQuantity(), OrderState.CLOSED,
                new ClientOrderData(this.getMockRepresentative().getId(), this.getMockClient(),
                        this.getMockWarehouseWorker().getId(),
                        LocalDateTime.of(2026, 1, 15, 9, 0),
                        this.getMockDeliveryWorker().getId()));
    }

    public Employee getMockManager() {
        return new Employee(UUID.randomUUID(), "manager", "m@m.mm", "phone",
                "123", UserRole.MANAGER);
    }

    public Employee getMockRepresentative() {
        return new Employee(UUID.randomUUID(), "representative", "r@r.rr", "phone",
                "123", UserRole.REPRESENTATIVE);
    }

    public Employee getMockWarehouseWorker() {
        return new Employee(UUID.randomUUID(), "warehouse worker", "w@w.ww", "phone",
                "123", UserRole.WAREHOUSE_WORKER);
    }

    public Employee getMockDeliveryWorker() {
        return new Employee(UUID.randomUUID(), "delivery worker", "d@d.dd", "phone",
                "123", UserRole.DELIVERY_WORKER);
    }

    public Product getMockClientProduct() {
        return new Product(UUID.randomUUID(), "codeClientProduct1", this.getMockClientArticle(),
                this.getMockProductAttributes(), new BigDecimal("25.00"));
    }

    public Product getMockSupplierProduct() {
        return new Product(UUID.randomUUID(), "codeSupplierProduct1", this.getMockSupplierArticle(),
                this.getMockProductAttributes(), new BigDecimal("25.00"));
    }

    public ProductInStock getMockProductInStock() {
        return new ProductInStock(this.getMockClientProduct(), 10, 5, 20);
    }

    public ProductWithQuantity getMockClientProductWithQuantity() {
        return new ProductWithQuantity(this.getMockClientProduct(), 3);
    }

    public ProductsWithQuantity getMockClientProductsWithQuantity() {
        ProductsWithQuantity result = new ProductsWithQuantity();
        result.add(this.getMockClientProductWithQuantity());
        return result;
    }

    public Supplier getMockSupplier() {
        return new Supplier(UUID.randomUUID(), "supplier1", "s@s.ss", "phone",
                new BigDecimal("100.00"));
    }

    public SupplierArticle getMockSupplierArticle() {
        return new SupplierArticle(UUID.randomUUID(), new ArticleData("supplierArticle1", "category",
                "description", this.getMockAttributes(), this.getMockPossibleAttributes(),
                new BigDecimal("0.22")), this.getMockSupplier().getId());
    }
}
