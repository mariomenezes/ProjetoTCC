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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mario on 03/11/16.
 */
public class ArduinoSensorState extends AsyncTask<String, String, Boolean> {

    SaveState saveState = null;


//    public ArduinoSensorState(float t, float l, int d, float h) {
//        this.temperatura = t;
//        this.luz = l;
//        this.diaSemana = d;
//        this.hora = h;
//    }
    public ArduinoSensorState() {

    }
    @Override
    protected Boolean doInBackground(String... params) {
        saveState = SaveState.getInstancia();

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

                updateSensorStatus(linha);


                Erro = false;


            } catch (Exception except) {
                Log.e("TESTE ", "CRASH StackTrace: " + Arrays.toString(except.getStackTrace()));
                Erro = true;
            }
            /*catch (Exception e) {


                Log.d("FALHOU"," TESTE");
                Erro = true;
            }*/

            return Erro;

        }
    }

    public void updateSensorStatus(String linha) {

        Log.d("TESTE", " DOIDO");
        int aux = 0;
        String read = "";
        ArrayList<String> consulta = new ArrayList<>();
        consulta.add("#t");
        consulta.add("#l");
        consulta.add("#d");
        consulta.add("#h");

        int j = 0;

        while (j < consulta.size()) {
            if (linha.contains(consulta.get(j))) {
                for (int i = 0; i < linha.length(); ++i) {
                    if (linha.charAt(i) == '#' && linha.charAt(i + 1) == 't') {
                        aux = i + 1;
                        while (linha.charAt(aux) != '#') {
                            read.concat(String.valueOf(linha.charAt(aux + 1)));
                            ++aux;
                        }
                        aux = 0;
                    }
                }
                switch (consulta.get(j)) {
                    case "#t":
                        saveState.setTemperatura(Float.valueOf(read));
                        break;
                    case "#l":
                        saveState.setLuz(Float.valueOf(read));
                        break;
                    case "#d":
                        saveState.setDiaSemana(Integer.valueOf(read));
                        break;
                    case "#h":
                        saveState.setHora(Float.valueOf(read));
                        break;
                }

                Log.d("VALOR LIDO", read);
            }
            read = "";
            ++j;
        }


        Log.d("CHEGOU", "FINAL");
    }

    @Override
    protected void onProgressUpdate(String... params) {
        Log.d("DORMIU", " UPDATE");

        // Runs on UI thread after publishProgress(Progress...) is invoked
        // from doInBackground()
        //return true;


        Log.d("ACORDOU", " UPDATE");
        //updateSensorStatus();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d("TESTE", " DORMIU");
        // Runs on the UI thread after doInBackground()

        try {
            Thread.sleep(3000);
            // Do some stuff
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        Log.d("TESTE", " ACORDOU");

    }

}
