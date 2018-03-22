package app.com.allinonenews.ui.source;

import java.util.List;

import app.com.allinonenews.base.BasePresenter;
import app.com.allinonenews.base.BaseView;
import app.com.allinonenews.model.Source;

/**
 * Created by mukesh on 5/4/17.
 */

public class SourceContract {
    interface View extends BaseView<Presenter>{
        void showSourceList(List<Source> sources);
        void showNoSourceAvailable();
       // void showCountryPopUp(List<String> country);
        //void showLanguagePopUp(List<Source> sources);
        void showCategoryPopUp(List<String> category);
        void showProgress(boolean isActive);
        void scrollListTo(int position);

        void countryListNotAvailable();
        //void categoryListNotAvailable();
    }

    interface Presenter extends BasePresenter{
        void loadSource();
        void clickOnCategoryFilter();
    }
}
