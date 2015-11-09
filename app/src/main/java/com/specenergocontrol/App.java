package com.specenergocontrol;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Комп on 20.08.2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getBaseContext());
        builder.defaultDisplayImageOptions(defaultOptions);
        ImageLoader.getInstance().init(builder.build());

        try {
            Realm.getInstance(getBaseContext());
        } catch (RealmMigrationNeededException e){
            Realm.deleteRealmFile(getBaseContext());
        }
    }
}
