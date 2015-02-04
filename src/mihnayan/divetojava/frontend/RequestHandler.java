package mihnayan.divetojava.frontend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Abonent;

/**
 * Abstract class that provides the interface of request processing.
 * @author Mikhail Mangushev (Mihnayan)
 */
public abstract class RequestHandler {

    protected HttpServletRequest request;
    protected Abonent abonent;

    /**
     * Creates and initializes the RequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the game data.
     */
    public RequestHandler(HttpServletRequest request, Abonent abonent) {
        this.request = request;
        this.abonent = abonent;
    }

    /**
     * Starts handling of request.
     * @param response HttpServletResponse object to write data sent to the client.
     */
    public abstract void handleRequest(HttpServletResponse response);

    /**
     * Returns request handling result as a JSON.
     * @return String containing JSON data.
     */
    public abstract String toJSON();
}
