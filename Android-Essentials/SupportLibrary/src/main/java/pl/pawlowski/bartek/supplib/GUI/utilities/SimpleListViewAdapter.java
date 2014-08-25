package pl.pawlowski.bartek.supplib.GUI.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author Bartosz Garet Pawłowski
 */
public abstract class SimpleListViewAdapter<AdapterItemClass, ViewHolderClass> extends BaseAdapter {

    /** App context */
    protected Context applicationContext;

    protected LayoutInflater inflater;

    protected List<AdapterItemClass> itemList;

    public SimpleListViewAdapter(Context context, List<AdapterItemClass> items){
        applicationContext = context;
        inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemList = items;
    }

    public SimpleListViewAdapter(Context context){
        applicationContext = context;
        inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setItemList(List<AdapterItemClass> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderClass viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(getViewItemLayoutId(), null);

            viewHolder = getViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderClass) convertView.getTag();
        }

        updateView(viewHolder, itemList.get(position));
        return convertView;
    }

    /**
     * @return
     *          Id of the layout to be inflated in superclass adapter
     */
    public abstract int getViewItemLayoutId();

    /**
     * @param inflatedView - recently created view using Id from getViewItemLayoutId method
     * @return
     *          ViewHolder to be used in superclass of this adapter
     */
    public abstract ViewHolderClass getViewHolder(View inflatedView);

    /**
     * @param viewHolder - previously passed viewHolder, to update view
     * @param item - item to be currently displayed
     */
    public abstract void updateView(ViewHolderClass viewHolder, AdapterItemClass item);

    /**
     * Adds item to item set and notifies the adapter about the change, to force reload its view
     * @param item - item to be added to adapter's set
     */
    public void addItem(AdapterItemClass item){
        if(item != null){
            itemList.add(item);

            notifyDataSetChanged();
        }
    }

    /**
     * @param position - position of the item in list
     * @return
     *          found item or exception is thrown if index is bigger than list size
     */
    public AdapterItemClass getItemByPosition(Integer position){
        return itemList.get(position);
    }
}
