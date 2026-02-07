package it.orderflow;

import it.orderflow.control.AppController;
import it.orderflow.dao.DAOType;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewType;

public class Main {

    public static void main(String[] args) {

        ConfigManager configManager = ConfigManager.getInstance();

        try {
            ViewType viewType = ViewType.getViewType(configManager.getProperty("view"));
            DAOType daoType = DAOType.getDAOType(configManager.getProperty("dao"));

            if (viewType != null && daoType != null) {
                AppController appController = new AppController(viewType, daoType);
                appController.start();
            } else {
                throw new InvalidInputException(InvalidInputException.InputType.CONFIG_DEFAULT);
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());

            System.out.println("Starting OrderFlow in demo version with CLI graphics");

            AppController appController = new AppController(ViewType.CLI, DAOType.CACHE);
            appController.start();
        }
    }
}