package mihnayan.divetojava.base;

/**
 * Abstract class representing the message.
 * @author Mikhail Mangushev (Mihnayan)
 */
public abstract class Msg {
    protected final Address from;
    protected final Address to;

    /**
     * Message constructor.
     * @param from Sender
     * @param to Recipient
     */
    public Msg(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Returns Sender address.
     * @return Address object
     */
    public Address getFrom() {
        return from;
    }

    /**
     * Returns Recipient address.
     * @return Address object
     */
    public Address getTo() {
        return to;
    }

    /**
     * Starts execution of instructions that are stored in the abonent.
     * @param abonent Executes instructions of abonent.
     */
    public abstract void exec(Abonent abonent);
}
