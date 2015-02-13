package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    private MessageService ms;
    private DBConnectionResource connectionResource;
    private Connection connection;

    /**
     * @param ms Real message system for interaction with other components.
     * @throws CreateServiceException occurs when the service cannot be created
     */
    public DatabaseService(MessageService ms) throws CreateServiceException {
        this.ms = ms;
        try {
            connectionResource = (DBConnectionResource)
                    ResourceFactory.instance().get(DBConnectionResource.class);
            Class.forName(connectionResource.getDriverClass());
        } catch (ClassNotFoundException | ResourceNotExistException e) {
            throw new CreateServiceException(e);
        }
    }

    @Override
    public void run() {
        DBConnectionResource dbcr;
        try {
            dbcr = (DBConnectionResource)
                    ResourceFactory.instance().get(DBConnectionResource.class);

            connection = DriverManager.getConnection(dbcr.getConnectionString(),
                    dbcr.getUserName(), dbcr.getPassword());
        } catch (ResourceNotExistException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageService getMessageService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Address getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

}
