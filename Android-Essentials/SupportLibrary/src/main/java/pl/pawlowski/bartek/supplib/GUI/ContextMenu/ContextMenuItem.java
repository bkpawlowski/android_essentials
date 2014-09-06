package pl.pawlowski.bartek.supplib.GUI.ContextMenu;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * ContextMenu list element
 */
public class ContextMenuItem implements Parcelable{
    /** Id of the imageView*/
    public int itemImageViewId;

    /** Id of the textView*/
    public int itemTextViewId;

    public ContextMenuItem(int itemImageViewId, int itemTextViewId) {
        this.itemImageViewId = itemImageViewId;
        this.itemTextViewId = itemTextViewId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemImageViewId);
        parcel.writeInt(itemTextViewId);
    }

    public static final Creator<ContextMenuItem> CREATOR = new Creator<ContextMenuItem>() {
        @Override
        public ContextMenuItem createFromParcel(Parcel parcel) {
            ContextMenuItem contextMenuItem = new ContextMenuItem(
                    parcel.readInt(),
                    parcel.readInt()
            );

            return contextMenuItem;
        }

        @Override
        public ContextMenuItem[] newArray(int i) {
            return new ContextMenuItem[i];
        }
    };
}
