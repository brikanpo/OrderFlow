package it.orderflow.view.javafx;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.UserRole;
import it.orderflow.view.ManageEmployeesView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewEmployee;

import java.util.List;

public class JavaFXManageEmployeesView extends JavaFXRootView implements ManageEmployeesView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyTextField nameField;
    private MyTextField emailField;
    private MyUserRoleChoice userRoleChoice;
    private MyTableViewEmployee table;

    public JavaFXManageEmployeesView() {
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
    public void displayManageEmployees() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage employees");

            infoLabel.setText("What do you want to do?");

            MyButton addEmployeeButton = new MyButton("Add employee");

            MyButton changeEmployeeRoleButton = new MyButton("Change employee role");

            addEmployeeButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_EMPLOYEE));
            changeEmployeeRoleButton.setOnAction(e -> this.notifyObservers(ViewEvent.CHANGE_EMPLOYEE_ROLE));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, addEmployeeButton, changeEmployeeRoleButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddEmployee() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add employees");

            infoLabel.setText("Enter the new employee's info");

            nameField = new MyTextField("Name");

            emailField = new MyTextField("Email");

            userRoleChoice = new MyUserRoleChoice();

            confirmButton.disableProperty().bind(nameField.textProperty().isEmpty().or(emailField.textProperty().isEmpty()).or(userRoleChoice.valueProperty().isNull()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_EMPLOYEE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, nameField, emailField, userRoleChoice, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayChangeRole(List<EmployeeBean> employees) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Change employee role");

            infoLabel.setText("Select which employee's role do you want to change");

            table = new MyTableViewEmployee(employees);

            userRoleChoice = new MyUserRoleChoice();

            confirmButton.disableProperty().bind(table.selectionModelProperty().isNull().or(userRoleChoice.valueProperty().isNull()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_EMPLOYEE_ROLE_CHANGE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, table, userRoleChoice, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public EmployeeBean getEmployeeBean() throws InvalidInputException {
        EmployeeBean employeeBean = new EmployeeBean();

        if (this.table == null) {
            employeeBean.setName(this.nameField.getText());
            employeeBean.setEmail(this.emailField.getText());
        } else {
            employeeBean = this.table.getSelectionModel().getSelectedItem();
        }
        employeeBean.setUserRole(UserRole.getRole(this.userRoleChoice.getValue()));

        this.table = null;

        return employeeBean;
    }
}
