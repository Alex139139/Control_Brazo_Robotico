package alex139139.controlbrazorobotico.Activities;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import alex139139.controlbrazorobotico.Manifest;
import alex139139.controlbrazorobotico.R;
////////////////////////////////////////////
import alex139139.controlbrazorobotico.Services.BluetoothService_Test;


public class BluetoothActivity extends AppCompatActivity {
//public class BluetoothActivity extends FragmentActivity {

    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayAdapter<String> mArrayAdapter2;
    private BluetoothDevice device;

    private TextView textView_StatusBT;
    private TextView textView_StatusBT_MAC;
    private Button button_Conect;
    private Button button_Scan;
    private ListView ListView_Device;
    private ListView ListView_DeviceE;

    ////////////////////////////////////////////
    ///////////Conectividad BT/////////////////

    //private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

    BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBTSocket;

    static final int STATE_SEARCHING = 1;
    static final int STATE_NO_SEARCHING = 2;
    static final int STATE_DISCONNECT = 3;
    static final int STATE_CONNECTING = 4;
    static final int STATE_CONNECTED = 5;
    static final int STATE_CONNECTED_MAC =6;
    static final int STATE_CONNECTION_FAILED = 7;
    static final int STATE_BT_ON = 8;
    static final int STATE_BT_OFF = 9;
    static final int STATE_FOUND = 10;
    static final int STATE_NOT_FOUND = 11;
    static final int STATE_TEST = 12;
    static final int STATE_MESSAGE_RECEIVED = 13;

    private static int REQUEST_ENABLE_BT = 1;

    ////////////////////////////////////////////
    private int mConnectionState;
    private String mBTaddress;
    private String mBTname;

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// "random" unique identifier
    //493489e8-785b-4fa3-8f7f-285bf74bd1e7
    //////////////////////////////////////////
    private ConnectedThread mConnectedThread;
    private ConnectThread mConnectThread;
    /////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        asignacionID();
        EstadoInicial_UI();

        //BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mArrayAdapter = new ArrayAdapter<String>(BluetoothActivity.this,android.R.layout.simple_list_item_1);
        mArrayAdapter2 = new ArrayAdapter<String>(BluetoothActivity.this,android.R.layout.simple_list_item_1);


        filtros_ACTION_BT();

