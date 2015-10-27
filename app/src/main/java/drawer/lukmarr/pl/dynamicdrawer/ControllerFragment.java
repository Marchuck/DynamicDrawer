package drawer.lukmarr.pl.dynamicdrawer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import drawer.lukmarr.pl.dynamicdrawer.connection.BluetoothEngine;


public class ControllerFragment extends Fragment {
    public static final String TAG = ControllerFragment.class.getSimpleName();
    private BluetoothDevice bluetoothDevice = null;
    private MainActivity parentActivity;
    private static ControllerFragment instance;
    private BluetoothEngine btEngine;
    private ProgressBar progressBar;

    public static ControllerFragment newInstance(BluetoothDevice device) {
        ControllerFragment fragment = new ControllerFragment();
        instance = fragment;
        Bundle b = new Bundle();
        b.putParcelable("DEVICE", device);
        fragment.setArguments(b);
        return fragment;
    }

    public ControllerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bluetoothDevice = (BluetoothDevice) getArguments().get("DEVICE");
        }
        if (bluetoothDevice != null) {
            Log.d(TAG, "device nonNull");
            btEngine = BluetoothEngine.getInstance(parentActivity, getView());
            Log.i(TAG, "connectiong via thread");
            btEngine.connectToDevice2(bluetoothDevice, this);

        } else {
            Log.e(TAG, "onCreate : failed to start bluetooth engine");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    parentActivity.setInitialFragment();
                }
            }, 1000);
            Snackbar.make(getView(), "Failed to connect", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        return v;
    }

    public static void onConnectionError() {
        Log.e(TAG, "onConnectionError ");
        View v = instance.getView();
        if (v != null && v.isShown())
            Snackbar.make(v, "Failed to connect", Snackbar.LENGTH_SHORT).show();
    }

    public void showLoader(final boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        final int vis = show ? View.VISIBLE : View.GONE;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setVisibility(vis);
                }
                if (!show) {
                    if (getView() != null && getView().isShown())
                        Snackbar.make(getView(), "Successfully connected", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
