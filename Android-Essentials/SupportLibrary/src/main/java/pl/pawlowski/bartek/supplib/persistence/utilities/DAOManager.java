package pl.pawlowski.bartek.supplib.persistence.utilities;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import pl.pawlowski.bartek.supplib.persistence.DAO.AbstractDAO;


/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 */
public class DAOManager {

    private static DAOManager instance;

    private HashMap<String, AbstractDAO<?>> registeredDAOs;

    public static DAOManager getInstance(){
        if(instance == null){
            instance = new DAOManager();
        }

        return instance;
    }

    private DAOManager(){
        registeredDAOs = new HashMap<String, AbstractDAO<?>>();
    }

    public AbstractDAO<?> getDAO(String daoIdentifier){
        return registeredDAOs.get(daoIdentifier);
    }

    public void registerDAO(String daoIdentifier, AbstractDAO<?> dao){
        registeredDAOs.put(daoIdentifier, dao);
    }

    public void unregisterDAO(String daoIdentifier){
        registeredDAOs.remove(daoIdentifier);
    }

    public void updateDAOsDatabaseReference(SQLiteDatabase database){
        for (AbstractDAO<?> dao : registeredDAOs.values()){
            dao.setDatabaseReference(database);
        }
    }
}
