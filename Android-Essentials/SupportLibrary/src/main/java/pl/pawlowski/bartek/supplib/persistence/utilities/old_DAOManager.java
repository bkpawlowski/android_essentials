package pl.pawlowski.bartek.supplib.persistence.utilities;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import pl.pawlowski.bartek.supplib.persistence.DAO.old_AbstractDAO;


/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 */
public class old_DAOManager {

    private static old_DAOManager instance;

    private HashMap<String, old_AbstractDAO<?>> registeredDAOs;

    public static old_DAOManager getInstance(){
        if(instance == null){
            instance = new old_DAOManager();
        }

        return instance;
    }

    private old_DAOManager(){
        registeredDAOs = new HashMap<String, old_AbstractDAO<?>>();
    }

    public old_AbstractDAO<?> getDAO(String daoIdentifier){
        return registeredDAOs.get(daoIdentifier);
    }

    public void registerDAO(String daoIdentifier, old_AbstractDAO<?> dao){
        registeredDAOs.put(daoIdentifier, dao);
    }

    public void unregisterDAO(String daoIdentifier){
        registeredDAOs.remove(daoIdentifier);
    }

    public void updateDAOsDatabaseReference(SQLiteDatabase database){
        for (old_AbstractDAO<?> dao : registeredDAOs.values()){
            dao.setDatabaseReference(database);
        }
    }
}
