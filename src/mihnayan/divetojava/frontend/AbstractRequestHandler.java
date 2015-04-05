package mihnayan.divetojava.frontend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Frontend;

/**
 * Abstract class that provides the interface of request processing.
 * @author Mikhail Mangushev (Mihnayan)
 */
abstract class AbstractRequestHandler {

    protected HttpServletRequest request;
    protected Frontend frontend;

    /**
     * Creates and initializes the RequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the game data.
     */
    public AbstractRequestHandler(HttpServletRequest request, Frontend frontend) {
        this.request = request;
        this.frontend = frontend;
    }

    /**
     * Builds and sends the response to client.
     * @param response HttpServletResponse object to write data sent to the client.
     */
    public abstract void buildResponse(HttpServletResponse response);

    /**
     * Returns request handling result as a JSON.
     * @return String containing JSON data.
     */
    public abstract String toJSON();
}
