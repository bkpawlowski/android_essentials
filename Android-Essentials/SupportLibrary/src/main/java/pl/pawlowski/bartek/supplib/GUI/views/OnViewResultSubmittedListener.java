package pl.pawlowski.bartek.supplib.GUI.views;

/**
 * Created by Bartosz Garet Pawlowski on 24.03.14.
 */
public interface OnViewResultSubmittedListener<T>{
    void onItemSubmitted(T submittedItem);
    String getTag();
}
