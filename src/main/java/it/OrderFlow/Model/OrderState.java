package it.OrderFlow.Model;

public enum OrderState {

    WAITING {
        @Override
        public String toString() {
            return "Waiting";
        }
    },
    READY {
        @Override
        public String toString() {
            return "Ready";
        }
    },
    CLOSED {
        @Override
        public String toString() {
            return "Closed";
        }
    };

    public static OrderState getState(String str) {
        return switch (str) {
            case "Waiting" -> WAITING;
            case "Ready" -> READY;
            case "Closed" -> CLOSED;
            default -> null;
        };
    }
}
