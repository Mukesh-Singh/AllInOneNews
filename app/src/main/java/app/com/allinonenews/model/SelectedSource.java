package app.com.allinonenews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mukesh on 5/4/17.
 */

public class SelectedSource implements Parcelable{
    private String sourceName;
    private String sourceId;


    public SelectedSource (){

    }
    protected SelectedSource(Parcel in) {
        sourceName = in.readString();
        sourceId = in.readString();
    }

    public static final Creator<SelectedSource> CREATOR = new Creator<SelectedSource>() {
        @Override
        public SelectedSource createFromParcel(Parcel in) {
            return new SelectedSource(in);
        }

        @Override
        public SelectedSource[] newArray(int size) {
            return new SelectedSource[size];
        }
    };

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceName);
        dest.writeString(sourceId);
    }
}
