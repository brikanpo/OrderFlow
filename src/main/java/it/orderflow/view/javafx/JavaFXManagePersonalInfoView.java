package it.orderflow.view.javafx;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ManagePersonalInfoView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;

public class JavaFXManagePersonalInfoView extends JavaFXRootView implements ManagePersonalInfoView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;
    private EmployeeBean employeeBean;
    private MyLabel nameLabel;
    private MyLabel emailLabel;
    private MyLabel phoneLabel;
    private MyTextField nameField;
    private MyTextField emailField;
    private MyTextField phoneField;
    private MyPasswordField passwordField;

    public JavaFXManagePersonalInfoView() {
        super();

        title = new MyTitle();

        infoLabel = new MyLabel();

        confirmButton = new MyButton("Confirm");

        backButton = new MyButton("Back");

        myNavigationBar = new MyNavigationBar();

        myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayManagePersonalInfo(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage personal info");

            infoLabel.setText("These are your info. What do you want to do?");

            nameLabel = new MyLabel("Name: " + employeeBean.getName());
            emailLabel = new MyLabel("Email: " + employeeBean.getEmail());
            phoneLabel = new MyLabel("Phone: " + employeeBean.getPhone());

            MyButton changePersonalInfoButton = new MyButton("Change personal info");

            MyButton changePasswordButton = new MyButton("Change password");

            backButton.setDisable(false);
            myNavigationBar.getHomeButton().setDisable(false);

            changePersonalInfoButton.setOnAction(e -> this.notifyObservers(ViewEvent.CHANGE_PERSONAL_INFO));
            changePasswordButton.setOnAction(e -> this.notifyObservers(ViewEvent.CHANGE_PASSWORD));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, nameLabel, emailLabel, phoneLabel, infoLabel,
                    changePersonalInfoButton, changePasswordButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayChangePersonalInfo(EmployeeBean employeeBean) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Change personal info");

            infoLabel.setText("These are your info. If you don't want to change something leave the field empty");

            nameField = new MyTextField("Name");

            emailField = new MyTextField("Email");

            phoneField = new MyTextField("Phone");

            backButton.setDisable(false);
            myNavigationBar.getHomeButton().setDisable(false);

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_PERSONAL_INFO));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, nameLabel, emailLabel, phoneLabel, infoLabel,
                    nameField, emailField, phoneField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayChangePassword(boolean hasDefaultPassword) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Change password");

            passwordField = new MyPasswordField();

            if (hasDefaultPassword) {
                infoLabel.setText("Change the default password");
                confirmButton.disableProperty().bind(passwordField.textProperty().isEmpty());
                backButton.setDisable(true);
                myNavigationBar.getHomeButton().setDisable(true);
            } else {
                infoLabel.setText("If you don't want to change the password leave the field empty");
            }

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_PASSWORD));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, passwordField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException {
        EmployeeAccessBean employeeAccessBean = new EmployeeAccessBean();
        employeeAccessBean.setPassword(passwordField.getText());
        return employeeAccessBean;
    }

    @Override
    public EmployeeBean getEmployeeBean() throws InvalidInputException {

        String name = this.nameField.getText();
        if (!name.isBlank()) this.employeeBean.setName(name);

        String email = this.emailField.getText();
        if (!email.isBlank()) this.employeeBean.setEmail(email);

        String phone = this.phoneField.getText();
        if (!phone.isBlank()) this.employeeBean.setPhone(phone);

        return employeeBean;
    }
}
