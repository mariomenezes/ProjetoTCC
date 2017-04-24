package com.mmc.nanopower;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.mmc.nanopower.Fuzzy.DecisionAssist;
import com.mmc.nanopower.Notification.ExecutaNotification;
import com.mmc.nanopower.communication.ArduinoSensorState;
//import com.mmc.nanopower.communication.ExecutarTarefaProgramadaReceiver;
//import com.mmc.nanopower.communication.SwitchStateListen1;
import com.mmc.nanopower.Classification.AprioriClassifi;
import com.mmc.nanopower.communication.CmdButtonToArduino;
//import com.mmc.nanopower.communication.SwitchStateListen2;
//import com.mmc.nanopower.communication.SwitchStateListen3;



//TODO provavelmente criar outra thread para verificar o estado dos botoes(dentro do onlistener nao funciona)

public class MainActivity extends AppCompatActivity {

    private Switch tomada_switch1;
    private Switch tomada_switch2;
    private Switch tomada_switch3;
    private Button connect_button;
    private ExecutaNotification executaNotification;

    private static final String TAG = "Classe Princial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

            gerarNotificacao();

        //TODO test apriori
        new AprioriClassifi().execute();
        //new BackgroundTest().execute();
        new ArduinoSensorState().execute();
        new DecisionAssist().execute();
////////////////////////////////////////////////////////////
//        int delay = 5000;   // delay de 5 seg.
//        int interval = 60000;  // intervalo de 1 seg.
//        Timer timer = new Timer();
//
//
//
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//                // colocar tarefas aqui ...
//                Log.d("FUNFOU"," amemmmmm");
//                //TODO precisa ser testado
//                new ArduinoSensorState().execute();
//                new DecisionAssist().execute();
//            }
//        }, delay, interval);
/////////////////////////////////////////////////////////////

        tomada_switch1 = (Switch) findViewById(R.id.tomada_switch1);
        tomada_switch2 = (Switch) findViewById(R.id.tomada_switch2);
        tomada_switch3 = (Switch) findViewById(R.id.tomada_switch3);

        //TODO alterar sozinho o estado das tomdadas, parou de funcionar
        //new SwitchStateListen1(tomada_switch1).execute("tomada1");
        //new SwitchStateListen2(tomada_switch2).execute("tomada2");
        //new SwitchStateListen3(tomada_switch3).execute("tomada3");

        tomada_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    Log.d(TAG, "BOTAO1 pressionado");
                   new CmdButtonToArduino().execute("t1h");
                }else{
                    new CmdButtonToArduino().execute("t1l");
                }

                }

        });



        tomada_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new CmdButtonToArduino().execute("t2h");
                }else{
                    new CmdButtonToArduino().execute("t2l");
                }

            }
        });


        tomada_switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new CmdButtonToArduino().execute("t3h");
                }else{
                    new CmdButtonToArduino().execute("t3l");
                }

            }
        });



        connect_button = (Button) findViewById(R.id.button_connect);

        connect_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "CLICOU teste");

                new ArduinoSensorState().execute();

            }

        });
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

    public void gerarNotificacao(){


        //parte usada para o broadcast receiver da notificação
        Intent notificationIntent = new Intent("CHAMAR_METODO_X");
        PendingIntent btLocationPendingIntent =
                PendingIntent.getBroadcast(this, 1234, notificationIntent,0);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Título");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(p);


        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String [] descs = new String[]{"Descrição 1", "Descrição 2", "Descrição 3", "Descrição 4"};
        for(int i = 0; i < descs.length; i++){
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.ic_launcher, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On Start .....");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "On Destroy .....");
    }


    @Override
    protected void onPause() {
        Log.i(TAG, "On Pause .....");
        unregisterReceiver(executaNotification);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
        registerReceiver(executaNotification, new IntentFilter("CHAMAR_METODO_X"));
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }
}
