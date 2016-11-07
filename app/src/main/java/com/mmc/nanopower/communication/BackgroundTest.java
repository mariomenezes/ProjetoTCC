package com.mmc.nanopower.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.mmc.nanopower.Fuzzy.DecisionAssist;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mario on 07/11/16.
 */
public class BackgroundTest extends AsyncTask<String, String, Boolean> {

////////////////////

    @Override
    protected Boolean doInBackground(String... params) {

        int delay = 5000;   // delay de 5 seg.
        int interval = 60000;  // intervalo de 1 seg.
        Timer timer = new Timer();



        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // colocar tarefas aqui ...
                Log.d("EXECUTANDO "," amemmmmm");
            }
        }, delay, interval);


        return null;
    }


}
