package com.example.gallusawa.googlemapslocation.view.mainActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.gallusawa.googlemapslocation.BasePresenter;
import com.example.gallusawa.googlemapslocation.BaseView;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

/**
 * Created by gallusawa on 8/24/17.
 */


   public interface MainActivityContract {
    interface View extends BaseView{

        void checkPermissions(Activity activity);
        void getLocationNow(android.view.View view);
        void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
        void locationObtained(boolean isObtained);
        void locationEnabled(boolean isEnabled);
        void showerror(String s);


    }
    interface Presenter extends BasePresenter<View>{

        void attachView(MainActivityContract.View view);
        void checkLocationActive(Activity activity);
        void init(Activity activity);
        void getContext(Context context);
        void getLocation(String lattitude, String longitude);
        void getLocationByAddress(String a, String b, String c, String d, String e);
        void getLocationByGeo(String a, String b, String c, String d, String e);
        void DettachedView();
    }

}




