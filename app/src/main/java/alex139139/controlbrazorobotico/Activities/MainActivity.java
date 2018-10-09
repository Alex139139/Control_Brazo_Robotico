package alex139139.controlbrazorobotico.Activities;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import alex139139.controlbrazorobotico.R;



public class MainActivity extends AppCompatActivity implements View.OnLongClickListener,View.OnTouchListener {

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

    private Button button_Bluetooth;

    private boolean AutoIncrement_boolean1 = false; //Incrementar
    private boolean AutoIncrement_boolean2 = false;
    private boolean AutoIncrement_boolean3 = false;
    private boolean AutoIncrement_boolean4= false;
    private boolean AutoIncrement_boolean5 = false;
    private boolean AutoIncrement_boolean6= false;

    private boolean AutoDecrement_boolean1 = false; //Reducir
    private boolean AutoDecrement_boolean2 = false;
    private boolean AutoDecrement_boolean3 = false;
    private boolean AutoDecrement_boolean4 = false;
    private boolean AutoDecrement_boolean5 = false;
    private boolean AutoDecrement_boolean6 = false;

    private Handler handlerRepetir= new Handler();

    //Cosas De Bluetooth-------------------------

    //-------------------------------------------

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

        button_Bluetooth = (Button)findViewById(R.id.button_Bluetooth_id);


        editText_M1.setText(GradosMotor_StringM1);
        editText_M2.setText(GradosMotor_StringM2);
        editText_M3.setText(GradosMotor_StringM3);
        editText_M4.setText(GradosMotor_StringM4);
        editText_M5.setText(GradosMotor_StringM5);
        editText_M6.setText(GradosMotor_StringM6);

        button_less_M1.setOnTouchListener(this);
        button_less_M2.setOnTouchListener(this);
        button_less_M3.setOnTouchListener(this);
        button_less_M4.setOnTouchListener(this);
        button_less_M5.setOnTouchListener(this);
        button_less_M6.setOnTouchListener(this);

        button_plus_M1.setOnTouchListener(this);
        button_plus_M2.setOnTouchListener(this);
        button_plus_M3.setOnTouchListener(this);
        button_plus_M4.setOnTouchListener(this);
        button_plus_M5.setOnTouchListener(this);
        button_plus_M6.setOnTouchListener(this);

        button_Bluetooth.setOnTouchListener(this);

        button_plus_M1.setOnLongClickListener(this);
        button_plus_M2.setOnLongClickListener(this);
        button_plus_M3.setOnLongClickListener(this);
        button_plus_M4.setOnLongClickListener(this);
        button_plus_M5.setOnLongClickListener(this);
        button_plus_M6.setOnLongClickListener(this);

