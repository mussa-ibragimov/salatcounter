package kz.ibragimov.salatcounter.event;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 *
 * Created by musa on 10/23/16.
 */

public class EventBus {
    public EventBus getEventBus() {
        return new EventBus();
    }

    private final Subject<Object, Object> BUS = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        BUS.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return BUS;
    }

    public <T> Subscription onEvent(Class<T> clazz, Action1<T> handler) {
        return BUS.ofType(clazz).subscribe(handler);
    }

    public boolean hasObservers() {
        return BUS.hasObservers();
    }
}
