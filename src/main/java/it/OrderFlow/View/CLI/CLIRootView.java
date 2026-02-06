package it.OrderFlow.View.CLI;

import it.OrderFlow.View.RootView;
import it.OrderFlow.View.ViewEvent;

import java.util.Scanner;

public class CLIRootView extends RootView {

    protected Scanner scanner = new Scanner(System.in);

    public CLIRootView() {
        super();
    }

    protected void confirmOperation(ViewEvent eventOnConfirm, ViewEvent eventOnCancel) {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            System.out.println("Do you want to confirm or cancel the operation?");
            System.out.println("1) Confirm");
            System.out.println("2) Cancel");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(eventOnConfirm);
                case "2" -> this.notifyObservers(eventOnCancel);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    protected void printTitle(String title) {
        System.out.println("--------------- " + title + " ---------------");
    }

    @Override
    public void displayErrorMessage(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void displaySuccessMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void close() {
    }
}
