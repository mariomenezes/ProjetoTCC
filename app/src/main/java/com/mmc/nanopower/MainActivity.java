package com.mmc.nanopower;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mmc.nanopower.Fuzzy.DecisionAssist;
import com.mmc.nanopower.communication.ArduinoSensorState;
import com.mmc.nanopower.communication.SwitchStateListen1;
import com.mmc.nanopower.Classification.AprioriClassifi;
import com.mmc.nanopower.communication.ArduinoPostRequest;
import com.mmc.nanopower.communication.SwitchStateListen2;
import com.mmc.nanopower.communication.SwitchStateListen3;
import com.mmc.nanopower.communication.SwitchStateListen4;


//TODO provavelmente criar outra thread para verificar o estado dos botoes(dentro do onlistener nao funciona)

public class MainActivity extends AppCompatActivity {

    private Switch tomada_switch1;
    private Switch tomada_switch2;
    private Switch tomada_switch3;
    private Switch tomada_switch4;
    private Switch switch_teste;
    private Button connect_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        //TODO test apriori
        new AprioriClassifi().execute();

        //Test Logica Fuzzy
        new DecisionAssist().execute("TCC");

        //Deixa consultando os valores dos sensores e salva na classe singleton SaveState
        //new ArduinoPostRequest().execute("state");

        new ArduinoSensorState().execute();

        //Chama a classe que ira ler os dados de SaveState e executar os comandos

        new ArduinoPostRequest().execute("fuzzy");




        tomada_switch1 = (Switch) findViewById(R.id.tomada_switch1);
        tomada_switch2 = (Switch) findViewById(R.id.tomada_switch2);
        tomada_switch3 = (Switch) findViewById(R.id.tomada_switch3);
        tomada_switch4 = (Switch) findViewById(R.id.tomada_switch4);

        new SwitchStateListen1(tomada_switch1).execute("tomada1");
        new SwitchStateListen2(tomada_switch2).execute("tomada2");
        new SwitchStateListen3(tomada_switch3).execute("tomada3");
        new SwitchStateListen4(tomada_switch4).execute("tomada4");
        //AsyncTask<String, Void, Boolean> s = new SwitchStateListen1(tomada_switch2).execute("tomada2");
        //s.execute("tomada2");
//        new SwitchStateListen1(tomada_switch3).execute("tomada3");
//        new SwitchStateListen1(tomada_switch4).execute("tomada4");

        tomada_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                   new ArduinoPostRequest().execute("t1high");
                }else{
                    new ArduinoPostRequest().execute("t1low");
                }

                }

        });



        tomada_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new ArduinoPostRequest().execute("t2high");
                }else{
                    new ArduinoPostRequest().execute("t2low");
                }

            }
        });


        tomada_switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new ArduinoPostRequest().execute("t3high");
                }else{
                    new ArduinoPostRequest().execute("t3low");
                }

            }
        });


        tomada_switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new ArduinoPostRequest().execute("t4high");
                }else{
                    new ArduinoPostRequest().execute("t4low");
                }

            }
        });

        /*switch_teste = (Switch) findViewById(R.id.switch_teste);

        switch_teste.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if(isChecked){
                    Log.d("BOTAO", "ativado");
                }else{
                    Log.d("BOTAO", "desativado");
                }

            }
        });
*/
        connect_button = (Button) findViewById(R.id.button_connect);

        connect_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("CLICOU", " teste");



            }

        });


        //ArduinoPostRequest a = new ArduinoPostRequest();
       // boolean b[] = a.getEstadoTomadas();
        //tomada_switch1.setChecked(true);
        //tomada_switch2.setChecked(true);
        //tomada_switch3.setChecked(true);
        //tomada_switch4.setChecked(true);

        //TSP_GA genetico = new TSP_GA(50, 100);
//        SwitchStateListen1 s = new SwitchStateListen1(tomada_switch1, tomada_switch2,tomada_switch3,tomada_switch4);
        //s.execute("teste");
        //String stemp = s.getLinha();
        //s.setEstadoTomada(stemp);


    }


    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE


    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }


}
