package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.ResultSet;
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
     * @param <T> Any type returned after handling the query results.
     * @return An object of type T which may contain data resulting from handling the request.
     */
    public static <T> T execQuery(Connection con, String sql, ResultHandler<T> handler) {
        Statement stmt = null;
        ResultSet result = null;
        T value = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
            result = stmt.getResultSet();
            value = handler.handle(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    private Executor() {

    }

}
