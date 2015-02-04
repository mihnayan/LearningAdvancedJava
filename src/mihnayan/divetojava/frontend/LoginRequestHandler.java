package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;

/**
 * Helper class that handle requests for user authentication.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class LoginRequestHandler extends RequestHandler {

    private static Logger log = Logger.getLogger(LoginRequestHandler.class.getName());

    private static ConcurrentHashMap<String, HttpSession> sessions =
            new ConcurrentHashMap<String, HttpSession>();
    public static Map<UserId, UserProfile> authenticatedUsers = new HashMap<UserId, UserProfile>();

    private String processedSessionId;
    private String currentUserName;
    private UserId currentUserId;
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
    public LoginRequestHandler(HttpServletRequest request, Abonent abonent) {
        super(request, abonent);
        processedSession = request.getSession();

        processedSessionId = processedSession.getId();
        currentUserName = (String) processedSession.getAttribute("userName");
        if (currentUserName == null) {
            currentUserName = request.getParameter(FRM_USER_NAME);
        }
        currentUserId = (UserId) processedSession.getAttribute("userId");
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
    public static HttpSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Creates and puts user profile to it session.
     * @param sessionId User session Id.
     * @param userId User Id.
     * @param userName Username.
     */
    //TODO: Probably this is unsafe method because client can to create and put user profile
    // for other session
    public static void setUser(String sessionId, UserId userId, String userName) {
        HttpSession session = sessions.get(sessionId);
        if (userId != null) {
            session.setAttribute("userId", userId);
            setAuthState(session, AuthState.LOGGED);
            UserProfile userProfile = new UserProfile();
            userProfile.setUserName(userName);
            authenticatedUsers.put(userId, userProfile);
        } else {
            setAuthState(session, AuthState.NOT_LOGGED);
        }
    }

    @Override
    public void handleRequest(HttpServletResponse response) {

        if (loginStatus == AuthState.NEW && currentUserName != null) {
            registerSession(processedSession, currentUserName);
            loginStatus = getAuthState(processedSession);
        }

        if (loginStatus == AuthState.NOT_LOGGED) {
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
                + "\"userId\": " + (currentUserId == null ? 0 : currentUserId) + ", "
                + "\"loginStatus\": \"" + loginStatus + "\", "
                + "\"text\": \"" + statusText + "\""
                + "}";
    }

    private void registerSession(HttpSession session, String username) {

        String sessionId = session.getId();

        session.setAttribute("userName", username);
        session.setAttribute("userId", null);
        setAuthState(session, AuthState.WAITING);
        sessions.put(sessionId, session);

        MessageService ms = abonent.getMessageService();
        Address to = ms.getAddressService().getAddress(AccountServer.class);

        ms.sendMessage(new MsgGetUserId(abonent.getAddress(), to, username, sessionId));
    }

    private void unregisterSession(HttpSession session) {
        setAuthState(session, AuthState.NEW);
        session.removeAttribute("userName");
        sessions.remove(session.getId());
    }

}
