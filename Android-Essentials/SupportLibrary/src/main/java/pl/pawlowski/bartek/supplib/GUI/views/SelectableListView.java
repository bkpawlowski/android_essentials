package pl.pawlowski.bartek.supplib.GUI.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

import pl.pawlowski.bartek.supplib.GUI.utilities.SelectableListViewAdapter;

/**
 * Created by Bartosz Garet Pawlowski on 31.03.14.
 */
public class SelectableListView extends ListView {
    public SelectableListView(Context context) {
        super(context);
    }

    public SelectableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof SelectableListViewAdapter){
            SelectableListViewAdapter selectableListViewAdapter = (SelectableListViewAdapter) adapter;
            SelectableListViewAdapter.CheckBoxStateChanger checkBoxStateChanger = selectableListViewAdapter.new CheckBoxStateChanger();

            setOnItemClickListener(checkBoxStateChanger);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
