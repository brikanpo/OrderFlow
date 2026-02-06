package it.OrderFlow.View.JavaFX;

import it.OrderFlow.View.JavaFX.MyComponents.MyErrorMessage;
import it.OrderFlow.View.RootView;
import it.OrderFlow.View.ViewEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

public class JavaFXRootView extends RootView {

    protected static Stage stage;

    public JavaFXRootView() {
        super();
        this.start();
    }

    public void start() {
        if (stage == null) {
            Platform.startup(() -> {
                stage = new Stage();
                stage.setTitle(this.getClass().getPackageName().split("\\.")[1]);
                stage.setOnCloseRequest(event -> this.notifyObservers(ViewEvent.EXIT));
            });
        }
    }

    public void runOnFXThread(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }

    @Override
    public void displayErrorMessage(Exception e) {
        this.runOnFXThread(() -> {
            MyErrorMessage errorMessage = new MyErrorMessage(e);
            errorMessage.showAndWait();
        });
    }

    @Override
    public void displaySuccessMessage(String message) {
    }

    @Override
    public void close() {
        Platform.exit();
    }
}
