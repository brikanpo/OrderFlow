package it.orderflow.model;

public enum UserRole {

    MANAGER {
        @Override
        public String toString() {
            return "Manager";
        }
    },
    REPRESENTATIVE {
        @Override
        public String toString() {
            return "Representative";
        }
    },
    WAREHOUSE_WORKER {
        @Override
        public String toString() {
            return "Warehouse worker";
        }
    },
    DELIVERY_WORKER {
        @Override
        public String toString() {
            return "Delivery worker";
        }
    };

    public static UserRole getRole(String str) {
        return switch (str) {
            case "Manager" -> MANAGER;
            case "Representative" -> REPRESENTATIVE;
            case "Warehouse worker" -> WAREHOUSE_WORKER;
            case "Delivery worker" -> DELIVERY_WORKER;
            default -> null;
        };
    }
}
