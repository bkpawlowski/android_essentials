package pl.pawlowski.bartek.supplib.GUI.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
public abstract class ItemListPreviewFragment<AdapterItemClass extends AbstractEntity> extends ActionBarMenuListFragment {

    /**
     * Klucz identyfikujacy obiekt przekazany do edycji
     */
    public static final String PASSED_ITEM_KEY = "-2547-x-previewed/edited-entity";

    /**
     * Klucz identyfikujacy instancje nasluchiwacza na rezultat edycji
     */
    private static final String OVRSL_TAG_KEY = "2547--saved-listener-tag";

    /**
     * Klucz identyfikujacy tryb w jakim znajduje sie widok
     */
    public static final String VIEW_MODE_KEY = "01283--view-mode-isEditorMode?";

    /**
     * Tag identyfikujacy edytor podpinany z tego fragmentu
     */
    public static final String ITEM_EDITOR_FRAGMENT_TAG = "12384-item-editor-fragment";

    /**
     * Klucz do zapisania Tagu identyfikujacego fragment oczekujacy na rezultat edytora
      */
    public static final String ITEM_EDITOR_RESULT_LISTENER_TAG = "1231-item-editor-result-listener-fragment-tag";

    protected ActionBarMenuListFragment onViewResultSubmittedListener;

    protected AdapterItemClass adapterItem;

    private boolean editorMode;

    private Menu menuInstance;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if(savedInstanceState == null){
            Bundle arguments = getArguments();
            if(arguments != null) {
                adapterItem = arguments.getParcelable(PASSED_ITEM_KEY);
                //przekazanie do podklasy itemu z ktorego powinny zostac uzupelnione widoki
                fillViewsFromDTO(adapterItem);

                editorMode = arguments.getBoolean(VIEW_MODE_KEY, false);
            }else{
                editorMode = true;
            }
        }else{
            String fragmentTag = savedInstanceState.getString(OVRSL_TAG_KEY);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            onViewResultSubmittedListener = (ActionBarMenuListFragment) fm.findFragmentByTag(fragmentTag);
            editorMode = savedInstanceState.getBoolean(VIEW_MODE_KEY, false);

            adapterItem = savedInstanceState.getParcelable(PASSED_ITEM_KEY);
        }

        super.onViewCreated(view, savedInstanceState);

    }

    protected void setEditorViewMode(boolean arg){
        editorMode = arg;
        initOptionsMenu(editorMode, menuInstance);
        Util.setViewsEnabled((ViewGroup) getView(), arg);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(onViewResultSubmittedListener != null){
            //TODO ciekaw jestem czy przy wielu podpietych fragmentach nie bedzie powodowac to niespojnosci
            //HINT-> listenery mialyby ten sam tag(ta so fragmenty-.- lAWL
            outState.putString(OVRSL_TAG_KEY, onViewResultSubmittedListener.getTag());
        }
        outState.putParcelable(PASSED_ITEM_KEY, adapterItem);   //ogarnac skad biore aktualne dane bo zapomnialem :D
        outState.putBoolean(VIEW_MODE_KEY, editorMode);

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
    protected abstract AdapterItemClass getDTOFromViews();

    /**
     * @item - object that View's should be filled up with
     */
    protected abstract void fillViewsFromDTO(AdapterItemClass item);

    private void initOptionsMenu(boolean editorMode, Menu menu){
        if(menu != null) {
            menu.findItem(R.id.ief_done).setVisible(editorMode);
            menu.findItem(R.id.ief_cancel_edit).setVisible(editorMode);
            menu.findItem(R.id.ief_edit).setVisible(!editorMode);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menuInstance = menu;
        menu.clear();
        inflater.inflate(R.menu.item_preview_menu, menu);
        setEditorViewMode(editorMode);
    }

    /**
     * Determines whether detach this fragment when user is done with editing or not
     * @return
     *  true if this fragment should be detached, false otherwise
     */
    protected boolean detachWhenDone(){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.ief_done == item.getItemId()){
            AdapterItemClass dto = getDTOFromViews();
            if(adapterItem == null){
                dto.setId(null);
            }else{
                dto.setId(adapterItem.getId());
            }

            submitChanges(dto);

            boolean detachFragment = detachWhenDone();
            //odpiecie fragmentu
            if(detachFragment) {
                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                setEditorViewMode(false);
            }
        }else if(R.id.ief_edit == item.getItemId()){
            setEditorViewMode(true);
        }else if(R.id.ief_cancel_edit == item.getItemId()){
            boolean detachFragment = detachWhenDone();
            //odpiecie fragmentu
            if(detachFragment) {
                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                setEditorViewMode(false);
            }
        }
        return true;
    }
}
