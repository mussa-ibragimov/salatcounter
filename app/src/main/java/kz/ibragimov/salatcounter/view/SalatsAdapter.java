package kz.ibragimov.salatcounter.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import kz.ibragimov.salatcounter.R;
import kz.ibragimov.salatcounter.databinding.ItemSalatBinding;
import kz.ibragimov.salatcounter.model.Salat;
import kz.ibragimov.salatcounter.view.adapters.DataBoundAdapter;
import kz.ibragimov.salatcounter.view.adapters.DataBoundViewHolder;
import kz.ibragimov.salatcounter.viewmodel.SalatsViewModel;

/**
 *
 * Created by musa on 7/18/16.
 */
public class SalatsAdapter extends DataBoundAdapter<ItemSalatBinding> {
    private static final String TAG = SalatsAdapter.class.getSimpleName();
    SimpleDateFormat sdf = null;
    SalatsViewModel mViewModel;
    String datePattern;

    public SalatsAdapter(SalatsViewModel viewModel, String pattern) {
        super(R.layout.item_salat);
        mViewModel = viewModel;
        datePattern = pattern;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemSalatBinding> holder, int position, List<Object> payloads) {
        Salat salat = mViewModel.getSalats().get(position);
        holder.binding.setItem(salat);
        if (sdf == null) {
            sdf = new SimpleDateFormat(datePattern, Locale.getDefault());
        }
        holder.binding.salatUpdateTime.setText(sdf.format(new Date(salat.lastUpdated)));
        holder.binding.setViewModel(mViewModel);
    }

    @Override
    public int getItemCount() {
        return mViewModel.getSalats().size();
    }
}
