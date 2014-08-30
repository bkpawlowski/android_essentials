package pl.pawlowski.bartek.supplib.GUI.views;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pl.pawlowski.bartek.supplib.alarm_management.GUI.AlarmStateChangeListenerFragment;

/**
 * Created by Bartek Garet Pawlowski on 2014-08-30.
 */
public abstract class ActionBarMenuFragment extends AlarmStateChangeListenerFragment {
    /**
     * OptionsMenu
     */
    private Menu actionBarOptionsMenu;

    /**
     * Custom actionbar display options, see ActionBar class
     */
    private int customActionBarDisplayOptions;

    /**
     * Default actionbar display options, that probably came from activity that configured ActionBar instance
     * - see ActionBar class
     */
    private int defaultActionBarDisplayOptions;

    public ActionBarMenuFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        defaultActionBarDisplayOptions = getActivity().getActionBar().getDisplayOptions();
        customActionBarDisplayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        initialiseActionBar(getActivity().getActionBar());
    }

    /**
     * Initialises actionbar settings in onCreateView method. Its called withing onResume callback body
     * @param actionBar - actionbar to be initialised
     */
    public abstract void initialiseActionBar(ActionBar actionBar);

    /**
     * Sets menu items visible
     *
     * @param visible - true if you want menu items to be visible, false otherwise
     */
    protected void setMenuItemsVisible(boolean visible) {
        if (actionBarOptionsMenu != null) {
            int size = actionBarOptionsMenu.size();
            MenuItem item = null;
            for (int i = 0; i < size; i++) {
                item = actionBarOptionsMenu.getItem(i);
                item.setVisible(visible);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.actionBarOptionsMenu = menu;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public Menu getActionBarOptionsMenu() {
        return actionBarOptionsMenu;
    }

    public int getCustomActionBarDisplayOptions() {
        return customActionBarDisplayOptions;
    }

    public int getDefaultActionBarDisplayOptions() {
        return defaultActionBarDisplayOptions;
    }
}
