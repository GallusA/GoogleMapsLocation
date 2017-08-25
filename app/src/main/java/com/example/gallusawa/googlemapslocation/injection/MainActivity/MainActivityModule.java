package com.example.gallusawa.googlemapslocation.injection.MainActivity;

import com.example.gallusawa.googlemapslocation.view.mainActivity.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gallusawa on 8/24/17.
 */

@Module
public class MainActivityModule {

    @Provides
    MainActivityPresenter providesMainActivityPresenter(){
        return new MainActivityPresenter();
    }
}