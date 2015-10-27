package drawer.lukmarr.pl.dynamicdrawer.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.UUID;

import drawer.lukmarr.pl.dynamicdrawer.ControllerFragment;
import drawer.lukmarr.pl.dynamicdrawer.MainActivity;

/**
 * Created by Lukasz Marczak on 2015-06-24.
 * Establishing bluetooth connection goes here
 */
public class BluetoothEngine {
    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static final String TAG = BluetoothEngine.class.getSimpleName();

    private BluetoothDevice bluetoothDevice = null;
    private BluetoothSocket bluetoothSocket = null;
    private static View displayView;


    private Thread connectingThread;
    private static MainActivity activity;
    private static ConnectedThread connectedThread = null;
    private static BluetoothEngine instance;

    public static BluetoothEngine getInstance(MainActivity _activity, View view) {
        if (instance == null)
            instance = new BluetoothEngine();
        activity = _activity;
        displayView = view;
        return instance;
    }

    public void writeData(String data) {
        Log.d(TAG, "writeData (" + data + ")");
        if (connectedThread != null) {
            connectedThread.write(data);
        } else {
//            ViewUtils.makeSnackBar("failed to write data", displayView, false);
            Log.e(TAG, "writeData failed to write data");
        }

    }

    @NonNull
    public static String prettyStatus(int bondState) {
        switch (bondState) {
            case BluetoothDevice.BOND_BONDED:
                return "paired";
            case BluetoothDevice.BOND_BONDING:
                return "pairing...";
            case BluetoothDevice.BOND_NONE:
            default:
                return "not paired";
        }
    }

    // connection status
    public enum STATUS {
        CONNECTED, DISCONNECTED, CONNECTION_ERROR, GET_DEVICE
    }


    public static STATUS CURRENT_STATUS;

    private BluetoothEngine() {
    }

    public void connectToDevice(@NonNull BluetoothDevice device, @NonNull ControllerFragment fragment) {
        fragment.showLoader(true);
        Log.d(TAG, "connectToDevice ");
        if (device == null) {
            Log.e(TAG, "connectToDevice : failed due to null device object");
            btDisconnect(STATUS.CONNECTION_ERROR);
            return;
        }
        bluetoothDevice = device;
        try {
            Log.d(TAG, "connectToDevice : trying to make bluetooth socket");
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
        } catch (IOException e) {
            Log.e(TAG, "Cannot make bluetooth socket");
            btDisconnect(STATUS.CONNECTION_ERROR);
            return;
        }

        connectedThread = new ConnectedThread(activity, this, bluetoothSocket);
        connectedThread.start();

        changeState(STATUS.CONNECTED);
        fragment.showLoader(false);
    }

    public void connectToDevice2(@NonNull final BluetoothDevice device, final ControllerFragment parent) {
        Log.d(TAG, "connectToDevice ");

        new Thread(new Runnable() {
            @Override
            public void run() {
                connectToDevice(device, parent);
            }
        }).start();
    }

    public void btDisconnect(STATUS comingStatus) {
        Log.d(TAG, "btDisconnect()");
        if (bluetoothSocket == null)
            return;

        if (connectedThread != null) {
            connectedThread.interrupt();
            connectedThread = null;
        }

        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "An error occurred during closing bluetooth socket");
        }

        bluetoothSocket = null;

        changeState(comingStatus);
        if (comingStatus == STATUS.CONNECTION_ERROR)
            ControllerFragment.onConnectionError();

    }

    public void changeState(STATUS iState) {

        CURRENT_STATUS = iState;
        String message;
        switch (iState) {
            case CONNECTED:
//                        Snackbar.make(displayView, "Connected with " + bluetoothDevice.getName(), Snackbar.LENGTH_SHORT).show();
                message = "Connected with " + bluetoothDevice.getName();
                break;
            case DISCONNECTED:
//                Snackbar.make(displayView, "Disconnected", Snackbar.LENGTH_SHORT).show();
                message = "Disconnected";
                break;
            case CONNECTION_ERROR:
            default:
//                Snackbar.make(displayView, "Error with connection", Snackbar.LENGTH_SHORT).show();
                message = "Error with connection";
                break;
        }
        final String fmes = message;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "message is " + fmes);
                if (displayView != null && displayView.isShown())
                    Snackbar.make(displayView, fmes, Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
