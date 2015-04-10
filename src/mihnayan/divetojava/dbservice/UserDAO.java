package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;

public class UserDAO {
    
    private final Connection con;
    
    private final static String TABLE = "user";
    private final static String P_KEY = "id";
    private final static String FIELD_USERNAME = "username";
    private final static String FIELD_FULLNAME = "fullname";
    
    private final static String SELECT_QUERY_PATTERN = "select %s from %s";
    
    private final static String ADD_QUERY_PATTERN = 
            String.format("insert into %s(%s, %s) values(?, ?)", TABLE, FIELD_USERNAME, 
                    FIELD_FULLNAME);
    
    private final static String DELETE_QUERY_PATTERN =
            String.format("delete from %s where id=?", TABLE);
    
    private final static ResultHandler<User> resultHandler =
            new ResultHandler<User>() {

        public User handle(ResultSet resultSet) throws SQLException {
            User dataSet = null;
            if (resultSet.next()) {
                UserId id = new UserId(resultSet.getString(P_KEY));
                dataSet = new User(id, resultSet.getString(FIELD_USERNAME));
                dataSet.setFullName(resultSet.getString(FIELD_FULLNAME));
            }
            return dataSet;
        }
    };

    public UserDAO(Connection con) {
        this.con = con;
    }
    
    public User get(UserId id) throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN + " where %s=?", "*", TABLE, P_KEY);
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, id.toString());
        
        return Executor.execQuery(stmt, resultHandler);
    }
    
    public User getByUsername(String username) throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN
                + " where %s=?", "*", TABLE, FIELD_USERNAME);
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, username);
        
        return Executor.execQuery(stmt, resultHandler);
    }
    
    public List<User> getUsers() throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN, "*", TABLE);
        PreparedStatement stmt = con.prepareStatement(sql);
        
        return Executor.execQuery(stmt, new ResultHandler<List<User>>() {
            public List<User> handle(ResultSet resultSet) throws SQLException {
                User dataSet = null;
                List<User> list = new ArrayList<>();
                while (resultSet.next()) {
                    UserId id = new UserId(resultSet.getString(P_KEY));
                    dataSet = new User(id, resultSet.getString(FIELD_USERNAME));
                    dataSet.setFullName(resultSet.getString(FIELD_FULLNAME));
                    list.add(dataSet);
                }
                return list;
            }
        });
    }
    
    public void add(User dataSet) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(ADD_QUERY_PATTERN);
        stmt.setString(1, dataSet.getUsername());
        stmt.setString(2, dataSet.getFullName());
        stmt.executeUpdate();
    }

    public void delete(UserId id) throws SQLException {
        String sql = String.format(DELETE_QUERY_PATTERN, P_KEY);
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, id.toString());
        
        stmt.execute();
    }
}
