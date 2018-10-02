package alex139139.controlbrazorobotico;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {


    private static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;

    private Button button_Conect;
    private ListView ListView_Device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        button_Conect = (Button)findViewById(R.id.button_Conect);
        ListView_Device =  (ListView)findViewById(R.id.ListView_Device);

        ((Button)findViewById(R.id.button_Conect)).setText(R.string.Bluetooth);
        button_Conect.setOnClickListener(this);

        registrarEventosBluetooth();

    }

    public void funcion_Bluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_Conect:
                funcion_Bluetooth();
                break;
        }
    }

    ///////////////////////   BroadcastReceiver  /////////////////////////////////////////////////////////////////

    // Instanciamos un BroadcastReceiver que se encargara de detectar si el estado
    // del Bluetooth del dispositivo ha cambiado mediante su handler onReceive
    private final BroadcastReceiver mReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            final String action = intent.getAction();
            // Filtramos por la accion. Nos interesa detectar BluetoothAdapter.ACTION_STATE_CHANGED

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                // Solicitamos la informacion extra del intent etiquetada como BluetoothAdapter.EXTRA_STATE
                // El segundo parametro indicara el valor por defecto que se obtendra si el dato extra no existe
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado)
                {
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
                        ((Button)findViewById(R.id.button_Conect)).setText(R.string.DesactivarBluetooth);

                        // Lanzamos un Intent de solicitud de visibilidad Bluetooth, al que añadimos un par
                        // clave-valor que indicara la duracion de este estado, en este caso 120 segundos
                        /*Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);startActivity(discoverableIntent);*/
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    };
    private void registrarEventosBluetooth() {
        // Registramos el BroadcastReceiver que instanciamos previamente para
        // detectar los distintos eventos que queremos recibir
        IntentFilter filtro = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
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
                Toast.makeText(this,"Bluetooth Encendido",Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth No se Encendio", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

}// cierre del activity
