package mihnayan.divetojava.dbservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import mihnayan.divetojava.base.User;

public class UserDAO {
    
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
                dataSet = new User(resultSet.getString(P_KEY), resultSet.getString(FIELD_USERNAME));
                dataSet.setFullName(resultSet.getString(FIELD_FULLNAME));
            }
            return dataSet;
        }
    };
    
    public static User get(Connection conn, String id) throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN + " where %s=?", "*", TABLE, P_KEY);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id.toString());
        
        return Executor.execQuery(stmt, resultHandler);
    }
    
    public static User get(SessionFactory sessionFactory, String id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.load(User.class, id);
        session.close();
        return user;
    }
    
    public static User getByUsername(Connection conn, String username) throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN
                + " where %s=?", "*", TABLE, FIELD_USERNAME);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        
        return Executor.execQuery(stmt, resultHandler);
    }
    
    public static User getByUsername(SessionFactory sessionFactory, String username) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("from User where username = :userName")
                .setString("userName", username).uniqueResult();
        session.close();
        return user;
    }
    
    public static List<User> getUsers(Connection conn) throws SQLException {
        String sql = String.format(SELECT_QUERY_PATTERN, "*", TABLE);
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        return Executor.execQuery(stmt, new ResultHandler<List<User>>() {
            public List<User> handle(ResultSet resultSet) throws SQLException {
                User dataSet = null;
                List<User> list = new ArrayList<>();
                while (resultSet.next()) {
                    dataSet = new User(resultSet.getString(P_KEY), resultSet.getString(FIELD_USERNAME));
                    dataSet.setFullName(resultSet.getString(FIELD_FULLNAME));
                    list.add(dataSet);
                }
                return list;
            }
        });
    }
    
    public static void add(Connection conn, User dataSet) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(ADD_QUERY_PATTERN);
        stmt.setString(1, dataSet.getUsername());
        stmt.setString(2, dataSet.getFullName());
        stmt.executeUpdate();
    }

    public static void delete(Connection conn, String id) throws SQLException {
        String sql = String.format(DELETE_QUERY_PATTERN, P_KEY);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id.toString());
        
        stmt.execute();
    }
    
    private UserDAO() {
        throw new AssertionError();
    }
}
