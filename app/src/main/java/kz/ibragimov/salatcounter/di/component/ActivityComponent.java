package kz.ibragimov.salatcounter.di.component;

import dagger.Component;
import kz.ibragimov.salatcounter.view.MainActivity;
import kz.ibragimov.salatcounter.di.ActivityScope;

/**
 *
 * Created by musa on 9/8/16.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(MainActivity mainActivity);
}
