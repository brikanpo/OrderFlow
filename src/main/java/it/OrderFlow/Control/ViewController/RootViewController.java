package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Control.RootController;
import it.OrderFlow.View.ViewObserver;

public abstract class RootViewController extends RootController implements ViewObserver {

    public abstract void start();
}
