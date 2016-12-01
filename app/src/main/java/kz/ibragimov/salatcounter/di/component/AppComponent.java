package kz.ibragimov.salatcounter.di.component;

import android.content.Context;
import android.content.SharedPreferences;


import javax.inject.Singleton;

import dagger.Component;
import kz.ibragimov.salatcounter.event.EventBus;
import kz.ibragimov.salatcounter.di.module.AppModule;
import kz.ibragimov.salatcounter.di.SharedPreferencesProvider;
import kz.ibragimov.salatcounter.model.SalatsModel;
import kz.ibragimov.salatcounter.viewmodel.SalatsViewModel;

/**
 *
 * Created by musa on 9/8/16.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    SharedPreferences sharedPreferences();
    SharedPreferencesProvider sharedPreferencesProvider();
    EventBus eventBus();
    Context context();
    SalatsViewModel salatsViewModel();
    SalatsModel salatsModel();

    void inject(SharedPreferencesProvider sharedPreferencesProvider);
    void inject(SalatsViewModel salatsViewModel);
    void inject(SalatsModel salatsModel);
}
