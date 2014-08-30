package pl.pawlowski.bartek.supplib.GUI.views;

import java.util.List;

import pl.pawlowski.bartek.supplib.GUI.ContextMenu.ContextMenuItem;
import pl.pawlowski.bartek.supplib.GUI.ContextMenu.ContextMenuProvider;

/**
 * Created by Bartosz Garet Pawlowski on 26.03.14.
 */
public abstract class ActionBarMenuListFragment<ItemClass> extends ActionBarMenuFragment
        implements OnViewResultSubmittedListener<ItemClass>, ContextMenuProvider {

    protected ActionBarMenuListFragment() {

    }

    @Override
    public List<ContextMenuItem> getContextMenuItems() {
        return null;
    }

    @Override
    public void onContextMenuItemSelected(Integer contextMenuId, Integer itemId) {

    }

    @Override
    public void onItemSubmitted(ItemClass submittedItem) {

    }
}
