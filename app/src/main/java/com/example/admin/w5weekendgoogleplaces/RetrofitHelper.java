package com.example.admin.w5weekendgoogleplaces;

import com.example.admin.w5weekendgoogleplaces.model.detailedplace.GoogleDetailedPlaceResponse;
import com.example.admin.w5weekendgoogleplaces.model.googleplaces.GooglePlacesResponse;
import com.example.admin.w5weekendgoogleplaces.model.queryautocomplete.GoogleAutoCompleteResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by admin on 9/30/2017.
 */

public class RetrofitHelper {
    public static final String BASE_URL = "https://maps.googleapis.com/";

    public static Retrofit create(){
        return  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Observable<Response<GoogleAutoCompleteResponse>> createAutoCompleteCall(String input) {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getAutoCompleteResponse("AIzaSyAPjckpge-wIGZHWNi_eM21icIMkbpUGp8", input);
    }

    public static Observable<Response<GooglePlacesResponse>> createPlacesCall(String latLng, int radius, String type) {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGooglePlacesResponse(latLng, "" + radius, type, "AIzaSyC6e3sfz0NWoq8_IdWX7FgNo7x013NE3-c");
    }

    public static Observable<Response<GoogleDetailedPlaceResponse>> createPlaceDetailedCall(String id) {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGooglePlaceDetailedResponse(id, "AIzaSyC6e3sfz0NWoq8_IdWX7FgNo7x013NE3-c");
    }

    interface ApiService{

        @GET("maps/api/place/nearbysearch/json")
        Observable<Response<GooglePlacesResponse>> getGooglePlacesResponse(
                @Query("location") String latLng, @Query("radius") String radius, @Query("name") String type, @Query("key") String key);

        @GET("maps/api/place/queryautocomplete/json")
        Observable<Response<GoogleAutoCompleteResponse>>getAutoCompleteResponse(
                @Query("key") String key, @Query("input") String input);

        @GET("maps/api/place/details/json")
        Observable<Response<GoogleDetailedPlaceResponse>> getGooglePlaceDetailedResponse(
                @Query("placeid") String id, @Query("key") String key);
    }
}
