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
import java.util.Arrays;

/**
 * Created by mario on 15/09/16.
 */
public class SwitchStateListen3 extends AsyncTask<String, String, Boolean> {

    private Switch tomada;

    public SwitchStateListen3(Switch s){
        tomada = s;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        Log.d("ENTROU", " CLASSE");
        String URL = "http://10.10.0.50/";
        String linha = "";
        Boolean Erro = true;

        //if (params.length > 0)
        // faço qualquer coisa com os parâmetros
        while (true) {
            Log.d("ENTROU", " WHILE");
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
                setEstadoTomada(params[0], linha);


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

    public void setEstadoTomada(String id_tomada, String linha) {

        Log.d("TOMADA3 ", "alterou");
        tomada.setChecked(linha.contains("t3o"));
    }
    @Override
    protected void onProgressUpdate(String... params) {
        Log.d("TESTE"," UPDATE");
        // Runs on UI thread after publishProgress(Progress...) is invoked
        // from doInBackground()
        //return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d("TESTE", " EXECUTE");
        // Runs on the UI thread after doInBackground()
    }
}
