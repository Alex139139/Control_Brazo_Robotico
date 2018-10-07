package alex139139.controlbrazorobotico.Activities;
import alex139139.controlbrazorobotico.Fragments.Test;
import alex139139.controlbrazorobotico.R;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class Pruebas extends FragmentActivity implements Test.TestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);
    }

    @Override
    public void sendData(String data) {

    }
}
