package mihnayan.divetojava.dbservice;

import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import mihnayan.divetojava.accountsrv.AccountServer;
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

    // emulation of a long process
    private static final int SLEEP_TIME = 5000;

    private MessageService ms;
    private Address address;
    
    private String jdbcDriverClass;
    private String dbConnectionString;
    private String dbUser;
    private String dbUserPassword;
    
    private Configuration config;
    private SessionFactory sessionFactory;

    /**
     * @param ms Real message system for interaction with other components.
     * @throws CreateServiceException occurs when the service cannot be created
     */
    public MainDatabaseService(MessageService ms) throws CreateServiceException {
        try {
            DBConnectionResource connectionResource = (DBConnectionResource)
                    ResourceFactory.instance().get(DBConnectionResource.class);
            
            jdbcDriverClass = connectionResource.getDriverClass();
            dbConnectionString = connectionResource.getConnectionString();
            dbUser = connectionResource.getUserName();
            dbUserPassword = connectionResource.getPassword();
            Class.forName(jdbcDriverClass);
            
        } catch (ClassNotFoundException | ResourceNotExistException e) {
            throw new CreateServiceException(e);
        }
        this.ms = ms;
        address = new Address();
        ms.getAddressService().setAddress(this);
       
        registerHibernate();
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
    public void requestUserByName(String username) {
        User user = UserDAO.getByUsername(sessionFactory, username);
        String resultText = "";
        MsgSetAuthenticationResult msg = new MsgSetAuthenticationResult(address,
                ms.getAddressService().getAddress(AccountServer.class), username, user, resultText);
        ms.sendMessage(msg);
    }
    
    private void registerHibernate() {
        config = new Configuration();
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.connection.driver_class", this.jdbcDriverClass);
        config.setProperty("hibernate.connection.url", this.dbConnectionString);
        config.setProperty("hibernate.connection.username", this.dbUser);
        config.setProperty("hibernate.connection.password", this.dbUserPassword);
        
        config.addAnnotatedClass(User.class);
        
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(config.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        
        sessionFactory = config.buildSessionFactory(serviceRegistry);
        
    }

}
