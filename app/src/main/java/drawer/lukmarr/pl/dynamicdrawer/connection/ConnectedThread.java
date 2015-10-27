package drawer.lukmarr.pl.dynamicdrawer.connection;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import drawer.lukmarr.pl.dynamicdrawer.MainActivity;

/**
 * Created by Lukasz Marczak on 2015-08-29.
 * Thread which represents connection. It is created after
 * successful bluetooth socket creation.
 */
public class ConnectedThread extends Thread {

    public static final String TAG = ConnectedThread.class.getSimpleName();
    private boolean connectionAvailable = true;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    //    private static ConnectedThread instance = null;
//    private static Handler handler = new Handler();

    private final MainActivity activity;
    private BluetoothEngine engine;

    public ConnectedThread(MainActivity parentActivity, BluetoothEngine directParent, BluetoothSocket socket) {
        Log.d(TAG, "ConnectedThread ");

        activity = parentActivity;
        engine = directParent;

        try {
            mmInStream = socket.getInputStream();
            mmOutStream = socket.getOutputStream();
        } catch (IOException e) {


            new Handler().post(new Runnable() {
                public void run() {
                    connectionAvailable = false;
                    engine.btDisconnect(BluetoothEngine.STATUS.CONNECTION_ERROR);
                }
            });
        }

    }

    public void run() {

        byte[] buffer = new byte[1024];
        int bytes;

        while (connectionAvailable) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                String string = "";
                if (bytes > 0) {
                    byte[] newbuffer = new byte[bytes];
                    for (int i = 0; i < bytes; i++)
                        newbuffer[i] = buffer[i];
                    final String data = new String(newbuffer, "US-ASCII");

                    Log.i(TAG, "reading: " + data);
                }

            } catch (IOException e) {
                Log.e(TAG, "error occurred during on opened input stream");
                try {
                    mmInStream.close();
                } catch (IOException ex) {
                    Log.e(TAG, "failed to close input stream ");
                }
                break;
            }
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    void write(int one) {
        if (BluetoothEngine.CURRENT_STATUS != BluetoothEngine.STATUS.CONNECTED)
            return;

        try {
            mmOutStream.write(one);
        } catch (IOException e) {

            new Handler().post(new Runnable() {
                public void run() {
                    connectionAvailable = false;
                    engine.btDisconnect(BluetoothEngine.STATUS.CONNECTION_ERROR);
                }
            });
        }
    }

    void write(String str) {
        if (BluetoothEngine.CURRENT_STATUS != BluetoothEngine.STATUS.CONNECTED)
            return;

        try {
            mmOutStream.write(str.getBytes());
        } catch (IOException e) {

            synchronized (activity) {
                connectionAvailable = false;
                engine.btDisconnect(BluetoothEngine.STATUS.CONNECTION_ERROR);
            }
        }

    }
}

