package com.mmc.nanopower;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.icu.util.Calendar;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
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
import com.mmc.nanopower.communication.ArduinoSensorState;
import com.mmc.nanopower.communication.ExecutarTarefaProgramadaReceiver;
import com.mmc.nanopower.communication.SwitchStateListen1;
import com.mmc.nanopower.Classification.AprioriClassifi;
import com.mmc.nanopower.communication.ArduinoPostRequest;
import com.mmc.nanopower.communication.SwitchStateListen2;
import com.mmc.nanopower.communication.SwitchStateListen3;

import java.util.Calendar;


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

        //gerarNotificacao();

        //TODO test apriori
        new AprioriClassifi().execute();

     //   boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ExecutarTarefaProgramadaReceiver"), PendingIntent.FLAG_NO_CREATE) == null);

       // if(alarmeAtivo) {
/*
            Log.i("NOVO ALARME ", "ATIVO");
            Intent intent = new Intent("ExecutarTarefaProgramadaReceiver");
            PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 40);

            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            //alarme.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), p);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60000, p);

            */
//        }
  //      else
    //        Log.i("ALARME ", "JA ATIVO");
        //Test Logica Fuzzy
        //new DecisionAssist().execute("TCC");

        //Deixa consultando os valores dos sensores e salva na classe singleton SaveState
//        new ArduinoSensorState().execute();

        //Chama a classe que ira ler os dados de SaveState e executar os comandos
        //new ArduinoPostRequest().execute("fuzzy");

        //PROGRAMAR LEITURA
//        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(this, ExecutarTarefaProgramadaReceiver.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  SystemClock.elapsedRealtime() + 60 * 1000, alarmIntent);
//
//        //Definir início para as 10 horas
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 33);
        calendar.set(Calendar.SECOND, 0);

//Definir intervalo de 6 horas
  //      long intervalo = 6*60*60*1000; //6 horas em milissegundos
        long intervalo = 60*1000; //1 minuto em milissegundos

        Intent tarefaIntent = new Intent(this, ExecutarTarefaProgramadaReceiver.class);
        PendingIntent tarefaPendingIntent = PendingIntent.getBroadcast(this, 0, tarefaIntent,0);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

//Definir o alarme para acontecer de 6 em 6 horas a partir das 10 horas
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervalo, tarefaPendingIntent);
//        PROGRAMAR LEITURA



        tomada_switch1 = (Switch) findViewById(R.id.tomada_switch1);
        tomada_switch2 = (Switch) findViewById(R.id.tomada_switch2);
        tomada_switch3 = (Switch) findViewById(R.id.tomada_switch3);
       // tomada_switch4 = (Switch) findViewById(R.id.tomada_switch4);

        //TODO alterar sozinho o estado das tomdadas, parou de funcionar
        //new SwitchStateListen1(tomada_switch1).execute("tomada1");
        //new SwitchStateListen2(tomada_switch2).execute("tomada2");
        //new SwitchStateListen3(tomada_switch3).execute("tomada3");

        tomada_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    Log.d("BOTAO1"," pressionado");
                   new ArduinoPostRequest().execute("t1h");
                }else{
                    new ArduinoPostRequest().execute("t1l");
                }

                }

        });



        tomada_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new ArduinoPostRequest().execute("t2h");
                }else{
                    new ArduinoPostRequest().execute("t2l");
                }

            }
        });


        tomada_switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    new ArduinoPostRequest().execute("t3h");
                }else{
                    new ArduinoPostRequest().execute("t3l");
                }

            }
        });



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

    public void gerarNotificacao(){

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Título");
        //builder.setContentText("Descrição");
        //builder.setSmallIcon(R.drawable.;
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.thiengo));
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
        //nm.notify(R.drawable.ic_launcher, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//    }



}
