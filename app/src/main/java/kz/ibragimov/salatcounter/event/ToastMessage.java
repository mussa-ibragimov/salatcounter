package kz.ibragimov.salatcounter.event;

/**
 *
 * Created by musa on 9/19/16.
 */
public class ToastMessage {
    public int toastResourceId;
    public String toast;
    public ToastMessage(String msg) {
        toast = msg;
    }
    public ToastMessage(int id) {
        toastResourceId = id;
    }
}
