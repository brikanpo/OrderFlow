package it.OrderFlow.View.JavaFX;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.*;
import it.OrderFlow.View.LoginView;
import it.OrderFlow.View.ViewEvent;

public class JavaFXLoginView extends JavaFXRootView implements LoginView {

    private MyTextField emailField;
    private MyPasswordField passwordField;
    private MyButton loginButton;
    private MyLabel infoLabel;

    public JavaFXLoginView() {
        super();
    }

    private void setup() {
        MyVBox layout = new MyVBox();

        MyTitle title = new MyTitle(stage.getTitle());

        infoLabel = new MyLabel("You are the first user. " +
                "Enter your credentials and select Register to register yourself as a Manager");

        emailField = new MyTextField("Email");

        passwordField = new MyPasswordField();

        loginButton = new MyButton("Register");

        MyButton exitButton = new MyButton("Exit");

        loginButton.disableProperty().bind(emailField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
        loginButton.setOnAction(e -> this.notifyObservers(ViewEvent.LOGIN));
        exitButton.setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));

        layout.getChildren().addAll(title, infoLabel, emailField, passwordField, loginButton, exitButton);

        MyScene scene = new MyScene(layout, null);
        stage.setScene(scene);
    }

    @Override
    public void displayFirstLogin() {
        this.runOnFXThread(() -> {
            this.setup();
            infoLabel.setVisible(true);
            stage.show();
        });
    }

    @Override
    public void displayLogin() {
        this.runOnFXThread(() -> {
            this.setup();
            infoLabel.setVisible(false);
            loginButton.setText("Login");
            stage.show();
        });
    }

    @Override
    public EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException {
        EmployeeAccessBean employeeAccessBean = new EmployeeAccessBean();
        employeeAccessBean.setEmail(emailField.getText());
        employeeAccessBean.setPassword(passwordField.getText());
        return employeeAccessBean;
    }
}
