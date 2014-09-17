package mihnayan.divetojava.base;


/**
 * Class representing the message
 * @author Mikhail Mangushev
 *
 */
public abstract class Msg {
	final protected Address from;
	final protected Address to;
	
	public Msg(Address from, Address to) {
		this.from = from;
		this.to = to;
	}
	
	public Address getFrom() {
		return from;
	}
	
	public Address getTo() {
		return to;
	}
	
	/**
	 * Starts execution of instructions that are stored in the abonent
	 * @param abonent
	 */
	public abstract void exec(Abonent abonent);
}
