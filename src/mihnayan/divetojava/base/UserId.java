package mihnayan.divetojava.base;

/**
 * Class represented user Id on the system. User Id can be obtained through class implementing
 * AccountService interface.
 * @author Mikhail Mangushev (Mihnayan)
 * @see AccountService#getUserId(String)
 */
public class UserId {

    private int id;

    /**
     * User Id constructor.
     * @param id Unique int identifier
     */
    public UserId(int id) {
        this.id = id;
    }
    
    public UserId(String id) {
        // method stab for working with user id in String format
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UserId)) {
            return false;
        }
        UserId other = (UserId) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ((Integer) id).toString();
    }

}
