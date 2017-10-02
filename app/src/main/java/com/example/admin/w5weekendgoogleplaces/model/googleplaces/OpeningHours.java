
package com.example.admin.w5weekendgoogleplaces.model.googleplaces;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours implements Parcelable {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Object> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.openNow);
        dest.writeList(this.weekdayText);
    }

    public OpeningHours() {
    }

    protected OpeningHours(Parcel in) {
        this.openNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.weekdayText = new ArrayList<Object>();
        in.readList(this.weekdayText, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<OpeningHours> CREATOR = new Parcelable.Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel source) {
            return new OpeningHours(source);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
