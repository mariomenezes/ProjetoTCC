package com.mmc.nanopower.communication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by mario on 15/09/16.
 */
public class SwitchStateListen extends AsyncTask<String, Void, Boolean> {

    private Switch tomada1;
    private Switch tomada2;
    private Switch tomada3;
    private Switch tomada4;

    public SwitchStateListen(Switch s1, Switch s2, Switch s3, Switch s4){
        tomada1 = s1;
        tomada2 = s2;
        tomada3 = s3;
        tomada4 = s4;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String URL = "http://10.10.0.50/";
        String linha = "";
        Boolean Erro = true;

        //if (params.length > 0)
            // faço qualquer coisa com os parâmetros
        while (true) {
            try {

                Log.d("TOMADA agora ", " TESTE");
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

                Log.d("TESTANDO", linha);
                setEstadoTomada(linha);

                Erro = false;

            } catch (Exception e) {
                Erro = true;
            }
            return Erro;
       }
    }

    public void setEstadoTomada(String linha) {

        Log.d("CHAMOU", " DOIDO");
        tomada1.setChecked(linha.contains("te1on"));
        tomada2.setChecked(linha.contains("te2on"));
        tomada3.setChecked(linha.contains("te3on"));
        tomada4.setChecked(linha.contains("te4on"));
    }

}
