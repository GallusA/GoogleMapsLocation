package com.example.gallusawa.googlemapslocation.view.mainActivity;


import android.content.pm.PackageManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gallusawa.googlemapslocation.R;
import com.example.gallusawa.googlemapslocation.injection.MainActivity.DaggerMainActivityComponent;


import javax.inject.Inject;



public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 10;
    private static final int REQUEST_CHECK_SETTINGS = 8;
    @Inject
    MainActivityPresenter presenter;
    EditText etState,etCity,etCountry,etZip,etStreet,etLatitude,etLongitude;
    String lattitude = "";
    String longitude = "";
    String a, b, c, d, e = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainActivityComponent.create().inject(this);


        etState = (EditText) findViewById(R.id.etState);
        etCity = (EditText) findViewById(R.id.etCity);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etStreet = (EditText) findViewById(R.id.etStreet);
        etZip = (EditText) findViewById(R.id.etZip);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);


        presenter.attachView(this);
        presenter.getContext(this);
        presenter.init(this);
        presenter.checkLocationActive(this);



    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }

    }



    public void getLocationNow(View view) {
        presenter.checkLocationActive(this);
        a = String.valueOf(etState.getText());
        b = String.valueOf(etCity.getText());
        c = String.valueOf(etCountry.getText());
        d = String.valueOf(etStreet.getText());
        e = String.valueOf(etZip.getText());
        lattitude = String.valueOf(etLatitude.getText());
        longitude = String.valueOf(etLongitude.getText());

        switch (view.getId()) {
            case R.id.btnMaps:
                if (lattitude.equals("") || longitude.equals(""))
                    Toast.makeText(this, "Insert Latitude and Longitude", Toast.LENGTH_LONG).show();
                else {
                    presenter.getLocation(etLatitude.getText().toString(), etLongitude.getText().toString());
                }
                break;
            case R.id.btnAddress:
                if (a.equals("") || b.equals("") || c.equals("") || d.equals(""))
                    Toast.makeText(this, "Insert all fields", Toast.LENGTH_LONG).show();
                else
                    presenter.getLocationByAddress(a, b, c, d, e);
                break;
            case R.id.btnGeocode:
                presenter.getLocationByGeo(a, b, c, d, e);
                break;
        }
    }


    @Override
    public void locationObtained(boolean isObtained) {

    }

    @Override
    public void locationEnabled(boolean isEnabled) {

    }

    @Override
    public void showerror(String s) {

    }


}