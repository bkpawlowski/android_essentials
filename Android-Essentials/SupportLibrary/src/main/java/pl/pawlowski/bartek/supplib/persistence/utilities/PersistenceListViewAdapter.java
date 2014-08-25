package pl.pawlowski.bartek.supplib.persistence.utilities;

import android.content.Context;

import java.util.List;

import pl.pawlowski.bartek.supplib.GUI.utilities.SelectableListViewAdapter;
import pl.pawlowski.bartek.supplib.GUI.utilities.SelectableListViewItemViewHolder;
import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractDTO;


/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 */
public abstract class PersistenceListViewAdapter<DTOItemClass extends AbstractDTO> extends SelectableListViewAdapter<DTOItemClass, SelectableListViewItemViewHolder> {

    public PersistenceListViewAdapter(Context context, List<DTOItemClass> items) {
        super(context, items);
    }

    protected PersistenceListViewAdapter(Context context) {
        super(context);
    }

    /**
     * Removes item from this adapter by its position in list
     * @param itemPosition - item's position
     * @return id of removed item
     */
    public Integer removeItemByPosition(int itemPosition){
        if(itemList.size() < itemPosition){
            System.out.println("Unable to remove item: position to remove = "+itemPosition+" item collection size = "+ itemList.size());
            return null;
        }else{
            DTOItemClass removedItem = itemList.remove(itemPosition);
            notifyDataSetChanged();

            return removedItem.getId();
        }
    }

    /**
     * Removes item by its id
     * @param itemId - id of the item to be removed
     */
    public void removeItemById(Integer itemId){
        for (DTOItemClass item : itemList){
            if(item.getId() == itemId){
                itemList.remove(item);

                notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * Removes item by its id
     * @param itemIds - list of the item ids to be removed
     */
    public void removeItemsById(List<Integer> itemIds){
        for (Integer itemId : itemIds) {
            removeItemById(itemId);
        }
        notifyDataSetChanged();
    }

    public DTOItemClass getItemById(Integer itemId){
        DTOItemClass foundItem = null;
        for (DTOItemClass item : itemList){
            if(item.getId() == itemId){
                foundItem = item;
                break;
            }
        }

        return foundItem;
    }
}
