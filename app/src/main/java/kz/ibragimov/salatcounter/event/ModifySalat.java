package kz.ibragimov.salatcounter.event;

import kz.ibragimov.salatcounter.model.Salat;

/**
 *
 * Created by musa on 12/1/16.
 */
public class ModifySalat {
    public Salat salat;
    public ModifySalat(Salat s) {
        salat = s;
    }
}
