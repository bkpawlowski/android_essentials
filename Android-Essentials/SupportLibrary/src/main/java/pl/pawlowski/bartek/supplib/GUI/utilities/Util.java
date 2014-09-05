package pl.pawlowski.bartek.supplib.GUI.utilities;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bartek Garet Pawlowski on 2014-09-01.
 */
public class Util {
    public static void setViewsEnabled(ViewGroup layout, boolean enabled) {
        layout.setEnabled(enabled);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setViewsEnabled((ViewGroup) child, enabled);
            } else {
                child.setEnabled(enabled);
            }
        }
    }
}
