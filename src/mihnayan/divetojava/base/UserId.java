package mihnayan.divetojava.base;

/**
 * Class represented user Id on the system. User Id can be obtained through class implementing
 * AccountService interface.
 * @author Mikhail Mangushev (Mihnayan)
 * @see AccountService#getUserId(String)
 */
public final class UserId {

    private String id;

    /**
     * User Id constructor.
     * @param id Unique string identifier
     */
    public UserId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
       return id.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return id.equals(obj);
    }

    @Override
    public String toString() {
        return id;
    }

}
