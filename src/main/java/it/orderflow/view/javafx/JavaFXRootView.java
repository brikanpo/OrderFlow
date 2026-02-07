package it.orderflow.view.javafx;

import it.orderflow.view.RootView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.MyErrorMessage;
import it.orderflow.view.javafx.mycomponents.MySuccessMessage;
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
        this.runOnFXThread(() -> {
            MySuccessMessage successMessage = new MySuccessMessage(message);
            successMessage.showAndWait();
        });
    }

    @Override
    public void close() {
        Platform.exit();
    }
}
