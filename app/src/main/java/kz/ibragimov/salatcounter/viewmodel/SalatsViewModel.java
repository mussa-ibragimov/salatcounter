package kz.ibragimov.salatcounter.viewmodel;

import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import kz.ibragimov.salatcounter.R;
import kz.ibragimov.salatcounter.di.component.AppComponent;
import kz.ibragimov.salatcounter.event.EventBus;
import kz.ibragimov.salatcounter.event.ModifySalat;
import kz.ibragimov.salatcounter.model.Salat;
import kz.ibragimov.salatcounter.model.SalatsModel;

/**
 *
 * Created by musa on 11/30/16.
 */
public class SalatsViewModel implements View.OnClickListener {

    @Inject
    SalatsModel salatsModel;
    @Inject
    EventBus mEventBus;
    Salat mSelectedSalat;

    public SalatsViewModel(AppComponent appComponent) {
        appComponent.inject(SalatsViewModel.this);
    }

    @Override
    public void onClick(View v) {
        mEventBus.send(new ModifySalat(new Salat()));
    }

    public List<Salat> getSalats() {
        return salatsModel.salats;
    }

    public void selectPosition(Salat salat) {
        mSelectedSalat = salat;
        mEventBus.send(salat);
    }

    public void subtract(Salat salat) {
        salatsModel.subtract(salat);
    }

    public void add(Salat salat) {
        salatsModel.add(salat);
    }

    public void edit() {
        if (mSelectedSalat != null) {
            mEventBus.send(new ModifySalat(mSelectedSalat));
        }
    }

    public void delete() {
        if (mSelectedSalat != null) {
            salatsModel.delete(mSelectedSalat);
        }
    }

    public void save(Salat salat, String name, String count, boolean created) {
        salatsModel.save(salat, name, count, created);
    }
}
