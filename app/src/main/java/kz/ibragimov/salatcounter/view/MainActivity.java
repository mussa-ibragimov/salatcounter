package kz.ibragimov.salatcounter.view;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.text.NumberFormat;
import javax.inject.Inject;

import kz.ibragimov.salatcounter.R;
import kz.ibragimov.salatcounter.App;
import kz.ibragimov.salatcounter.databinding.ActivityMainBinding;
import kz.ibragimov.salatcounter.databinding.DialogAddSalatBinding;
import kz.ibragimov.salatcounter.di.component.ActivityComponent;
import kz.ibragimov.salatcounter.di.component.DaggerActivityComponent;
import kz.ibragimov.salatcounter.event.EventBus;
import kz.ibragimov.salatcounter.event.ModifySalat;
import kz.ibragimov.salatcounter.event.SalatDeleted;
import kz.ibragimov.salatcounter.event.SalatSaved;
import kz.ibragimov.salatcounter.event.ToastMessage;
import kz.ibragimov.salatcounter.model.Salat;
import kz.ibragimov.salatcounter.viewmodel.SalatsViewModel;

public class MainActivity extends AppCompatActivity {

    SalatsAdapter mAdapter;
    BottomSheetBehavior mBottomSheetBehavior;
    ActivityMainBinding mBinding;
    ActivityComponent mComponent;
    @Inject
    SalatsViewModel mViewModel;
    @Inject
    EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mComponent = DaggerActivityComponent.builder().appComponent(getApp().getAppComponent()).build();
        mComponent.inject(MainActivity.this);
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbar);

        mBinding.fab.setOnClickListener(mViewModel);

        mAdapter = new SalatsAdapter(mViewModel, getString(R.string.last_updated_time));
        mBinding.list.setAdapter(mAdapter);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet);
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }

    public void onSalatSelected(Salat salat) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBinding.salat.setText(getString(R.string.bottom_sheet_salat, salat.name, salat.count));
    }

    Toast messageToast;

    private void onToastMessage(ToastMessage message) {
        if (messageToast != null) {
            messageToast.cancel();
        }
        if (message.toast == null) {
            messageToast = Toast.makeText(getApplicationContext(), message.toastResourceId, Toast.LENGTH_LONG);
        } else {
            messageToast = Toast.makeText(getApplicationContext(), message.toast, Toast.LENGTH_LONG);
        }
        messageToast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.onEvent(Salat.class, this::onSalatSelected);
        mEventBus.onEvent(ToastMessage.class, this::onToastMessage);
        mEventBus.onEvent(SalatSaved.class, event -> {
            mAdapter.notifyDataSetChanged();
            onToastMessage(new ToastMessage(getString(event.created ? R.string.salat_added : R.string.salat_edited, event.name)));
        });
        mEventBus.onEvent(ModifySalat.class, this::modifySalat);
        mEventBus.onEvent(SalatDeleted.class, event -> {
            mAdapter.notifyDataSetChanged();
            onToastMessage(new ToastMessage(getString(R.string.salat_deleted, event.name)));
        });
    }

    public void onClick(View b) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        switch (b.getId()) {
            case R.id.edit:
                mViewModel.edit();
                break;
            case R.id.delete:
                mViewModel.delete();
                break;
        }
    }

    private void modifySalat(ModifySalat modifySalat) {
        Salat salat = modifySalat.salat;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = this.getLayoutInflater();
        DialogAddSalatBinding dialogAddSalatBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_salat, null, false);
        @SuppressLint("InflateParams") View dialogView = dialogAddSalatBinding.getRoot();
        dialogAddSalatBinding.salatName.setText(salat.name);
        dialogAddSalatBinding.salatCount.setText(NumberFormat.getInstance().format(salat.count));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        dialogBuilder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> mViewModel.save(salat, dialogAddSalatBinding.salatName.getText().toString(), dialogAddSalatBinding.salatCount.getText().toString(), salat.name == null));
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        dialogAddSalatBinding.salatCount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mViewModel.save(salat, dialogAddSalatBinding.salatName.getText().toString(), dialogAddSalatBinding.salatCount.getText().toString(), salat.name == null);
                alertDialog.dismiss();
                return true;
            }
            return false;
        });
    }
}
