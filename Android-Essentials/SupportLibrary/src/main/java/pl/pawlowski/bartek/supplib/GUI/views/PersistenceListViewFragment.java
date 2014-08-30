package pl.pawlowski.bartek.supplib.GUI.views;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import pl.pawlowski.bartek.supplib.GUI.ContextMenu.SelectableListViewWithContextMenu;
import pl.pawlowski.bartek.supplib.R;
import pl.pawlowski.bartek.supplib.persistence.DAO.AbstractDAO;
import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractEntity;
import pl.pawlowski.bartek.supplib.persistence.utilities.DAOManager;
import pl.pawlowski.bartek.supplib.persistence.utilities.PersistenceListViewAdapter;

/**
 * Created by Bartosz Garet Pawlowski on 27.03.14.
 *
 * Fragment that has its superclasses capabilities plus
 * handles default actions on listview items
 *
 */
public abstract class PersistenceListViewFragment<AdapterItemClass extends AbstractEntity> extends ActionBarMenuListFragment<AdapterItemClass> {
    private static final String EDITOR_FRAGMENT_TAG = "111--x-23423-ed-fragment-tag";
    private static final String SELECTION_MODE_KEY = "--ssk";
    private static final String SELECTED_ITEMS_KEY = "i--ssk";

    protected PersistenceListViewAdapter<AdapterItemClass> listViewAdapter;

    protected ItemListEditorFragment itemEditorFragment;

    protected AbstractDAO<AdapterItemClass, Integer> dao;

    protected Class<? extends ItemListEditorFragment> editorFragmentClass;

    protected ListView listView;

    private View selectionActionBarView;

    private boolean selectionMode;

    private boolean selectionModeEnabled;

    protected PersistenceListViewFragment(){
        dao = (AbstractDAO<AdapterItemClass, Integer>) DAOManager.getInstance().getDAO(getEntityClass());
        listViewAdapter = getAdapter();
        listViewAdapter.setItemList(getRecordsFromDatabase());
        editorFragmentClass = getEditorFragmentClass();
        selectionModeEnabled = true;
    }

    protected abstract ListView getListView(View view);

    protected abstract Class<? extends AbstractEntity> getEntityClass();

    protected abstract PersistenceListViewAdapter getAdapter();

    protected abstract Class<? extends ItemListEditorFragment> getEditorFragmentClass();

