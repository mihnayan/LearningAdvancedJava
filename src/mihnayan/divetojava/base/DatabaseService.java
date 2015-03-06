package mihnayan.divetojava.base;

public interface DatabaseService extends Abonent {

    UserDataSet getUser(UserId id);
}
