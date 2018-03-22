package app.com.allinonenews.model;

import java.util.List;

/**
 * Created by mukesh on 31/3/17.
 */

public class Source {

    /**
     * id : abc-news-au
     * name : ABC News (AU)
     * description : Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.
     * url : http://www.abc.net.au/news
     * category : general
     * language : en
     * country : au
     * urlsToLogos : {"small":"http://i.newsapi.org/abc-news-au-s.png","medium":"http://i.newsapi.org/abc-news-au-m.png","large":"http://i.newsapi.org/abc-news-au-l.png"}
     * sortBysAvailable : ["top"]
     */

    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
    private UrlsToLogosEntity urlsToLogos;
    private List<String> sortBysAvailable;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UrlsToLogosEntity getUrlsToLogos() {
        return urlsToLogos;
    }

    public void setUrlsToLogos(UrlsToLogosEntity urlsToLogos) {
        this.urlsToLogos = urlsToLogos;
    }

    public List<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    public void setSortBysAvailable(List<String> sortBysAvailable) {
        this.sortBysAvailable = sortBysAvailable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class UrlsToLogosEntity {
        /**
         * small : http://i.newsapi.org/abc-news-au-s.png
         * medium : http://i.newsapi.org/abc-news-au-m.png
         * large : http://i.newsapi.org/abc-news-au-l.png
         */

        private String small;
        private String medium;
        private String large;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Source){
            Source s=(Source)obj;
            return this.id.equalsIgnoreCase(s.id);

        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Id: "+id+", Name: "+name;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
