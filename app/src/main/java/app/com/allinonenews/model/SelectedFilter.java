package app.com.allinonenews.model;

/**
 * Created by mukesh on 5/4/17.
 */

public class SelectedFilter {
    private String country;
    private String language;
    private String category;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
