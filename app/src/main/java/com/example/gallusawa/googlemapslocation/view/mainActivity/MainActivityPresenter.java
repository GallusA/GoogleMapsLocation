package com.example.gallusawa.googlemapslocation.view.mainActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gallusawa.googlemapslocation.R;
import com.example.gallusawa.googlemapslocation.model.AddressResponse;
import com.example.gallusawa.googlemapslocation.model.GeoLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by gallusawa on 8/24/17.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter , MainActivityContract{

    private static final String TAG = "MainActivityPresenter";
    public static final String GEO_KEY = "AIzaSyAORqT5XlVO0ZiXXlXpLDpwYTwzTKSVobw";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    MainActivityContract.View view;
    Context context;

    LatLng yourLocation;
    double newLattitude;
    double newLongitude;
    Activity activity;
    EditText etLatitude, etLongitude, etStreet, etCity, etState, etZip, etCountry;
    String lat = "";
    String lon = "";
    String a, b, c, d, e = "";


    public void attachView(MainActivityContract.View view) {
        this.view = view;
    }



    @Override
    public void checkLocationActive(final Activity activity) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });
        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    public void init(Activity activity) {
        this.activity = activity;
        checkPermissions(activity);
    }


    private void checkPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void getLocation(String lat, String lon) {

        newLattitude = Double.parseDouble(lat);
        newLongitude = Double.parseDouble(lon);

        yourLocation = new LatLng(newLattitude, newLongitude);
        Bundle args = new Bundle();
        args.putParcelable("yourLocation", yourLocation);
        Intent intentMap = new Intent(activity, MapsActivity.class);
        intentMap.putExtras(args);
        activity.startActivity(intentMap);
        view.locationObtained(true);
    }

    @Override
    public void getLocationByAddress(String a, String b, String c, String d, String e) {
        String address = a + "+" + b + "+" + c + "+" + d + "+" + e;
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("address", address)
                .addQueryParameter("key", GEO_KEY)
                .build();
        Log.d(TAG, "getGeocodeAddress: " + url.toString());
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                GeoLocation addressResponse = gson.fromJson(response.body().string(), GeoLocation.class);
                Log.d(TAG, "onResponse: " + addressResponse.getResults().get(0).getFormattedAddress());
                getLocation(
                        String.valueOf(addressResponse.getResults().get(0).getGeometry().getLocation().getLat().toString())
                        , String.valueOf(addressResponse.getResults().get(0).getGeometry().getLocation().getLng()));
            }
        });
    }

    @Override
    public void getLocationByGeo(String a, String b, String c, String d, String e) {
        String address = a + "+" + b + "+" + c + "+" + d + "+" + e;
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        if (gc.isPresent()) {
            try {
                List<Address> list = gc.getFromLocationName(address, 1);
                newLattitude = list.get(0).getLatitude();
                newLongitude = list.get(0).getLongitude();
                getLocation(String.valueOf(newLattitude),String.valueOf(newLongitude));

            } catch (IOException el) {
                el.printStackTrace();
            }

        }
    }


    @Override
    public void AttachedView(MainActivityContract.View view) {

    }

    @Override
    public void DettachedView() {

    }
}


