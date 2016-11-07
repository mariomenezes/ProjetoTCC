package com.mmc.nanopower.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by mario on 03/11/16.
 */
public class ExecutarTarefaProgramadaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //      Código a executar

        Log.d("ALARME ", "executando agora");
        lerSensores();

    }

    public void lerSensores(){
        String URL = "http://10.10.0.50/";
        String linha = "";
        boolean Erro = true;

        Log.d("SENSORES ", "LENDO");
        try {
        HttpClient client = new DefaultHttpClient();
        HttpGet requisicao = new HttpGet();
        requisicao.setHeader("Content-Type",
                "text/plain; charset=utf-8");
        requisicao.setURI(new URI(URL));
        HttpResponse resposta = client.execute(requisicao);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                resposta.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");

        while ((linha = br.readLine()) != null) {
            sb.append(linha);
        }

        br.close();

        linha = sb.toString();

        Log.d("RESPOSTA ", linha);

        Erro = false;

    } catch (Exception e) {
            Log.d("PROBLEMA ", "exception");
        Erro = true;
    }
    }


}