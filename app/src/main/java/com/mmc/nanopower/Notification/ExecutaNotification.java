/*Só chamado caso seja a notificação seja clicada*/

package com.mmc.nanopower.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mmc.nanopower.communication.SendCmdFromNotifi;

/**
 * Created by mario on 24/04/17.
 */
public class ExecutaNotification extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if(action.equalsIgnoreCase("CHAMAR_METODO_X")){
            //Chame o seu método
            new SendCmdFromNotifi().execute();
        }
    }


}
