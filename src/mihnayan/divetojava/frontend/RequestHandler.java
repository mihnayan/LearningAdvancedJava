package mihnayan.divetojava.frontend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Abonent;

public abstract class RequestHandler {
	
	protected HttpServletRequest request;
	protected Abonent abonent;

	public RequestHandler(HttpServletRequest request, Abonent abonent) {
		this.request = request;
		this.abonent = abonent;
	}
	
	public abstract void handleRequest(HttpServletResponse response);
	
	public abstract String toJSON();

}
