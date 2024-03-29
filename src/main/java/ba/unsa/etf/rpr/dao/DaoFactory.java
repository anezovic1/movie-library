package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Administrator;
import ba.unsa.etf.rpr.domain.User;
import ba.unsa.etf.rpr.domain.Watchlist;

/**
 * Factory method for singleton implementation od DAOs
 * @author Anida Nezovic
 */
public class DaoFactory {
    public DaoFactory() {
    }

    private static final MovieDao movieDao = new MovieDaoSQLImpl();
    private static final WatchlistDao watchlistDao = new WatchlistDaoSQLImpl();
    private static final UserDao userDao = new UserDaoSQLImpl();
    private static final AdministratorDao adminDao = new AdministratorDaoSQLImpl();
    public static MovieDao movieDao() {
        return movieDao;
    }
    public static WatchlistDao watchlistDao() {
        return watchlistDao;
    }
    public static UserDao userDao() {
        return userDao;
    }
    public static AdministratorDao adminDao() {
        return adminDao;
    }

}
