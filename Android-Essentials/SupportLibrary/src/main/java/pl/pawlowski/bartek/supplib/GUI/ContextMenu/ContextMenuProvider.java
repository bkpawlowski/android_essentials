package pl.pawlowski.bartek.supplib.GUI.ContextMenu;

import java.util.List;

/**
 * Created by Bartosz Garet Pawlowski on 06.04.14.
 * Provides ContextMenu items
 */
public interface ContextMenuProvider extends OnContextMenuItemSelectedListener{

    /**
     * @return - list of the items that should be inside ContextMenu
     */
    public List<ContextMenuItem> getContextMenuItems();
}
