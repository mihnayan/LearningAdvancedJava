package mihnayan.divetojava.resourcesystem;

/**
 * Resource that contains required parameters for connection to database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class DBConnectionResource implements Resource {

    private String driverClass;
    private String connectionString;
    private String userName;
    private String password;

    /**
     * @return Class name for database driver.
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * @return Connection string (URI) for connecting to database.
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * @return Username of database owner.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return Username password.
     */
    public String getPassword() {
        return password;
    }

}
