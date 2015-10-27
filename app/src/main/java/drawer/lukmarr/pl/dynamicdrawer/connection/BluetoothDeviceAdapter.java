package drawer.lukmarr.pl.dynamicdrawer.connection;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import drawer.lukmarr.pl.dynamicdrawer.ControllerFragment;
import drawer.lukmarr.pl.dynamicdrawer.R;


/**
 * Created by Lukasz Marczak on 2015-08-27.
 */
public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private static final String TAG = BluetoothDeviceAdapter.class.getSimpleName();
    private List<BluetoothDevice> pairedDevices = new ArrayList<>();
    private FragmentActivity fragmentActivity;

    public BluetoothDeviceAdapter(FragmentActivity activity, List<BluetoothDevice> set) {
        Log.d(TAG, "BluetoothDeviceAdapter ");
        pairedDevices = set;
        this.fragmentActivity = activity;
        Collections.sort(pairedDevices, new Comparator<BluetoothDevice>() {
            @Override
            public int compare(BluetoothDevice lhs, BluetoothDevice rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
                notifyItemRangeChanged(0, getItemCount());
            }
        }, 1000);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_device_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {

        BluetoothDevice device = pairedDevices.get(position);

        vh.mac_address.setText(device.getAddress());
        vh.name.setText(device.getName());
        vh.bond_state.setText(BluetoothEngine.prettyStatus(device.getBondState()));

        /**returning uuid of device: {@link android.os.ParcelUuid}*/

        if (device.getUuids() != null)
            for (ParcelUuid parcelUuid : device.getUuids())
                Log.d(TAG, "uuids() = " + parcelUuid.toString());
    }

    @Override
    public int getItemCount() {
        return pairedDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView mac_address, name, bond_state;
        public RelativeLayout dataParent;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.data_parent);
            name = (TextView) v.findViewById(R.id.name);
            mac_address = (TextView) v.findViewById(R.id.mac_address);
            bond_state = (TextView) v.findViewById(R.id.bond_state);
            setupListeners();
        }

        private void setupListeners() {
            dataParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int fixedPosition = getAdapterPosition() < 0 ? 0 : getAdapterPosition();
                            Log.d(TAG, "onClick item " + fixedPosition);
                            BluetoothDevice device = pairedDevices.get(fixedPosition);
                            fragmentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, ControllerFragment.newInstance(device))
                                    .commit();
//                            ViewUtils.getInstance().switchToControllerFragment(fragmentActivity, device);

                        }
                    }, 500);
                }
            });
        }
    }
}
