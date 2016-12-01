package kz.ibragimov.salatcounter.event;

import kz.ibragimov.salatcounter.model.Salat;

/**
 *
 * Created by musa on 7/19/16.
 */
public class SalatLongClicked {
    public Salat salat;
    public SalatLongClicked(Salat s) {
        salat = s;
    }
}
