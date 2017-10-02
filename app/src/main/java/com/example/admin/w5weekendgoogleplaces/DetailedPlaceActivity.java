package com.example.admin.w5weekendgoogleplaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.w5weekendgoogleplaces.model.detailedplace.GoogleDetailedPlaceResponse;
import com.example.admin.w5weekendgoogleplaces.model.detailedplace.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DetailedPlaceActivity extends AppCompatActivity {

    private static final String TAG = "DetailedActivityTag";
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvHours)
    TextView tvHours;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    private Result detailedResult;
    private Observer<Response<GoogleDetailedPlaceResponse>> placeObserver = new Observer<Response<GoogleDetailedPlaceResponse>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {


        }

        @Override
        public void onNext(@NonNull Response<GoogleDetailedPlaceResponse> googlePlacesResponseResponse) {
            if (googlePlacesResponseResponse != null) {
                detailedResult = googlePlacesResponseResponse.body().getResult();
                //Address, photos, reviews
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {
            if(detailedResult == null) {
                Log.d(TAG, "onComplete: " + "is null");
            }
            tvName.setText(detailedResult.getName());
            tvAddress.setText(detailedResult.getFormattedAddress());
            tvPhone.setText(detailedResult.getFormattedPhoneNumber());
            String hours = "";
            if(detailedResult.getOpeningHours() != null) {
                for (String s :
                        detailedResult.getOpeningHours().getWeekdayText()) {
                    hours += s + "\n";
                }

            }
            else
                hours = "Not Listed";
            tvHours.setText(hours);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_place);
        ButterKnife.bind(this);

        String id = getIntent().getStringExtra("result");
        Log.d(TAG, "onCreate: " + id);

        final Observable<Response<GoogleDetailedPlaceResponse>> geocodeResponseCall = RetrofitHelper.createPlaceDetailedCall(id);
        geocodeResponseCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(placeObserver);

    }
}
