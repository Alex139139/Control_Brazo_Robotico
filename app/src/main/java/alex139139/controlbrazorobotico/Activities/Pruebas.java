package alex139139.controlbrazorobotico.Activities;
import alex139139.controlbrazorobotico.R;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class Pruebas extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Test fragment = new Test();
            transaction.replace(R.id.Test_Fragment_id,fragment);
            transaction.commit();
        }

    }


}
