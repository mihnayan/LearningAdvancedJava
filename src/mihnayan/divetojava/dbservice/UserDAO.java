package mihnayan.divetojava.dbservice;

import java.sql.SQLException;

import mihnayan.divetojava.base.UserId;

public class UserDAO {
    
    private final static String TABLE = "user";
    private final static String P_KEY = "id";
    private final static String FIELD_USERNAME = "username";
    private final static String FIELD_FULLNAME = "fullname";

    public UserDataSet get(UserId id) throws SQLException {
        return null;
    }
    
    public UserDataSet getByUsername(String username) throws SQLException {
        String sql = "";
        return null;
    }
    
    public void add(UserDataSet dataSet) throws SQLException {
        
    }

    public void delete(UserId id) throws SQLException {
        
    }
}
