package mihnayan.divetojava.base;

import javax.servlet.http.HttpSession;

/**
 * Wrapper under HttpSession object.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class UserSession {

    private HttpSession session;
    private User user;
    
    public UserSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getHttpSession() {
        return session;
    }
    
    public String getId() {
        return session.getId();
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
}
