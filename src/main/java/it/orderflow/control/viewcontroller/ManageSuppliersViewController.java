package it.orderflow.control.viewcontroller;

import it.orderflow.beans.SupplierBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.control.logiccontroller.ManageSuppliersLogicController;
import it.orderflow.view.ManageSuppliersView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.selectionviews.SupplierSelectionView;

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
            default -> throw new UnsupportedOperationException();
        }
    }
}
