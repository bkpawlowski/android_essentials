package pl.pawlowski.bartek.supplib.GUI.ContextMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.pawlowski.bartek.supplib.GUI.views.SelectableListView;
import pl.pawlowski.bartek.supplib.persistence.DTO.Identifiable;


/**
 * Created by Bartosz Garet Pawlowski on 24.03.14.
 */
public class SelectableListViewWithContextMenu extends SelectableListView implements OnContextMenuItemSelectedListener{

    protected List<OnContextMenuItemSelectedListener> onContextMenuItemSelectedListeners;

    protected ContextMenuProvider contextMenuProvider;

    protected ContextMenuDialogFragment contextMenu;

    private ArrayList<ContextMenuItem> contextMenuItems;

    public SelectableListViewWithContextMenu(Context context) {
        super(context);
        onContextMenuItemSelectedListeners = new LinkedList<OnContextMenuItemSelectedListener>();
    }

    public SelectableListViewWithContextMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        onContextMenuItemSelectedListeners = new LinkedList<OnContextMenuItemSelectedListener>();
    }

    public SelectableListViewWithContextMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onContextMenuItemSelectedListeners = new LinkedList<OnContextMenuItemSelectedListener>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return SelectableListViewWithContextMenu.this.onItemLongClick(parent, view, position, id);
            }
        });

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectableListViewWithContextMenu.this.onItemShortClick(parent, view, position, id);
            }
        });
    }

    public void setContextMenuItems(ArrayList<ContextMenuItem> contextMenuItems){
        this.contextMenuItems = contextMenuItems;
    }

    public void addOnContextMenuItemSelectedListener(OnContextMenuItemSelectedListener listener){
        if(listener != null){
            onContextMenuItemSelectedListeners.add(listener);
        }
    }

    public void removeOnContextMenuItemSelectedListener(OnContextMenuItemSelectedListener listener){
        if(listener != null){
            onContextMenuItemSelectedListeners.remove(listener);
        }
    }

    public void registerForItemSelectionContextMenu(ContextMenuProvider provider){
        contextMenuProvider = provider;
        initContextMenu();
    }

    protected boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        Context ctx = getContext();
        if(ctx instanceof FragmentActivity){
            if(contextMenuItems.size() > 0) {
                if (!contextMenu.isAdded()) {
                    Bundle arguments = new Bundle();
                    Identifiable item = (Identifiable) getAdapter().getItem(position);
                    arguments.putInt(ContextMenuDialogFragment.ITEM_ID, item.getId());
                    contextMenu.setArguments(arguments);

                    contextMenu.show(((FragmentActivity) ctx).getSupportFragmentManager(), "contextMenu");
                }else{
                    Log.i(getClass().getSimpleName(), "ContextMenu already visible");
                }
            }else{
                Log.i(getClass().getSimpleName(), "ContextMenuItems.size() = 0");
            }
        }else{
            Log.e(getClass().getSimpleName(), "Activities different than FragmentActivity are not supported");
            throw new RuntimeException("THIS SHOULD BE USED INSIDE A FRAGMENT!");
        }

        return false;
    }

    protected void onItemShortClick(AdapterView<?> parent, View view, int position, long id){

    }

    @Override
    public void onContextMenuItemSelected(Integer contextMenuItemId, Integer itemId) {
        for (OnContextMenuItemSelectedListener listener : onContextMenuItemSelectedListeners){
            listener.onContextMenuItemSelected(contextMenuItemId, itemId);
        }
    }

    private void initContextMenu(){
        if(contextMenuProvider == null){
            Log.d(getClass().getSimpleName(), "contextMenuProvider == null");
        }else {
            contextMenuItems = contextMenuProvider.getContextMenuItems();
            if(contextMenuItems == null){
                contextMenuItems = new ArrayList<ContextMenuItem>();
            }
            contextMenu = new ContextMenuDialogFragment();
            contextMenu.setItems(contextMenuItems);

            contextMenu.addOnMenuItemSelectedListener(this);
            contextMenu.addOnMenuItemSelectedListener(contextMenuProvider);
        }
    }
}
