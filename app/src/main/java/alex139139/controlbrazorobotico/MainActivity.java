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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String GradosMotor_StringM1 = "179";
    String GradosMotor_StringM2 = "80";
    String GradosMotor_StringM3 = "50";
    String GradosMotor_StringM4 = "0";
    String GradosMotor_StringM5 = "40";
    String GradosMotor_StringM6 = "20";


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

    private int GradosM1= 90;


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



        Motor_On_Off(switch_M1);
        Motor_On_Off(switch_M2);
        Motor_On_Off(switch_M3);
        Motor_On_Off(switch_M4);
        Motor_On_Off(switch_M5);
        Motor_On_Off(switch_M6);


        button_plus_M1.setOnClickListener(MainActivity.this);
        button_plus_M2.setOnClickListener(MainActivity.this);
        button_plus_M3.setOnClickListener(MainActivity.this);
        button_plus_M4.setOnClickListener(MainActivity.this);
        button_plus_M5.setOnClickListener(MainActivity.this);
        button_plus_M6.setOnClickListener(MainActivity.this);

        button_less_M1.setOnClickListener(MainActivity.this);
        button_less_M2.setOnClickListener(MainActivity.this);
        button_less_M3.setOnClickListener(MainActivity.this);
        button_less_M4.setOnClickListener(MainActivity.this);
        button_less_M5.setOnClickListener(MainActivity.this);
        button_less_M6.setOnClickListener(MainActivity.this);



        editText_M1.setText(GradosMotor_StringM1);
        editText_M2.setText(GradosMotor_StringM2);
        editText_M3.setText(GradosMotor_StringM3);
        editText_M4.setText(GradosMotor_StringM4);
        editText_M5.setText(GradosMotor_StringM5);
        editText_M6.setText(GradosMotor_StringM6);








    }//Cierre Funcion onCreate
    //metodos Para sumar o restar grados

/*    public void Control_OnOff(View view){

        int valorGrados_M1_int= 0;
        String valorGrados_M1_String = String.valueOf(valorGrados_M1_int);
        String valorGrados_M1_String = editText_M1.getText().toString();
        valorGrados_M1_int = Integer.parseInt(valorGrados_M1_String);
        txtView_Estado_M1.setText(valorGrados_M1_String);

    }*/
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button1m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                restaGrados(editText_M1,GradosMotor_StringM1);
                break;
            case R.id.button2m:
                Toast.makeText(MainActivity.this, "-1,2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3m:
                Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4m:
                Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5m:
                Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6m:
                Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button1p:
               // Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                sumaGrados(editText_M1,GradosMotor_StringM1);
                break;
            case R.id.button2p:
                Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3p:
                Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4p:
                Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5p:
                Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6p:
                Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                break;
        }
    }//Cierre Funcion onClick

    public void sumaGrados (EditText editText_M, String GradosMotor_StringM){
        String GradosMotor_String = editText_M.getText().toString();
        int GradosMotor_int;
        int Flag =0;

        if(editText_M.getText().toString().isEmpty()){
            if(Flag == 0){
                editText_M.setText(GradosMotor_StringM);
            }else{
                Flag = 1;
                GradosMotor_int =Integer.parseInt(GradosMotor_String);
                GradosMotor_String = String.valueOf(GradosMotor_int);
                editText_M.setText(GradosMotor_String);
            }

        }else{
            GradosMotor_int =Integer.parseInt(GradosMotor_String);
            if(GradosMotor_int >= 180 || GradosMotor_int < 0){
                //editText_M.setText(GradosMotor_String);

            }else if (GradosMotor_int < 180){
                GradosMotor_int ++;
                //editText_M.setText(GradosMotor_String);
                GradosMotor_String = String.valueOf(GradosMotor_int);
                editText_M.setText(GradosMotor_String);
            }
        }
    }

    public void restaGrados (EditText editText_M,String GradosMotor_StringM ){
        String GradosMotor_String = editText_M.getText().toString();
        int GradosMotor_int =0;

        if(editText_M.getText().toString().isEmpty()){
            if(GradosMotor_int == 0){
                editText_M.setText(GradosMotor_StringM);
            }else{
                GradosMotor_String = String.valueOf(GradosMotor_int);
                editText_M.setText(GradosMotor_String);
            }
        }else{
            GradosMotor_int =Integer.parseInt(GradosMotor_String);
            if(GradosMotor_int < 0){
                //editText_M.setText(GradosMotor_String);
            }else if(GradosMotor_int > 0){
                GradosMotor_int --;
                //editText_M.setText(GradosMotor_String);
                GradosMotor_String = String.valueOf(GradosMotor_int);
                editText_M.setText(GradosMotor_String);
            }
        }
    }




}



