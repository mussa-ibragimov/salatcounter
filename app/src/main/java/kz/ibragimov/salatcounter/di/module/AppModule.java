package kz.ibragimov.salatcounter.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kz.ibragimov.salatcounter.App;
import kz.ibragimov.salatcounter.event.EventBus;
import kz.ibragimov.salatcounter.di.SharedPreferencesProvider;
import kz.ibragimov.salatcounter.model.SalatsModel;
import kz.ibragimov.salatcounter.viewmodel.SalatsViewModel;

/**
 *
 * Created by musa on 9/8/16.
 */
@Module
public class AppModule {

    private final App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    SharedPreferences sharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }


    @Provides
    @Singleton
    SharedPreferencesProvider sharedPreferencesProvider() {
        return new SharedPreferencesProvider(mApp.getAppComponent());
    }

    @Provides
    @Singleton
    Context context() {
        return mApp.getApplicationContext();
    }

    @Provides
    @Singleton
    SalatsViewModel salatsViewModel() {
        return new SalatsViewModel(mApp.getAppComponent());
    }

    @Provides
    @Singleton
    SalatsModel salatsModel() {
        return new SalatsModel(mApp.getAppComponent());
    }
}
