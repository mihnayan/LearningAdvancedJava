package mihnayan.divetojava.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the address of recipient in message system.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class Address {
    private static AtomicInteger abonentCreator = new AtomicInteger();
    private final int abonentId;

    /**
     * Constructs the address.
     */
    public Address() {
        this.abonentId = abonentCreator.incrementAndGet();
    }

    @Override
    public int hashCode() {
        return abonentId;
    }
}
