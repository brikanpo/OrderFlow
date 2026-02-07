package it.orderflow.view;

import java.util.ArrayList;
import java.util.List;

public abstract class RootView implements BasicView {

    private final List<ViewObserver> observers = new ArrayList<>();

    public void notifyObservers(ViewEvent event) {
        for (ViewObserver obs : this.observers) {
            obs.onEvent(event);
        }
    }

    @Override
    public void addObserver(ViewObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public abstract void close();
}
