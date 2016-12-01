package kz.ibragimov.salatcounter.di;

import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.Module;
import kz.ibragimov.salatcounter.di.component.AppComponent;

/**
 *
 * Created by musa on 9/8/16.
 */
@Module
public class SharedPreferencesProvider {

    @Inject
    SharedPreferences mPrefs;

    public SharedPreferencesProvider(AppComponent appComponent) {
        appComponent.inject(this);
    }
}
