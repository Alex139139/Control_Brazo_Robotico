package alex139139.controlbrazorobotico;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;


public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {


    private static int REQUEST_ENABLE_BT = 1;
    private static int DISCOVERABLE_DURATION_120= 120;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter mArrayAdapter;
    //private ArrayList mArrayList = new ArrayList();

    private Button button_Conect;
    private Button button_Scan;
    private ListView ListView_Device;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        button_Conect = (Button) findViewById(R.id.button_Conect);
        button_Scan = (Button) findViewById(R.id.button_scan);
        ListView_Device = (ListView) findViewById(R.id.ListView_Device);

        EstadoInicial_Bluetooth();
        registrar_EventosBluetooth();

        button_Conect.setOnClickListener(this);
        button_Scan.setOnClickListener(this);

        //

    }

    public void EstadoInicial_Bluetooth(){
        if (mBluetoothAdapter.isEnabled()) {
            ((Button) findViewById(R.id.button_Conect)).setText(R.string.DesactivarBluetooth);
            pairedDevicesList();
        } else if (!mBluetoothAdapter.isEnabled()) {
            ((Button) findViewById(R.id.button_Conect)).setText(R.string.ActivarBluetooth);
        }
        if(mBluetoothAdapter.isDiscovering()){
            ((Button) findViewById(R.id.button_scan)).setText(R.string.detenerBusqueda);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            ((Button) findViewById(R.id.button_scan)).setText(R.string.buscar);
        }
    }
    public void funcion_Bluetooth(){

        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"Dispositivo Sin Bluetooth ",Toast.LENGTH_SHORT).show();

        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);


            }
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
    }

    public  void scanBluetooth(){
        /*Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION_120); //En caso de querer mas duracion
        //startActivity(discoverableIntent);
        startActivityForResult(discoverableIntent, DISCOVERABLE_DURATION_120);*/

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }
        if(mBluetoothAdapter.isDiscovering()){

            mBluetoothAdapter.cancelDiscovery();
        }else if(!mBluetoothAdapter.isDiscovering()){

            mBluetoothAdapter.startDiscovery();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_Conect:
                funcion_Bluetooth();
                break;
            case R.id.button_scan:
                scanBluetooth();
                break;
        }
    }
    ///////////////////////   BroadcastReceiver  /////////////////////////////////////////////////////////////////

    // Instanciamos un BroadcastReceiver que se encargara de detectar si el estado
    // del Bluetooth del dispositivo ha cambiado mediante su handler onReceive
    private final BroadcastReceiver mReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList mArrayList = new ArrayList();
            final String action = intent.getAction();
            // Filtramos por la accion. Nos interesa detectar BluetoothAdapter.ACTION_STATE_CHANGED

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                //mArrayAdapter.notifyDataSetChanged();
                mArrayAdapter = new ArrayAdapter(BluetoothActivity.this,android.R.layout.simple_list_item_1, mArrayList);
                ListView_Device.setAdapter(mArrayAdapter);
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(BluetoothActivity.this,"Iniciando Busqueda ",Toast.LENGTH_SHORT).show();
                ((Button) findViewById(R.id.button_scan)).setText(R.string.detenerBusqueda);

            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(BluetoothActivity.this,"Busqueda Detenida ",Toast.LENGTH_SHORT).show();

                ((Button) findViewById(R.id.button_scan)).setText(R.string.buscar);
            }

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Solicitamos la informacion extra del intent etiquetada como BluetoothAdapter.EXTRA_STATE
                // El segundo parametro indicara el valor por defecto que se obtendra si el dato extra no existe
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado) {
                    // Apagado
                    case BluetoothAdapter.STATE_OFF:
                    {
                        ((Button)findViewById(R.id.button_Conect)).setText(R.string.ActivarBluetooth);
                        Toast.makeText(BluetoothActivity.this, "Bluetooth Apagado", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    // Encendido
                    case BluetoothAdapter.STATE_ON:
                    {
                        pairedDevicesList();
                        ((Button)findViewById(R.id.button_Conect)).setText(R.string.DesactivarBluetooth);

                        break;
                    }
                    default:
                        break;
                }
            }
            /*if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                final int estadoScaneo = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.ERROR);
                switch (estadoScaneo) {
                    //Conectable y Decubrible
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toast.makeText(BluetoothActivity.this,"Buscando...",Toast.LENGTH_SHORT).show();
                        break;
                    //Conectable pero Indescubrible
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Toast.makeText(BluetoothActivity.this,"Solo Dispositivos Conetados",Toast.LENGTH_SHORT).show();
                        break;
                     //Inconectable e Indescubrible
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Toast.makeText(BluetoothActivity.this,"Sin Buscar y sin localizar",Toast.LENGTH_SHORT).show();
                        break;
                }
            }*/
        }


    };

    private void registrar_EventosBluetooth() {
        // Registramos el BroadcastReceiver que instanciamos previamente para
        // detectar los distintos eventos que queremos recibir
        IntentFilter filtro = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filtro.addAction(BluetoothDevice.ACTION_FOUND);
        filtro.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filtro.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver , filtro);
    }



    // Ademas de realizar la destruccion de la actividad, eliminamos el registro del
    // BroadcastReceiver.
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver );
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //// En Lee el requestCode que se origina un activity//////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"Bluetooth On",Toast.LENGTH_SHORT).show();
                pairedDevicesList();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == DISCOVERABLE_DURATION_120){
            if (resultCode == DISCOVERABLE_DURATION_120) {
                //Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Busqueda cancelada", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void pairedDevicesList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList mArrayList = new ArrayList();
        if (pairedDevices.size()>0) {
            for(BluetoothDevice bt : pairedDevices) {
                mArrayList.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
        //final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, mArrayList);
        ListView_Device.setAdapter(mArrayAdapter);
        //ListView_Device.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }




}// cierre del activity
