package pl.pawlowski.bartek.supplib.GUI.ContextMenu;

/**
 * Created by Bartosz Garet Pawlowski on 16.03.14.
 */
public interface OnContextMenuItemSelectedListener {

    /**
     * @param contextMenuId - id of the context menu item
     * @param itemId - id of the item for which context menu was opened
     */
    public void onContextMenuItemSelected(Integer contextMenuId, Integer itemId);
}
