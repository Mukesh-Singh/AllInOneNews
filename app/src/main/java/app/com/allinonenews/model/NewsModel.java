package app.com.allinonenews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mukesh on 27/3/17.
 */

public class NewsModel implements Parcelable{

    /**
     * author : Lauren Fox, CNN
     * title : First casualty for House Freedom Caucus after health care meltdown
     * description : Rep. Ted Poe announced Sunday he has resigned from the conservative House Freedom Caucus over its opposition to the Republican health care plan, becoming the group's first public casualty in the fall-out over its role in defeating the bill.
     * url : http://www.cnn.com/2017/03/26/politics/poe-resigns-freedom-caucus/
     * urlToImage : //i2.cdn.cnn.com/cnnnext/dam/assets/160713103843-rep-ted-poe-texas-super-tease.jpg
     * publishedAt : 2017-03-26T22:35:49Z
     */

    private int _id;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;


    public NewsModel (){

    }
    public NewsModel(Parcel in) {
        _id=in.readInt();
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
        publishedAt = in.readString();
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(publishedAt);
    }

    @Override
    public String toString() {
        return "{"+getTitle()+"/t"+getAuthor()+"/t"+getUrl()+"/t"+getUrlToImage()+"/t"+getPublishedAt()+"/t"+getDescription()+"}";
    }

    @Override
    public int hashCode() {
        return title.hashCode()+url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NewsModel){
            NewsModel m=(NewsModel)obj;
            return this.title.equalsIgnoreCase(m.getTitle()) && this.url.equalsIgnoreCase(m.getUrl());
        }
        else
            return false;
    }

}
