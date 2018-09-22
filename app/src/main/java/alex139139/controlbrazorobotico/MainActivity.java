package alex139139.controlbrazorobotico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch switch_M1;
    private Switch switch_M2;
    private Switch switch_M3;
    private Switch switch_M4;
    private Switch switch_M5;
    private Switch switch_M6;

    private EditText editText_M1;
    private EditText editText_M2;
    private EditText editText_M3;
    private EditText editText_M4;
    private EditText editText_M5;
    private EditText editText_M6;

    private Button button_less_M1;
    private Button button_less_M2;
    private Button button_less_M3;
    private Button button_less_M4;
    private Button button_less_M5;
    private Button button_less_M6;

    private Button button_plus_M1;
    private Button button_plus_M2;
    private Button button_plus_M3;
    private Button button_plus_M4;
    private Button button_plus_M5;
    private Button button_plus_M6;

    private TextView txtView_Estado_M1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_M1 = (Switch)findViewById(R.id.switch_Motor1);
        switch_M2 = (Switch)findViewById(R.id.switch_Motor2);
        switch_M3 = (Switch)findViewById(R.id.switch_Motor3);
        switch_M4 = (Switch)findViewById(R.id.switch_Motor4);
        switch_M5 = (Switch)findViewById(R.id.switch_Motor5);
        switch_M6 = (Switch)findViewById(R.id.switch_Motor6);

        editText_M1 = (EditText)findViewById(R.id.editText);
        editText_M2 = (EditText)findViewById(R.id.editText2);
        editText_M3 = (EditText)findViewById(R.id.editText3);
        editText_M4 = (EditText)findViewById(R.id.editText4);
        editText_M5 = (EditText)findViewById(R.id.editText5);
        editText_M6 = (EditText)findViewById(R.id.editText6);

        button_less_M1 = (Button)findViewById(R.id.button1m);
        button_less_M2 = (Button)findViewById(R.id.button2m);
        button_less_M3 = (Button)findViewById(R.id.button3m);
        button_less_M4 = (Button)findViewById(R.id.button4m);
        button_less_M5 = (Button)findViewById(R.id.button5m);
        button_less_M6 = (Button)findViewById(R.id.button6m);

        button_plus_M1 = (Button)findViewById(R.id.button1p);
        button_plus_M2 = (Button)findViewById(R.id.button2p);
        button_plus_M3 = (Button)findViewById(R.id.button3p);
        button_plus_M4 = (Button)findViewById(R.id.button4p);
        button_plus_M5 = (Button)findViewById(R.id.button5p);
        button_plus_M6 = (Button)findViewById(R.id.button6p);

        txtView_Estado_M1 = (TextView)findViewById(R.id.status_M1);

        Motor_On_Off(switch_M1);
        Motor_On_Off(switch_M2);
        Motor_On_Off(switch_M3);
        Motor_On_Off(switch_M4);
        Motor_On_Off(switch_M5);
        //Motor_On_Off(switch_M6);



    }
    //metodos Para sumar o restar grados

    public void Control_OnOff(View view){

        int valorGrados_M1_int= 0;
        String valorGrados_M1_String = String.valueOf(valorGrados_M1_int);
        //String valorGrados_M1_String = editText_M1.getText().toString();
        valorGrados_M1_int = Integer.parseInt(valorGrados_M1_String);
        txtView_Estado_M1.setText(valorGrados_M1_String);

    }
    public void Motor_On_Off(Switch switch_M){

        switch_M.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Toast.makeText(MainActivity.this, "Motor encendido", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Motor apagado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//Cierre de la funcion Motor_On_Off

    public void sumar_Grados(View view ){
        //button_plus_M.hasOnClickListeners();


    }

}
