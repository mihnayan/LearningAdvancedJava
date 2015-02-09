package mihnayan.divetojava.dbservice;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;

/**
 * Represents service for working with Database.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class DatabaseService implements Abonent, Runnable {

    private MessageService ms;

    /**
     * @param ms Real message system for interaction with other components.
     */
    public DatabaseService(MessageService ms) {
        this.ms = ms;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageService getMessageService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Address getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

}
