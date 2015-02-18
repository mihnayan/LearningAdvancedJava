package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.CreateServiceException;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.resourcesystem.DBConnectionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

/**
 * Represents service for working with Database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class DatabaseService implements Abonent, Runnable {

    private static Logger log = Logger.getLogger(DatabaseService.class.getName());

    private static final int SLEEP_TIME = 100;

    private MessageService ms;
    private Address address;
    private DBConnectionResource connectionResource;
    private Connection connection;

    /**
     * @param ms Real message system for interaction with other components.
     * @throws CreateServiceException occurs when the service cannot be created
     */
    public DatabaseService(MessageService ms) throws CreateServiceException {
        try {
            connectionResource = (DBConnectionResource)
                    ResourceFactory.instance().get(DBConnectionResource.class);
            Class.forName(connectionResource.getDriverClass());
        } catch (ClassNotFoundException | ResourceNotExistException e) {
            throw new CreateServiceException(e);
        }
        this.ms = ms;
        address = new Address();
    }

    @Override
    public void run() {
        try {
            connection = DriverManager.getConnection(connectionResource.getConnectionString(),
                    connectionResource.getUserName(), connectionResource.getPassword());
        } catch (SQLException e) {
            log.log(Level.WARNING, e.getMessage());
        }

        while (true) {
            try {
                ms.execForAbonent(this);
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MessageService getMessageService() {
        return ms;
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