    /**
     * @return
     *      List of records to be displayed in this fragment
     */
    protected List<AdapterItemClass> getRecordsFromDatabase(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new LinkedList<AdapterItemClass>();
        }
    }

    /**
     * @return  - id of the container where new fragments may be attached
     */
    protected abstract int getFragmentHookContainerId();

    public boolean isSelectionModeEnabled() {
        return selectionModeEnabled;
    }

    public void setSelectionModeEnabled(boolean selectionModeEnabled) {
        this.selectionModeEnabled = selectionModeEnabled;
    }

    @Override
    public void onDetach() {
        setSelectionMode(false);
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SELECTION_MODE_KEY, selectionMode);
        if(selectionMode) {
            outState.putIntegerArrayList(SELECTED_ITEMS_KEY, listViewAdapter.getSelectedItemsIds());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectionActionBarView = inflater.inflate(R.layout.confirm_actionbar_layout, null);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.action_done) {
                    List<Integer> selectedItems = getSelectedItemsList();
                    removeItems(selectedItems);
                    setSelectionMode(false);
                } else if (i == R.id.action_cancel) {
                    setSelectionMode(false);
                } else if(i == R.id.action_selectall){
                    CheckBox cb = (CheckBox) v;
                    listViewAdapter.setAllItemsSelected(cb.isChecked());
                }
            }
        };

        selectionActionBarView.findViewById(R.id.action_done).setOnClickListener(listener);
        selectionActionBarView.findViewById(R.id.action_cancel).setOnClickListener(listener);
        selectionActionBarView.findViewById(R.id.action_selectall).setOnClickListener(listener);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null) {
            setSelectionMode(savedInstanceState.getBoolean(SELECTION_MODE_KEY));
            if(selectionMode) {
                List<Integer> selectedItems = savedInstanceState.getIntegerArrayList(SELECTED_ITEMS_KEY);
                listViewAdapter.setItemsSelected(selectedItems);
            }
        }

        listView = getListView(view);
        listView.setAdapter(listViewAdapter);

        if(listView instanceof SelectableListViewWithContextMenu){
            SelectableListViewWithContextMenu listViewWithContextMenu = (SelectableListViewWithContextMenu) listView;
            listViewWithContextMenu.registerForItemSelectionContextMenu(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.listview_menu, menu);

        int size = menu.size();
        MenuItem item;
        for (int i = 0; i < size; i++) {
            item = menu.getItem(i);
            if(item.getItemId() == R.id.action_listview_menu_remove) {
                item.setVisible(selectionModeEnabled);
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_listview_menu_new) {
            showCreateNewItemFragment();
        } else if (i == R.id.action_listview_menu_remove) {
            setSelectionMode(true);
        } else if (i == R.id.action_listview_menu_order) {
            Toast.makeText(getActivity(), "Change item order LOL", Toast.LENGTH_LONG).show();
        } else {

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets this fragment into "Selection" mode where you can go across listview item and
     * select them ie. to be removed
     * @param selectionMode - true if you want to turn on selectionMode, false otherwise
     */
    protected void setSelectionMode(boolean selectionMode){
        this.selectionMode = selectionMode;
        listViewAdapter.setSelectionMode(selectionMode);

        ActionBar actionBar = getActivity().getActionBar();
        if(selectionMode){
            actionBar.setCustomView(selectionActionBarView);
            actionBar.setDisplayOptions(getCustomActionBarDisplayOptions());
        }else{
            actionBar.setDisplayOptions(getDefaultActionBarDisplayOptions());
        }

        //niewidoczne jesli jestesmy w trybie wybierania
        setMenuItemsVisible(!selectionMode);

        setBackButtonHandlingLocked(selectionMode);
    }

    /**
     * For selection mode purpose
     */
    private void setMenuItemsVisible(){
        setMenuItemsVisible(!selectionMode);
    }

    @Override
    protected void onBackButtonPressed() {
        if(selectionMode){
            setSelectionMode(false);
        }else{
            super.onBackButtonPressed();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        setMenuItemsVisible();
    }

    /**
     * @return
     *      List of Ids of currently selected items
     */
    protected List<Integer> getSelectedItemsList(){
        return listViewAdapter.getSelectedItemsIds();
    }

    /**
     * Removes the item identified by id passed in parameter
     * @param itemId - id of the item to be removed and persists it
     */
    protected void removeItem(Integer itemId){
        try {
            dao.deleteById(itemId);
            listViewAdapter.removeItemById(itemId);
        } catch (SQLException e) {

            //TODO komunikat na maske do aktywnosci o bledzie
            e.printStackTrace();
        }
    }

    /**
     * Removes the items identified by ids passed in parameter and persists it
     * @param itemIds - ids of the items to be removed
     */
    protected void removeItems(List<Integer> itemIds){
        try {
            dao.deleteIds(itemIds);
            listViewAdapter.removeItemsById(itemIds);
        } catch (SQLException e) {
            //TODO komunikat na maske do aktywnosci o bledzie
            e.printStackTrace();
        }
    }

    /**
     * Adds new item to this view and persists it
     * @param item - item to be added
     */
    protected void addNewItem(AdapterItemClass item){
        try {
            dao.create(item);
            listViewAdapter.addItem(item);
        } catch (SQLException e) {
            //TODO komunikat na maske do aktywnosci o bledzie
            e.printStackTrace();
        }
    }

    /**
     * Updates item passed in parameter
     * @param item - updated version of the item
     */
    protected void updateItem(AdapterItemClass item){
        AdapterItemClass existingItem = listViewAdapter.getItemById(item.getId());
        try {
            dao.update(existingItem);
            existingItem.merge(item);
        } catch (SQLException e) {
            //TODO komunikat na maske do aktywnosci o bledzie
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSubmitted(AdapterItemClass submittedItem) {
        if(submittedItem.getId() == null){
            addNewItem(submittedItem);
        }else{
            updateItem(submittedItem);
        }
    }

    /**
     * Shows fragment for creating new item purposes
     */
    protected void showCreateNewItemFragment(){
        showItemEditorFragment(null);
    }

    /**
     * Shows fragment for editing item purposes
     * @param item - item to be edited
     */
    protected void showItemEditorFragment(AdapterItemClass item){
        if(editorFragmentClass == null){
            Toast.makeText(getActivity(), "EditorFragment Class NULL!", Toast.LENGTH_SHORT).show(); //TODO wyjebac
        }else {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment f = fm.findFragmentByTag(EDITOR_FRAGMENT_TAG);

            if (f == null) {
                try {
                    itemEditorFragment = editorFragmentClass.newInstance();
                    itemEditorFragment.setOnViewResultSubmittedListener(this);
                }catch(Exception e){
                    e.printStackTrace();
                }
            } else {
                itemEditorFragment = (ItemListEditorFragment) f;
                itemEditorFragment.setOnViewResultSubmittedListener(this);
            }

            FragmentTransaction ft = fm.beginTransaction();
            if (ft != null) {
                Bundle arguments = null;
                if (item != null) {
                    arguments = new Bundle();
                    arguments.putParcelable(ItemListEditorFragment.PASSED_ITEM_KEY, item);
                }
                //przekazanie Itemu do fragmentus(arguments);
                itemEditorFragment.setArguments(arguments);

                int containerId = getFragmentHookContainerId();
                ft.replace(containerId, itemEditorFragment, EDITOR_FRAGMENT_TAG);
//                ft.add(containerId, itemEditorFragment, EDITOR_FRAGMENT_TAG);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}
