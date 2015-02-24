package mihnayan.divetojava.dbservice;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents callback method for using in queries to database.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface ResultHandler {

    /**
     * Method that handles result of query.
     * @param result result of query.
     * @throws SQLException occurs when the database query fails.
     */
    void handle(ResultSet result) throws SQLException;
}
