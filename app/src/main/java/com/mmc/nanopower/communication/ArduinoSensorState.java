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

/*Exemplo de saída
* </html>t1ft2ft3f#t32#l4#d5#h19#m5</html>
* */

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

                Log.d("CARALHO ", linha);


                //updateSensorStatus(linha);


                Erro = false;


            } catch (Exception except) {
                Log.e("TESTE ", "CRASH StackTrace: " + Arrays.toString(except.getStackTrace()));
                Erro = true;
            }
            /*catch (Exception e) {


                Log.d("FALHOU"," TESTE");
                Erro = true;
            }*/



            try {
                Thread.sleep(60000);
                // Do some stuff
                Log.d("SENSOR ", "DORMIU");
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            Log.d("TESTE", " ACORDOU");

            //return Erro;
            teste(linha);

        }
    }

    private void teste(String read){
        if(read.contains("#t")){
            Log.d("TEMPOK", read);
        }
    }

    public void updateSensorStatus(String linha) {

        Log.d("TESTE", " SENSOR");
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
                    if (linha.charAt(i) == '#') {
                        aux = i + 1;
                        while (linha.charAt(aux) != '#') {
                            read.concat(String.valueOf(linha.charAt(aux + 1)));
                            ++aux;
                        }
                        aux = 0;
                    }
                }
                Log.d("PASSANDO ",read);
                switch (consulta.get(j)) {
                    case "#t":
                        Log.d("SELIGA ",read);
                        //saveState.setTemperatura(Float.valueOf(read));
                        break;
                    case "#l":
                        //saveState.setLuz(Float.valueOf(read));
                        break;
                    case "#d":
                        //saveState.setDiaSemana(Integer.valueOf(read));
                        break;
                    case "#h":
                        //saveState.setHora(Float.valueOf(read));
                        break;
                }

                Log.d("VALOR ", read);
            }
            read = "";
            ++j;
        }


        Log.d("CHEGOU", "FINAL");
    }

    @Override
    protected void onProgressUpdate(String... params) {
        //Log.d("DORMIU", " UPDATE");

        // Runs on UI thread after publishProgress(Progress...) is invoked
        // from doInBackground()
        //return true;


      //  Log.d("ACORDOU", " UPDATE");
        //updateSensorStatus();
    }

    @Override
    protected void onPostExecute(Boolean result) {
      //  Log.d("TESTE", " DORMIU");
        // Runs on the UI thread after doInBackground()


    }

}
