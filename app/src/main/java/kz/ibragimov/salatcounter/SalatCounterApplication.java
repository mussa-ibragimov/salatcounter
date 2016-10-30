package kz.ibragimov.salatcounter;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 *
 * Created by musa on 7/18/16.
 */
public class SalatCounterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
