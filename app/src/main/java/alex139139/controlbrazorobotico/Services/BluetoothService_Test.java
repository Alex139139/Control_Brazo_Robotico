package alex139139.controlbrazorobotico.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import alex139139.controlbrazorobotico.R;

public class BluetoothService_Test extends Service {

    //////////////////////////////////////////////////////
    BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBTSocket;
    //////////////////////////////////////////////////////


    //////////////////////////////////////////////////////
    private ConnectedThread mConnectedThread;
    private ConnectThread mConnectThread;

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    private Context context;

    public BluetoothService_Test(){
        super();

        this.context = this.getApplicationContext();
    }

    public BluetoothService_Test(Context c){

        this.context = c;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {

        //Log.d(TAG, "Servicio creado...");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "Servicio iniciado...");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "Servicio destruido...");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ConnectThread extends Thread {
        //private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device, UUID mUUID) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBTSocket = tmp;
        }

        @Override
        public void run() {
 /*           setName("ConnectThread");
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //connectionFailed();
                return;

            }
            synchronized (PrinterService.this) {
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice);*/
        }

        public void cancel() {
            try {
                mBTSocket.close();
                mBTSocket = null;
            } catch (IOException e) {
                Log.e("PrinterService", "close() of connect socket failed", e);
            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
                //mBTSocket = null;

            } catch(IOException e) {
                Log.d("CONNECTTHREAD","Could not close connection:" + e.toString());
                return false;
            }
            return true;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            mmSocket = socket;
            InputStream tempIn = null;
            OutputStream temOut = null;

            try {
                tempIn = mmSocket.getInputStream();
                temOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tempIn;
            mmOutStream = temOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        //Call this from the main activity to shutdown the connection

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

}
