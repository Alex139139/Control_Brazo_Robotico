package alex139139.controlbrazorobotico.Services;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
    private BluetoothDevice mBTDevice;
    //////////////////////////////////////////////////////
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// "random" unique identifier
    //493489e8-785b-4fa3-8f7f-285bf74bd1e7
    static final int STATE_DISCONNECT = 3;
    static final int STATE_DISCONNECTION_FAILED = 4;
    static final int STATE_CONNECTING = 5;
    static final int STATE_CONNECTED = 6;
    static final int STATE_CONNECTED_MAC =7;
    static final int STATE_CONNECTION_FAILED = 8;
    static final int STATE_CONNECTION_LOST = 9;
    static final int STATE_MESSAGE_RECEIVED = 15;

    // Constants that indicate the current connection state
    public static final int S_STATE_NONE = 0;       // we're doing nothing
    public static final int S_STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int S_STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int S_STATE_CONNECTED = 3; // now connected to a remote device
    private int mState;

    //////////////////////////////////////////////////////
    private ConnectedThread mConnectedThread;
    private ConnectThread mConnectThread;

    //////////////////////////////////////////////////////
    private Handler mHandler;
    //////////////////////////////////////////////////////
    private static final UUID mBTUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// "random" unique identifier
    //493489e8-785b-4fa3-8f7f-285bf74bd1e7

    //////////////////////////////////////////////////////


    private Context context;

    public BluetoothService_Test(){

    }

    public BluetoothService_Test(Context c,Handler handler){
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        mState = S_STATE_NONE;
        mHandler = handler;
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

    public synchronized void start() {

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

    }
    public synchronized void connect(BluetoothDevice device) {


        // Cancel any thread attempting to make a connection
        if (mState == S_STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        // Update UI titleupdateUserInterfaceTitle();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized void stop() {

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mState = S_STATE_NONE;
        // Update UI title

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */

    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized int getState() {
        return mState;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity

        Message msg =mHandler.obtainMessage(STATE_CONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString("device_name",device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        Message msg2 =mHandler.obtainMessage(STATE_CONNECTED_MAC);
        Bundle bundle2 = new Bundle();
        bundle2.putString("device_MAC",device.getAddress());
        msg2.setData(bundle2);
        mHandler.sendMessage(msg2);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectionLost() {
        // Send a failure message back to the Activity
        mHandler.obtainMessage(STATE_CONNECTION_LOST).
                sendToTarget();
        mState = S_STATE_NONE;
        // Update UI title


        // Start the service over to restart listening mode
        BluetoothService_Test.this.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectionFailed() {
        // Send a failure message back to the Activity
        mHandler.obtainMessage(STATE_CONNECTION_FAILED)
                .sendToTarget();

        mState = S_STATE_NONE;
        // Update UI title


        // Start the service over to restart listening mode
        BluetoothService_Test.this.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        //private final String mmAddress;
        //private  final String mmName;
        //private  final UUID mmUUID;


//        public ConnectThread(BluetoothDevice device,final String name, final String address,UUID uuid) {
//                this.mmAddress = address;
//                this.mmName = name;
//                this.mmDevice = device;
//                //this.mmSocket = socket;
//                this.mmUUID = uuid;
//                BluetoothSocket tmp = null;
//                try {
//                    tmp = mmDevice.createRfcommSocketToServiceRecord(mmUUID);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //mmSocket = tmp;
//                mBTSocket = tmp;
//        }

        public ConnectThread(BluetoothDevice device) {

            this.mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(mBTUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mmSocket = tmp;
            mBTSocket = tmp;
            mState = S_STATE_CONNECTING;
            mHandler.obtainMessage(STATE_CONNECTING)
                    .sendToTarget();
        }

        @Override
        public void run() {
            boolean connected = false;
            mBluetoothAdapter.cancelDiscovery();
            try {
                mBTSocket.connect();
                //mmSocket.connect();
                connected = true;
            } catch(IOException e) {

                try {
                    mBTSocket.connect();
                    //mmSocket.close();
                    connected = false;
                } catch(IOException close) {

                    connected = false;
                }
            }
            if(connected){
                synchronized (BluetoothService_Test.this) {
                    mConnectThread = null;
                }
                // Reset the ConnectThread because we're done
                connected(mBTSocket,mmDevice);
                //mHandler.obtainMessage(STATE_CONNECTED, mmName).sendToTarget();
                //mHandler.obtainMessage(STATE_CONNECTED_MAC,mmAddress).sendToTarget();
            }else{
                connectionFailed();
                return;
            }


        }

        public void cancel() {
            try {
                mBTSocket.close();
                //mBTSocket = null;

            } catch (IOException e) {
                mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
                //mBTSocket = null;
            } catch(IOException e) {
                mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
                return false;
            }
            return true;
        }
//        public BluetoothSocket createBluetoothSocket(BluetoothDevice device, UUID mUUID) throws IOException {
//            try {
//                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
//                //Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
//                return (BluetoothSocket) m.invoke(device, mUUID);
//            } catch (Exception e) {
//                //Log.e(BluetoothActivity.class.getSimpleName(), "Could not create Insecure RFComm Connection",e);
//            }
//            return  device.createRfcommSocketToServiceRecord(mUUID);
//        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ConnectedThread extends Thread{
       // private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            mBTSocket = socket;
            //mmSocket = socket;
            InputStream tempIn = null;
            OutputStream temOut = null;

            try {
                tempIn = mBTSocket.getInputStream();
                temOut = mBTSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tempIn;
            mmOutStream = temOut;
            mState = S_STATE_CONNECTED;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (mState == S_STATE_CONNECTED ){
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    //bytes = mmInStream.available();

                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer)
                            .sendToTarget();
                    if(bytes != 0) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }
        public void write(byte[] buffer ) {
                    //converts entered String into bytes
            try {
                mmOutStream.write(buffer);
                //mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        //Call this from the main activity to shutdown the connection

        public void cancel() {
            try {
                mBTSocket.close();
                mHandler.obtainMessage(STATE_DISCONNECT).sendToTarget();
            } catch (IOException e) {
                mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
                //mBTSocket = null;
            } catch(IOException e) {
                mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
                return false;
            }
            return true;
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


}
