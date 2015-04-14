package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.CreateServiceException;
import mihnayan.divetojava.base.DatabaseService;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.resourcesystem.DBConnectionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

/**
 * Represents service for working with Database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MainDatabaseService implements DatabaseService, Runnable {

    private static Logger log = Logger.getLogger(MainDatabaseService.class.getName());

    private static final int SLEEP_TIME = 100;

    private MessageService ms;
    private Address address;
    
    private String dbConnectionString;
    private String dbUser;
    private String dbUserPassword;

    /**
     * @param ms Real message system for interaction with other components.
     * @throws CreateServiceException occurs when the service cannot be created
     */
    public MainDatabaseService(MessageService ms) throws CreateServiceException {
        try {
            DBConnectionResource connectionResource = (DBConnectionResource)
                    ResourceFactory.instance().get(DBConnectionResource.class);
            Class.forName(connectionResource.getDriverClass());
            
            dbConnectionString = connectionResource.getConnectionString();
            dbUser = connectionResource.getUserName();
            dbUserPassword = connectionResource.getPassword();
        } catch (ClassNotFoundException | ResourceNotExistException e) {
            throw new CreateServiceException(e);
        }
        this.ms = ms;
        address = new Address();
    }

    @Override
    public void run() {
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
    
    @Override
    public User getUser(String username) {
        try (Connection conn = 
                DriverManager.getConnection(dbConnectionString, dbUser, dbUserPassword)) {
            return UserDAO.getByUsername(conn, username);
        } catch (SQLException e) {
            log.log(Level.WARNING, e.getMessage());
            return null;
        }
    }

}
