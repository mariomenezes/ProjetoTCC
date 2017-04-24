package com.mmc.nanopower.communication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by mario on 24/04/17.
 */
public class SendCmdFromNotifi extends AsyncTask<String, String, Boolean> {

    private static final String TAG = "SendCmdFromNotifi";
    @Override
    protected Boolean doInBackground(String... params) {

        Log.d(TAG, " Entrou no método background");
        String URL = "http://10.10.0.50/";

        //Conctatena com o comando para desligar tomada.
        URL.concat("params[0]");

        String linha = "";
        Boolean Erro = true;

        //if (params.length > 0)
        // faço qualquer coisa com os parâmetros
        //while (true) {
            try {

                Log.d(TAG, "entrou no try");
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
                //setEstadoTomada(params[0], linha);


                Erro = false;


            }catch (Exception except) {
                Log.e("TESTE ","CRASH StackTrace: "+ Arrays.toString(except.getStackTrace()));
                Erro = true;
            }
            /*catch (Exception e) {


                Log.d("FALHOU"," TESTE");
                Erro = true;
            }*/

            return Erro;
    }
}
