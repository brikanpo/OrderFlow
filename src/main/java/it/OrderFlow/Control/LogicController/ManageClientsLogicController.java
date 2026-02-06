package it.OrderFlow.Control.LogicController;

import it.OrderFlow.Beans.AddressBean;
import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Control.Statement;
import it.OrderFlow.Control.TransactionSafeController;
import it.OrderFlow.DAO.ClientDAO;
import it.OrderFlow.Exceptions.AlreadyInUseException;
import it.OrderFlow.Exceptions.EntityException;
import it.OrderFlow.Exceptions.EntityNotFoundException;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Address;
import it.OrderFlow.Model.Client;

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

    public List<ClientBean> getClientsList() throws Exception {
        List<Client> clients = this.getClientDAO().loadAll();

        return clients.stream()
                .map(ClientBean::new)
                .toList();
    }

    public void addSelectedClient(ClientBean clientBean) {
        this.setTempClientBean(clientBean);
    }

    public void saveNewClient(ClientBean clientBean) throws Exception {
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
                Address address = new Address(addressBean.getAddress(), addressBean.getCap(), addressBean.getCity(),
                        addressBean.getProvince());
                tempClient.changeAddress(address);
            }

            this.startOperation();
            this.addStatement(this.getClientDAO(), new Statement<>(List.of(tempClient), Statement.Type.SAVE));
            this.endOperation();

        } else throw new AlreadyInUseException(EntityException.Entity.CLIENT, AlreadyInUseException.Param.EMAIL);
    }

    public void changeClientInfo(ClientBean clientBean) throws Exception {
        Client oldClient = this.getClientDAO().loadClient(this.getTempClientBean().getEmail());
        if (oldClient != null) {
            Client newClient = oldClient.clone();

            String name = clientBean.getName();
            if (name != null) newClient.changeName(name);

            String email = clientBean.getEmail();
            if (email != null) newClient.changeEmail(email);

            String phone = clientBean.getPhone();
            if (phone != null) newClient.changePhone(phone);

            AddressBean addressBean = clientBean.getAddressBean();
            if (addressBean != null) {
                Address address = new Address(addressBean.getAddress(), addressBean.getCap(), addressBean.getCity(),
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
