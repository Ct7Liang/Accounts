package android.develop.ct7liang.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import android.develop.ct7liang.accounts.bean.Account;
import android.develop.ct7liang.accounts.bean.Query;
import android.develop.ct7liang.accounts.bean.User;

import android.develop.ct7liang.greendao.AccountDao;
import android.develop.ct7liang.greendao.QueryDao;
import android.develop.ct7liang.greendao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig queryDaoConfig;
    private final DaoConfig userDaoConfig;

    private final AccountDao accountDao;
    private final QueryDao queryDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        queryDaoConfig = daoConfigMap.get(QueryDao.class).clone();
        queryDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        queryDao = new QueryDao(queryDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(Query.class, queryDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        accountDaoConfig.getIdentityScope().clear();
        queryDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public QueryDao getQueryDao() {
        return queryDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
