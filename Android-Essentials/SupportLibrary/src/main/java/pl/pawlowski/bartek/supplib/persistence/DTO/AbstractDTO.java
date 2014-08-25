package pl.pawlowski.bartek.supplib.persistence.DTO;

import android.os.Parcelable;

/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 */
public abstract class AbstractDTO implements Identifiable, Parcelable {

    protected AbstractDTO() {

    }

    protected Integer id;

    protected Object obj;

    public abstract void merge(AbstractDTO dto);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
