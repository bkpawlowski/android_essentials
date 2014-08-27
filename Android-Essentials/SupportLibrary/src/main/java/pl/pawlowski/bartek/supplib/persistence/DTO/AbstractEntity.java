package pl.pawlowski.bartek.supplib.persistence.DTO;

import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Bartek Garet Pawlowski on 2014-08-27.
 */
public abstract class AbstractEntity implements Identifiable, Parcelable {

    @DatabaseField(index = true, generatedId = true)
    protected Integer id;

    protected Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    protected AbstractEntity(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract void merge(AbstractEntity entity);
}
