package pl.pawlowski.bartek.supplib.GUI.ContextMenu;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.pawlowski.bartek.supplib.R;


/**
 * Przedstawia menu kontekstowe dedykowane elementowi ListView
 */
public class ContextMenuDialogFragment extends DialogFragment {

    /** Klucz do wyciagniecia Id itemu z bundle'a dla ktorego zostal powolany ten dialog do zycia*/
    public static final String ITEM_ID = "--x-item-id";

    /** Klucz do wyciagniecia elementow menu kontekstowego przy odtwarzaniu tego widoku*/
    public static final String CONTEXT_MENU_ITEMS = "-1xxx-savedstate-contextmenu-items";

    /** Lista tworzaca pozycje menu kontekstowego znajdujacego sie w tym oknie dialogowym*/
    private ArrayList<ContextMenuItem> items;

    /** Id itemu dla ktorego zostalo otwarte to okno*/
    private Integer itemId;

    private List<OnContextMenuItemSelectedListener> menuItemSelectedListeners;

    public void addOnMenuItemSelectedListener(OnContextMenuItemSelectedListener listener){
        if(listener != null){
            menuItemSelectedListeners.add(listener);
        }
    }

    public void removeOnMenuItemSelectedListener(OnContextMenuItemSelectedListener listener){
        if(listener != null){
            menuItemSelectedListeners.remove(listener);
        }
    }

    public ContextMenuDialogFragment() {
        //TODO przekazac item do ktorego podpinany jest dialog\
        items = getContextMenuItems();
        menuItemSelectedListeners = new LinkedList<OnContextMenuItemSelectedListener>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.contextmenu_popup_window, null);
        ListView listView = (ListView) view.findViewById(R.id.CTXM_ListView);

        List<ContextMenuItem> localItems;
        if(savedInstanceState == null) {
            Bundle arguments = getArguments();
            if(arguments != null){
                itemId = arguments.getInt(ITEM_ID);
            }

            localItems = items;
        }else{
            localItems = savedInstanceState.getParcelableArrayList(CONTEXT_MENU_ITEMS);
            itemId = savedInstanceState.getInt(ITEM_ID, -1);
        }

        BaseAdapter adapter = new ContextMenuListViewAdapter(getActivity(), localItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContextMenuItem selectedItem = items.get(position);

                for(OnContextMenuItemSelectedListener listener : menuItemSelectedListeners){
                    listener.onContextMenuItemSelected(position, itemId);
                }
                ContextMenuDialogFragment.this.dismiss();
            }
        });

        return view;
    }



    protected ArrayList<ContextMenuItem> getContextMenuItems(){
        return new ArrayList<ContextMenuItem>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ITEM_ID, itemId);
        outState.putParcelableArrayList(CONTEXT_MENU_ITEMS, items);

        super.onSaveInstanceState(outState);
    }
}
