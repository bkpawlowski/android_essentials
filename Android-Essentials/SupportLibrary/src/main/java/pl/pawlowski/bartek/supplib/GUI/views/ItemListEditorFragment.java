package pl.pawlowski.bartek.supplib.GUI.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pl.pawlowski.bartek.supplib.GUI.utilities.Util;
import pl.pawlowski.bartek.supplib.R;
import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractEntity;

/**
 * Created by Bartosz Garet Pawlowski on 24.03.14.
 */
public abstract class ItemListEditorFragment<AdapterItemClass extends AbstractEntity> extends ActionBarMenuListFragment {

    /**
     * Klucz identyfikujacy obiekt przekazany do edycji
     */
    public static final String PASSED_ITEM_KEY = "-2547-x";

    /**
     * Klucz identyfikujacy instancje nasluchiwacza na rezultat edycji
     */
    private static final String OVRSL_TAG_KEY = "2547-x";

    /**
     * Klucz identyfikujacy tryb w jakim znajduje sie widok
     */
    public static final String VIEW_MODE_KEY = "01283-01xxx";

    protected ActionBarMenuListFragment onViewResultSubmittedListener;

    protected Integer adapterItemId;

    private boolean editorMode;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        if(arguments != null) {
            AdapterItemClass item = arguments.getParcelable(PASSED_ITEM_KEY);
            adapterItemId = item.getId();
            //przekazanie do podklasy itemu z ktorego powinny zostac uzupelnione widoki
            fillViewsFromDTO(item);

            ViewGroup vg = (ViewGroup) view;
            editorMode = arguments.getBoolean(VIEW_MODE_KEY, false);
        }

        if(savedInstanceState == null){

        }else{
            String fragmentTag = savedInstanceState.getString(OVRSL_TAG_KEY);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            onViewResultSubmittedListener = (ActionBarMenuListFragment) fm.findFragmentByTag(fragmentTag);
            editorMode = savedInstanceState.getBoolean(VIEW_MODE_KEY, false);
        }

        setEditorViewMode(editorMode);

        super.onViewCreated(view, savedInstanceState);
    }

    public void setEditorViewMode(boolean arg){
        Util.setViewsEnabled((ViewGroup) getView(), arg);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(onViewResultSubmittedListener != null){
            //TODO ciekaw jestem czy przy wielu podpietych fragmentach nie bedzie powodowac to niespojnosci
            //HINT-> listenery mialyby ten sam tag(ta so fragmenty-.- lAWL
            outState.putString(OVRSL_TAG_KEY, onViewResultSubmittedListener.getTag());
            outState.putBoolean(VIEW_MODE_KEY, editorMode);
        }

        super.onSaveInstanceState(outState);
    }

    public void setOnViewResultSubmittedListener(ActionBarMenuListFragment listener){
        this.onViewResultSubmittedListener = listener;
    }

    protected <T> void submitChanges(T changes){
        if(onViewResultSubmittedListener != null){
            onViewResultSubmittedListener.onItemSubmitted(changes);
        }
    }

    /**
     * @return object created by data from this view's Fields
     */
    protected abstract AdapterItemClass getDTOFromFields();

    /**
     * @item - object that View's should be filled up with
     */
    protected abstract void fillViewsFromDTO(AdapterItemClass item);

    private void initOptionsMenu(Menu menu, boolean editorMode){
        menu.findItem(R.id.ief_done).setVisible(editorMode);
        menu.findItem(R.id.ief_edit).setVisible(!editorMode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.item_editor_menu, menu);
        initOptionsMenu(menu, editorMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.ief_done == item.getItemId()){
            AdapterItemClass dto = getDTOFromFields();
            dto.setId(adapterItemId);

            submitChanges(dto);
            //odpiecie fragmentu
            getActivity().getSupportFragmentManager().popBackStack();
        }else if(R.id.ief_edit == item.getItemId()){
            setEditorViewMode(true);
        }else if(R.id.ief_cancel_edit == item.getItemId()){
            setEditorViewMode(false);
//            restoreViewState();
        }
        return true;
    }
}
