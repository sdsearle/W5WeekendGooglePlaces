
package com.example.admin.w5weekendgoogleplaces.model.googleplaces;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viewport implements Parcelable {

    @SerializedName("northeast")
    @Expose
    private Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.northeast, flags);
        dest.writeParcelable(this.southwest, flags);
    }

    public Viewport() {
    }

    protected Viewport(Parcel in) {
        this.northeast = in.readParcelable(Northeast.class.getClassLoader());
        this.southwest = in.readParcelable(Southwest.class.getClassLoader());
    }

    public static final Parcelable.Creator<Viewport> CREATOR = new Parcelable.Creator<Viewport>() {
        @Override
        public Viewport createFromParcel(Parcel source) {
            return new Viewport(source);
        }

        @Override
        public Viewport[] newArray(int size) {
            return new Viewport[size];
        }
    };
}
