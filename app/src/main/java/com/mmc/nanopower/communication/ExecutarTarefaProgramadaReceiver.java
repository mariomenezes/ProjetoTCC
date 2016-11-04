package com.mmc.nanopower.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mario on 03/11/16.
 */
public class ExecutarTarefaProgramadaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //      Código a executar

        Log.i("ALARME ", "executou");
    }

}