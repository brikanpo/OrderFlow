package it.orderflow.control.logiccontroller;

import it.orderflow.beans.AddressBean;
import it.orderflow.beans.ClientBean;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.ClientDAO;
import it.orderflow.exceptions.*;
import it.orderflow.model.Address;
import it.orderflow.model.Client;

import java.util.List;

public class ManageClientsLogicController extends TransactionSafeController {

    private ClientDAO clientDAO;
    private ClientBean tempClientBean;

    public ManageClientsLogicController(ClientDAO clientDAO) {
        this.setClientDAO(clientDAO);
    }

    public ClientDAO getClientDAO() {
        return this.clientDAO;
    }

    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public ClientBean getTempClientBean() {
        return this.tempClientBean;
    }

    public void setTempClientBean(ClientBean tempClientBean) {
        this.tempClientBean = tempClientBean;
    }

    public List<ClientBean> getClientsList() throws PersistenceException {
        List<Client> clients = this.getClientDAO().loadAll();

        return clients.stream()
                .map(ClientBean::new)
                .toList();
    }

    public void addSelectedClient(ClientBean clientBean) {
        this.setTempClientBean(clientBean);
    }

    public void saveNewClient(ClientBean clientBean)
            throws AlreadyInUseException, InvalidInputException, PersistenceException {
        Client targetClient = this.getClientDAO().loadClient(clientBean.getEmail());
        if (targetClient == null) {

            String name = clientBean.getName();
            String email = clientBean.getEmail();
            if (name == null || email == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            Client tempClient = new Client(name, email);

            String phone = clientBean.getPhone();
            if (phone != null) tempClient.changePhone(phone);

            AddressBean addressBean = clientBean.getAddressBean();
            if (addressBean != null) {
                Address address = new Address(addressBean.getStreetAddress(), addressBean.getCap(), addressBean.getCity(),
                        addressBean.getProvince());
                tempClient.changeAddress(address);
            }

            this.startOperation();
            this.addStatement(this.getClientDAO(), new Statement<>(List.of(tempClient), Statement.Type.SAVE));
            this.endOperation();

        } else throw new AlreadyInUseException(EntityException.Entity.CLIENT, AlreadyInUseException.Param.EMAIL);
    }

    public void changeClientInfo(ClientBean clientBean)
            throws EntityNotFoundException, PersistenceException {
        Client oldClient = this.getClientDAO().loadClient(this.getTempClientBean().getEmail());
        if (oldClient != null) {
            Client newClient = oldClient.copy();

            String name = clientBean.getName();
            if (name != null) newClient.changeName(name);

            String email = clientBean.getEmail();
            if (email != null) newClient.changeEmail(email);

            String phone = clientBean.getPhone();
            if (phone != null) newClient.changePhone(phone);

            AddressBean addressBean = clientBean.getAddressBean();
            if (addressBean != null) {
                Address address = new Address(addressBean.getStreetAddress(), addressBean.getCap(), addressBean.getCity(),
                        addressBean.getProvince());
                newClient.changeAddress(address);
            }

            this.startOperation();
            this.addStatement(this.getClientDAO(), new Statement<>(List.of(newClient, oldClient), Statement.Type.UPDATE));
            this.endOperation();

            this.setTempClientBean(new ClientBean());

        } else throw new EntityNotFoundException(EntityException.Entity.CLIENT);
    }
}
