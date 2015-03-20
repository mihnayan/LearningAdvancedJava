package mihnayan.divetojava.base;


/**
 * Represents a single entry in the table 'USER'.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class User {

    private UserId id;
    private String username;
    private String fullName;

    public User(UserId id, String username) {
        this(username);
        this.id = id;
    }

    /**
     * Creates a new entry with a required field "username".
     * @param username Name of user (login).
     */
    public User(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof User)) {
            return false;
        }
        
        User other = (User) obj;
        return id.equals(other.id) && username.equals(other.username);
    }

    @Override
    public String toString() {
        return "name: " + username + "; id: " + id.toString();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
    
    
}
