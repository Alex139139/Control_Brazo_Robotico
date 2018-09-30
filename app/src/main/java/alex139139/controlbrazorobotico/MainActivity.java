package alex139139.controlbrazorobotico;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.annotation.Repeatable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener,View.OnTouchListener {

    private String GradosMotor_StringM1 = "179";
    private String GradosMotor_StringM2 = "80";
    private String GradosMotor_StringM3 = "50";
    private String GradosMotor_StringM4 = "0";
    private String GradosMotor_StringM5 = "180";
    private String GradosMotor_StringM6 = "80";

    private int GradosMotor_intM1 = 179;
    private int GradosMotor_intM2 = 80;
    private int GradosMotor_intM3 = 50;
    private int GradosMotor_intM4 = 0;
    private int GradosMotor_intM5 = 180;
    private int GradosMotor_intM6 = 90;

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



    private Handler handlerRepetir= new Handler();

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


        editText_M1.setText(GradosMotor_StringM1);
        editText_M2.setText(GradosMotor_StringM2);
        editText_M3.setText(GradosMotor_StringM3);
        editText_M4.setText(GradosMotor_StringM4);
        editText_M5.setText(GradosMotor_StringM5);
        editText_M6.setText(GradosMotor_StringM6);


        button_plus_M1.setOnClickListener(this);
        button_plus_M2.setOnClickListener(this);
        button_plus_M3.setOnClickListener(this);
        button_plus_M4.setOnClickListener(this);
        button_plus_M5.setOnClickListener(this);
        button_plus_M6.setOnClickListener(this);

        button_less_M1.setOnClickListener(this);
        button_less_M2.setOnClickListener(this);
        button_less_M3.setOnClickListener(this);
        button_less_M4.setOnClickListener(this);
        button_less_M5.setOnClickListener(this);
        button_less_M6.setOnClickListener(this);

        /*button_plus_M1.setOnLongClickListener(MainActivity.this);
        button_plus_M2.setOnLongClickListener(MainActivity.this);
        button_plus_M3.setOnLongClickListener(MainActivity.this);
        button_plus_M4.setOnLongClickListener(MainActivity.this);
        button_plus_M5.setOnLongClickListener(MainActivity.this);
        button_plus_M6.setOnLongClickListener(MainActivity.this);

        button_less_M1.setOnLongClickListener(MainActivity.this);
        button_less_M2.setOnLongClickListener(MainActivity.this);
        button_less_M3.setOnLongClickListener(MainActivity.this);
        button_less_M4.setOnLongClickListener(MainActivity.this);
        button_less_M5.setOnLongClickListener(MainActivity.this);
        button_less_M6.setOnLongClickListener(MainActivity.this);
        button_less_M1.setOnLongClickListener(MainActivity.this);*/

        //button_less_M1.setOnTouchListener(this);
        //button_plus_M1.setOnTouchListener(this);

    }//Cierre Funcion onCreate


    //metodos Para sumar o restar grados



    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button1m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                comprobarTextEdit(editText_M1,GradosMotor_StringM1,GradosMotor_intM1);
                GradosMotor_intM1 = restaGrados(GradosMotor_intM1,editText_M1);

                break;
            case R.id.button2m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                //GradosMotor_intM2=restaGrados(editText_M2,GradosMotor_StringM2,GradosMotor_intM2);
                break;
            case R.id.button3m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                //GradosMotor_intM3=restaGrados(editText_M3,GradosMotor_StringM3,GradosMotor_intM3);
                break;
            case R.id.button4m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                //GradosMotor_intM4=restaGrados(editText_M4,GradosMotor_StringM4,GradosMotor_intM4);
                break;
            case R.id.button5m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                //GradosMotor_intM5=restaGrados(editText_M5,GradosMotor_StringM5,GradosMotor_intM5);
                break;
            case R.id.button6m:
                //Toast.makeText(MainActivity.this, "-1", Toast.LENGTH_SHORT).show();
                //GradosMotor_intM6=restaGrados(editText_M6,GradosMotor_StringM6,GradosMotor_intM6);
                break;
            case R.id.button1p:
                // Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM1=sumaGrados(editText_M1,GradosMotor_StringM1,GradosMotor_intM1);
                break;
            case R.id.button2p:
                //Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM2=sumaGrados(editText_M2,GradosMotor_StringM2,GradosMotor_intM2);
                break;
            case R.id.button3p:
                //Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM3=sumaGrados(editText_M3,GradosMotor_StringM3,GradosMotor_intM3);
                break;
            case R.id.button4p:
                //Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM4=sumaGrados(editText_M4,GradosMotor_StringM4,GradosMotor_intM4);
                break;
            case R.id.button5p:
                //Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM5=sumaGrados(editText_M5,GradosMotor_StringM5,GradosMotor_intM5);
                break;
            case R.id.button6p:
                //Toast.makeText(MainActivity.this, "+1", Toast.LENGTH_SHORT).show();
                GradosMotor_intM6=sumaGrados(editText_M6,GradosMotor_StringM6,GradosMotor_intM6);
                break;
        }
    }//Cierre Funcion onClick


    public int sumaGrados (EditText editText_M, String GradosMotor_StringM ,int GradosMotor_intM){
        GradosMotor_intM = Integer.parseInt(editText_M.getText().toString());
        if (GradosMotor_intM < 180 ){
            GradosMotor_intM ++;
        }
        editText_M.setText(String.valueOf(GradosMotor_intM));
        return GradosMotor_intM;
    }


    public int restaGrados (int GradosMotor_intM, EditText editText_M ){
        GradosMotor_intM = Integer.parseInt(editText_M.getText().toString());
        if (GradosMotor_intM > 0 ){
            GradosMotor_intM --;
        }
        editText_M.setText(String.valueOf(GradosMotor_intM));
        return GradosMotor_intM;
    }

    public void comprobarTextEdit(EditText editText_M, String GradosMotor_StringM , int GradosMotor_intM){
        String GradosMotor_String;
        if(editText_M.getText().toString().isEmpty()){
            if(GradosMotor_intM == Integer.parseInt(GradosMotor_StringM)) {
                editText_M.setText(GradosMotor_StringM);
            }else{
                GradosMotor_String = String.valueOf(GradosMotor_intM);
                editText_M.setText(GradosMotor_String);
            }
        }else{
            GradosMotor_String = editText_M.getText().toString();
            if(Integer.parseInt(GradosMotor_String) < 0 || Integer.parseInt(GradosMotor_String)> 180 ){
                Toast.makeText(this,"Ingresa un valor entre 0 - 180",Toast.LENGTH_SHORT).show();
                GradosMotor_String = String.valueOf(GradosMotor_intM);
                editText_M.setText(GradosMotor_String);
            }else if(Integer.parseInt(GradosMotor_String) > 0){
                Toast.makeText(this,"Correcto",Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void bucleFuncion(){

    }
    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
/*        switch(v.getId()) {

            case R.id.button1m:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:


                        break;
                    case  MotionEvent.ACTION_UP:

                        break;
                }
                break;

            case R.id.button1p:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case  MotionEvent.ACTION_UP:
                        break;
                }
                break;
        }*/
        return true;
    }

    public Runnable mfunction = new Runnable() {
        @Override
        public void run() {
            //GradosMotor_intM1=restaGrados(editText_M1,GradosMotor_StringM1,GradosMotor_intM1);
            handlerRepetir.postDelayed(this,250);//se ejecutara cada x mili segundos
        }
    };







}//Cierre Main Activity




/*
    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()) {

            case R.id.button1m:
                restaGradosRepetible();
                break;
        }
        return true;
    }

    private void restaGradosRepetible() {
        final Handler handler= new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GradosMotor_intM1 = restaGrados(editText_M1, GradosMotor_StringM1, GradosMotor_intM1);//llamamos nuestro metodo
                handler.postDelayed(this,500);//se ejecutara cada 10 segundos
            }
        },2000);//empezara a ejecutarse después de 5 milisegundos
    }
*/



