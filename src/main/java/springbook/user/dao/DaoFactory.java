package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        final ConnectionMaker connectionMaker = new DConnectionMaker();
        return new UserDao(connectionMaker);
    }

}
