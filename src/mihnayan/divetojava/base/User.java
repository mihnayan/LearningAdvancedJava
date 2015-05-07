package mihnayan.divetojava.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * Represents a single entry in the table 'USER'.
 * @author Mikhail Mangushev (Mihnayan)
 */
@Entity
@Table(name="user")
public final class User {

    @Id
    @Column(name="id")
    private String id;
    
    @Column(name="username")
    private String username;
    
    @Column(name="fullname")
    private String fullName;

    public User(String id, String username) {
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

    public String getId() {
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
