package kz.ibragimov.salatcounter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.squareup.otto.Subscribe;

import java.text.NumberFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.ibragimov.salatcounter.bus.BusProvider;
import kz.ibragimov.salatcounter.model.Salat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.salat)
    TextView mSalat;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.list)
    RecyclerView mSalats;
    SalatsAdapter mAdapter;
    BottomSheetBehavior mBottomSheetBehavior;
    Salat mSelectedSalat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        setSupportActionBar(mToolbar);

        mFab.setOnClickListener(MainActivity.this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSalats.setLayoutManager(layoutManager);
        mAdapter = new SalatsAdapter();
        mSalats.setAdapter(mAdapter);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    @Override
    public void onClick(View view) {
        editSalat(null);
    }

    @Subscribe
    public void onSalatSelected(Salat salat) {
        mSelectedSalat = salat;
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mSalat.setText(getString(R.string.bottom_sheet_salat, mSelectedSalat.name, mSelectedSalat.count));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getBus().register(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(MainActivity.this);
    }

    @OnClick({R.id.edit, R.id.delete})
    public void onClick(Button b) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        switch (b.getId()) {
            case R.id.edit:
                editSalat(mSelectedSalat);
                break;
            case R.id.delete:
                mSelectedSalat.delete();
                String text = getString(R.string.salat_deleted, mSelectedSalat.name);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                mAdapter.update();
                break;
        }
    }

    private void editSalat(final Salat s) {
        final Salat salat = s == null ? new Salat() : s;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_add_salat, null);
        final EditText name = ButterKnife.findById(dialogView, R.id.salat_name);
        name.setText(salat.name);
        final EditText count = ButterKnife.findById(dialogView, R.id.salat_count);
        count.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save(salat, name.getText().toString(), count.getText().toString(), s == null);
                    return true;
                }
                return false;
            }
        });
        count.setText(NumberFormat.getInstance().format(salat.count));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save(salat, name.getText().toString(), count.getText().toString(), s == null);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void save(Salat salat, String name, String count, boolean created) {
        if (validData(name, count)) {
            salat.name = name;
            salat.count = Integer.parseInt(count);
            salat.lastUpdated = new Date().getTime();
            salat.save();
            String text = getString(created ? R.string.salat_added : R.string.salat_edited, salat.name);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            mAdapter.update();
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_could_create_new_salat, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validData(String name, String count) {
        return name !=  null && !name.isEmpty() && count != null && !count.isEmpty();
    }
}
