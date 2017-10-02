
package com.example.admin.w5weekendgoogleplaces.model.queryautocomplete;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction implements Parcelable {

    private static final String TAG = "PredictionTag";
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("matched_substrings")
    @Expose
    private List<MatchedSubstring> matchedSubstrings = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private StructuredFormatting structuredFormatting;
    @SerializedName("terms")
    @Expose
    private List<Term> terms = null;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {


            dest.writeString(this.description);
            dest.writeString(this.id);
            dest.writeList(this.matchedSubstrings);
            dest.writeString(this.placeId);
            dest.writeString(this.reference);
            dest.writeParcelable(this.structuredFormatting, flags);
            dest.writeList(this.terms);
            dest.writeStringList(this.types);
        }catch (Exception e){
            Log.d(TAG, "writeToParcel: " + e.getMessage());
        }
    }

    public Prediction() {
    }

    protected Prediction(Parcel in) {
        this.description = in.readString();
        this.id = in.readString();
        this.matchedSubstrings = new ArrayList<MatchedSubstring>();
        in.readList(this.matchedSubstrings, MatchedSubstring.class.getClassLoader());
        this.placeId = in.readString();
        this.reference = in.readString();
        this.structuredFormatting = in.readParcelable(StructuredFormatting.class.getClassLoader());
        this.terms = new ArrayList<Term>();
        in.readList(this.terms, Term.class.getClassLoader());
        this.types = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Prediction> CREATOR = new Parcelable.Creator<Prediction>() {
        @Override
        public Prediction createFromParcel(Parcel source) {
            return new Prediction(source);
        }

        @Override
        public Prediction[] newArray(int size) {
            return new Prediction[size];
        }
    };
}
