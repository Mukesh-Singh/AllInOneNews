package app.com.allinonenews.model;

import java.util.List;

/**
 * Created by mukesh on 27/3/17.
 */

public class NewsResponseModel {

    /**
     * status : ok
     * source : google-news
     * sortBy : top
     * articles : [{"author":"Lauren Fox, CNN","title":"First casualty for House Freedom Caucus after health care meltdown","description":"Rep. Ted Poe announced Sunday he has resigned from the conservative House Freedom Caucus over its opposition to the Republican health care plan, becoming the group's first public casualty in the fall-out over its role in defeating the bill.","url":"http://www.cnn.com/2017/03/26/politics/poe-resigns-freedom-caucus/","urlToImage":"//i2.cdn.cnn.com/cnnnext/dam/assets/160713103843-rep-ted-poe-texas-super-tease.jpg","publishedAt":"2017-03-26T22:35:49Z"}]
     */

    private String status;
    private String source;
    private String sourceName;
    private String sortBy;
    private List<NewsModel> articles;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<NewsModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsModel> articles) {
        this.articles = articles;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
