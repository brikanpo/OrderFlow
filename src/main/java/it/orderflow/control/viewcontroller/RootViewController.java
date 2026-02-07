package it.orderflow.control.viewcontroller;

import it.orderflow.control.RootController;
import it.orderflow.view.ViewObserver;

public abstract class RootViewController extends RootController implements ViewObserver {

    public abstract void start();
}
