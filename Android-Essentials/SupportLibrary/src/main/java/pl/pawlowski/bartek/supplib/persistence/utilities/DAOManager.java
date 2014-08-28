package pl.pawlowski.bartek.supplib.persistence.utilities;

import java.util.HashMap;

import pl.pawlowski.bartek.supplib.persistence.DAO.AbstractDAO;
import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractEntity;


/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 */
public class DAOManager {

    private static DAOManager instance;

    private HashMap<Class<? extends AbstractEntity>, AbstractDAO<? extends AbstractEntity, Integer>> registeredDAOs;

    public static DAOManager getInstance(){
        if(instance == null){
            instance = new DAOManager();
        }

        return instance;
    }

    private DAOManager(){
        registeredDAOs = new HashMap<Class<? extends AbstractEntity>, AbstractDAO<? extends AbstractEntity, Integer>>();
    }

    public AbstractDAO<? extends AbstractEntity, Integer> getDAO(Class<? extends AbstractEntity> daoEntityClass){
        return registeredDAOs.get(daoEntityClass);
    }

    public void registerDAO(Class<? extends AbstractEntity> daoEntityClass, AbstractDAO<? extends AbstractEntity, Integer> dao){
        registeredDAOs.put(daoEntityClass, dao);
    }

    public void unregisterDAO(Class<? extends AbstractEntity> daoEntityClass){
        registeredDAOs.remove(daoEntityClass);
    }
}
