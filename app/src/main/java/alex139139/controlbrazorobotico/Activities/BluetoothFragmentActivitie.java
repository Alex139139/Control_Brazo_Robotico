package alex139139.controlbrazorobotico.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import alex139139.controlbrazorobotico.Classs.MyBroadcastReciver;
import alex139139.controlbrazorobotico.R;

public class BluetoothFragmentActivitie extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_fragment_activitie);

        broadcastReceiver = new MyBroadcastReciver();
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(".BluetoothService_TesT.ACTION_BT_SERVICE_NONE");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
