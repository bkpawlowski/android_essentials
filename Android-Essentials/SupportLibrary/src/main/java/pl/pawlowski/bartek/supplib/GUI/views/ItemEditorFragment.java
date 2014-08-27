package pl.pawlowski.bartek.supplib.GUI.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.pawlowski.bartek.supplib.R;
import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractEntity;

/**
 * Created by Bartosz Garet Pawlowski on 24.03.14.
 */
public abstract class ItemEditorFragment<AdapterItemClass extends AbstractEntity> extends ActionBarMenuFragment {

    public static final String PASSED_ITEM_KEY = "-2547-x";

    private static final String OVRSL_TAG_KEY = "2547-x";

    protected ActionBarMenuFragment onViewResultSubmittedListener;

    protected Integer adapterItemId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null) {
            AdapterItemClass item = arguments.getParcelable(PASSED_ITEM_KEY);
            adapterItemId = item.getId();
            //przekazanie do podklasy itemu z ktorego powinny zostac uzupelnione widoki
            fillViewsFromDTO(item);
        }

        if(savedInstanceState == null){

        }else{
            String fragmentTag = savedInstanceState.getString(OVRSL_TAG_KEY);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            onViewResultSubmittedListener = (ActionBarMenuFragment) fm.findFragmentByTag(fragmentTag);
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(onViewResultSubmittedListener != null){
            //TODO ciekaw jestem czy przy wielu podpietych fragmentach nie bedzie powodowac to niespojnosci
            //HINT-> listenery mialyby ten sam tag(ta so fragmenty-.- lAWL
            outState.putString(OVRSL_TAG_KEY, onViewResultSubmittedListener.getTag());
        }

        super.onSaveInstanceState(outState);
    }

    public void setOnViewResultSubmittedListener(ActionBarMenuFragment listener){
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.item_editor_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.ief_done == item.getItemId()){
            AdapterItemClass dto = getDTOFromFields();
            dto.setId(adapterItemId);

            submitChanges(dto);
            //odpiecie fragmentu
            getActivity().getSupportFragmentManager().popBackStack();
        }else{

        }
        return true;
    }
}
