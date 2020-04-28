package com.tj.myandroid.greendao.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.tj.myandroid.greendao.Animals;

import com.tj.myandroid.greendao.dao.AnimalsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig animalsDaoConfig;

    private final AnimalsDao animalsDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        animalsDaoConfig = daoConfigMap.get(AnimalsDao.class).clone();
        animalsDaoConfig.initIdentityScope(type);

        animalsDao = new AnimalsDao(animalsDaoConfig, this);

        registerDao(Animals.class, animalsDao);
    }
    
    public void clear() {
        animalsDaoConfig.clearIdentityScope();
    }

    public AnimalsDao getAnimalsDao() {
        return animalsDao;
    }

}
