package mihnayan.divetojava.msgsystem;

/**
 * Class representing the message
 * @author Mikhail Mangushev
 *
 */
public abstract class Msg {
	final private Address from;
	final private Address to;
	
	public Msg(Address from, Address to) {
		this.from = from;
		this.to = to;
	}
	
	protected Address getFrom() {
		return from;
	}
	
	protected Address getTo() {
		return to;
	}
	
	/**
	 * Starts execution of instructions that are stored in the abonent
	 * @param abonent
	 */
	public abstract void exec(Abonent abonent);
}
