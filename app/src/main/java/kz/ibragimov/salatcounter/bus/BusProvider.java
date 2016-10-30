package kz.ibragimov.salatcounter.bus;

import com.squareup.otto.Bus;

/**
 *
 * Created by musa on 7/18/16.
 */
public class BusProvider {

    private static Bus mBus;

    public static Bus getBus() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }
}
