package app.com.allinonenews.ui.details;

import app.com.allinonenews.base.BasePresenter;
import app.com.allinonenews.base.BaseView;

/**
 * Created by mukesh on 3/4/17.
 */

public class DetailContract {
    interface View extends BaseView<Presenter> {
        void loadUrlInWebView(String url);

        void showLoadingIndicator(boolean isActive);

        void refreshWebView();
    }

    interface Presenter extends BasePresenter {
        void loadUrl(String uri);

        void refresh();
    }
}
