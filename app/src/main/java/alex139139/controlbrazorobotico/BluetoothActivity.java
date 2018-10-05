package alex139139.controlbrazorobotico;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends AppCompatActivity  {


    private static int REQUEST_ENABLE_BT = 1;
    private static int DISCOVERABLE_DURATION_120= 120;
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> mArrayAdapter;




    private Button button_Conect;
    private Button button_Scan;
    private ListView ListView_Device;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        button_Conect = (Button) findViewById(R.id.button_Conect_id);
        button_Scan = (Button) findViewById(R.id.button_scan_id);
        ListView_Device = (ListView) findViewById(R.id.ListView_Device_id);
        mArrayAdapter = new ArrayAdapter<String>(BluetoothActivity.this,android.R.layout.simple_list_item_1);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ListView_Device.setAdapter(mArrayAdapter);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        filtros_ACTION_BT();
        EstadoInicial_Bluetooth();

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
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        //mBluetoothStatus.setText("Bluetooth enabled"); // textView  que muestra si el bluettoth esta encendido

        Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();
        //((Button)findViewById(R.id.button_Conect_id)).setText(R.string.apagar_bluetooth);

    }
    private void bluetoothOff(){
        mBluetoothAdapter.disable(); // turn off
        mArrayAdapter.clear();
        //mBluetoothStatus.setText("Bluetooth disabled");
        Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
        ((Button)findViewById(R.id.button_Conect_id)).setText(R.string.encender_bluetooth);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void EstadoInicial_Bluetooth(){
        if (mBluetoothAdapter.isEnabled()) {
            ((Button) findViewById(R.id.button_Conect_id)).setText(R.string.apagar_bluetooth);

        } else if (!mBluetoothAdapter.isEnabled()) {
            ((Button) findViewById(R.id.button_Conect_id)).setText(R.string.encender_bluetooth);
        }
        if(mBluetoothAdapter.isDiscovering()){
            ((Button) findViewById(R.id.button_scan_id)).setText(R.string.cancelar);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            ((Button) findViewById(R.id.button_scan_id)).setText(R.string.buscar);
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
                Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void discover(){
        // Check if the device is already discovering
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            //Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBluetoothAdapter.isEnabled()) {
                mArrayAdapter.clear(); // clear items
                mBluetoothAdapter.startDiscovery();
                //Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth apagado!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mArrayAdapter.notifyDataSetChanged();
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(BluetoothActivity.this,"Iniciando Busqueda ",Toast.LENGTH_SHORT).show();
                ((Button) findViewById(R.id.button_scan_id)).setText(R.string.cancelar);

            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){ Toast.makeText(BluetoothActivity.this,"Busqueda Finalizada ",Toast.LENGTH_SHORT).show();

                ((Button) findViewById(R.id.button_scan_id)).setText(R.string.buscar);
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Solicitamos la informacion extra del intent etiquetada como BluetoothAdapter.EXTRA_STATE
                // El segundo parametro indicara el valor por defecto que se obtendra si el dato extra no existe
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado) {
                    // Apagado
                    case BluetoothAdapter.STATE_OFF:
                    {
                        ((Button)findViewById(R.id.button_Conect_id)).setText(R.string.encender_bluetooth);
                        Toast.makeText(BluetoothActivity.this, "Bluetooth Apagado", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    // Encendido
                    case BluetoothAdapter.STATE_ON:
                    {
                        listPairedDevices();
                        ((Button)findViewById(R.id.button_Conect_id)).setText(R.string.apagar_bluetooth);
                        break;
                    }
                }
            }
        }

    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public  void filtros_ACTION_BT(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver,filter );
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

}// cierre del activity
