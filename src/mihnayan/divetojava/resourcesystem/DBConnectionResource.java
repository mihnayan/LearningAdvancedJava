package mihnayan.divetojava.resourcesystem;

/**
 * Resource that contains required parameters for connection to database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class DBConnectionResource implements Resource {

    private String driverClass;
    private String dbType;
    private String server;
    private String port;
    private String dbName;
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
        StringBuilder conStr = new StringBuilder();
        conStr.append(dbType).append("://").
               append(server).append(":").
               append(port).append("/").
               append(dbName);
        return conStr.toString();
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
