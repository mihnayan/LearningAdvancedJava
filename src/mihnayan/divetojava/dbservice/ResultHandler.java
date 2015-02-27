package mihnayan.divetojava.dbservice;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents callback method for using in queries to database.
 * @author Mikhail Mangushev (Mihnayan)
 * @param <T> Any type returned after handling the query results.
 */
public interface ResultHandler<T> {

    /**
     * Method that handles result of query.
     * @param result Result of query.
     * @return An object of type T obtained after handling of ResultSet.
     * @throws SQLException occurs when the database query fails.
     */
    T handle(ResultSet result) throws SQLException;
}
