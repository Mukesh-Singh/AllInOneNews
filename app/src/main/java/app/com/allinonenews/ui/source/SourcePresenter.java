package app.com.allinonenews.ui.source;

import java.util.List;

import app.com.allinonenews.data.SourceDataSource;
import app.com.allinonenews.data.repository.SourceRepository;
import app.com.allinonenews.model.SelectedFilter;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.model.Source;
import app.com.allinonenews.util.PrefUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 5/4/17.
 */

public class SourcePresenter implements SourceContract.Presenter {

    private final PrefUtil pref;
    private SourceContract.View mView;
    private SourceRepository repository;

    public SourcePresenter(SourceRepository repository, SourceContract.View view, PrefUtil pref) {
        checkNotNull(repository);
        checkNotNull(view);
        checkNotNull(pref);
        this.mView = view;
        this.mView.setPresenter(this);
        this.repository = repository;
        this.pref = pref;

    }

    @Override
    public void loadSource() {
        SelectedFilter filter=pref.getFilter();
        mView.showProgress(true);
        repository.getSources( new SourceDataSource.GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                int pos=checkSelectedSource(list);
                mView.showProgress(false);
                mView.showSourceList(list);
                if (pos>0 && pos<list.size())
                    mView.scrollListTo(pos);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showProgress(false);
                mView.showNoSourceAvailable();
            }
        });
    }

    @Override
    public void clickOnCategoryFilter() {
        repository.getCategories(new SourceDataSource.GetCategoryCallback() {
            @Override
            public void onLoadSuccess(List<String> list) {
                mView.showCategoryPopUp(list);
            }

            @Override
            public void onDataNotAvailable() {
                mView.countryListNotAvailable();
            }
        });
    }

    private int checkSelectedSource(List<Source> list) {
        SelectedSource source=pref.getSelectedSource();
        for (int i=0;i<list.size();i++){
            Source s=list.get(i);
            if (s.getId().equalsIgnoreCase(source.getSourceId())) {
                s.setSelected(true);
                return i;
            }

        }
        return -1;
    }



    @Override
    public void start() {

    }
}
