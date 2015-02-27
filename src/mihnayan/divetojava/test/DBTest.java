package mihnayan.divetojava.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import mihnayan.divetojava.dbservice.Executor;
import mihnayan.divetojava.dbservice.ResultHandler;

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
    public static final String CREATE_TABLE =
            "CREATE TABLE user(id INTEGER AUTO_INCREMENT PRIMARY KEY, "
            + "user_name VARCHAR(50) NOT NULL UNIQUE, full_name VARCHAR(100))";

    public static final String INSERT_DATA =
            "INSERT INTO user(user_name, full_name) VALUES ('jbond', 'James Bond'), "
            + "('fgump', 'Forest Gump'), ('sman', 'Superman')";

    public static final String SELECT_DATA =
            "SELECT * FROM user";

    //Columns index
    public static final int COL_ID = 1;
    public static final int COL_USER_NAME = 2;
    public static final int COL_FULL_NAME = 3;

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

            //Create table
            System.out.println(Executor.execCommand(con, CREATE_TABLE));

            //Insert data
            System.out.println(Executor.execCommand(con, INSERT_DATA));

            //Select and handle data
            HashMap<Integer, String> recs = Executor.execQuery(con, SELECT_DATA,
                    new ResultHandler<HashMap<Integer, String>>() {

                public HashMap<Integer, String> handle(ResultSet result) {
                    HashMap<Integer, String> vals = new HashMap<Integer, String>();
                    try {
                        while (result.next()) {
                            vals.put(result.getInt(COL_ID),
                                    result.getString(COL_USER_NAME)
                                    + " (" + result.getString(COL_FULL_NAME) + ")");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return vals;
                }
            });
            System.out.println(recs.toString());
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
