package kz.ibragimov.salatcounter;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import kz.ibragimov.salatcounter.di.component.AppComponent;
import kz.ibragimov.salatcounter.di.component.DaggerAppComponent;
import kz.ibragimov.salatcounter.di.module.AppModule;

/**
 *
 * Created by musa on 7/18/16.
 */
public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(App.this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