        button_less_M1.setOnLongClickListener(this);
        button_less_M2.setOnLongClickListener(this);
        button_less_M3.setOnLongClickListener(this);
        button_less_M4.setOnLongClickListener(this);
        button_less_M5.setOnLongClickListener(this);
        button_less_M6.setOnLongClickListener(this);
        button_less_M1.setOnLongClickListener(this);

    }//Cierre Funcion onCreate







    //metodos Para sumar o restar grados
    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){

            case R.id.button1m:
                AutoDecrement_boolean1 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;
            case R.id.button2m:
                AutoDecrement_boolean2 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;
            case R.id.button3m:
                AutoDecrement_boolean3 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;
            case R.id.button4m:
                AutoDecrement_boolean4 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;
            case R.id.button5m:
                AutoDecrement_boolean5 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;
            case R.id.button6m:
                AutoDecrement_boolean6 =true;
                handlerRepetir.post(new handlerRepetir_class_Resta());
                break;

            ///////////////////////////////////////////////
            case R.id.button1p:
                AutoIncrement_boolean1 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
            case R.id.button2p:
                AutoIncrement_boolean2 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
            case R.id.button3p:
                AutoIncrement_boolean3 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
            case R.id.button4p:
                AutoIncrement_boolean4 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
            case R.id.button5p:
                AutoIncrement_boolean5 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
            case R.id.button6p:
                AutoIncrement_boolean6 =true;
                handlerRepetir.post(new handlerRepetir_class_Suma());
                break;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){

            case R.id.button1m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M1,GradosMotor_StringM1,GradosMotor_intM1);
                    GradosMotor_intM1=restaGrados(GradosMotor_intM1,editText_M1);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean1 = false;
                }
                break;
            case R.id.button2m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M2,GradosMotor_StringM2,GradosMotor_intM2);
                    GradosMotor_intM2=restaGrados(GradosMotor_intM2,editText_M2);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean2 = false;
                }

                break;
            case R.id.button3m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M3,GradosMotor_StringM3,GradosMotor_intM3);
                    GradosMotor_intM3=restaGrados(GradosMotor_intM3,editText_M3);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean3 = false;
                }
                break;
            case R.id.button4m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M4,GradosMotor_StringM4,GradosMotor_intM4);
                    GradosMotor_intM4=restaGrados(GradosMotor_intM4,editText_M4);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean4 = false;
                }
                break;
            case R.id.button5m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M5,GradosMotor_StringM5,GradosMotor_intM5);
                    GradosMotor_intM5=restaGrados(GradosMotor_intM5,editText_M5);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean5 = false;
                }
                break;
            case R.id.button6m:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M6,GradosMotor_StringM6,GradosMotor_intM6);
                    GradosMotor_intM6=restaGrados(GradosMotor_intM6,editText_M6);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoDecrement_boolean6 = false;
                }
                break;

             ///////////////////////////////////////////////
            case R.id.button1p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M1,GradosMotor_StringM1,GradosMotor_intM1);
                    GradosMotor_intM1=sumaGrados(GradosMotor_intM1,editText_M1);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoIncrement_boolean1 = false;
                }
                break;
            case R.id.button2p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M2,GradosMotor_StringM2,GradosMotor_intM2);
                    GradosMotor_intM2=sumaGrados(GradosMotor_intM2,editText_M2);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    AutoIncrement_boolean2 = false;
                }
                break;
            case R.id.button3p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M3,GradosMotor_StringM3,GradosMotor_intM3);
                    GradosMotor_intM3=sumaGrados(GradosMotor_intM3,editText_M3);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoIncrement_boolean3 = false;
                }
                break;
            case R.id.button4p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M4,GradosMotor_StringM4,GradosMotor_intM4);
                    GradosMotor_intM4=sumaGrados(GradosMotor_intM4,editText_M4);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoIncrement_boolean4 = false;
                }
                break;
            case R.id.button5p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M5,GradosMotor_StringM5,GradosMotor_intM5);
                    GradosMotor_intM5=sumaGrados(GradosMotor_intM5,editText_M5);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoIncrement_boolean5 = false;
                }
                break;
            case R.id.button6p:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    comprobarTextEdit(editText_M6,GradosMotor_StringM6,GradosMotor_intM6);
                    GradosMotor_intM6=sumaGrados(GradosMotor_intM6,editText_M6);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    AutoIncrement_boolean6 = false;
                }
                break;

                /////////////////////////////////////////
            case R.id.button_Bluetooth_id:
                if(event.getAction() == MotionEvent.ACTION_DOWN ){
                    Intent intent = new Intent(MainActivity.this,BluetoothActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return false;
    }




    public int sumaGrados (int GradosMotor_intM, EditText editText_M){
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

            }
        }
    }


    class handlerRepetir_class_Suma implements Runnable{
        @Override
        public void run() {
            if(AutoIncrement_boolean1){
                GradosMotor_intM1=sumaGrados(GradosMotor_intM1,editText_M1);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }

            if(AutoIncrement_boolean2){
                GradosMotor_intM2=sumaGrados(GradosMotor_intM2,editText_M2);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }

            if(AutoIncrement_boolean3){
                GradosMotor_intM3=sumaGrados(GradosMotor_intM3,editText_M3);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }else

            if(AutoIncrement_boolean4){
                GradosMotor_intM4=sumaGrados(GradosMotor_intM4,editText_M4);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }

            if(AutoIncrement_boolean5){
                GradosMotor_intM5=sumaGrados(GradosMotor_intM5,editText_M5);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }

            if(AutoIncrement_boolean6){
                GradosMotor_intM6=sumaGrados(GradosMotor_intM6,editText_M6);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
        }
    }

    class handlerRepetir_class_Resta implements Runnable{

        @Override
        public void run() {
            if (AutoDecrement_boolean1){
                GradosMotor_intM1=restaGrados(GradosMotor_intM1,editText_M1);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
            if (AutoDecrement_boolean2){
                GradosMotor_intM2=restaGrados(GradosMotor_intM2,editText_M2);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
            if (AutoDecrement_boolean3){
                GradosMotor_intM3=restaGrados(GradosMotor_intM3,editText_M3);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
            if (AutoDecrement_boolean4){
                GradosMotor_intM4=restaGrados(GradosMotor_intM4,editText_M4);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
            if (AutoDecrement_boolean5){
                GradosMotor_intM5=restaGrados(GradosMotor_intM5,editText_M5);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }
            if (AutoDecrement_boolean6){
                GradosMotor_intM6=restaGrados(GradosMotor_intM6,editText_M6);
                handlerRepetir.postDelayed(this,100);//se ejecutara cada x mili segundos
            }

        }
    }


}//Cierre Main Activity







