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
    
    public static ConcurrentHashMap<String, UserSession> authenticatedSessions =
            new ConcurrentHashMap<>();

    private String processedSessionId;
    private String currentUserName;
    private UserId currentUserId = null;
    private AuthState loginStatus;
    private String statusText;

    private HttpSession processedSession;

    // field for user name in html form
    private static final String FRM_USER_NAME = "user-name";

    /**
     * Creates LoginRequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the login data (typically,
     *        the implementation of the AccountService interface).
     */
    public LoginRequestHandler(HttpServletRequest request, Frontend frontend) {
        super(request, frontend);
        processedSession = request.getSession();

        processedSessionId = processedSession.getId();
        
        // new
        UserSession userSession = authenticatedSessions.get(processedSessionId);
        if (userSession != null) {
             currentUserName = userSession.getUser().getUsername();
             currentUserId = userSession.getUser().getId();
        } else {
            currentUserName = (String) processedSession.getAttribute("userName");
            if (currentUserName == null) {
                currentUserName = request.getParameter(FRM_USER_NAME);
            }
        }
        
//        currentUserName = (String) processedSession.getAttribute("userName");
//        if (currentUserName == null) {
//            currentUserName = request.getParameter(FRM_USER_NAME);
//        }
//        currentUserId = (UserId) processedSession.getAttribute("userId");
        loginStatus = getAuthState(processedSession);
    }

    /**
     * Sets authentication status for session.
     * @param session The session for which status must be set.
     * @param authState The status which must be set.
     */
    public static void setAuthState(HttpSession session, AuthState authState) {
        session.setAttribute("authState", authState);
    }

    /**
     * Returns authentication status for session. if the session has no authentication status,
     * the status is set to the AuthState.NEW.
     * @param session The session status of which should be returned.
     * @return One of values of AuthState.
     */
    public static AuthState getAuthState(HttpSession session) {
        if (session.getAttribute("authState") == null || session.isNew()) {
            setAuthState(session, AuthState.NEW);
        }
        return (AuthState) session.getAttribute("authState");
    }

    /**
     * Returns registered session by it session Id.
     * @param sessionId Session Id for session that must be returned.
     * @return HttpSession object.
     */
    public static UserSession getAuthenticatedSession(String sessionId) {
        return authenticatedSessions.get(sessionId);
    }


    public static void setUser(UserSession userSession) {
        if (userSession.getUser() != null) {
            setAuthState(userSession.getHttpSession(), AuthState.LOGGED);
            authenticatedSessions.put(userSession.getId(), userSession);
        } else {
            setAuthState(userSession.getHttpSession(), AuthState.FAILED);
        }
    }
    
    public static Set<User> getAuthenticatedUsers() {
        Set<User> users = new HashSet<>();
        for (UserSession userSession : authenticatedSessions.values()) {
            users.add(userSession.getUser());
        }
        return users;
    }

    @Override
    public void buildResponse(HttpServletResponse response) {

        if (loginStatus == AuthState.NEW && currentUserName != null) {
            registerSession(processedSession, currentUserName);
            loginStatus = getAuthState(processedSession);
        }

        if (loginStatus == AuthState.FAILED) {
            unregisterSession(processedSession);
        }

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
                + "\"sessionId\": \"" + processedSessionId + "\", "
                + "\"userName\": \"" + (currentUserName == null ? "" : currentUserName) + "\", "
                + "\"userId\": \"" + (currentUserId == null ? "" : currentUserId) + "\", "
                + "\"loginStatus\": \"" + loginStatus + "\", "
                + "\"text\": \"" + statusText + "\""
                + "}";
    }

    private void registerSession(HttpSession session, String username) {
        
        UserSession userSession = new UserSession(session);

        session.setAttribute("userName", username);
        setAuthState(session, AuthState.WAITING);

        MessageService ms = frontend.getMessageService();
        Address to = ms.getAddressService().getAddress(AccountServer.class);
        
        ms.sendMessage(
                new MsgAuthenticateUserSession(frontend.getAddress(), to, userSession, username));
    }

    private void unregisterSession(HttpSession session) {
        setAuthState(session, AuthState.NEW);
        session.removeAttribute("userName");
//        sessions.remove(session.getId());
    }

}
