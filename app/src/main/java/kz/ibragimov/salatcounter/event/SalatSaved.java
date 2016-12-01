package kz.ibragimov.salatcounter.event;

/**
 *
 * Created by musa on 12/1/16.
 */
public class SalatSaved {
    public boolean created;
    public String name;
    public SalatSaved(String v1, boolean v2) {
        created = v2;
        name = v1;
    }
}
