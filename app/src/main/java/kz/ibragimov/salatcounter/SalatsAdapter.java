package kz.ibragimov.salatcounter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.ibragimov.salatcounter.bus.BusProvider;
import kz.ibragimov.salatcounter.model.Salat;
import kz.ibragimov.salatcounter.tasks.SalatsRetrievalTask;

/**
 *
 * Created by musa on 7/18/16.
 */
public class SalatsAdapter extends RecyclerView.Adapter<SalatsAdapter.SalatHolder> {
    private static final String TAG = SalatsAdapter.class.getSimpleName();
    SimpleDateFormat sdf = null;
    NumberFormat nf = null;
    List<Salat> mSalats = new ArrayList<>();

    public SalatsAdapter() {
        update();
    }

    @Override
    public SalatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salat_list, parent, false);
        return new SalatHolder(view);
    }

    @Override
    public void onBindViewHolder(SalatHolder holder, int position) {
        Salat salat = mSalats.get(position);
        holder.name.setText(salat.name);
        if (nf == null) {
            nf = NumberFormat.getInstance();
        }
        holder.count.setText(nf.format(salat.count));
        if (sdf == null) {
            String pattern = holder.itemView.getContext().getString(R.string.last_updated_time);
            sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        }
        holder.time.setText(sdf.format(new Date(salat.lastUpdated)));
    }

    @Override
    public int getItemCount() {
        return mSalats.size();
    }

    public void update() {
        new SalatsRetrievalTask() {
            @Override
            protected void onPostExecute(List<Salat> salats) {
                super.onPostExecute(salats);
                mSalats.clear();
                mSalats.addAll(salats);
                notifyDataSetChanged();
            }
        }.execute();
    }

    class SalatHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.salat_name)
        TextView name;
        @BindView(R.id.salat_update_time)
        TextView time;
        @BindView(R.id.salat_count)
        TextView count;
        @BindView(R.id.parent_view)
        View parentView;

        public SalatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(SalatHolder.this, itemView);
            itemView.setClickable(true);
            itemView.setOnLongClickListener(SalatHolder.this);
        }

        @OnClick({R.id.add, R.id.subtract})
        public void onButtonClick(ImageButton b) {
            Salat salat = mSalats.get(getAdapterPosition());
            switch (b.getId()) {
                case R.id.add:
                    salat.count++;
                    break;
                case R.id.subtract:
                    salat.count--;
                    break;
            }
            salat.save();
            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "onLongClick");
            BusProvider.getBus().post(mSalats.get(getAdapterPosition()));
            return true;
        }
    }

}
