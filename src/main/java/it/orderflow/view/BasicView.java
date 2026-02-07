package it.orderflow.view;

public interface BasicView {

    void addObserver(ViewObserver observer);

    void displayErrorMessage(Exception e);

    void displaySuccessMessage(String message);

    void close();
}
