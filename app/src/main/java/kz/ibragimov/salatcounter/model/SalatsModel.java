package kz.ibragimov.salatcounter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import kz.ibragimov.salatcounter.R;
import kz.ibragimov.salatcounter.event.EventBus;
import kz.ibragimov.salatcounter.di.component.AppComponent;
import kz.ibragimov.salatcounter.event.SalatDeleted;
import kz.ibragimov.salatcounter.event.SalatSaved;
import kz.ibragimov.salatcounter.event.SalatsUpdated;
import kz.ibragimov.salatcounter.event.ToastMessage;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 * Created by musa on 11/30/16.
 */
public class SalatsModel {

    public List<Salat> salats = new ArrayList<>();
    CompositeSubscription mSubs;
    @Inject
    EventBus mEventBus;

    public SalatsModel(AppComponent appComponent) {
        appComponent.inject(SalatsModel.this);
        mSubs = new CompositeSubscription();
        retrieveSalats();
    }

    private void retrieveSalats() {
        mSubs.add(Observable.just(Salat.getAll())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Salat>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Salat> list) {
                        salats.clear();
                        salats.addAll(list);
                        mEventBus.send(new SalatsUpdated());
                    }
                }));
    }

    public void save(Salat salat, String name, String count, boolean created) {
        if (count == null || count.isEmpty()) {
            count = "0";
        }
        // TODO: Must be localized
        if (name == null || name.isEmpty()) {
            name = "Salat: " + String.valueOf(salats.size() + 1);
        }
        Observable.just(Salat.save(salat, name, count))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mEventBus.send(new ToastMessage(R.string.error_could_create_new_salat));
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (created) {
                            salats.add(salat);
                        }
                        mEventBus.send(new SalatSaved(salat.name, created));
                    }
                });
    }

    public void delete(Salat salat) {
        Observable.just(Salat.delete(salat))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        salats.remove(salat);
                        mEventBus.send(new SalatDeleted(salat.name));
                    }
                });
    }

    public void subtract(Salat salat) {
        salat.count--;
        salat.lastUpdated = new Date().getTime();
        save(salat);
    }

    public void add(Salat salat) {
        salat.count++;
        salat.lastUpdated = new Date().getTime();
        save(salat);
    }

    public void save(Salat salat) {
        Observable.just(salat.save())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        mEventBus.send(new SalatSaved(salat.name, false));
                    }
                });
    }
}
