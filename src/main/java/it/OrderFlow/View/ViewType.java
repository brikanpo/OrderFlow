package it.OrderFlow.View;

public enum ViewType {
    CLI,
    JAVAFX;

    public static ViewType getViewType(String str) {
        return switch (str) {
            case "CLI" -> CLI;
            case "JavaFX" -> JAVAFX;
            default -> null;
        };
    }
}
