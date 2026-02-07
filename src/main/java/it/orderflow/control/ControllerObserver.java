package it.orderflow.control;

public interface ControllerObserver {

    void onEvent(ControllerEvent event);
}
