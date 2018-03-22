package app.com.allinonenews.model;

import java.util.List;

/**
 * Created by mukesh on 31/3/17.
 */

public class SourceResponseModel {
    private String status;
    private List<Source> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
