package app.com.allinonenews.data.model;

/**
 * Created by mukesh on 23/3/18.
 */

public class TopNewsRequestParams {
    private String country;
    private String category;
    private String sources;


    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public String getSources() {
        return sources;
    }

    public static class TopNewsRequestParamsBuilder{
        private String country;
        private String category;
        private String sources;

        public void setCountry(String country) {
            this.country = country;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setSources(String sources) {
            this.sources = sources;
        }

        public TopNewsRequestParams build(){
            TopNewsRequestParams topNewsRequestParams=new TopNewsRequestParams();
            topNewsRequestParams.category=this.category;
            topNewsRequestParams.country=this.country;
            topNewsRequestParams.sources=this.sources;
            return topNewsRequestParams;
        }
    }
}
