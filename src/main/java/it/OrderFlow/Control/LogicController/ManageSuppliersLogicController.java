package it.OrderFlow.Control.LogicController;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.Control.TransactionSafeController;
import it.OrderFlow.DAO.SupplierDAO;
import it.OrderFlow.Exceptions.AlreadyInUseException;
import it.OrderFlow.Exceptions.EntityException;
import it.OrderFlow.Exceptions.EntityNotFoundException;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Supplier;

import java.math.BigDecimal;
import java.util.List;

public class ManageSuppliersLogicController extends TransactionSafeController {

    private final SupplierDAO supplierDAO;
    private SupplierBean tempSupplierBean;

    public ManageSuppliersLogicController(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    public SupplierDAO getSupplierDAO() {
        return this.supplierDAO;
    }

    public SupplierBean getTempSupplierBean() {
        return this.tempSupplierBean;
    }

    public void setTempSupplierBean(SupplierBean tempSupplierBean) {
        this.tempSupplierBean = tempSupplierBean;
    }

    public List<SupplierBean> getSuppliersList() throws Exception {
        List<Supplier> suppliers = this.getSupplierDAO().loadAll();

        return suppliers.stream()
                .map(SupplierBean::new)
                .toList();
    }

    public void addSelectedSupplier(SupplierBean supplierBean) {
        this.setTempSupplierBean(supplierBean);
    }

    public void saveNewSupplier(SupplierBean supplierBean) throws Exception {
        Supplier targetSupplier = this.getSupplierDAO().loadSupplier(supplierBean.getEmail());
        if (targetSupplier == null) {

            String name = supplierBean.getName();
            String email = supplierBean.getEmail();
            if (name == null || email == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            Supplier tempSupplier = new Supplier(name, email);

            String phone = supplierBean.getPhone();
            if (phone != null) tempSupplier.changePhone(phone);

            BigDecimal transportFee = supplierBean.getTransportFee();
            if (transportFee != null) tempSupplier.changeTransportFee(transportFee);

            this.startOperation();
            this.addStatement(this.getSupplierDAO(), new Statement<>(List.of(tempSupplier), Statement.Type.SAVE));
            this.endOperation();

        } else throw new AlreadyInUseException(EntityException.Entity.SUPPLIER, AlreadyInUseException.Param.EMAIL);


    }

    public void changeSupplierInfo(SupplierBean supplierBean) throws Exception {
        Supplier oldSupplier = this.getSupplierDAO().loadSupplier(this.getTempSupplierBean().getEmail());
        if (oldSupplier != null) {
            Supplier newSupplier = oldSupplier.clone();

            String name = supplierBean.getName();
            if (name != null) newSupplier.changeName(name);

            String email = supplierBean.getEmail();
            if (email != null) newSupplier.changeEmail(email);

            String phone = supplierBean.getPhone();
            if (phone != null) newSupplier.changePhone(phone);

            BigDecimal transportFee = supplierBean.getTransportFee();
            if (transportFee != null) newSupplier.changeTransportFee(transportFee);

            this.startOperation();
            this.addStatement(this.getSupplierDAO(), new Statement<>(List.of(newSupplier, oldSupplier), Statement.Type.UPDATE));
            this.endOperation();

            this.setTempSupplierBean(new SupplierBean());

        } else throw new EntityNotFoundException(EntityException.Entity.SUPPLIER);
    }
}
