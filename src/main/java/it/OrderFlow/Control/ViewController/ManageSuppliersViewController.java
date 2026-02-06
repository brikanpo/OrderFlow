package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.ManageSuppliersLogicController;
import it.OrderFlow.View.ManageSuppliersView;
import it.OrderFlow.View.SelectionComponents.SupplierSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class ManageSuppliersViewController extends RootViewController {

    private ManageSuppliersView view;
    private SupplierSelectionView supplierSelectionView;
    private ManageSuppliersLogicController logicController;

    public ManageSuppliersViewController(ManageSuppliersView view,
                                         SupplierSelectionView supplierSelectionView,
                                         ManageSuppliersLogicController logicController) {
        this.setView(view);
        this.setSupplierSelectionView(supplierSelectionView);
        this.setLogicController(logicController);
    }

    public ManageSuppliersView getView() {
        return this.view;
    }

    public void setView(ManageSuppliersView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public SupplierSelectionView getSupplierSelectionView() {
        return this.supplierSelectionView;
    }

    public void setSupplierSelectionView(SupplierSelectionView supplierSelectionView) {
        this.supplierSelectionView = supplierSelectionView;
        this.supplierSelectionView.addObserver(this);
    }

    public ManageSuppliersLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageSuppliersLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        this.getView().displayManageSuppliers();
    }

    @Override
    public void onEvent(ViewEvent viewEvent) {
        switch (viewEvent) {
            case ADD_SUPPLIER -> this.getView().displayAddSupplier();
            case SAVE_SUPPLIER -> {
                try {
                    SupplierBean supplierBean = this.getView().getSupplierBean();

                    this.getLogicController().saveNewSupplier(supplierBean);

                    this.getView().displaySuccessMessage("Operation add supplier successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case CHANGE_SUPPLIER_INFO -> {
                try {
                    List<SupplierBean> supplierBeans = this.getLogicController().getSuppliersList();

                    this.getSupplierSelectionView().displaySuppliers(supplierBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_SUPPLIER -> {
                try {
                    SupplierBean supplierBean = this.getSupplierSelectionView().selectedSupplier();

                    this.getLogicController().addSelectedSupplier(supplierBean);

                    this.getView().displayChangeSupplierInfo(supplierBean);
                } catch (Exception e) {
                    this.getSupplierSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_SUPPLIER_INFO_CHANGE -> {
                try {
                    SupplierBean supplierBean = this.getView().getSupplierBean();

                    this.getLogicController().changeSupplierInfo(supplierBean);

                    this.getView().displaySuccessMessage("Operation change supplier info was successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case BACK -> this.start();
            case HOME -> this.notifyObservers(ControllerEvent.HOME);
            case LOGOUT -> this.notifyObservers(ControllerEvent.LOGOUT);
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
        }
    }
}
