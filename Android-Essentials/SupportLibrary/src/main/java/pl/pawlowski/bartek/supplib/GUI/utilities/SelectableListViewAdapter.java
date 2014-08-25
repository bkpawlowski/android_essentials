package pl.pawlowski.bartek.supplib.GUI.utilities;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractDTO;


/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 *
 * Extension of {@link SimpleListViewAdapter}
 * by adding the feature of handling items selection by checkboxes
 */
public abstract class SelectableListViewAdapter<
                    AdapterItemClass extends AbstractDTO,
                    ViewHolderClass extends SelectableListViewItemViewHolder>
        extends SimpleListViewAdapter<AdapterItemClass, ViewHolderClass> {

    /** Determines whether adapter should work in selection mode */
    protected boolean selectionMode;

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    public SelectableListViewAdapter(Context context, List<AdapterItemClass> items) {
        super(context, items);
        selectionMode = false;
    }

    protected SelectableListViewAdapter(Context context) {
        super(context);
    }

    @Override
    public void updateView(ViewHolderClass viewHolder, AdapterItemClass item) {
        if(selectionMode){
            viewHolder.checkBox.setVisibility(View.VISIBLE);

            if(item.getObj() == null || Boolean.FALSE.equals(item.getObj())){
                viewHolder.checkBox.setChecked(false);
            }else{
                viewHolder.checkBox.setChecked(true);
            }
        }else{
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets whether adapter should be in selectionMode
     * @param selectionMode
     */
    public void setSelectionMode(boolean selectionMode){
        this.selectionMode = selectionMode;
        setAllItemsSelected(false); //TODO to moze jakis watek?
        notifyDataSetChanged();
    }

    public boolean getSelectionMode() {
        return selectionMode;
    }

    /**
     * @param selection - flag that determines whether items in listview is marked as selected or not
     */
    public void setAllItemsSelected(boolean selection){
        for (AdapterItemClass item : itemList){
            item.setObj(selection);
        }

        notifyDataSetChanged();
    }

    /**
     * @param itemsToSelect - id of the items to be selected
     */
    public void setItemsSelected(List<Integer> itemsToSelect){
        if(itemsToSelect != null && itemsToSelect.size() > 0){
            for (Integer itemId : itemsToSelect){
                for (AdapterItemClass item : itemList){
                    if(itemId.equals(item.getId())){
                        item.setObj(true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @return
     *         list of currently selected items
     */
    public List<AdapterItemClass> getSelectedItems(){
        List<AdapterItemClass> selectedItems = new LinkedList<AdapterItemClass>();

        for (AdapterItemClass item : itemList){
            if(Boolean.TRUE.equals(item.getObj())){
                selectedItems.add(item);
            }
        }

        return selectedItems;
    }

    public ArrayList<Integer> getSelectedItemsIds(){
        ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        for (AdapterItemClass item : itemList){
            if(Boolean.TRUE.equals(item.getObj())){
                selectedItems.add(item.getId());
            }
        }

        return selectedItems;
    }

    /**
     * Handles change of selection checkbox state while item of listview is clicked by the user
     */
    public final class CheckBoxStateChanger implements AdapterView.OnItemClickListener{

        public CheckBoxStateChanger(){

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolderClass viewHolder = (ViewHolderClass) view.getTag();

            if(selectionMode){
                viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());

                AdapterItemClass item = getItemByPosition(position);
                item.setObj(viewHolder.checkBox.isChecked());
            }
        }
    }
}
