
package com.example.admin.w5weekendgoogleplaces.model.queryautocomplete;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructuredFormatting implements Parcelable {

    @SerializedName("main_text")
    @Expose
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    @Expose
    private List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
    @SerializedName("secondary_text")
    @Expose
    private String secondaryText;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstrings;
    }

    public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstring> mainTextMatchedSubstrings) {
        this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mainText);
        dest.writeList(this.mainTextMatchedSubstrings);
        dest.writeString(this.secondaryText);
    }

    public StructuredFormatting() {
    }

    protected StructuredFormatting(Parcel in) {
        this.mainText = in.readString();
        this.mainTextMatchedSubstrings = new ArrayList<MainTextMatchedSubstring>();
        in.readList(this.mainTextMatchedSubstrings, MainTextMatchedSubstring.class.getClassLoader());
        this.secondaryText = in.readString();
    }

    public static final Parcelable.Creator<StructuredFormatting> CREATOR = new Parcelable.Creator<StructuredFormatting>() {
        @Override
        public StructuredFormatting createFromParcel(Parcel source) {
            return new StructuredFormatting(source);
        }

        @Override
        public StructuredFormatting[] newArray(int size) {
            return new StructuredFormatting[size];
        }
    };
}
