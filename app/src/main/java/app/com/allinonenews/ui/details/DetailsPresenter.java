package app.com.allinonenews.ui.details;

/**
 * Created by mukesh on 3/4/17.
 */

public class DetailsPresenter implements DetailContract.Presenter{
    private DetailContract.View mView;

    public DetailsPresenter(DetailContract.View view){
        this.mView=view;
    }
    @Override
    public void loadUrl(String url) {
        mView.loadUrlInWebView(url);
    }

    @Override
    public void refresh() {
        mView.refreshWebView();
    }

    @Override
    public void start() {

    }
}
