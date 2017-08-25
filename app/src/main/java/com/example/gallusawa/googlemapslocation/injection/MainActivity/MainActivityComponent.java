package com.example.gallusawa.googlemapslocation.injection.MainActivity;

import com.example.gallusawa.googlemapslocation.view.mainActivity.MainActivity;

import dagger.Component;

/**
 * Created by gallusawa on 8/24/17.
 */

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);
}