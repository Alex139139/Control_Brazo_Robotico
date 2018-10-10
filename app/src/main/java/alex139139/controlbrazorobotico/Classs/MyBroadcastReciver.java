package alex139139.controlbrazorobotico.Classs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReciver extends BroadcastReceiver {
    public final static String ACTION_BT_SERVICE_NONE = ".BluetoothService_TesT.ACTION_BT_SERVICE_NONE";
    public final static String ACTION_BT_SERVICE_CONNECTING = "ACTION_BT_SERVICE_CONNECTING";
    public final static String ACTION_BT_SERVICE_CONNECTED = "ACTION_BT_SERVICE_CONNECTED";
    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