        if (mArrayAdapter == null) {
            // Device does not support Bluetooth
           // mBluetoothStatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(),"Bluetooth device not found!",Toast.LENGTH_SHORT).show();
        }
        else {
            button_Scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discover();
                }
            });
            button_Conect.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    bluetooth_On_Off();
                }
            });
            ListView_Device.setOnItemClickListener(mDeviceClickListener);
            ListView_DeviceE.setOnItemClickListener(mDeviceClickListener);
            textView_StatusBT.setOnLongClickListener(mDeviceLongClickListener);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    //// En Lee el requestCode que se origina un activity//////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"Bluetooth On",Toast.LENGTH_SHORT).show();

            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void asignacionID(){
        textView_StatusBT = (TextView)findViewById(R.id.textView_StadusBT_id);
        textView_StatusBT_MAC = (TextView)findViewById(R.id.textView_StadusBT_MAC_id);
        button_Conect = (Button) findViewById(R.id.button_Conect_id);
        button_Scan = (Button) findViewById(R.id.button_scan_id);
        ListView_Device = (ListView) findViewById(R.id.ListView_Device_id);
        ListView_DeviceE =(ListView) findViewById(R.id.ListView_DeviceE_id);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void bluetooth_On_Off(){
        if (!mBluetoothAdapter.isEnabled()) {
            bluetoothOn();
        }
        if (mBluetoothAdapter.isEnabled()) {
            bluetoothOff();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void bluetoothOn( ){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
    private void bluetoothOff(){
        mBluetoothAdapter.disable();
        handlerBT.obtainMessage(STATE_BT_OFF)
                .sendToTarget();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void EstadoInicial_UI(){
        if (mBluetoothAdapter.isEnabled()) {
            handlerBT.obtainMessage(STATE_BT_ON)
                    .sendToTarget();
            listPairedDevices();
        } else if (!mBluetoothAdapter.isEnabled()) {
            handlerBT.obtainMessage(STATE_BT_OFF)
                    .sendToTarget();
        }
        if(mBluetoothAdapter.isDiscovering()){
            handlerBT.obtainMessage(STATE_SEARCHING)
                    .sendToTarget();
        }
        if(!mBluetoothAdapter.isDiscovering()){
            handlerBT.obtainMessage(STATE_NO_SEARCHING)
                    .sendToTarget();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void listPairedDevices(){
        mArrayAdapter.clear();
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(mBluetoothAdapter.isEnabled()) {
            if (pairedDevices.size()>0) {
                for (BluetoothDevice device : pairedDevices){
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                ListView_Device.setAdapter(mArrayAdapter);
            }
            else {
                handlerBT.obtainMessage(STATE_NOT_FOUND)
                        .sendToTarget();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void discover(){

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        else{
            if(mBluetoothAdapter.isEnabled()) {
                mArrayAdapter2.clear();
                if(mBTSocket != null){

                    mConnectThread.cancel();
                    mBTaddress=null;
                    mBluetoothAdapter.startDiscovery();

                }else{
                    mBluetoothAdapter.startDiscovery();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth apagado!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public  void filtros_ACTION_BT(){

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver,filter );
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mArrayAdapter2.add(device.getName() + "\n" + device.getAddress());
                ListView_DeviceE.setAdapter(mArrayAdapter2);
                mArrayAdapter2.notifyDataSetChanged();
                handlerBT.obtainMessage(STATE_FOUND)
                        .sendToTarget();
            }

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                   // device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //String name = device.getName();
                    //textView_StatusBT.setText("Conectado a: " + name);
                    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
            }


            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                handlerBT.obtainMessage(STATE_SEARCHING)
                        .sendToTarget();
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                if(mArrayAdapter2.isEmpty()){
                    handlerBT.obtainMessage(STATE_NOT_FOUND)
                            .sendToTarget();
                }
                handlerBT.obtainMessage(STATE_NO_SEARCHING)
                        .sendToTarget();

            }

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado) {

                    case BluetoothAdapter.STATE_OFF:
                        mArrayAdapter.clear();
                        mArrayAdapter2.clear();
                        handlerBT.obtainMessage(STATE_BT_OFF)
                                .sendToTarget();
                        break;


                    case BluetoothAdapter.STATE_ON:

                        listPairedDevices();
                        handlerBT.obtainMessage(STATE_BT_ON)
                                .sendToTarget();
                        break;
                }
            }

        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private AdapterView.OnLongClickListener mDeviceLongClickListener =new AdapterView.OnLongClickListener(){

        @Override
        public boolean onLongClick(View v) {
            new Thread(){
                public void run(){
                    if(mBTSocket != null) {
                        mBTaddress = null;
                        //mConnectedThread.cancel();
                        mConnectThread.cancel();

                        handlerBT.obtainMessage(STATE_DISCONNECT)
                                .sendToTarget();
                    }
                    else{
                        handlerBT.obtainMessage(STATE_DISCONNECT)
                                .sendToTarget();
                    }
                }
            }.start();
            return false;
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private  AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            //mBluetoothAdapter.cancelDiscovery(); //Revisar esto
            handlerBT.obtainMessage(STATE_CONNECTING)
                    .sendToTarget();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);
            if(!address.equals(mBTaddress)){
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

                if(mBTSocket == null){

                    Conexcion(device,name,address);
                }

            }else{
                Toast.makeText(getBaseContext(), "Dispositivo Conectado", Toast.LENGTH_SHORT).show();
            }
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Conexcion(BluetoothDevice device,final String name, final String addrees){
        if(mBTSocket == null){

            mConnectThread =new ConnectThread(device);
            new Thread(){
                public void run(){
                    boolean connected = false;
                    try {
                        mBTSocket.connect();
                        connected = true;
                    } catch(IOException e) {
                        Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
                        try {
                            //bTSocket.close();
                            mBTSocket.close();
                            handlerBT.obtainMessage(STATE_CONNECTION_FAILED)
                                    .sendToTarget();
                            connected = false;
                        } catch(IOException close) {
                            Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                            handlerBT.obtainMessage(STATE_CONNECTION_FAILED)
                                    .sendToTarget();
                            connected = false;
                        }
                    }
                    if(connected){
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();
                        handlerBT.obtainMessage(STATE_CONNECTED, name)
                                .sendToTarget();
                        handlerBT.obtainMessage(STATE_CONNECTED_MAC,addrees)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    }
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
    private class ConnectThread extends Thread {
        //private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
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
        public BluetoothSocket createBluetoothSocket(BluetoothDevice device, UUID mUUID) throws IOException {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                //Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                return (BluetoothSocket) m.invoke(device, mUUID);
            } catch (Exception e) {
                //Log.e(BluetoothActivity.class.getSimpleName(), "Could not create Insecure RFComm Connection",e);
            }
            return  device.createRfcommSocketToServiceRecord(mUUID);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final Handler handlerBT = new Handler(new Handler.Callback() {

        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {

            byte[] buffer   = null;
            String mensaje  = null;

            switch (msg.what){
                case STATE_SEARCHING:
                    ((Button) findViewById(R.id.button_scan_id)).setText(R.string.cancelar);
                    textView_StatusBT_MAC.setText("");
                    textView_StatusBT.setText("Buscando ...handler");
                    break;
                case STATE_NO_SEARCHING:
                    ((Button) findViewById(R.id.button_scan_id)).setText(R.string.buscar);
                    break;
                case STATE_CONNECTING:
                    textView_StatusBT.setText("Conectando... handler");
                    break;
                case STATE_CONNECTED:
                    textView_StatusBT.setText("Conectado a: " + (String)(msg.obj));
                    break;
                case STATE_CONNECTED_MAC:
                    textView_StatusBT_MAC.setText((String)(msg.obj));
                    break;
                case STATE_DISCONNECT:
                    textView_StatusBT.setText("Desconetado...handler" );
                    textView_StatusBT_MAC.setText("");
                    break;
                case STATE_BT_ON:
                    ((Button) findViewById(R.id.button_Conect_id)).setText(R.string.apagar_bluetooth);
                    textView_StatusBT.setText("Encendido...handler");
                    break;
                case STATE_BT_OFF:
                    ((Button) findViewById(R.id.button_Conect_id)).setText(R.string.encender_bluetooth);
                    textView_StatusBT.setText("Apagado...handler");
                    break;
                case STATE_FOUND:
                    textView_StatusBT.setText("Dispositivos Encontrados ...handler");
                    break;
                case STATE_NOT_FOUND:
                    textView_StatusBT.setText("No se encontraron Dispositivos ...handler");
                    break;
                case STATE_TEST:
                    break;
                case STATE_CONNECTION_FAILED:
                    textView_StatusBT.setText("Connection Failed...handler");
                    break;

                case STATE_MESSAGE_RECEIVED:
                    //despues

                    break;
            }
            return true;
        }
    });
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*public class ServerConnectThread extends Thread{
        private BluetoothSocket bTSocket;

        public ServerConnectThread() { }

        public void acceptConnect(BluetoothAdapter bTAdapter, UUID mUUID) {
            BluetoothServerSocket temp = null;
            try {
                temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name:Alex139139", mUUID);
            } catch(IOException e) {
                Log.d("SERVERCONNECT", "Could not get a BluetoothServerSocket:" + e.toString());
            }
            while(true) {
                try {
                    bTSocket = temp.accept();
                } catch (IOException e) {
                    Log.d("SERVERCONNECT", "Could not accept an incoming connection.");
                    break;
                }
                if (bTSocket != null) {
                    try {
                        temp.close();
                    } catch (IOException e) {
                        Log.d("SERVERCONNECT", "Could not close ServerSocket:" + e.toString());
                    }
                    break;
                }
            }
        }

        public void closeConnect() {
            try {
                bTSocket.close();
            } catch(IOException e) {
                Log.d("SERVERCONNECT", "Could not close connection:" + e.toString());
            }
        }
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private class HiloConexion extends Thread
    {
        private final BluetoothSocket   socket;         // Socket
        private final InputStream       inputStream;    // Flujo de entrada (lecturas)
        private final OutputStream      outputStream;   // Flujo de salida (escrituras)

        public HiloConexion(BluetoothSocket socket)
        {
            this.socket = socket;
            setName(socket.getRemoteDevice().getName() + " [" + socket.getRemoteDevice().getAddress() + "]");
            InputStream tmpInputStream = null;
            OutputStream tmpOutputStream = null;
            try {
                tmpInputStream = socket.getInputStream();
                tmpOutputStream = socket.getOutputStream();
            }
            catch(IOException e){

            }
            inputStream = tmpInputStream;
            outputStream = tmpOutputStream;
        }
        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            //setEstado(ESTADO_CONECTADO);

            // Mientras se mantenga la conexion el hilo se mantiene en espera ocupada
            // leyendo del flujo de entrada
            while(true)
            {
                try {
                    // Leemos del flujo de entrada del socket
                    bytes = inputStream.read(buffer);

                    // Enviamos la informacion a la actividad a traves del handler.
                    // El metodo handleMessage sera el encargado de recibir el mensaje
                    // y mostrar los datos recibidos en el TextView

                    //handler.obtainMessage(MSG_LEER, bytes, -1, buffer).sendToTarget();
                }
                catch(IOException e) {

                }
            }
        }
        public void escribir(byte[] buffer)
        {
            try {
                // Escribimos en el flujo de salida del socket
                outputStream.write(buffer);

                // Enviamos la informacion a la actividad a traves del handler.
                // El metodo handleMessage sera el encargado de recibir el mensaje
                // y mostrar los datos enviados en el Toast

                //handler.obtainMessage(MSG_ESCRIBIR, -1, -1, buffer).sendToTarget();
            }
            catch(IOException e) {

            }
        }
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void onPause() {
        super.onPause();
            //SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
            //.Editor miEditor = datos.edit();
            //miEditor.putString("name",mBTname);
            //miEditor.putString("MAC",mBTaddress);
            //miEditor.apply();
    }

    public void onResume() {
        super.onResume();
        //SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        //mBTaddress = datos.getString("MAC","MAC no Guardada");
        //mBTname = datos.getString("name","Nombre no Guardado");
        //handlerBT.obtainMessage(STATE_TEST)
        // .sendToTarget();
            //textView_StatusBT.setText(mBTname);
            //textView_StatusBT_MAC.setText(mBTaddress);
    }



}// cierre del activity

