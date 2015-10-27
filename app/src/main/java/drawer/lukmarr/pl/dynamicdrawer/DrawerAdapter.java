package drawer.lukmarr.pl.dynamicdrawer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lukasz on 27.10.15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.Maludzinski> {
    public static final String TAG = DrawerAdapter.class.getSimpleName();
    private List<String> dataSet;
    private MainActivity parent;

    public DrawerAdapter(MainActivity parent, @NonNull List<String> dataSet) {
        this.dataSet = dataSet;
        this.parent = parent;
    }

    @Override
    public Maludzinski onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
        return new Maludzinski(view);
    }

    @Override
    public void onBindViewHolder(Maludzinski holder, final int position) {
        Log.d(TAG, "onBindViewHolder [" + position + "]");

        holder.itemName.setText(dataSet.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick " + dataSet.get(position));
                onItemClick(dataSet.get(position));
            }
        });
    }

    public void onItemClick(String comName) {
        parent.closeDrawer();
        parent.switchTo(comName);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addNewItem(String string) {
        Log.d(TAG, "addNewItem ");
        dataSet.add(string);
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }


    public class Maludzinski extends RecyclerView.ViewHolder {
        private TextView itemName;
        private RelativeLayout parent;

        public Maludzinski(View v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.name);
            parent = (RelativeLayout) v.findViewById(R.id.parent);
        }
    }
}
