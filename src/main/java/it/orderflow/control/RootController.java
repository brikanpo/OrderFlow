package it.orderflow.control;

import java.util.ArrayList;
import java.util.List;

public class RootController {

    protected List<ControllerObserver> observers;

    public RootController() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(ControllerObserver observer) {
        this.observers.add(observer);
    }

    public void notifyObservers(ControllerEvent event) {
        for (ControllerObserver obs : observers) {
            obs.onEvent(event);
        }
    }

    public <T> void sendResult(ControllerEvent event, T result) {
        for (ControllerObserver obs : observers) {
            if (obs instanceof ControllerResultObserver) ((ControllerResultObserver) obs).onResult(event, result);
        }
    }
}
