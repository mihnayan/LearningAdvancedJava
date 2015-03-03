package mihnayan.divetojava.dbservice;

import mihnayan.divetojava.base.UserId;

/**
 * Represents a single entry in the table 'USER'.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class UserDataSet {

    private UserId id;
    private String username;
    private String fullName;

    public UserDataSet(UserId id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Creates a new entry with a required field "username".
     * @param username Name of user (login).
     */
    public UserDataSet(String username) {
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
}