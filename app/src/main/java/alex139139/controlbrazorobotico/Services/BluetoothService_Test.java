package alex139139.controlbrazorobotico.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import alex139139.controlbrazorobotico.Activities.BluetoothActivity;
import alex139139.controlbrazorobotico.Activities.MainActivity;


public class BluetoothService_Test extends Service {

    private Context context;
    //////////////////////////////////////////////////////
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBTSocket;
    private BluetoothDevice mBTDevice;
    //////////////////////////////////////////////////////
    LocalBroadcastManager localBroadcastManager;
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    // Constants that indicate the current connection state
    public static final int S_STATE_NONE = 0;       // we're doing nothing
    public static final int S_STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int S_STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int S_STATE_CONNECTED = 3; // now connected to a remote device
    public static final int S_STATE_CONENECTION_LOST = 4;
    public static final int S_STATE_CONNECTION_FAILED = 5;
    public static final int S_STATE_DISCONNECTED = 6;
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    public int mState;
    public final static  int RETURN_STATE = 1 ;
    public final static  int SEND_DATA = 2;
    public final static  int CONNECTED_DEVICE = 3;


    public final static  String STRING_RETURN_STATE ="STRING_RETURN_STATE" ;
    public final static  String STRING_SEND_DATA = "STRING_SEND_DATA";
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    public final static String ACTION_BT_SERVICE_DISCONNECTED = "BuetoothServisce_Test.ACTION_BT_SERVICE_DISCONNECTED";
    public final static String ACTION_BT_SERVICE_CONNECTING = "BuetoothServisce_Test.ACTION_BT_SERVICE_CONNECTING";
    public final static String ACTION_BT_SERVICE_CONNECTED = "BuetoothServisce_Test.ACTION_BT_SERVICE_CONNECTED";
    public final static String ACTION_BT_SERVICE_CONNECTION_LOST = "BuetoothServisce_Test.ACTION_BT_SERVICE_CONNECTION_LOST";
    public final static String ACTION_BT_SERVICE_CONNECTION_FAILED = "BuetoothServisce_Test.ACTION_BT_SERVICE_CONNECTION_FAILED";
    //////////////////////////////////////////////////////
    public ConnectedThread mConnectedThread;
    private ConnectThread mConnectThread;
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    private static final UUID mBTUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// "random" unique identifier
    //493489e8-785b-4fa3-8f7f-285bf74bd1e7
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BluetoothService_Test() {

    }
    public BluetoothService_Test(Context c) {
        mState = S_STATE_NONE;
        this.context = c;
    }

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

            }
            //se destruye a si mismo
            //boolean stopped =stopSelfResult(starId);
            // stop(starId);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread backgroundThread  = new HandlerThread("CounterThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        backgroundThread .start();
        mServiceHandler =  new CountHandler(backgroundThread.getLooper());
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
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
        //Intent resultIntent = new Intent(ACTION_BT_SERVICE_DISCONNECTED);
        //localBroadcastManager.sendBroadcast(resultIntent);
        stop();
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
    public synchronized void stop() {

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mState = S_STATE_DISCONNECTED;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //que hacer despues de 10 segundos
                Intent resultIntent = new Intent(ACTION_BT_SERVICE_DISCONNECTED);
                localBroadcastManager.sendBroadcast(resultIntent);
            }
        }, 1000);


        // Update UI title
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
    public synchronized void connected(BluetoothSocket socket) {
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
    private void connectionLost() {
        // Send a failure message back to the Activity
        mState = S_STATE_CONENECTION_LOST;
        // Update UI title
        Intent resultIntent = new Intent(ACTION_BT_SERVICE_CONNECTION_LOST);
        localBroadcastManager.sendBroadcast(resultIntent);
        // Start the service over to restart listening mode
        BluetoothService_Test.this.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectionFailed() {
        // Send a failure message back to the Activity
        mState = S_STATE_NONE;
        // Update UI title
        Intent resultIntent = new Intent(ACTION_BT_SERVICE_CONNECTION_FAILED);
        localBroadcastManager.sendBroadcast(resultIntent);
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
            Intent resultIntent = new Intent(ACTION_BT_SERVICE_CONNECTING);
            localBroadcastManager.sendBroadcast(resultIntent);

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
                connected(mBTSocket);
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

            }
        }
        public boolean cancel_bis() {
            try {
                mBTSocket.close();
            } catch(IOException e) {
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
    public class ConnectedThread extends Thread{
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

            mState = S_STATE_CONNECTED;
            String nameDevice = mBTDevice.getName();
            String nameAddress = mBTDevice.getAddress();
            Intent resultIntent = new Intent(ACTION_BT_SERVICE_CONNECTED);
            resultIntent.putExtra("addressDevice",nameAddress);
            resultIntent.putExtra("nameDevice",nameDevice);
            localBroadcastManager.sendBroadcast(resultIntent);
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
                    if(bytes != 0) {
                        //Mensaje recibido
                        //mServiceHandler.obtainMessage(STATE_MESSAGE_RECIVED,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }
        public  void write(byte[] bytes ) {
                    //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
                //mmOutStream.write(bytes);
            } catch (IOException e) {
                connectionFailed();
            }
        }
        //Call this from the main activity to shutdown the connection
        public void cancel() {
            try {
                mBTSocket.close();

            } catch (IOException e) {

            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public  void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != S_STATE_CONNECTED){
                return;
            }
            else{
                r = mConnectedThread;
                connectionFailed();
            }
        }

        // Perform the write unsynchronized
        r.write(out);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    class IncomingHandler  extends Handler{
        @Override
        public void handleMessage(Message msg){
            //Sacamos los datos del mensaje
            //Aqui se ejecutara el metodo de suma y resta creo

            switch (msg.what){
                case RETURN_STATE:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    outMessenger = msg.replyTo;
                    Message message =Message.obtain(null,RETURN_STATE,0,mState);
                    try {
                        outMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case CONNECTED_DEVICE:
                    String nameDevice = mBTDevice.getName();
                    String nameAddress = mBTDevice.getAddress();

                    outMessenger = msg.replyTo;
                    Message message2 = Message.obtain(null,CONNECTED_DEVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("addressDevice",nameAddress);
                    bundle.putString("nameDevice",nameDevice);
                    message2.setData(bundle);
                    try {
                        outMessenger.send(message2);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case SEND_DATA:
                    mConnectedThread.write("Hola señora loca".getBytes());
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    Messenger inMessenger = new Messenger(new IncomingHandler ());        //recibe los mensajes del activity

    private  Messenger outMessenger;                                     //Respuesta resultado de la operacion al activity
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public IBinder onBind(Intent intent) {
        return inMessenger.getBinder();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


}


