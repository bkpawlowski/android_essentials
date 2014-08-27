package pl.pawlowski.bartek.supplib.persistence.DAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import pl.pawlowski.bartek.supplib.persistence.DTO.old_AbstractDTO;

/**
 * @Author Bartosz Garet Pawłowski on 15.03.14.
 */
public abstract class old_AbstractDAO<DTOClass extends old_AbstractDTO> {
    protected SQLiteDatabase database;

    protected old_AbstractDAO(){

    }

    public void setDatabaseReference(SQLiteDatabase database){
        this.database = database;
    }

    public abstract void createNew(DTOClass item);

    public abstract void delete(DTOClass item);

    public abstract void deleteItems(List<DTOClass> items);

    public abstract void delete(Integer itemId);

    public abstract void deleteItemsById(List<Integer> items);

    public abstract void update(DTOClass item);

    public abstract List<DTOClass> getAllRecords();
}
