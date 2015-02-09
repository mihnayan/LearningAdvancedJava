package mihnayan.divetojava.resourcesystem;

/**
 * Resource that contains required parameters for connection to database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class DBConnectionResource implements Resource {

    private String connectionString;
    private String userName;
    private String password;

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