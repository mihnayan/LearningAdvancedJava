package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class for working with database queries.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class Executor {

    /**
     * Executes a SQL statement against the connection and returns the number of rows affected.
     * @param con Connection that is used for executing of SQL command.
     * @param sql SQL string.
     * @return Number of row affected after command execution.
     */
    public static int execCommand(Connection con, String sql) {
        Statement stmt = null;
        int updateCount = 0;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
            updateCount = stmt.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return updateCount;
    }

    /**
     * Executes a SQL query against the connection and returns the number of rows affected.
     * @param con Connection that is used for executing of SQL command.
     * @param sql SQL string.
     * @param handler Handler that handles result of query.
     */
    public static void execQuery(Connection con, String sql, ResultHandler handler) {

    }

    private Executor() {

    }

}
