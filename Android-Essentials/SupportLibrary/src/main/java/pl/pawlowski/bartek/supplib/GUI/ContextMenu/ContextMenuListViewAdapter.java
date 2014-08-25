package pl.pawlowski.bartek.supplib.GUI.ContextMenu;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.pawlowski.bartek.supplib.GUI.utilities.SimpleListViewAdapter;
import pl.pawlowski.bartek.supplib.GUI.utilities.SimpleListViewItemViewHolder;
import pl.pawlowski.bartek.supplib.R;

/**
 * Created by bartek-mint on 16.03.14.
 */
public class ContextMenuListViewAdapter extends SimpleListViewAdapter<ContextMenuItem, SimpleListViewItemViewHolder> {

    public ContextMenuListViewAdapter(Context context, List<ContextMenuItem> items) {
        super(context, items);
    }

    @Override
    public int getViewItemLayoutId() {
        return R.layout.contextmenu_popup_window_listviewitem;
    }

    @Override
    public SimpleListViewItemViewHolder getViewHolder(View inflatedView) {
        SimpleListViewItemViewHolder viewHolder = new SimpleListViewItemViewHolder();
        viewHolder.imageView = (ImageView) inflatedView.findViewById(R.id.CTXMItem_ImageView);
        viewHolder.textView = (TextView) inflatedView.findViewById(R.id.CTXMItem_TextView);

        return viewHolder;
    }

    @Override
    public void updateView(SimpleListViewItemViewHolder viewHolder, ContextMenuItem item) {
        viewHolder.imageView.setImageResource(item.itemImageView);
        viewHolder.textView.setText(item.itemTextView);
    }
}
