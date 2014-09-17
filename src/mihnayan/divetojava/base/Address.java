package mihnayan.divetojava.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents abonent address
 * @author Mikhail Mangushev
 *
 */
public class Address {
	static private AtomicInteger abonentCreator = new AtomicInteger();
	final private int abonentId;
	
	public Address() {
		this.abonentId = abonentCreator.incrementAndGet();
	}
	
	@Override
	public int hashCode() {
		return abonentId;
	}
}
