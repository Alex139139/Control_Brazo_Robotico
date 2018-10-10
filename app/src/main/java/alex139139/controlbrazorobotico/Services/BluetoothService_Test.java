package alex139139.controlbrazorobotico.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;



public class BluetoothService_Test extends Service implements Serializable {

    private Context context;
    //////////////////////////////////////////////////////
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBTSocket;
    private BluetoothDevice mBTDevice;
    //////////////////////////////////////////////////////
    // Constants that indicate the current connection state
    public static final int S_STATE_NONE = 0;       // we're doing nothing
    public static final int S_STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int S_STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int S_STATE_CONNECTED = 3; // now connected to a remote device

    public final static String ACTION_BT_SERVICE_NONE = ".Services.BluetoothService_TesT.ACTION_BT_SERVICE_NONE";
    public final static String ACTION_BT_SERVICE_CONNECTING = ".Services.BluetoothService_TesT.ACTION_BT_SERVICE_CONNECTING";
    public final static String ACTION_BT_SERVICE_CONNECTED = ".Services.BluetoothService_TesT.ACTION_BT_SERVICE_CONNECTED";

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BluetoothService_Test() {

    }
    public BluetoothService_Test(Context c, Handler handler) {
        mState = S_STATE_NONE;
        mHandler = handler;
        this.context = c;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private CountHandler mServiceHandler;

    private final class CountHandler extends Handler{
        CountHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            Intent request = (Intent) msg.obj;
            String address = request.getStringExtra("device_address");
            int starId =msg.arg1;
            if (address != null){
                mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
                mBTDevice = mBluetoothAdapter.getRemoteDevice(address);
                connect(mBTDevice);
                connectionStates();
            }

            //se destruye a si mismo
            //boolean stopped =stopSelfResult(starId);
            // stop(starId);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread backgroundThread  = new HandlerThread("CounterThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        backgroundThread .start();

        mServiceHandler =  new CountHandler(backgroundThread.getLooper());
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "Servicio iniciado...")
        //if(intent != null){}
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
        return START_NOT_STICKY;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        stop();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            if (mState != S_STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
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
    public synchronized int getState() {
        return mState;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
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
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public synchronized BluetoothDevice get_device(Handler mHandler){
//        if(mBTDevice == null){
//            return null;
//        }else{
//            Message msg =mHandler.obtainMessage(STATE_CONNECTED);
//            Bundle bundle = new Bundle();
//            bundle.putString("device_name",mBTDevice.getName());
//            msg.setData(bundle);
//            mHandler.sendMessage(msg);
//            Message msg2 =mHandler.obtainMessage(STATE_CONNECTED_MAC);
//            Bundle bundle2 = new Bundle();
//            bundle2.putString("device_MAC",mBTDevice.getAddress());
//            msg2.setData(bundle2);
//            mHandler.sendMessage(msg2);
//            return mBTDevice;
//        }
//
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectionLost() {
        // Send a failure message back to the Activity
        //mHandler.obtainMessage(STATE_CONNECTION_LOST).sendToTarget();
        mState = S_STATE_NONE;
        // Update UI title
        // Start the service over to restart listening mode
        BluetoothService_Test.this.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectionFailed() {
        // Send a failure message back to the Activity
       // mHandler.obtainMessage(STATE_CONNECTION_FAILED).sendToTarget();

        mState = S_STATE_NONE;
        // Update UI title


        // Start the service over to restart listening mode
        BluetoothService_Test.this.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

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
            //mHandler.obtainMessage(STATE_CONNECTING).sendToTarget();
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
                //mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
            } catch(IOException e) {
               // mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
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
                   // mHandler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
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
               // mHandler.obtainMessage(STATE_DISCONNECT).sendToTarget();
            } catch (IOException e) {
               // mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
                //mBTSocket = null;
            } catch(IOException e) {
                //mHandler.obtainMessage(STATE_DISCONNECTION_FAILED).sendToTarget();
                return false;
            }
            return true;
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void connectionStates(){
        sendBroadcast(mState);
    }

    private void sendBroadcast(int state){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(BluetoothService_Test.this);
        switch (state){
            case S_STATE_NONE:
                Intent resultIntent1 = new Intent(ACTION_BT_SERVICE_NONE);
                //resultIntent1.putExtra("Valor1",1);
                broadcastManager.sendBroadcast(resultIntent1);

                break;
            case S_STATE_CONNECTING:
                Intent resultIntent2 = new Intent(ACTION_BT_SERVICE_CONNECTING);
                broadcastManager.sendBroadcast(resultIntent2);
                break;
            case S_STATE_CONNECTED:
                Intent resultIntent3 = new Intent(ACTION_BT_SERVICE_CONNECTED);

                broadcastManager.sendBroadcast(resultIntent3);
                break;
        }
    }

}
