package kz.ibragimov.salatcounter.tasks;

import android.os.AsyncTask;
import java.util.List;
import kz.ibragimov.salatcounter.model.Salat;

/**
 *
 * Created by musa on 7/18/16.
 */
public class SalatsRetrievalTask extends AsyncTask<Void, Void, List<Salat>> {

    @Override
    protected List<Salat> doInBackground(Void... voids) {
        return Salat.getAll();
    }
}
