package mihnayan.divetojava.msgsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.Msg;

/**
 * Implementation of MessageService interface.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MessageSystem implements MessageService {

    private AddressSrv addressService = new AddressSrv();

    private Map<Address, ConcurrentLinkedQueue<Msg>> messages =
            new HashMap<Address, ConcurrentLinkedQueue<Msg>>();

    @Override
    public void sendMessage(Msg message) {
        getQueue(message.getTo()).add(message);
    }

    @Override
    public void execForAbonent(Abonent abonent) {
        Queue<Msg> messageQueue = getQueue(abonent.getAddress());
        while (!messageQueue.isEmpty()) {
            messageQueue.poll().exec(abonent);
        }
    }

    @Override
    public AddressSrv getAddressService() {
        return addressService;
    }

    private Queue<Msg> getQueue(Address address) {
        if (!messages.containsKey(address)) {
            messages.put(address, new ConcurrentLinkedQueue<Msg>());
        }
        return messages.get(address);
    }

}
