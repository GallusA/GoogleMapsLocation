package com.example.gallusawa.googlemapslocation;

/**
 * Created by gallusawa on 8/24/17.
 */

public interface BasePresenter <V extends  BaseView>{
    void AttachedView(V view);
    void DettachedView();
}
