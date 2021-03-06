package mihnayan.divetojava.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import mihnayan.divetojava.base.User;
import mihnayan.divetojava.dbservice.Executor;
import mihnayan.divetojava.dbservice.UserDAO;

/**
 * Simple test class for testing classes that work with database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class DBTest {

//    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/test";
    public static final String USER = "testur";
    public static final String PASSWORD = "test";

    //SQLs
    public static final String DROP_TABLE = "DROP TABLE user";
    
    public static final String CREATE_TABLE =
            "CREATE TABLE user(id VARCHAR(40) PRIMARY KEY DEFAULT '', "
            + "username VARCHAR(50) NOT NULL UNIQUE, fullname VARCHAR(100))";
    
    public static final String CREATE_TRIGGER = 
            "CREATE TRIGGER user_BEFORE_INSERT "
            + "BEFORE INSERT ON user FOR EACH ROW "
            + " begin set new.id = uuid();  end";

    public static final String INSERT_DATA =
            "INSERT INTO user(user_name, full_name) VALUES ('jbond', 'James Bond'), "
            + "('fgump', 'Forest Gump'), ('sman', 'Superman')";

    public static final String SELECT_DATA =
            "SELECT * FROM user";

    //Columns index
    public static final int COL_ID = 1;
    public static final int COL_USER_NAME = 2;
    public static final int COL_FULL_NAME = 3;

    public static void printUser(User user) {
        System.out.println(user + " ("
                + user.getFullName() + ")");
        System.out.println("hashCode: " + user.hashCode() + "\n");
    }
    
    /**
     * Entry point of DBTest class.
     * @param args No parameters are required.
     */
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            System.out.println(con.getMetaData().getDatabaseProductName() + " "
                    + con.getMetaData().getDatabaseMajorVersion());

            //Drop table
            System.out.println(Executor.execCommand(con, DROP_TABLE));
            
            //Create table
            System.out.println(Executor.execCommand(con, CREATE_TABLE));
            
            //Create trigger
            System.out.println(Executor.execCommand(con, CREATE_TRIGGER));

            //Insert data
            
            User dataSet = new User("jbond");
            dataSet.setFullName("James Bond");
            UserDAO.add(con, dataSet);
            
            dataSet = new User("fgump");
            dataSet.setFullName("Forest Gump");
            UserDAO.add(con, dataSet);
            
            dataSet = new User("sman");
            dataSet.setFullName("superman");
            UserDAO.add(con, dataSet);

            //Select and handle data
            System.out.println("\n List of users: ");
            List<User> users = UserDAO.getUsers(con);
            for (User user : users) {
                printUser(user);
            }
            
            System.out.println("\n jbond by username: ");
            User bond = UserDAO.getByUsername(con, "jbond");
            printUser(bond);
            
            System.out.println("\n jbond by id: ");
            String bondId = bond.getId();
            printUser(UserDAO.get(con, bondId));
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private DBTest() {

    }
}
