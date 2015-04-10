package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.base.UserSession;

/**
 * Helper class that handle requests for user authentication.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
class LoginRequestHandler extends AbstractRequestHandler {

    private static Logger log = Logger.getLogger(LoginRequestHandler.class.getName());
    
    public static final ConcurrentHashMap<String, UserSession> SESSIONS =
            new ConcurrentHashMap<>();

    // field for user name in html form
    private static final String FRM_USER_NAME = "user-name";
    
    // HttpSession attribute names 
    private static final String AUTHSTATE_ATTR = "authState";
    private static final String USERNAME_ATTR = "userName";
    
    private String sessionId;
    private String userName;
    private UserId userId;
    private AuthState loginStatus;
    private String statusText;

    /**
     * Creates LoginRequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the login data (typically,
     *        the implementation of the AccountService interface).
     */
    public LoginRequestHandler(HttpServletRequest request, Frontend frontend) {
        super(request, frontend);
        
        HttpSession httpSession = request.getSession();
        sessionId = httpSession.getId();
        userName = (String) httpSession.getAttribute(USERNAME_ATTR);
        loginStatus = getAuthState(httpSession);

        switch (loginStatus) {
        case NEW:
            if (userName == null) {
                userName = request.getParameter(FRM_USER_NAME);
            }
            if (userName != null) {
                UserSession session = new UserSession(httpSession);
                httpSession.setAttribute(USERNAME_ATTR, userName);
                loginStatus = AuthState.WAITING;
                saveAuthState(httpSession, loginStatus);
                authenticateUserSession(session, userName);
            }
            break;
            
        case WAITING:
            break;
            
        case LOGGED:
            UserSession session = SESSIONS.get(sessionId);
            userId = session.getUser().getId();
            loginStatus = AuthState.LOGGED;
            break;
            
        case FAILED:
            httpSession.removeAttribute(USERNAME_ATTR);
            saveAuthState(httpSession, AuthState.NEW);
            break;
        }
    }
    
    static AuthState getAuthState(HttpSession httpSession) {
        if (SESSIONS.containsKey(httpSession.getId())) {
            return AuthState.LOGGED;
        }
        AuthState authState = (AuthState) httpSession.getAttribute(AUTHSTATE_ATTR);
        if (authState == null) {
            authState = AuthState.NEW;
        }
        return authState;
    }

    /**
     * Returns registered session by it session Id.
     * @param sessionId Session Id for session that must be returned.
     * @return UserSession object.
     */
    static UserSession getAuthenticatedSession(String sessionId) {
        return SESSIONS.get(sessionId);
    }

    static void setUser(UserSession userSession) {
        if (userSession.getUser() != null) {
            saveAuthState(userSession.getHttpSession(), AuthState.LOGGED);
            SESSIONS.put(userSession.getId(), userSession);
        } else {
            saveAuthState(userSession.getHttpSession(), AuthState.FAILED);
        }
    }
    
    static Set<User> getAuthenticatedUsers() {
        Set<User> users = new HashSet<>();
        for (UserSession userSession : SESSIONS.values()) {
            users.add(userSession.getUser());
        }
        return users;
    }
    
    private static void saveAuthState(HttpSession session, AuthState authState) {
        session.setAttribute(AUTHSTATE_ATTR, authState);
    }
    
    private void authenticateUserSession(UserSession userSession, String userName) {
        MessageService ms = frontend.getMessageService();
        Address to = ms.getAddressService().getAddress(AccountServer.class);
        ms.sendMessage(
                new MsgAuthenticateUserSession(frontend.getAddress(), to, userSession, userName));
    }

    @Override
    public void buildResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(toJSON());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rules of building user data.
     * User data are json format:
     * {
     *     sessionId: 0,
     *     userName: "",
     *     userId: "",
     *     loginStatus: AuthState,
     *     text: "some text"
     * }
     */
    @Override
    public String toJSON() {
        return "{"
                + "\"sessionId\": \"" + sessionId + "\", "
                + "\"userName\": \"" + (userName == null ? "" : userName) + "\", "
                + "\"userId\": \"" + (userId == null ? "" : userId) + "\", "
                + "\"loginStatus\": \"" + loginStatus + "\", "
                + "\"text\": \"" + statusText + "\""
                + "}";
    }

}
