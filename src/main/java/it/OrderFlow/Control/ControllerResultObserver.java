package it.OrderFlow.Control;

public interface ControllerResultObserver extends ControllerObserver {

    <T> void onResult(ControllerEvent event, T result);
}
