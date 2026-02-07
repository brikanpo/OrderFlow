package it.orderflow.control;

public interface ControllerResultObserver extends ControllerObserver {

    <T> void onResult(ControllerEvent event, T result);
}
