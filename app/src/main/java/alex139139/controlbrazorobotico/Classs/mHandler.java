package alex139139.controlbrazorobotico.Classs;

import android.os.Message;
import android.os.Handler;
import android.os.Messenger;
import android.widget.Button;
import android.widget.TextView;

import alex139139.controlbrazorobotico.R;


public class mHandler extends Handler {
    static final int STATE_SEARCHING = 1;
    static final int STATE_NO_SEARCHING = 2;
    static final int STATE_DISCONNECT = 3;
    static final int STATE_DISCONNECTION_FAILED = 4;
    static final int STATE_CONNECTING = 5;
    static final int STATE_CONNECTED = 6;
    static final int STATE_CONNECTED_MAC =7;
    static final int STATE_CONNECTION_FAILED = 8;
    static final int STATE_CONNECTION_LOST = 9;
    static final int STATE_BT_ON = 10;
    static final int STATE_BT_OFF = 11;
    static final int STATE_FOUND = 12;
    static final int STATE_NOT_FOUND = 13;
    static final int STATE_TEST = 14;
    static final int STATE_MESSAGE_RECEIVED = 15;

    private TextView textView_StatusBT;
    private TextView textView_StatusBT_MAC;
    private Button button_Conect;
    private Button button_Scan;
    public mHandler(){

    }

    public mHandler(TextView textView_StatusBT,TextView textView_StatusBT_MAC,Button button_Scan, Button  button_Conect){
        this.textView_StatusBT = textView_StatusBT;
        this.textView_StatusBT_MAC = textView_StatusBT_MAC;
        this.button_Conect = button_Conect;
        this.button_Scan = button_Scan;
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {

            case STATE_SEARCHING:
                button_Scan.setText(R.string.cancelar);
                textView_StatusBT_MAC.setText("");
                textView_StatusBT.setText("Buscando ...handler");
                break;
            case STATE_NO_SEARCHING:
                button_Scan.setText(R.string.buscar);
                break;
            case STATE_CONNECTING:
                textView_StatusBT.setText("Conectando... handler");
                break;
            case STATE_CONNECTED:
                //mBTname = msg.getData().getString("device_name");
                //textView_StatusBT.setText(mBTname);
                 textView_StatusBT.setText("Conectado a: " + (String)(msg.obj));
                break;
            case STATE_CONNECTED_MAC:
                //mBTaddress = msg.getData().getString("device_MAC");
                //textView_StatusBT_MAC.setText(mBTaddress);
                textView_StatusBT_MAC.setText((String)(msg.obj));
                break;
            case STATE_CONNECTION_FAILED:
                textView_StatusBT.setText("Connection Failed...handler");
                break;
            case STATE_DISCONNECT:
                textView_StatusBT.setText("Desconetado...handler" );
                textView_StatusBT_MAC.setText("");
                break;
            case STATE_CONNECTION_LOST:
                textView_StatusBT.setText("Conexion Perdida...handler" );
                break;
            case STATE_DISCONNECTION_FAILED:
                textView_StatusBT.setText("Fallo en Desconexion...handler" );
                textView_StatusBT_MAC.setText("");
                break;
            case STATE_BT_ON:
                button_Conect.setText(R.string.apagar_bluetooth);
                textView_StatusBT.setText("Encendido...handler");
                break;
            case STATE_BT_OFF:
                button_Conect.setText(R.string.encender_bluetooth);
                textView_StatusBT.setText("Apagado...handler");
                break;
            case STATE_FOUND:
                textView_StatusBT.setText("");
                textView_StatusBT.setText("Dispositivos Encontrados ...handler");
                break;
            case STATE_NOT_FOUND:
                textView_StatusBT.setText("No se encontraron Dispositivos ...handler");
                break;
            case STATE_TEST:
                break;

            case STATE_MESSAGE_RECEIVED:
                //despues

                break;
        }

    }
    final Messenger mMessenger = new Messenger(new mHandler());
   /* @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }*/


}

