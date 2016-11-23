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
import java.util.Timer;
import java.util.TimerTask;

/*Exemplo de saída
* </html>t1ft2ft3f#t32#l4#d5#h19#m5</html>
* */

/**
 * Created by mario on 03/11/16.
 */
public class ArduinoSensorState extends AsyncTask<String, String, Boolean> {

    private SaveState saveState = SaveState.getInstancia();
    int delay = 5000;   // delay de 5 seg.
    int interval = 150000;  // intervalo de 2.5 min.
    Timer timer = new Timer();

    public ArduinoSensorState() {

    }
    @Override
    protected Boolean doInBackground(String... params) {

        //TODO verificar se mesmo enviando comando para tomada, o arduino continua respondendo
        //corretamente o estado dos sensores
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                //saveState = SaveState.getInstancia();
                Log.d("ENTROU", " CLASSE");
                String URL = "http://10.10.0.50/";
                String linha = "";
                boolean Erro = true;
                /////alteracao teste aqui

                //Sera executada uma tomada por vez pois, o Arduino esta programado para atender
                //Apenas um comando por vez e retorna o resultado, visando melhorar o tempo de
                //respposta e resolver os travamentos que ocorriam com a etrategia de procurar
                //varios comandos na mesma requisicao

                /*TODO, checar a necessidade de verificar o estado atual da tomada antes de mandar
                o comando, ou deixar o arduino cuidar disso. Talvez o Arduino fique sobrecarregado
                */

                //informa apenas se existe um acao de ligar ou desligar pendente
                if(saveState.getFuzzyActiveT1()){
                    //Caso seja de ligar, será enviado comando de lgiar
                    if(saveState.getTomada1()) {
                        Log.d("ENVIO COMANDOS", "sera enviado ligar tomada 1");
                        URL += "t1h";
                        Log.d("COMANDO DENTRO DO IF", URL);
                    }
                    //caso contrario desliga
                    else {
                        Log.d("ENVIO COMANDOS", "sera enviado desligar tomada 1");
                        URL += "t1l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT1(false);
                }
                //TOMADA 2
                else if(saveState.getFuzzyActiveT2()){
                    //Caso seja de ligar, será enviado comando de lgiar
                    if(saveState.getTomada2()) {
                        Log.d("ENVIO COMANDOS", "sera enviado ligar tomada 2");
                        URL += "t2h";
                    }
                    //caso contrario desliga
                    else {
                        Log.d("ENVIO COMANDOS", "sera enviado desligar tomada 2");
                        URL += "t2l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT2(false);
                }
                //Para tomada 3
                else if(saveState.getFuzzyActiveT3()){
                    //Caso seja de ligar, será enviado comando de lgiar
                    if(saveState.getTomada3()) {
                        Log.d("ENVIO COMANDOS", "sera enviado ligar tomada 3");
                        URL += "t3h";
                    }
                    //caso contrario desliga
                    else {
                        Log.d("ENVIO COMANDOS", "sera enviado desligar tomada 3");
                        URL += "t3l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT3(false);
                }

                //////////////////////////

                try {

                    Log.d("TOMADA agora ", " TESTE");
                    Log.d("TOMADA agora ", URL);

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


                    updateSensorStatus(linha);


                    Erro = false;


                } catch (Exception except) {
                    Log.e("TESTE ", "CRASH StackTrace: " + Arrays.toString(except.getStackTrace()));
                    Erro = true;
                }

                try {
                    Thread.sleep(60000);
                    // Do some stuff
                    Log.d("SENSOR ", "DORMIU");
                } catch (Exception e) {
                    e.getLocalizedMessage();
                    Erro = true;
                }
                Log.d("TESTE", " ACORDOU");



                    Log.d("EXECUTANDO "," amemmmmm1");
                }
        }, delay, interval);

        return true;

     }
    //}

    private void teste(String read){
        if(read.contains("#t")){
            Log.d("TEMPOK", read);
        }
    }

    public void updateSensorStatus(String linha) {

        String t,l,d,h,m;

        int idx1,idx2,idx3,idx4,idx5,idx6;

        idx1 = linha.indexOf('!');
        idx2 = linha.indexOf('@');
        idx3 = linha.indexOf('#');
        idx4 = linha.indexOf('$');
        idx5 = linha.indexOf('%');
        idx6 = linha.indexOf('&');

        t = linha.substring(idx1 + 1, idx2);
        l = linha.substring(idx2 + 1, idx3);
        d = linha.substring(idx3 + 1, idx4);
        h = linha.substring(idx4 + 1, idx5);
        m = linha.substring(idx5 + 1, idx6);

        Log.d("TEMP",t);
        Log.d("LUZ",l);
        Log.d("DIA",d);
        Log.d("HORA",h);
        Log.d("MINUTO",m);

        Log.d("UPDATE", " SENSOR");

//        Double temp = Double.valueOf(t);
//        Double luz = Double.valueOf(l);
//        Double dia = Double.valueOf(d);
//        Double hora = Double.valueOf(h);
//        Double min = Double.valueOf(m);
//
        double temp = Double.valueOf(t);
        double luz = Double.valueOf(l);
        double dia = Double.valueOf(d);
        double hora = Double.valueOf(h);
        double min = Double.valueOf(m);

        saveState.setTemperatura(temp);
        saveState.setLuz(luz);
        saveState.setDiaSemana(dia);
        saveState.setHora(hora);
        saveState.setMinuto(min);


        Log.d("CHEGOU", "FINAL ASENSOR STATE");
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
